package MAS.ManagedBean;

import MAS.Bean.WorkgroupBean;
import MAS.Entity.Workgroup;
import MAS.Exception.NotFoundException;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import java.util.List;

@ManagedBean
public class WorkgroupManagedBean {
    @EJB
    private WorkgroupBean workgroupBean;

    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;

    public List<Workgroup> getAllOwnedWorkgroups() throws NotFoundException {
        return workgroupBean.getOwnedWorkgroups(authManagedBean.getUserId());
    }

    public void delete(long id) {
        try {
            workgroupBean.removeWorkgroup(id);
        } catch (NotFoundException e) {
        }
    }


    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }

}
