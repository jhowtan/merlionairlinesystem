package MAS.Bean;

import MAS.Common.Constants;
import MAS.Common.Permissions;
import MAS.Entity.Aircraft;
import MAS.Entity.Airport;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

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
    @EJB
    AttributesBean attributesBean;
    @EJB
    CostsBean costsBean;
    @EJB
    ScheduleDevelopmentBean scheduleDevelopmentBean; //REMOVE THIS AFTER TESTING

    @PostConstruct
    public void init() {
        if (!attributesBean.getBooleanAttribute("DATABASE_INITIALIZED", false)) {
            attributesBean.setBooleanAttribute("DATABASE_INITIALIZED", true);

            List<Long> permissionIds = new ArrayList<>();
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

            try {
                long acTypeId = fleetBean.createAircraftType("A380 NR", 323546, 30, 2, 0.120, 634, 276800);
                //long seatConfId = fleetBean.createAircraftSeatConfig("ss|sss|ss/ss|sss|ss/ss|sss|ss/_3e", "A3180 ABC test", 5800, acTypeId);
                long seatConfId = fleetBean.createAircraftSeatConfig("s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/_0s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/" +
                        "s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/_1ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/" +
                        "ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/" +
                        "ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/" +
                        "ss|ss|ss/ss|ss|ss/ss|ss|ss/_2sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/" +
                        "sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/" +
                        "sss|ssss|sss/sss|ssss|sss/_3e", "A380 NR Normal", 20580, acTypeId);
                long acId = fleetBean.createAircraft("9V-ABC", new Date());
                fleetBean.getAircraft(acId).setSeatConfig(fleetBean.getAircraftSeatConfig(seatConfId));
                fleetBean.createAircraftSeatConfig("s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/" +
                        "s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/" +
                        "s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/_0s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/" +
                        "s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/" +
                        "s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/_1e", "A380 NR Luxury", 23200, acTypeId);

                acTypeId = fleetBean.createAircraftType("B747 400", 183380, 20, 2, 0.090, 570, 184600);
                seatConfId = fleetBean.createAircraftSeatConfig("s|s/s|s/s|s/s|s/s|s/s|s/_0s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/_1ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/" +
                        "ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/_2sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/" +
                        "sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/_3e", "B747 300-ER Normal", 20310, acTypeId);
                acId = fleetBean.createAircraft("9V-ABD", new Date());
                fleetBean.getAircraft(acId).setSeatConfig(fleetBean.getAircraftSeatConfig(seatConfId));

                acTypeId = fleetBean.createAircraftType("B777 200-ER", 171177, 25, 2, 0.0765, 590, 138100);
                seatConfId = fleetBean.createAircraftSeatConfig("s|s/s|s/s|s/s|s/s|s/s|s/_0s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/_1ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/" +
                        "ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/_2sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/" +
                        "sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/_3e", "B777 300-ER Normal", 20041, acTypeId);
                acId = fleetBean.createAircraft("9V-AET", new Date());
                fleetBean.getAircraft(acId).setSeatConfig(fleetBean.getAircraftSeatConfig(seatConfId));

                acTypeId = fleetBean.createAircraftType("B777 300-ER", 181283, 25, 2, 0.0765, 590, 167800);
                seatConfId = fleetBean.createAircraftSeatConfig("s|s/s|s/s|s/s|s/s|s/s|s/_0s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/_1ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/" +
                        "ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/_2sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/" +
                        "sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/_3e", "B777 300-ER Normal", 23500, acTypeId);
                acId = fleetBean.createAircraft("9V-DET", new Date());
                fleetBean.getAircraft(acId).setSeatConfig(fleetBean.getAircraftSeatConfig(seatConfId));
            } catch (Exception e) {
                e.printStackTrace();
            }


            try {
                int minimumStay = 1;
                int maximumStay = 30;
                int advancePurchase = 60;
                int minimumPassengers = 1;
                int milesAccrual = 100;
                fareRuleBean.createFareRule(Constants.FARE_NORMAL, minimumStay, maximumStay, advancePurchase, minimumPassengers, milesAccrual, true);
                fareRuleBean.createFareRule(Constants.FARE_LATE, minimumStay + 9, maximumStay, advancePurchase - 30, minimumPassengers, milesAccrual, false);
                fareRuleBean.createFareRule(Constants.FARE_DOUBLE, minimumStay, maximumStay, advancePurchase, minimumPassengers + 1, milesAccrual - 10, false);
                fareRuleBean.createFareRule(Constants.FARE_EARLY, minimumStay, 7, advancePurchase + 30, minimumPassengers, milesAccrual - 80, false);
                fareRuleBean.createFareRule(Constants.FARE_EXPENSIVE, minimumStay, maximumStay + 60, advancePurchase - 60, minimumPassengers, milesAccrual, true);

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                costsBean.createCost(Constants.COST_FUEL, 0.38, "Market Fuel Price", 0);
                costsBean.createCost(Constants.COST_PER_AIRCRAFT, 285000000, "Aircraft Purchase", -1);
                costsBean.createCost(Constants.COST_PER_FLIGHT, 10000, "Food & Beverages", -1);
                costsBean.createCost(Constants.COST_PER_MAINTENANCE, 10000, "Maintenance Cost", -1);
                costsBean.createCost(Constants.COST_ANNUAL, 500000, "Marketing Costs", -1);
                costsBean.createCost(Constants.COST_ANNUAL, 1000000, "Licensing Fees", -1);
                costsBean.createCost(Constants.COST_ANNUAL, 100000000, "Misc Fees", -1);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                long ctryId = routeBean.createCountry("Singapore", "SGP");
                long ctId = routeBean.createCity("Singapore", ctryId);
                long apId = routeBean.createAirport("Changi Airport", 1.3644202, 103.9915308, "SIN", 3, ctId);

                /*long ctry2Id = routeBean.createCountry("United Kingdom", "GBR");
                long ct2Id = routeBean.createCity("London", ctry2Id);
                long ap2Id = routeBean.createAirport("Heathrow Airport", 51.4700223, -0.4542955, "LHR", 4, ct2Id);

                long ctry3Id = routeBean.createCountry("United States", "USA");
                long ct3Id = routeBean.createCity("New York", ctry3Id);
                long ap3Id = routeBean.createAirport("John F. Kennedy International Airport", 40.6413111, -73.77813909999998, "JFK", 4, ct3Id);*/

                long ctry4Id = routeBean.createCountry("South Korea", "KOR");
                long ct4Id = routeBean.createCity("Seoul", ctry4Id);
                long ap4Id = routeBean.createAirport("Incheon International Airport", 37.4601908, 126.44069569999999, "ICN", 4, ct4Id);

                long ctry5Id = routeBean.createCountry("Hong Kong", "HKG");
                long ct5Id = routeBean.createCity("Hong Kong", ctry5Id);
                long ap5Id = routeBean.createAirport("Hong Kong International Airport", 22.308047, 113.9184808, "HKG", 4, ct5Id);

                /*long routeId = routeBean.createRoute(apId, ap2Id);

                List<Aircraft> allAircraft = fleetBean.getAllAircraft();
                long aa1Id = routeBean.createAircraftAssignment(allAircraft.get(0).getId(), routeId);
                long aa2Id = routeBean.createAircraftAssignment(allAircraft.get(1).getId(), routeId);*/

//                if ((flightScheduleBean.getAllFlights().size() == 0) && (aircraftMaintenanceSlotBean.getAllSlots().size() == 0)) {
//                    String flight1Code = "MA11";
//                    Date departure1Time = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2015-12-09 12:00:00");
//                    Date arrival1Time = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2015-12-10 08:00:00");
//                    long flight1Id = flightScheduleBean.createFlight(flight1Code, departure1Time, arrival1Time, aa1Id, true);
//
//                    String flight2Code = "MA12";
//                    Date departure2Time = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2015-11-12 02:00:00");
//                    Date arrival2Time = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2015-11-12 18:00:00");
//                    long flight2Id = flightScheduleBean.createFlight(flight2Code, departure2Time, arrival2Time, aa2Id, true);

//                    long ac1Id = fleetBean.getAllAircraft().get(0).getId();
//                    long ac2Id = fleetBean.getAllAircraft().get(1).getId();
//                    System.out.println("maintenance slot: " + aircraftMaintenanceSlotBean.createSlot(arrival1Time, 2.0, ap2Id, ac1Id));
//                    System.out.println("maintenance slot: " + aircraftMaintenanceSlotBean.createSlot(arrival2Time, 2.0, ap2Id, ac2Id));

//                    if (bookingClassBean.getAllBookingClasses().size() == 0) {
//                        bookingClassBean.createBookingClass("Y", 40, 3, fareRuleBean.getAllFareRules().get(0).getId(), flight1Id);
//                        bookingClassBean.createBookingClass("Z", 20, 2, fareRuleBean.getAllFareRules().get(1).getId(), flight1Id);
//                        bookingClassBean.createBookingClass("C", 20, 3, fareRuleBean.getAllFareRules().get(2).getId(), flight2Id);
//                    }
//                }
                try {
                    List<Long> apIds = new ArrayList<>();
                    List<Airport> allAp = routeBean.getAllAirports();
                    for (int i = 0; i < allAp.size(); i++) {
                        apIds.add(allAp.get(i).getId());
                    }
                    List<Long> acIds = new ArrayList<>();
                    List<Aircraft> allAc = fleetBean.getAllAircraft();
                    for (int i = 0; i < allAc.size(); i++) {
                        acIds.add(allAc.get(i).getId());
                    }
                    List<Long> hubIds = new ArrayList<>();
                    hubIds.add(apId);
                    scheduleDevelopmentBean.addAirports(apIds);
                    scheduleDevelopmentBean.addAircrafts(acIds);
                    scheduleDevelopmentBean.addHubs(hubIds);
                    scheduleDevelopmentBean.process();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
