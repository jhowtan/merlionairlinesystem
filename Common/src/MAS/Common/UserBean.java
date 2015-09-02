package MAS.Common;

import MAS.CommonInterface.Exceptions.BadPasswordException;
import MAS.CommonInterface.Exceptions.InvalidResetHashException;
import MAS.CommonInterface.Exceptions.NotFoundException;
import MAS.CommonInterface.UserBeanRemote;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;

@Stateless(name = "UserEJB")
public class UserBean implements UserBeanRemote {
    @PersistenceContext
    EntityManager em;

    public UserBean() {
    }

    public long createPermission(String name) {
        Permission permission = new Permission();
        permission.setName(name);
        em.persist(permission);
        em.flush();
        return permission.getId();
    }

    public void removePermission(long id) {
        throw new NotImplementedException();
    }

    public long createRole(String name) {
        Role role = new Role();
        role.setName(name);
        em.persist(role);
        em.flush();
        return role.getId();
    }

    public void removeRole(long id) {
        throw new NotImplementedException();
    }

    public void createUser(String username, String firstName, String lastName, String email, String phone) {
        User user = new User();
        user.setUsername(username);
        user.setSalt(Utils.generateSecureRandom(32));
        user.setLocked(false);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPhone(phone);
        user.setDeleted(false);
        em.persist(user);
    }

    public void removeUser(long id) {
        throw new NotImplementedException();
    }

    public void generateResetHash(long id) throws NotFoundException {
        User user = em.find(User.class, id);
        if(user == null) throw new NotFoundException();
        user.setResetHash(Utils.generateSecureRandom(32));
        user.setResetExpiry(Utils.hoursFromNow(72));
    }

    public void resetPassword(long id, String resetHash, String newPassword) throws NotFoundException, InvalidResetHashException, BadPasswordException {
        User user = em.find(User.class, id);
        if(user == null)
            throw new NotFoundException();
        if(!user.getResetHash().equals(resetHash) || user.getResetExpiry().after(new Date()))
            throw new InvalidResetHashException();
        if(!Utils.isGoodPassword(newPassword))
            throw new BadPasswordException();
        user.setPasswordHash(Utils.hash(newPassword, user.getSalt()));
        em.persist(user);
    }

}
