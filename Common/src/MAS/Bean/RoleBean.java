package MAS.Bean;

import MAS.Entity.Permission;
import MAS.Entity.Role;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless(name = "RoleEJB")
@LocalBean
public class RoleBean {
    @PersistenceContext
    EntityManager em;

    public RoleBean() {
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

    public List<Permission> getAllPermissions() {
        return em.createQuery("SELECT p from Permission p").getResultList();
    }



}
