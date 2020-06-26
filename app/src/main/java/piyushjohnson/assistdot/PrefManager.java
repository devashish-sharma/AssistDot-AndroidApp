package piyushjohnson.assistdot;

/**
 * Created by samuelraj on 06/04/18.
 */

import android.content.Context;
import android.content.SharedPreferences;


public class PrefManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "Guider";

    private static final String isOneTime = "IsOneTime";

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void IsOneTime(boolean isFirstTime) {
        editor.putBoolean(isOneTime, isFirstTime);
        editor.commit();
    }

    public boolean IsOneTime() {
        return pref.getBoolean(isOneTime, true);
    }

}
