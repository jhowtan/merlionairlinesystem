package MAS.Bean;

import MAS.Common.Constants;
import MAS.Common.Permissions;
import MAS.Common.Utils;
import MAS.Entity.*;
import MAS.Exception.InvalidLoginException;
import MAS.Exception.InvalidResetHashException;
import MAS.Exception.NotFoundException;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Stateless(name = "UserEJB")
public class UserBean {
    @PersistenceContext
    private EntityManager em;

    @EJB
    private MailBean mailBean;

    public UserBean() {
    }

    public long createUser(String username, String firstName, String lastName, String email, String phone, Airport baseAirport) {
        User user = new User();
        user.setUsername(username.toLowerCase());
        user.setSalt(Utils.generateSalt());
        user.setLocked(false);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email.toLowerCase());
        user.setPhone(phone);
        user.setDeleted(false);
        user.setResetHash(Utils.generateSalt());
        user.setResetExpiry(Utils.hoursFromNow(72));
        user.setBaseAirport(baseAirport);
        user.setCurrentLocation(baseAirport);
        em.persist(user);
        em.flush();

        String msg = "Dear " + user.getFirstName() + ",\n\n" +
                "Welcome to Merlion Airlines!\n\n" +
                "Please access the link below to activate your account and set your password:\n" +
                Constants.WEB_ROOT + "Auth/resetPassword.xhtml?u=" + user.getId() + "&h=" + user.getResetHash() + "\n\n" +
                "For security reasons, the link will expire in 3 days.\n\n" +
                "Yours Sincerely,\n" +
                "Merlion Airlines";
        mailBean.send(user.getEmail(), user.getFirstName() + " " + user.getLastName(), "Merlion Airlines Account Activation", msg);

