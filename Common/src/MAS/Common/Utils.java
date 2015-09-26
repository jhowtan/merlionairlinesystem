package MAS.Common;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Calendar;
import java.util.Date;
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

    public static Date minutesLater(Date date, int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, minutes);
        return calendar.getTime();
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

}
