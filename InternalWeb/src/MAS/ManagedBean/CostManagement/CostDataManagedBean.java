package MAS.ManagedBean.CostManagement;

import MAS.Bean.CostsBean;
import MAS.Entity.Cost;
import MAS.ManagedBean.Auth.AuthManagedBean;
import MAS.ManagedBean.CommonManagedBean;
import com.google.gson.Gson;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ManagedBean
public class CostDataManagedBean {

    @EJB
    CostsBean costsBean;

    @ManagedProperty(value="#{commonManagedBean}")
    CommonManagedBean commonManagedBean;

    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;

    public CommonManagedBean getCommonManagedBean() {
        return commonManagedBean;
    }

    public void setCommonManagedBean(CommonManagedBean commonManagedBean) {
        this.commonManagedBean = commonManagedBean;
    }

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }

    private class CostItem {
        public String date;
        public String value;
    }

    public void search() {
        Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String query = params.get("q");
        List<Cost> costs = costsBean.getAllCostOfType(Integer.parseInt(query));

        ArrayList<CostItem> costItems = new ArrayList<>();

        for (Cost cost : costs) {
            CostItem r = new CostItem();
            String date = commonManagedBean.formatDate("yyyy-MM-dd HH:mm:ss", cost.getDate());
            r.date = date;
            r.value = String.valueOf(cost.getAmount());
            costItems.add(r);
        }

        Gson gson = new Gson();
        String json = gson.toJson(costItems);

        if(!authManagedBean.isAuthenticated()) {
            json = "[]";
        }

        FacesContext ctx = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) ctx.getExternalContext().getResponse();
        response.setContentLength(json.length());
        response.setContentType("application/json");

        try {
            response.getOutputStream().write(json.getBytes());
            response.getOutputStream().flush();
            response.getOutputStream().close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ctx.responseComplete();
    }
}