        return user.getId();
    }

    public long createUserWithoutEmail(String username, String firstName, String lastName, String email, String phone, Airport baseAirport) {
        User user = new User();
        user.setUsername(username.toLowerCase());
        user.setSalt(Utils.generateSalt());
        user.setLocked(false);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email.toLowerCase());
        user.setPhone(phone);
        user.setDeleted(false);
        user.setResetHash(Utils.generateSalt());
        user.setResetExpiry(Utils.hoursFromNow(72));
        user.setBaseAirport(baseAirport);
        user.setCurrentLocation(baseAirport);
        em.persist(user);
        em.flush();
        return user.getId();
    }

    public User forgotPassword(String usernameEmail) throws NotFoundException {
        try {
            User user = em.createQuery("SELECT u FROM User u WHERE u.username = :usernameEmail OR u.email = :usernameEmail", User.class)
                    .setParameter("usernameEmail", usernameEmail.toLowerCase())
                    .setMaxResults(1)
                    .getSingleResult();

            user.setResetHash(Utils.generateSalt());
            user.setResetExpiry(Utils.hoursFromNow(72));
            em.persist(user);

            String msg = "Dear " + user.getFirstName() + ",\n\n" +
                    "Welcome to Merlion Airlines!\n\n" +
                    "Please access the link below to reset your password:\n" +
                    Constants.WEB_ROOT + "Auth/resetPassword.xhtml?u=" + user.getId() + "&h=" + user.getResetHash() + "\n\n" +
                    "For security reasons, the link will expire in 3 days.\n\n" +
                    "Yours Sincerely,\n" +
                    "Merlion Airlines";
            mailBean.send(user.getEmail(), user.getFirstName() + " " + user.getLastName(), "Merlion Airlines Password Reset", msg);
            return user;
        } catch (NoResultException e) {
            throw new NotFoundException();
        }
    }

    public User setRoles(long id, List<Long> roleIds) throws NotFoundException {
        User user = em.find(User.class, id);
        if (user == null) throw new NotFoundException();
        Role role;
        ArrayList<Role> roles = new ArrayList<>();
        for (Long roleId : roleIds) {
            role = em.find(Role.class, roleId);
            if (role != null) {
                roles.add(role);
            }
        }
        user.setRoles(roles);
        em.persist(user);
        return user;
    }

    public boolean isUsernameUnique(String username) {
        return (Long) em.createQuery("SELECT COUNT(u) FROM User u WHERE u.username = :username").setParameter("username", username.toLowerCase()).getSingleResult() == 0;
    }

    public boolean isEmailUnique(String email) {
        return (Long) em.createQuery("SELECT COUNT(u) FROM User u WHERE u.email = :email").setParameter("email", email.toLowerCase()).getSingleResult() == 0;
    }

    public void removeUser(long id) throws NotFoundException {
        User user = em.find(User.class, id);
        if (user == null) throw new NotFoundException();
        user.setDeleted(true);
        user.setLocked(true);
        user.setRoles(new ArrayList<>());
        for(Workgroup workgroup : user.getWorkgroups()) {
            workgroup.getUsers().remove(user);
        }
        em.persist(user);
    }

    public void resetPassword(long id, String resetHash, String newPassword) throws NotFoundException, InvalidResetHashException {
        User user = em.find(User.class, id);
        if (user == null)
            throw new NotFoundException();
        if (!isResetHashValid(id, resetHash))
            throw new InvalidResetHashException();
        user.setResetHash(null);
        user.setResetExpiry(null);
        user.setPasswordHash(Utils.hash(newPassword, user.getSalt()));
        em.persist(user);
    }

    public void changePassword(long id, String newPassword) throws NotFoundException {
        User user = em.find(User.class, id);
        if (user == null)
            throw new NotFoundException();
        user.setPasswordHash(Utils.hash(newPassword, user.getSalt()));
        em.persist(user);
    }

    public void changeJob(long id, int jobId) throws NotFoundException {
        User user = em.find(User.class, id);
        if (user == null) throw new NotFoundException();
        user.setJob(jobId);
        em.persist(user);
    }

    public long login(String username, String password) throws InvalidLoginException {
        try {
            User user = em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                    .setParameter("username", username.toLowerCase())
                    .getSingleResult();
            if (user.isDeleted())
                throw new InvalidLoginException();
            if(user.isLocked())
                throw new InvalidLoginException();
            if (!Utils.hash(password, user.getSalt()).equals(user.getPasswordHash().toString()))
                throw new InvalidLoginException();
            return user.getId();
        } catch (NoResultException e) {
            throw new InvalidLoginException();
        }
    }

    public List<User> getAllUsers() {
        return em.createQuery("SELECT u from User u WHERE NOT u.deleted", User.class).getResultList();
    }

    public List<User> getUsersWithJobs(int jobId) {
        return em.createQuery("SELECT u from User u WHERE NOT u.deleted AND u.job = :job", User.class)
                .setParameter("job", jobId).getResultList();
    }

    public List<User> getUsersAtAirportWithJob(String airportId, int jobId) throws NotFoundException {
        Airport airport = em.find(Airport.class, airportId);
        if (airport == null) throw new NotFoundException();
        return em.createQuery("SELECT u from User u WHERE NOT u.deleted AND u.job = :job AND u.currentLocation = :airport", User.class)
                .setParameter("airport", airport)
                .setParameter("job", jobId).getResultList();
    }

    public boolean isResetHashValid(Long id, String resetHash) {
        User user = em.find(User.class, id);
        if (user == null) return false;
        if (user.getResetHash() == null || !user.getResetHash().equals(resetHash)) return false;
        if (user.getResetExpiry() == null || user.getResetExpiry().before(new Date())) return false;
        return true;
    }

    public void setLocked(long id, boolean isLocked) throws NotFoundException {
        User user = em.find(User.class, id);
        if (user == null) throw new NotFoundException();
        user.setLocked(isLocked);
        em.persist(user);
    }

    public void updateUserInfo(long id, String phone) throws NotFoundException {
        User user = em.find(User.class, id);
        if (user == null) throw new NotFoundException();
        user.setPhone(phone);
        em.persist(user);
    }

    public long adminUpdateUserInfo(long id, String firstName, String lastName, String email, String phone, Airport baseAirport)
        throws NotFoundException {
        User user = em.find(User.class, id);
        if (user == null) throw new NotFoundException();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPhone(phone);
        user.setBaseAirport(baseAirport);
        em.persist(user);

        return user.getId();
    }

    public User getUser(long id) throws NotFoundException {
        User user = em.find(User.class, id);
        if (user == null) throw new NotFoundException();
        return user;
    }

    public List<User> searchForUser(String query) {
        return em.createQuery("SELECT u from User u WHERE NOT u.deleted AND (u.username LIKE :query OR CONCAT(u.firstName, ' ', u.lastName) LIKE :query)", User.class)
                .setParameter("query", "%" + query + "%")
                .getResultList();
    }

    public List<User> searchForFlightCrew(String query) {
        return em.createQuery("SELECT u from User u WHERE NOT u.deleted AND (u.username LIKE :query OR CONCAT(u.firstName, ' ', u.lastName) LIKE :query) AND (u.job = :cabincrew OR u.job = :cockpitcrew)", User.class)
                .setParameter("query", "%" + query + "%")
                .setParameter("cabincrew", Constants.cabinCrewJobId)
                .setParameter("cockpitcrew", Constants.cockpitCrewJobId)
                .getResultList();
    }

    @EJB
    RoleBean roleBean;
    @EJB
    RouteBean routeBean;
    @EJB
    CrewCertificationBean crewCertificationBean;
    @EJB
    FleetBean fleetBean;
    public void addCrew() {
        try {
            System.out.println("CREATING CREW");
            //Initialize some flight crew
            String[] firstNames = {"Daryl", "John", "Jon", "Louis", "Jacob", "Mark", "Marcus", "Larry", "Aaron", "Barry", "Colin", "David", "Erica", "Erik", "Amanda", "Clara", "Grace", "Hannah", "Chloe", "Jessica", "Irene", "Fiona", "Olivia", "Penelope", "Ian", "Evan", "Joe", "Jane", "Ryan", "Victor", "Steward"};
            String[] lastNames = {"Tan", "Jones", "Avery", "Campbell", "Bond", "Davidson", "Bell", "Jackson", "Hill", "Thomson", "Terry", "Underwood", "Vance", "Scott", "Powell", "Reid"};
            List<Airport> airports = routeBean.getAllAirports();
//            ArrayList<Long> flightCrewPermissions = new ArrayList<>();
//            flightCrewPermissions.add(roleBean.findPermission(Permissions.FLIGHT_BID).getId());
//            flightCrewPermissions.add(roleBean.findPermission(Permissions.CREW_CERTIFICATION).getId());
//            flightCrewPermissions.add(roleBean.findPermission(Permissions.FLIGHT_REPORTING).getId());
//            long roleId = roleBean.createRole("Flight Crew", flightCrewPermissions);
//            for (int i = 200; i < 400; i++) { //Cabin crew
//                String selFName = firstNames[(int) (Math.random() * firstNames.length)];
//                String selLName = lastNames[(int) (Math.random() * lastNames.length)];
//                String username = selFName.concat(selLName).toLowerCase().concat(String.valueOf(i));
//                long userId = createUserWithoutEmail(username, selFName, selLName, "merlionairlines+".concat(username).concat("@ma.com"),
//                        "+65 6555-1234", airports.get(i % airports.size()));
//                changePassword(userId, "password");
//                setRoles(userId, Arrays.asList(roleId));
//                changeJob(userId, Constants.cabinCrewJobId);
//                List<AircraftType> acTypes = fleetBean.getAllAircraftTypes();
//                for (int j = 0; j < acTypes.size(); j++) {
//                    Certification certification = new Certification();
//                    certification.setAircraftType(acTypes.get(j));
//                    certification.setExpiry(Utils.oneYearLater());
//                    certification.setApprovalDate(new Date());
//                    certification.setApprover(searchForUser("crewmgr").get(0));
//                    certification.setApprovalStatus(1);
//                    certification.setOwner(getUser(userId));
//                    crewCertificationBean.createCrewCertification(certification);
//                }
//            }
//            for (int i = 50; i < 100; i++) { //Pilots
//                String selFName = firstNames[(int) (Math.random() * firstNames.length)];
//                String selLName = lastNames[(int) (Math.random() * lastNames.length)];
//                String username = selFName.concat(selLName).toLowerCase().concat(String.valueOf(i));
//                long userId = createUserWithoutEmail(username, selFName, selLName, "merlionairlines+".concat(username).concat("@ma.com"),
//                        "+65 6555-1234", airports.get(i % airports.size()));
//                changePassword(userId, "password");
//                setRoles(userId, Arrays.asList(roleId));
//                changeJob(userId, Constants.cockpitCrewJobId);
//                List<AircraftType> acTypes = fleetBean.getAllAircraftTypes();
//                for (int j = 0; j < acTypes.size(); j++) {
//                    Certification certification = new Certification();
//                    certification.setAircraftType(acTypes.get(j));
//                    certification.setExpiry(Utils.oneYearLater());
//                    certification.setApprovalDate(new Date());
//                    certification.setApprover(searchForUser("crewmgr").get(0));
//                    certification.setApprovalStatus(1);
//                    certification.setOwner(getUser(userId));
//                    crewCertificationBean.createCrewCertification(certification);
//                }
//            }
            ArrayList<Long> maintPermissions = new ArrayList<>();
            maintPermissions.add(roleBean.findPermission(Permissions.CREW_CERTIFICATION).getId());
            maintPermissions.add(roleBean.findPermission(Permissions.MAINTENANCE_REPORTING).getId());
            long roleId = roleBean.createRole("Maintenance Crew", maintPermissions);
            for (int i = 50; i < 100; i++) { //Maintenance crew
                String selFName = firstNames[(int) (Math.random() * firstNames.length)];
                String selLName = lastNames[(int) (Math.random() * lastNames.length)];
                String username = selFName.concat(selLName).toLowerCase().concat(String.valueOf(i));
                long userId = createUserWithoutEmail(username, selFName, selLName, "merlionairlines+".concat(username).concat("@ma.com"),
                        "+65 6555-4325", airports.get(i % airports.size()));
                changePassword(userId, "password");
                setRoles(userId, Arrays.asList(roleId));
                changeJob(userId, Constants.maintenanceCrewJobId);
                List<AircraftType> acTypes = fleetBean.getAllAircraftTypes();
                for (int j = 0; j < acTypes.size(); j++) {
                    Certification certification = new Certification();
                    certification.setAircraftType(acTypes.get(j));
                    certification.setExpiry(Utils.oneYearLater());
                    certification.setApprovalDate(new Date());
                    certification.setApprover(searchForUser("crewmgr").get(0));
                    certification.setApprovalStatus(1);
                    certification.setOwner(getUser(userId));
                    crewCertificationBean.createCrewCertification(certification);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
