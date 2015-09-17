package MAS.Bean;

import MAS.Entity.User;
import MAS.Entity.Workgroup;
import MAS.Exception.NotFoundException;

import javax.ejb.EJB;
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

    @EJB
    private UserBean userBean;

    public WorkgroupBean() {
    }

    public long createWorkgroup(String name, String description, long ownerId, List<Long> userIds) throws NotFoundException {
        Workgroup workgroup = new Workgroup();
        workgroup.setName(name);
        workgroup.setDescription(description);
        User owner = userBean.getUser(ownerId);
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

    public void removeWorkgroup(long id) throws NotFoundException {
        Workgroup workgroup = em.find(Workgroup.class, id);
        if (workgroup == null) throw new NotFoundException();
        em.remove(workgroup);
    }

    public boolean isNameUnique(String name) {
        return (Long) em.createQuery("SELECT COUNT(w) FROM Workgroup w WHERE w.name = :name")
                .setParameter("name", name)
                .getSingleResult() == 0;
    }

    public List<Workgroup> getOwnedWorkgroups(long userId) throws NotFoundException {
        User owner = em.find(User.class, userId);
        if (owner == null) throw new NotFoundException();

        return em.createQuery("SELECT w from Workgroup w WHERE w.owner = :owner", Workgroup.class)
                .setParameter("owner", owner)
                .getResultList();
    }

    public List<Workgroup> getUserWorkgroups(long userId) throws NotFoundException {
        User user = em.find(User.class, userId);
        if (user == null) throw new NotFoundException();

        List<Workgroup> allWorkgroups = em.createQuery("SELECT w from Workgroup w", Workgroup.class).getResultList();
        ArrayList<Workgroup> userWorkgroups = new ArrayList<>();

        for (Workgroup w : allWorkgroups) {
            if (w.getUsers().contains(user)) {
                userWorkgroups.add(w);
            }
        }

        return userWorkgroups;
    }

}
