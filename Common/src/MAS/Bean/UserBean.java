package MAS.Bean;

import MAS.Common.Constants;
import MAS.Common.Utils;
import MAS.Entity.*;
import MAS.Exception.InvalidLoginException;
import MAS.Exception.InvalidResetHashException;
import MAS.Exception.NotFoundException;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Stateless(name = "UserEJB")
@LocalBean
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

}
