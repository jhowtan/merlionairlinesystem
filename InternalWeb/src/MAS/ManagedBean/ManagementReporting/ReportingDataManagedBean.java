package MAS.ManagedBean.ManagementReporting;

import MAS.ManagedBean.Auth.AuthManagedBean;
import MAS.ManagedBean.CommonManagedBean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import java.util.Map;

@ManagedBean
public class ReportingDataManagedBean {
    @ManagedProperty(value="#{commonManagedBean}")
    CommonManagedBean commonManagedBean;

    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;

    @ManagedProperty(value="#{profitabilityReportManagedBean}")
    private ProfitabilityReportManagedBean profitabilityReportManagedBean;

    private class ReportItem {
        public String name;
        public String value;
    }

    public void search() {
        Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String query = params.get("q");

//        ArrayList<ReportItem> reportItems = new ArrayList<>();
//
//        for (ReportItem cost : costs) {
//            ReportItem r = new ReportItem();
//            String date = commonManagedBean.formatDate("yyyy-MM-dd HH:mm:ss", cost.getDate());
//            r.name = date;
//            r.value = String.valueOf(cost.getAmount());
//            costItems.add(r);
//        }

//        Gson gson = new Gson();
//        String json = gson.toJson(costItems);

//        if(!authManagedBean.isAuthenticated()) {
//            json = "[]";
//        }

//        FacesContext ctx = FacesContext.getCurrentInstance();
//        HttpServletResponse response = (HttpServletResponse) ctx.getExternalContext().getResponse();
//        response.setContentLength(json.length());
//        response.setContentType("application/json");

//        try {
//            response.getOutputStream().write(json.getBytes());
//            response.getOutputStream().flush();
//            response.getOutputStream().close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        ctx.responseComplete();
    }

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }


}
