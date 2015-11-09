package MAS.Bean;

import MAS.WebService.AwardMiles;
import MAS.WebService.PartnerMilesService;

import javax.ejb.Stateless;

@Stateless(name = "PartnerMilesEJB")
public class PartnerMilesBean {
    public PartnerMilesBean() {
    }

    public void awardMiles(String ffpNumber, int miles) {
        new PartnerMilesService().getPartnerMilesPort().awardMiles(ffpNumber, miles);
    }

}
