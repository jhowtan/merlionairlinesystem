package MAS.Common;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Math.*;
import static java.lang.Math.sqrt;

public class Utils {

    public static String generateSalt() {
        return new BigInteger(130, new SecureRandom()).toString(32);
    }

    public static Date hoursFromNow(int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR, hours);
        return calendar.getTime();
    }

    public static Date oneYearLater() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.YEAR, 1);
        return calendar.getTime();
    }

    public static Date minutesLater(Date date, int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, minutes);
        return calendar.getTime();
    }

    public static int yearsBetween(Date first, Date last) {
        Calendar a = getCalendar(first);
        Calendar b = getCalendar(last);
        int diff = b.get(Calendar.YEAR) - a.get(Calendar.YEAR);
        if (a.get(Calendar.MONTH) > b.get(Calendar.MONTH) ||
                (a.get(Calendar.MONTH) == b.get(Calendar.MONTH) && a.get(Calendar.DATE) > b.get(Calendar.DATE))) {
            diff--;
        }
        return diff;
    }

    public static Date addTimeToDate(Date date, String time) {
        if (time.length() != 5)
            return null;
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            String dateString = Integer.toString(cal.get(Calendar.YEAR)) + "-" +
                    Integer.toString(cal.get(Calendar.MONTH)+1) + "-" +
                    Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
            return new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(dateString + " " + time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTime(date);
        return cal;
    }

    public static String hash(String plaintext) {
        String hash = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            hash = new BigInteger(1, md.digest(plaintext.getBytes())).toString(16);
            while(hash.length() < 32) {
                hash = "0" + hash;
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hash;
    }

    public static String hash(String plaintext, String salt) {
        return hash(plaintext + salt);
    }

    public static String truncate(String str, int chars) {
        if (str.length() > chars) {
            return str.substring(0, chars) + "...";
        }
        return str;
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(lon1 - lon2));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return dist;
    }

    public static double calculateDuration(double distance, double speed) {
        double duration = distance / (speed * Constants.OPERATIONAL_SPEED / 60);
        duration = (int)duration + Constants.TAKE_OFF_AND_LAND_TIME;
        return duration;
    }

    public static String convertBookingReference(long bookingReferenceLong) {
        return Long.toString(bookingReferenceLong, 36).toUpperCase();
    }

    public static long convertBookingReference(String bookingReferenceString) {
        return Long.parseLong(bookingReferenceString, 36);
    }

    public static double makeNiceMoney(double amount) {
        //Round up or down
        double round = amount % 10;
        amount = ((int)amount/10) * 10;
        if (round >= 5) {
            amount += 9.99;
        } else {
            amount -= 0.01;
        }
        return amount;
    }

    public static String padRight(String s, int n) {
        return String.format("%1$-" + n + "s", s);
    }

    public static String padLeft(String s, int n) {
        return String.format("%1$" + n + "s", s);
    }

}
