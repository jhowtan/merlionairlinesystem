package MAS.Bean;

import MAS.Common.Permissions;
import MAS.Entity.Aircraft;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
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
    @EJB
    FlightScheduleBean flightScheduleBean;
    @EJB
    AircraftMaintenanceSlotBean aircraftMaintenanceSlotBean;
    @EJB
    FareRuleBean fareRuleBean;
    @EJB
    BookingClassBean bookingClassBean;

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
            try {
                Long roleId = roleBean.createRole("Super Admin", permissionIds);
                Long userId = userBean.createUser("admin", "Jonathan", "Lau", "merlionairlines+admin@gmail.com", "+65 6555-7777");
                userBean.setPassword(userId, "password");
                userBean.setRoles(userId, Arrays.asList(roleId));

                ArrayList<Long> airlinePlannerPermissions = new ArrayList<>();
                airlinePlannerPermissions.add(roleBean.findPermission(Permissions.MANAGE_FLEET).getId());
                airlinePlannerPermissions.add(roleBean.findPermission(Permissions.MANAGE_ROUTES).getId());
                airlinePlannerPermissions.add(roleBean.findPermission(Permissions.MANAGE_FLIGHT).getId());
                roleId = roleBean.createRole("Airline Planner", airlinePlannerPermissions);
                userId = userBean.createUser("daryl", "Daryl", "Ho", "merlionairlines+daryl@gmail.com", "+65 6555-8888");
                userBean.setPassword(userId, "password");
                userBean.setRoles(userId, Arrays.asList(roleId));

                ArrayList<Long> revenueManagerPermissions = new ArrayList<>();
                revenueManagerPermissions.add(roleBean.findPermission(Permissions.MANAGE_FARE_RULES).getId());
                revenueManagerPermissions.add(roleBean.findPermission(Permissions.MANAGE_BOOKING_CLASSES).getId());
                roleId = roleBean.createRole("Revenue Manager", revenueManagerPermissions);
                userId = userBean.createUser("thad", "Thaddeus", "Loh", "merlionairlines+thad@gmail.com", "+65 6555-9999");
                userBean.setPassword(userId, "password");
                userBean.setRoles(userId, Arrays.asList(roleId));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /*if (fleetBean.getAllAircraftTypes().size() == 0) {
            try {
                long acTypeId = fleetBean.createAircraftType("A380 NR", 323546);
                //long seatConfId = fleetBean.createAircraftSeatConfig("ss|sss|ss/ss|sss|ss/ss|sss|ss/_3e", "A3180 ABC test", 5800, acTypeId);
                long seatConfId = fleetBean.createAircraftSeatConfig("s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/_0s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/" +
                        "s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/_1ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/" +
                        "ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/" +
                        "ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/" +
                        "ss|ss|ss/ss|ss|ss/ss|ss|ss/_2sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/" +
                        "sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/" +
                        "sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/_3e", "A380 NR Normal", 270000, acTypeId);
                long acId = fleetBean.createAircraft("9V-ABC", new Date());
                fleetBean.getAircraft(acId).setSeatConfig(fleetBean.getAircraftSeatConfig(seatConfId));
                fleetBean.createAircraftSeatConfig("s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/" +
                        "s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/" +
                        "s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/_0s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/" +
                        "s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/" +
                        "s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/_1e", "A380 NR Luxury", 250000, acTypeId);

                acTypeId = fleetBean.createAircraftType("B747 300-ER", 183380);
                seatConfId = fleetBean.createAircraftSeatConfig("s|s/s|s/s|s/s|s/s|s/s|s/_0s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/_1ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/" +
                        "ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/_2sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/" +
                        "sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/_3e", "B747 300-ER Normal", 333400, acTypeId);
                acId = fleetBean.createAircraft("9V-ABD", new Date());
                fleetBean.getAircraft(acId).setSeatConfig(fleetBean.getAircraftSeatConfig(seatConfId));

                acTypeId = fleetBean.createAircraftType("B777 300-ER", 171177);
                seatConfId = fleetBean.createAircraftSeatConfig("s|s/s|s/s|s/s|s/s|s/s|s/_0s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/_1ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/" +
                        "ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/_2sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/" +
                        "sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/_3e", "B777 300-ER Normal", 247208, acTypeId);
                acId = fleetBean.createAircraft("9V-AET", new Date());
                fleetBean.getAircraft(acId).setSeatConfig(fleetBean.getAircraftSeatConfig(seatConfId));

                acTypeId = fleetBean.createAircraftType("B777 400-ER", 174177);
                seatConfId = fleetBean.createAircraftSeatConfig("s|s/s|s/s|s/s|s/s|s/s|s/_0s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/_1ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/" +
                        "ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/_2sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/" +
                        "sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/_3e", "B777 300-ER Normal", 267232, acTypeId);
                acId = fleetBean.createAircraft("9V-DET", new Date());
                fleetBean.getAircraft(acId).setSeatConfig(fleetBean.getAircraftSeatConfig(seatConfId));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/


        if (fareRuleBean.getAllFareRules().size() == 0) {
            try {
                int minimumStay = 1;
                int maximumStay = 30;
                int advancePurchase = 60;
                int minimumPassengers = 1;
                int milesAccrual = 100;
                fareRuleBean.createFareRule("SVR-2", minimumStay, maximumStay, advancePurchase, minimumPassengers, milesAccrual, false);
                fareRuleBean.createFareRule("SVR-3", minimumStay+9, maximumStay, advancePurchase-30, minimumPassengers, milesAccrual-25, false);
                fareRuleBean.createFareRule("SVR-4", minimumStay, maximumStay + 30, advancePurchase + 30, minimumPassengers + 1, milesAccrual - 50, false);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /*if (routeBean.getAllAirports().size() == 0) {
            try {
                long ctryId = routeBean.createCountry("Singapore", "SGP");
                long ctId = routeBean.createCity("Singapore", ctryId);
                long apId = routeBean.createAirport("Changi Airport", 1.3644202, 103.9915308, "SIN", 3, ctId);

                long ctry2Id = routeBean.createCountry("United Kingdom", "GBR");
                long ct2Id = routeBean.createCity("London", ctry2Id);
                long ap2Id = routeBean.createAirport("Heathrow Airport", 51.4700223, -0.4542955, "LHR", 4, ct2Id);

                long routeId = routeBean.createRoute(apId, ap2Id);

                List<Aircraft> allAircraft = fleetBean.getAllAircraft();
                long aa1Id = routeBean.createAircraftAssignment(allAircraft.get(0).getId(), routeId);
                long aa2Id = routeBean.createAircraftAssignment(allAircraft.get(1).getId(), routeId);

                if ((flightScheduleBean.getAllFlights().size() == 0) && (aircraftMaintenanceSlotBean.getAllSlots().size() == 0)) {
                    String flight1Code = "MA11";
                    Date departure1Time = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2015-10-09 12:00:00");
                    Date arrival1Time = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2015-10-10 08:00:00");
                    long flight1Id = flightScheduleBean.createFlight(flight1Code, departure1Time, arrival1Time, aa1Id);

                    String flight2Code = "MA12";
                    Date departure2Time = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2015-10-12 02:00:00");
                    Date arrival2Time = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2015-10-12 18:00:00");
                    long flight2Id = flightScheduleBean.createFlight(flight2Code, departure2Time, arrival2Time, aa2Id);

                    long ac1Id = fleetBean.getAllAircraft().get(0).getId();
                    long ac2Id = fleetBean.getAllAircraft().get(1).getId();
                    System.out.println("maintenance slot: " + aircraftMaintenanceSlotBean.createSlot(arrival1Time, 2.0, ap2Id, ac1Id));
                    System.out.println("maintenance slot: " + aircraftMaintenanceSlotBean.createSlot(arrival2Time, 2.0, ap2Id, ac2Id));

                    if (bookingClassBean.getAllBookingClasses().size() == 0) {
                        bookingClassBean.createBookingClass("Y", 40, 3, fareRuleBean.getAllFareRules().get(0).getId(), flight1Id);
                        bookingClassBean.createBookingClass("Z", 20, 2, fareRuleBean.getAllFareRules().get(1).getId(), flight1Id);
                        bookingClassBean.createBookingClass("C", 20, 3, fareRuleBean.getAllFareRules().get(2).getId(), flight2Id);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/
    }

}
