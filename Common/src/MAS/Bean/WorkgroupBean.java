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

    public long editWorkgroup(long wgId, String name, String description, List<Long> userIds) throws NotFoundException {
        Workgroup workgroup = em.find(Workgroup.class, wgId);
        workgroup.setName(name);
        workgroup.setDescription(description);
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

    public long changeWorkgroupOwner(long wgId, long newOwnerId) throws NotFoundException {
        Workgroup workgroup = em.find(Workgroup.class, wgId);
        if (workgroup == null) throw new NotFoundException();

        User newOwner = em.find(User.class, newOwnerId);
        if (newOwner == null) throw new NotFoundException();

        if (workgroup.getUsers().contains(newOwner)) {
            workgroup.setOwner(newOwner);
            em.persist(workgroup);
            return newOwnerId;
        }
        else {
            throw new NotFoundException("New owner of the workgroup is not part of the workgroup.");
        }
    }

    public void removeWorkgroup(long id) throws NotFoundException {
        Workgroup workgroup = em.find(Workgroup.class, id);
        if (workgroup == null) throw new NotFoundException();
        em.remove(workgroup);
    }

    public void removeUserFromWorkgroup(long userId, long wgId) throws NotFoundException {
        Workgroup workgroup = em.find(Workgroup.class, wgId);
        User user = em.find(User.class, userId);
        if (workgroup.getUsers().remove(user)) {
            em.persist(workgroup);
        }
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

    public List<Workgroup> searchForWorkgroup(String query) {
        return em.createQuery("SELECT w from Workgroup w WHERE w.name LIKE :query", Workgroup.class)
                .setParameter("query", "%" + query + "%")
                .getResultList();
    }

}
