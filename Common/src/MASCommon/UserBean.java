package MASCommon;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless(name = "UserEJB")
public class UserBean {
    @PersistenceContext
    EntityManager em;

    public UserBean() {
    }

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
