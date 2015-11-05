package MAS.Bean;

import MAS.Common.Constants;
import MAS.Common.Permissions;
import MAS.Common.Utils;
import MAS.Entity.Airport;
import MAS.Entity.BookingClass;
import MAS.Entity.PNR;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.lang.reflect.Field;
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
    @EJB
    PNRBean pnrBean;
    @EJB
    BookFlightBean bookFlightBean;

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

            // INITIALIZE SOME AIRPORTS
            try {
                routeBean.createAirport("SIN", "Changi Airport", "SIN", 1.3644202, 103.9915308, 10);
                routeBean.createAirport("HKG", "Hong Kong International Airport", "HKG", 22.308047, 113.9184808, 5);
                routeBean.createAirport("LHR", "Heathrow Airport", "LON", 51.4700223, -0.4542955, 0);
                routeBean.createAirport("JFK", "John F. Kennedy International Airport", "NYC", 40.6413111, -73.77813909999998, 0);
                routeBean.createAirport("SFO", "San Francisco International Airport", "SFO", 37.6213129, -122.3789554, 0);
                routeBean.createAirport("ICN", "Incheon International Airport", "SEL", 37.4601908, 126.44069569999999, 0);
                routeBean.createAirport("NRT", "Narita International Airport", "TYO", 35.7719867, 140.39285010000003, 0);
                routeBean.createAirport("KUL", "Kuala Lumpur International Airport", "KUL", 2.75419, 101.70474000000001, 0);
                routeBean.createAirport("BKK", "Suvarnabhumi Airport", "BKK", 13.6899991, 100.75011240000003, 0);
                routeBean.createAirport("BJS", "Beijing Capital International Airport", "BJS", 40.0798573, 116.60311209999997, 0);
                routeBean.createAirport("BCN", "Barcelona-El Prat Airport", "BCN", 41.297445, 2.083294099999989, 0);
                routeBean.createAirport("DXB", "Dubai International Airport", "DXB", 25.2531745, 55.36567279999997, 8);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // INITIALIZE SOME USERS
            try {
                Long roleId = roleBean.createRole("Super Admin", permissionIds);
                Long userId = userBean.createUser("admin", "Jonathan", "Lau", "merlionairlines+admin@gmail.com", "+65 6555-7777", routeBean.findAirportByCode("SIN"));
                userBean.changePassword(userId, "password");
                userBean.setRoles(userId, Arrays.asList(roleId));

                ArrayList<Long> airlinePlannerPermissions = new ArrayList<>();
                airlinePlannerPermissions.add(roleBean.findPermission(Permissions.MANAGE_FLEET).getId());
                airlinePlannerPermissions.add(roleBean.findPermission(Permissions.MANAGE_ROUTES).getId());
                airlinePlannerPermissions.add(roleBean.findPermission(Permissions.MANAGE_FLIGHT).getId());
                roleId = roleBean.createRole("Airline Planner", airlinePlannerPermissions);
                userId = userBean.createUser("daryl", "Daryl", "Ho", "merlionairlines+daryl@gmail.com", "+65 6555-8888", routeBean.findAirportByCode("SIN"));
                userBean.changePassword(userId, "password");
                userBean.setRoles(userId, Arrays.asList(roleId));

                ArrayList<Long> revenueManagerPermissions = new ArrayList<>();
                revenueManagerPermissions.add(roleBean.findPermission(Permissions.MANAGE_FARE_RULES).getId());
                revenueManagerPermissions.add(roleBean.findPermission(Permissions.MANAGE_BOOKING_CLASSES).getId());
                roleId = roleBean.createRole("Revenue Manager", revenueManagerPermissions);
                userId = userBean.createUser("thad", "Thaddeus", "Loh", "merlionairlines+thad@gmail.com", "+65 6555-9999", routeBean.findAirportByCode("SIN"));
                userBean.changePassword(userId, "password");
                userBean.setRoles(userId, Arrays.asList(roleId));

                //Initialize some flight crew
                String[] firstNames = {"Daryl", "John", "Jon", "Louis", "Jacob", "Mark", "Marcus", "Larry", "Aaron", "Barry", "Colin", "David", "Erica", "Erik", "Amanda", "Clara", "Grace", "Hannah", "Chloe", "Jessica", "Irene", "Fiona", "Olivia", "Penelope", "Ian", "Evan", "Joe", "Jane", "Ryan", "Victor", "Steward"};
                String[] lastNames = {"Tan", "Jones", "Avery", "Campbell", "Bond", "Davidson", "Bell", "Jackson", "Hill", "Thomson", "Terry", "Underwood", "Vance", "Scott", "Powell", "Reid"};
                List<Airport> airports = routeBean.getAllAirports();
                ArrayList<Long> flightCrewPermissions = new ArrayList<>();
                flightCrewPermissions.add(roleBean.findPermission(Permissions.FLIGHT_BID).getId());
                roleId = roleBean.createRole("Flight Crew", flightCrewPermissions);
                for (int i = 0; i < 50; i ++) { //Cabin crew
                    String selFName = firstNames[(int)(Math.random() * firstNames.length)];
                    String selLName = lastNames[(int)(Math.random() * lastNames.length)];
                    String username = selFName.concat(selLName).toLowerCase().concat(String.valueOf(i));
                    userId = userBean.createUserWithoutEmail(username, selFName, selLName, "merlionairlines+".concat(username).concat("@ma.com"),
                            "+65 6555-1234", airports.get((int) (Math.random() * airports.size())));
                    userBean.changePassword(userId, "password");
                    userBean.setRoles(userId, Arrays.asList(roleId));
                    userBean.changeJob(userId, 3);
                }
                for (int i = 0; i < 10; i ++) { //Pilots
                    String selFName = firstNames[(int)(Math.random() * firstNames.length)];
                    String selLName = lastNames[(int)(Math.random() * lastNames.length)];
                    String username = selFName.concat(selLName).toLowerCase().concat(String.valueOf(i));
                    userId = userBean.createUserWithoutEmail(username, selFName, selLName,"merlionairlines+".concat(username).concat("@ma.com"),
                            "+65 6555-1234", airports.get((int)(Math.random() * airports.size())) );
                    userBean.changePassword(userId, "password");
                    userBean.setRoles(userId, Arrays.asList(roleId));
                    userBean.changeJob(userId, 4);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // INITIALIZE SOME FARE RULES
            try {
                int minimumStay = 1;
                int maximumStay = 30;
                int advancePurchase = 60;
                int minimumPassengers = 1;
                int milesAccrual = 100;
                fareRuleBean.createFareRule(Constants.FARE_NORMAL, minimumStay, maximumStay, advancePurchase, minimumPassengers, milesAccrual, true, 1.0);
                fareRuleBean.createFareRule(Constants.FARE_LATE, minimumStay + 9, maximumStay, advancePurchase - 30, minimumPassengers, milesAccrual, false, 1.35);
                fareRuleBean.createFareRule(Constants.FARE_DOUBLE, minimumStay, maximumStay, advancePurchase, minimumPassengers + 1, milesAccrual - 10, false, 0.90);
                fareRuleBean.createFareRule(Constants.FARE_EARLY, minimumStay, 7, advancePurchase + 30, minimumPassengers, milesAccrual - 80, false, 0.85);
                fareRuleBean.createFareRule(Constants.FARE_EXPENSIVE, minimumStay, maximumStay + 60, advancePurchase - 60, minimumPassengers, milesAccrual, true, 1.5);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // INITIALIZE SOME AIRCRAFT TYPES AND SEAT CONFIGS
            try {
                long acTypeId = fleetBean.createAircraftType("A380 NR", 323546, 30, 2, 0.120, 634, 276800);
                long seatConfId = fleetBean.createAircraftSeatConfig("s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/s|s/_0s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/" +
                        "s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/_1ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/" +
                        "ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/" +
                        "ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/" +
                        "ss|ss|ss/ss|ss|ss/ss|ss|ss/_2sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/" +
                        "sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/" +
                        "sss|ssss|sss/sss|ssss|sss/_3e", "A380 NR Normal", 20580, acTypeId);
                long acId = fleetBean.createAircraft("9V-ABC", new Date(), routeBean.getAirport("SIN").getId());
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
                acId = fleetBean.createAircraft("9V-ABD", new Date(), routeBean.getAirport("SIN").getId());
                fleetBean.getAircraft(acId).setSeatConfig(fleetBean.getAircraftSeatConfig(seatConfId));

                acTypeId = fleetBean.createAircraftType("B777 200-ER", 171177, 25, 2, 0.0765, 590, 138100);
                seatConfId = fleetBean.createAircraftSeatConfig("s|s/s|s/s|s/s|s/s|s/s|s/_0s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/_1ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/" +
                        "ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/_2sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/" +
                        "sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/_3e", "B777 300-ER Normal", 20041, acTypeId);
                acId = fleetBean.createAircraft("9V-AET", new Date(), routeBean.getAirport("SIN").getId());
                fleetBean.getAircraft(acId).setSeatConfig(fleetBean.getAircraftSeatConfig(seatConfId));

                acTypeId = fleetBean.createAircraftType("B777 300-ER", 181283, 25, 2, 0.0765, 590, 167800);
                seatConfId = fleetBean.createAircraftSeatConfig("s|s/s|s/s|s/s|s/s|s/s|s/_0s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/s|s|s/_1ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/" +
                        "ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/ss|ss|ss/_2sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/" +
                        "sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/sss|ssss|sss/_3e", "B777 300-ER Normal", 23500, acTypeId);
                acId = fleetBean.createAircraft("9V-DET", new GregorianCalendar(2005, 05, 05).getTime(), routeBean.getAirport("SIN").getId());
                fleetBean.getAircraft(acId).setSeatConfig(fleetBean.getAircraftSeatConfig(seatConfId));

                // INITIALIZE FLIGHT & BOOKING CLASS FOR 2ND SYS RELEASE
                long r1 = routeBean.createRoute("SIN", "HKG");
                long r2 = routeBean.createRoute("HKG", "SFO");
                long aa1Id = routeBean.createAircraftAssignment(acId, r1);
                long aa2Id = routeBean.createAircraftAssignment(acId, r2);
                long flight1Id = flightScheduleBean.createFlight("MA11", Utils.addTimeToDate(new Date(), "12:00"), Utils.addTimeToDate(new Date(), "15:00"), aa1Id);
                long flight2Id = flightScheduleBean.createFlight("MA12", Utils.addTimeToDate(new Date(), "16:00"), Utils.addTimeToDate(new Date(), "19:00"), aa2Id);

                long bk1Id = bookingClassBean.createBookingClass("T", 0, 3, fareRuleBean.getFareRuleByName(Constants.FARE_NORMAL).getId(), flight1Id, 300);
                long bk2Id = bookingClassBean.createBookingClass("T", 0, 3, fareRuleBean.getFareRuleByName(Constants.FARE_NORMAL).getId(), flight2Id, 2000);

                ArrayList<BookingClass> b = new ArrayList<>();
                ArrayList<String> p = new ArrayList<>();
                b.add(bookingClassBean.getBookingClass(bk1Id));
                b.add(bookingClassBean.getBookingClass(bk2Id));
                p.add("TAN/KELLY");
                PNR pnr = bookFlightBean.bookFlights(b, p);
                pnrBean.setSpecialServiceRequest(pnr, pnrBean.getPassengerNumber(pnr, "TAN/KELLY"), Constants.SSR_ACTION_CODE_FFP, "B6/12345655");
                pnrBean.updatePNR(pnr);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // INITIALIZE COSTS
            try {
                costsBean.createCost(Constants.COST_FUEL, 0.38, "Market Fuel Price", 0);
                costsBean.createCost(Constants.COST_PER_AIRCRAFT, 285000000, "Aircraft Purchase", -1);
                costsBean.createCost(Constants.COST_PER_FLIGHT, 10000, "Food & Beverages", -1);
                costsBean.createCost(Constants.COST_PER_MAINTENANCE, 10000, "Maintenance Cost", -1);
                costsBean.createCost(Constants.COST_ANNUAL, 500000, "Marketing Costs", -1);
                costsBean.createCost(Constants.COST_ANNUAL, 1000000, "Licensing Fees", -1);
                costsBean.createCost(Constants.COST_PROFIT_MARGIN, 1.4, "Baseline Profit Margin", -1);
                //costsBean.createCost(Constants.COST_ANNUAL, 100000000, "Misc Fees", -1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
