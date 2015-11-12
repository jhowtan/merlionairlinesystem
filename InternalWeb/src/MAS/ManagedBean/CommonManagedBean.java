package MAS.ManagedBean;

import MAS.Common.Constants;
import MAS.Common.Utils;
import MAS.Entity.AircraftAssignment;
import MAS.Entity.Route;
import MAS.Entity.User;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@ManagedBean
public class CommonManagedBean {

    public String getRoot() {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
    }

    public String truncate(String str, int chars) {
        return Utils.truncate(str, chars);
    }

    public static String formatDate(String format, Date date) {
        if (date == null) {
            date = new Date();
        }
        try {
            return new SimpleDateFormat(format).format(date);
        } catch (Exception e) {
            return null;
        }
    }

    public static String getJobString(int job) {
        return Constants.JOB_NAMES[job];
    }

    public static String formatDate(String format, String timezone, Date date) {
        if (date == null) {
            date = new Date();
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            sdf.setTimeZone(TimeZone.getTimeZone(timezone));
            return sdf.format(date);
        } catch (Exception e) {
            return null;
        }
    }

    public static String formatJob(int jobId) {
        if (jobId == 4) {
            return "Pilot";
        } else if (jobId == 3) {
            return "Cabin Crew";
        }
        return "No Job";
    }

    public static String formatRoute(Route route) {
        return route.getOrigin().getId() + " - " + route.getDestination().getId();
    }

    public String formateDoubleRounded(double val) {
        return Integer.toString((int)val);
    }

    public String timezoneNiceName(String timezone) {
        return TimeZone.getTimeZone(timezone).getDisplayName();
    }

    public String formatMoney(double amount) {
        NumberFormat numberFormat = new DecimalFormat("#,##0.00");
        return "$" + numberFormat.format(amount);
    }

    public static String formatDoubleTwoDecimal(double amount) {
        NumberFormat numberFormat = new DecimalFormat("#,##0.00");
        return numberFormat.format(amount);
    }

    public String reportStatusToString(int status){
        return Constants.REPORT_STATUS[status];
    }

    public List truncateList(List list, int size) {
        return list.subList(0, Math.min(size, list.size()));
    }

    public String makeParagraph(String string) {
        return string.replace("<", "&lt;").replace(">", "&gt;").replace("\n", "<br>");
    }

    public void redirect(String path) {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(getRoot() + path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String displayDouble(double value, String format) {
        DecimalFormat formatter = new DecimalFormat(format);
        formatter.setRoundingMode(RoundingMode.HALF_UP);
        return formatter.format(value);
    }

    public String urlEncode(String str) {
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    public static String getDayString(int day) {
        switch (day) {
            case Calendar.MONDAY:
                return "Monday";
            case Calendar.TUESDAY:
                return "Tuesday";
            case Calendar.WEDNESDAY:
                return "Wednesday";
            case Calendar.THURSDAY:
                return "Thursday";
            case Calendar.FRIDAY:
                return "Friday";
            case Calendar.SATURDAY:
                return "Saturday";
            case Calendar.SUNDAY:
                return "Sunday";
        }
        return "";
    }

    public List<String> getMonthsOfYear() {
        ArrayList<String> monthsOfYear = new ArrayList<>(Arrays.asList(Constants.MONTHS_OF_YEAR));
        return monthsOfYear;
    }

    public String getTierString(int tier) {
        switch (tier) {
            case Constants.FFP_TIER_BLUE:
                return "blue";
            case Constants.FFP_TIER_SILVER:
                return "silver";
            case Constants.FFP_TIER_GOLD:
                return "gold";
        }
        return "blue";
    }

    public String getTravelClassString(int tier) {
        switch (tier) {
            case Constants.FIRST_CLASS:
                return Constants.FIRST_CLASS_LABEL;
            case Constants.BUSINESS_CLASS:
                return Constants.BUSINESS_CLASS_LABEL;
            case Constants.PREMIUM_EC_CLASS:
                return Constants.PREMIUM_EC_CLASS_LABEL;
        }
        return Constants.ECONOMY_CLASS_LABEL;
    }

    public static String formatAA(AircraftAssignment aa) {
        return aa.getAircraft().getTailNumber() + " : " + aa.getRoute().getOrigin().getName() + " - " + aa.getRoute().getDestination().getName();
    }

    public static String formatName(User user) {
        return user.getFirstName() + " " + user.getLastName();
    }

    public static double random() {
        return Math.random();
    }


    public static String padRight(String s, int n) {
        return Utils.padRight(s, n);
    }

    public static String padLeft(String s, int n) {
        return Utils.padLeft(s, n);
    }
}
