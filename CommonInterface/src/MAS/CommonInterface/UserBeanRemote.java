package MAS.CommonInterface;

import javax.ejb.Remote;

@Remote
public interface UserBeanRemote {
    void createUser(String username, String password, String firstName, String lastName, String email);
}
