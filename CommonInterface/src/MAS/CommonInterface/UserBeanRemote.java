package MAS.CommonInterface;

import MAS.CommonInterface.Exceptions.BadPasswordException;
import MAS.CommonInterface.Exceptions.InvalidResetHashException;
import MAS.CommonInterface.Exceptions.NotFoundException;

import javax.ejb.Remote;

@Remote
public interface UserBeanRemote {

    long createPermission(String name);

    void removePermission(long id);

    long createRole(String name);

    void removeRole(long id);

    void createUser(String username, String firstName, String lastName, String email, String phone);

    void removeUser(long id);

    void generateResetHash(long id) throws NotFoundException;

    void resetPassword(long id, String resetHash, String newPassword) throws NotFoundException, InvalidResetHashException, BadPasswordException;
}
