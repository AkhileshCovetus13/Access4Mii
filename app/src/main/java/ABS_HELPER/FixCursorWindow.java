package ABS_HELPER;

import android.database.CursorWindow;

import java.lang.reflect.Field;

/**
 * Created by admin18 on 12/7/19.
 */

public class FixCursorWindow {

    public static void fix() {
        try {
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, 102400 * 1024); //the 102400 is the new size added
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
