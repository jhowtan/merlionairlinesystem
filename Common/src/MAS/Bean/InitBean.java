package MAS.Bean;

import MAS.Common.Permissions;
import MAS.Entity.Permission;
import MAS.Exception.NotFoundException;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Singleton
@Startup
public class InitBean {
    @EJB
    RoleBean roleBean;
    @EJB
    UserBean userBean;
    @EJB
    FleetBean fleetBean;
    @EJB
    RouteBean routeBean;

    @PostConstruct
    public void init() {
        List<Long> permissionIds = new ArrayList<>();
        if(roleBean.getAllPermissions().size() == 0) {
            for(Field permissionField : Permissions.class.getDeclaredFields()) {
                try {
                    permissionIds.add(roleBean.createPermission((String) permissionField.get(null)));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            Long roleId = roleBean.createRole("Super Admin", permissionIds);
            Long userId = userBean.createUser("admin", "John", "Smith", "nobody@example.com", "1234567");
            try {
                userBean.setPassword(userId, "password");
                userBean.setRoles(userId, Arrays.asList(roleId));
            } catch (NotFoundException e) {
                e.printStackTrace();
            }
        }

        if (fleetBean.getAllAircraftTypes().size() == 0) {
            try {
                long acTypeId = fleetBean.createAircraftType("A380 ABC", 4000);
                long seatConfId = fleetBean.createAircraftSeatConfig("ss|sss|ss/ss|sss|ss/ss|sss|ss/_e", "A3180 ABC test", 5800, acTypeId);
                long acId = fleetBean.createAircraft("9V-ABC", new Date());
                fleetBean.getAircraft(acId).setSeatConfig(fleetBean.getAircraftSeatConfig(seatConfId));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (routeBean.getAllAirports().size() == 0) {
            try {
                long ctryId = routeBean.createCountry("Singapore", "SGP");
                long ctId = routeBean.createCity("Singapore", ctryId);
                long apId = routeBean.createAirport("Changi Airport", 1.3644202, 103.9915308, "SIN", 3, ctId);

                long ctry2Id = routeBean.createCountry("United Kingdom", "GBR");
                long ct2Id = routeBean.createCity("London", ctry2Id);
                long ap2Id = routeBean.createAirport("Heathrow Airport", 51.4700223, -0.4542955, "LHR", 4, ct2Id);

                routeBean.createRoute(apId, ap2Id);
            } catch (Exception e) {

            }
        }
    }

}
