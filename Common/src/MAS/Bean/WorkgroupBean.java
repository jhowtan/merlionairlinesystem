package MAS.Bean;

import MAS.Entity.User;
import MAS.Entity.Workgroup;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Stateless(name = "WorkgroupEJB")
@LocalBean
public class WorkgroupBean {
    @PersistenceContext
    private EntityManager em;

    public WorkgroupBean() {
    }

    public long createWorkgroup(String name, User owner, List<Long> userIds) {
        Workgroup workgroup = new Workgroup();
        workgroup.setName(name);
        workgroup.setOwner(owner);
        User user;
        ArrayList<User> users = new ArrayList<>();
        for(Long userId : userIds) {
            user = em.find(User.class, userId);
            if (user != null) {
                users.add(user);
            }
        }
        workgroup.setUsers(users);
        em.persist(workgroup);
        em.flush();
        return workgroup.getId();
    }

    public List<Workgroup> getAllWorkgroups() {
        return em.createQuery("SELECT w from Workgroup w", Workgroup.class).getResultList();
    }


}
