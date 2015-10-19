package sg.mas.gatecontrol;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Utils {

    public static final String getRemoteApi(Context ctx) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);
        return "http://" + pref.getString("host", "127.0.0.1") + ":8080/InternalWeb_war_exploded/Remote/";
    }

}
