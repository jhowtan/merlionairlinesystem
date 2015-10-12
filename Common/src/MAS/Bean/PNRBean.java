package MAS.Bean;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless(name = "PNREJB")
@LocalBean
public class PNRBean {
    @PersistenceContext
    EntityManager em;

    public PNRBean() {
    }


}
