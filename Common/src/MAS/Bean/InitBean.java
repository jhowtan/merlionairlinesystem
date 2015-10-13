package MAS.Bean;

import MAS.Common.Constants;
import MAS.Common.Permissions;
import MAS.Entity.Aircraft;
import MAS.Entity.Airport;
import MAS.Exception.NotFoundException;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;

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

    @PostConstruct
    public void init() {
        if (!attributesBean.getBooleanAttribute("DATABASE_INITIALIZED", false)) {
            attributesBean.setBooleanAttribute("DATABASE_INITIALIZED", true);

            // INITIALIZE ALL PERMISSIONS
            List<Long> permissionIds = new ArrayList<>();
            for (Field permissionField : Permissions.class.getDeclaredFields()) {
                try {
                    permissionIds.add(roleBean.createPermission((String) permissionField.get(null)));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            // INITIALIZE ALL COUNTRIES
            for (int i = 0; i < Constants.COUNTRY_CODES.length; i++) {
                routeBean.createCountry(Constants.COUNTRY_CODES[i], Constants.COUNTRY_NAMES[i]);
            }

            // INITIALIZE SOME CITIES
            try {
                routeBean.createCity("BNE", "Brisbane", "AUS", "Australia/Brisbane");
                routeBean.createCity("MEL", "Melbourne", "AUS", "Australia/Melbourne");
                routeBean.createCity("PER", "Perth", "AUS", "Australia/Perth");
                routeBean.createCity("DAC", "Dhaka", "BGD", "Asia/Dhaka");
                routeBean.createCity("SAO", "Sao Paulo", "BRA", "America/Sao_Paulo");
                routeBean.createCity("BWN", "Bandar Seri Begawan", "BRN", "Asia/Brunei");
                routeBean.createCity("BLC", "Bali", "CMR", "Africa/Douala");
                routeBean.createCity("YTO", "Toronto", "CAN", "America/Toronto");
                routeBean.createCity("BJS", "Beijing", "CHN", "Asia/Shanghai");
                routeBean.createCity("KMG", "Kunming", "CHN", "Asia/Shanghai");
                routeBean.createCity("SHA", "Shanghai", "CHN", "Asia/Shanghai");
                routeBean.createCity("FRA", "Frankfurt", "DEU", "Europe/Berlin");
                routeBean.createCity("MUC", "Munich", "DEU", "Europe/Berlin");
                routeBean.createCity("HKG", "Hong Kong", "HKG", "Asia/Hong_Kong");
                routeBean.createCity("BOM", "Mumbai", "IND", "Asia/Kolkata");
                routeBean.createCity("CCU", "Kolkata", "IND", "Asia/Kolkata");
                routeBean.createCity("DEL", "New Delhi", "IND", "Asia/Kolkata");
                routeBean.createCity("MAA", "Chennai", "IND", "Asia/Kolkata");
                routeBean.createCity("JKT", "Jakarta", "IDN", "Asia/Jakarta");
                routeBean.createCity("SUB", "Surabaya", "IDN", "Asia/Jakarta");
                routeBean.createCity("MIL", "Milan", "ITA", "Europe/Rome");
                routeBean.createCity("FUK", "Fukuoka", "JPN", "Asia/Tokyo");
                routeBean.createCity("TYO", "Tokyo", "JPN", "Asia/Tokyo");
                routeBean.createCity("KUL", "Kuala Lumpur", "MYS", "Asia/Kuala_Lumpur");
                routeBean.createCity("RGN", "Yangon", "MMR", "Asia/Rangoon");
                routeBean.createCity("AMS", "Amsterdam", "NLD", "Europe/Amsterdam");
                routeBean.createCity("CHC", "Christchurch", "NZL", "Pacific/Auckland");
                routeBean.createCity("SIN", "Singapore", "SGP", "Asia/Singapore");
                routeBean.createCity("CPT", "Cape Town", "ZAF", "Africa/Johannesburg");
                routeBean.createCity("JNB", "Johannesburg", "ZAF", "Africa/Johannesburg");
                routeBean.createCity("SEL", "Seoul", "KOR", "Asia/Seoul");
                routeBean.createCity("BCN", "Barcelona", "ESP", "Europe/Madrid");
                routeBean.createCity("CMB", "Colombo", "LKA", "Asia/Colombo");
                routeBean.createCity("ZRH", "Zurich", "CHE", "Europe/Zurich");
                routeBean.createCity("TPE", "Taipei", "TWN", "Asia/Taipei");
                routeBean.createCity("BKK", "Bangkok", "THA", "Asia/Bangkok");
                routeBean.createCity("IST", "Istanbul", "TUR", "Europe/Istanbul");
                routeBean.createCity("DXB", "Dubai", "ARE", "Asia/Dubai");
                routeBean.createCity("LON", "London", "GBR", "Europe/London");
                routeBean.createCity("MAN", "Manchester", "GBR", "Europe/London");
                routeBean.createCity("CHI", "Chicago", "USA", "America/Chicago");
                routeBean.createCity("HOU", "Houston", "USA", "America/Chicago");
                routeBean.createCity("MIA", "Miami", "USA", "America/New_York");
                routeBean.createCity("MXA", "Manila", "USA", "America/Chicago");
                routeBean.createCity("NYC", "New York", "USA", "America/New_York");
                routeBean.createCity("OCW", "Washington", "USA", "America/New_York");
                routeBean.createCity("PHT", "Paris", "USA", "America/Chicago");
                routeBean.createCity("SFO", "San Francisco", "USA", "America/Los_Angeles");
                routeBean.createCity("HAN", "Hanoi", "VNM", "Asia/Ho_Chi_Minh");
                routeBean.createCity("SGN", "Ho Chi Minh City", "VNM", "Asia/Ho_Chi_Minh");
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                Airport changiAirport = routeBean.createAirport("SIN", "Changi Airport", "SIN", 1.3644202, 103.9915308, 5);
                Airport heathrowAirport = routeBean.createAirport("LHR", "Heathrow Airport", "LON", 51.4700223, -0.4542955, 3);

                long routeId = routeBean.createRoute(changiAirport.getId(), heathrowAirport.getId());

                List<Aircraft> allAircraft = fleetBean.getAllAircraft();
                long aa1Id = routeBean.createAircraftAssignment(allAircraft.get(0).getId(), routeId);
                long aa2Id = routeBean.createAircraftAssignment(allAircraft.get(1).getId(), routeId);

                if ((flightScheduleBean.getAllFlights().size() == 0) && (aircraftMaintenanceSlotBean.getAllSlots().size() == 0)) {
                    String flight1Code = "MA11";
                    Date departure1Time = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2015-12-09 12:00:00");
                    Date arrival1Time = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2015-12-10 08:00:00");
                    long flight1Id = flightScheduleBean.createFlight(flight1Code, departure1Time, arrival1Time, aa1Id, true);

                    String flight2Code = "MA12";
                    Date departure2Time = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2015-11-12 02:00:00");
                    Date arrival2Time = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2015-11-12 18:00:00");
                    long flight2Id = flightScheduleBean.createFlight(flight2Code, departure2Time, arrival2Time, aa2Id, true);

//                    long ac1Id = fleetBean.getAllAircraft().get(0).getId();
//                    long ac2Id = fleetBean.getAllAircraft().get(1).getId();
//                    System.out.println("maintenance slot: " + aircraftMaintenanceSlotBean.createSlot(arrival1Time, 2.0, ap2Id, ac1Id));
//                    System.out.println("maintenance slot: " + aircraftMaintenanceSlotBean.createSlot(arrival2Time, 2.0, ap2Id, ac2Id));

//                    if (bookingClassBean.getAllBookingClasses().size() == 0) {
//                        bookingClassBean.createBookingClass("Y", 40, 3, fareRuleBean.getAllFareRules().get(0).getId(), flight1Id);
//                        bookingClassBean.createBookingClass("Z", 20, 2, fareRuleBean.getAllFareRules().get(1).getId(), flight1Id);
//                        bookingClassBean.createBookingClass("C", 20, 3, fareRuleBean.getAllFareRules().get(2).getId(), flight2Id);
//                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                Long roleId = roleBean.createRole("Super Admin", permissionIds);
                Long userId = userBean.createUser("admin", "Jonathan", "Lau", "merlionairlines+admin@gmail.com", "+65 6555-7777", routeBean.findAirportByCode("SIN"));
                userBean.setPassword(userId, "password");
                userBean.setRoles(userId, Arrays.asList(roleId));

                ArrayList<Long> airlinePlannerPermissions = new ArrayList<>();
                airlinePlannerPermissions.add(roleBean.findPermission(Permissions.MANAGE_FLEET).getId());
                airlinePlannerPermissions.add(roleBean.findPermission(Permissions.MANAGE_ROUTES).getId());
                airlinePlannerPermissions.add(roleBean.findPermission(Permissions.MANAGE_FLIGHT).getId());
                roleId = roleBean.createRole("Airline Planner", airlinePlannerPermissions);
                userId = userBean.createUser("daryl", "Daryl", "Ho", "merlionairlines+daryl@gmail.com", "+65 6555-8888", routeBean.findAirportByCode("SIN"));
                userBean.setPassword(userId, "password");
                userBean.setRoles(userId, Arrays.asList(roleId));

                ArrayList<Long> revenueManagerPermissions = new ArrayList<>();
                revenueManagerPermissions.add(roleBean.findPermission(Permissions.MANAGE_FARE_RULES).getId());
                revenueManagerPermissions.add(roleBean.findPermission(Permissions.MANAGE_BOOKING_CLASSES).getId());
                roleId = roleBean.createRole("Revenue Manager", revenueManagerPermissions);
                userId = userBean.createUser("thad", "Thaddeus", "Loh", "merlionairlines+thad@gmail.com", "+65 6555-9999", routeBean.findAirportByCode("SIN"));
                userBean.setPassword(userId, "password");
                userBean.setRoles(userId, Arrays.asList(roleId));
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                long acTypeId = fleetBean.createAircraftType("A380 NR", 323546, 30, 2, 0.08, 634, 270000);
                //long seatConfId = fleetBean.createAircraftSeatConfig("ss|sss|ss/ss|sss|ss/ss|sss|ss/_3e", "A3180 ABC test", 5800, acTypeId);
                long seatConfId = fleetBean.createAircraftSeatConfig("s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/_0s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/" +
                        "s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/_1ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/" +
                        "ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/" +
                        "ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/" +
                        "ss|ss|ss/ss|ss|ss/ss|ss|ss/_2sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/" +
                        "sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/" +
                        "sss|ssss|sss/sss|ssss|sss/_3e", "A380 NR Normal", 2580, acTypeId);
                long acId = fleetBean.createAircraft("9V-ABC", new Date());
                fleetBean.getAircraft(acId).setSeatConfig(fleetBean.getAircraftSeatConfig(seatConfId));
                fleetBean.createAircraftSeatConfig("s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/" +
                        "s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/" +
                        "s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/_0s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/" +
                        "s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/" +
                        "s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/_1e", "A380 NR Luxury", 3200, acTypeId);

                acTypeId = fleetBean.createAircraftType("B747 300-ER", 183380, 20, 2, 0.09, 570, 333400);
                seatConfId = fleetBean.createAircraftSeatConfig("s|s/s|s/s|s/s|s/s|s/s|s/_0s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/_1ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/" +
                        "ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/_2sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/" +
                        "sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/_3e", "B747 300-ER Normal", 2310, acTypeId);
                acId = fleetBean.createAircraft("9V-ABD", new Date());
                fleetBean.getAircraft(acId).setSeatConfig(fleetBean.getAircraftSeatConfig(seatConfId));

                acTypeId = fleetBean.createAircraftType("B777 300-ER", 171177, 25, 2, 0.085, 590, 245208);
                seatConfId = fleetBean.createAircraftSeatConfig("s|s/s|s/s|s/s|s/s|s/s|s/_0s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/_1ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/" +
                        "ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/_2sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/" +
                        "sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/_3e", "B777 300-ER Normal", 2041, acTypeId);
                acId = fleetBean.createAircraft("9V-AET", new Date());
                fleetBean.getAircraft(acId).setSeatConfig(fleetBean.getAircraftSeatConfig(seatConfId));

                acTypeId = fleetBean.createAircraftType("B777 400-ER", 174177, 25, 2, 0.085, 590, 245208);
                seatConfId = fleetBean.createAircraftSeatConfig("s|s/s|s/s|s/s|s/s|s/s|s/_0s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/_1ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/" +
                        "ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/_2sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/" +
                        "sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/_3e", "B777 300-ER Normal", 3500, acTypeId);
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
                costsBean.createCost(Constants.COST_PER_AIRCRAFT, 85000000, "Aircraft Purchase", -1);
                costsBean.createCost(Constants.COST_PER_FLIGHT, 10000, "Food & Beverages", -1);
                costsBean.createCost(Constants.COST_PER_MAINTENANCE, 10000, "Maintenance Cost", -1);
                costsBean.createCost(Constants.COST_ANNUAL, 500000, "Marketing Costs", -1);
                costsBean.createCost(Constants.COST_ANNUAL, 1000000, "Licensing Fees", -1);
                costsBean.createCost(Constants.COST_ANNUAL, 200000000, "Misc Fees", -1);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}
