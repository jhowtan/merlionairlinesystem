package MAS.Common;

import MAS.CommonInterface.UserBeanRemote;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless(name = "UserEJB")
public class UserBean implements UserBeanRemote {
    @PersistenceContext
    EntityManager em;

    public UserBean() {
    }

    @Override
    public void createUser(String username, String password, String firstName, String lastName, String email) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        em.persist(user);
    }
}
