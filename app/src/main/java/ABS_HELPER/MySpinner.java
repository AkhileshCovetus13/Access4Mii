package ABS_HELPER;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Spinner;

import java.lang.reflect.Field;

/**
 * Created by admin1 on 7/1/19.
 */

public class MySpinner extends Spinner {

   /* public MySpinner(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }*/

    public MySpinner(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }



    public MySpinner(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void setSelection(int position, boolean animate) {
        ignoreOldSelectionByReflection();
        super.setSelection(position, animate);
    }

    private void ignoreOldSelectionByReflection() {
        try {
            Class<?> c = this.getClass().getSuperclass().getSuperclass().getSuperclass();
            Field reqField = c.getDeclaredField("mOldSelectedPosition");
            reqField.setAccessible(true);
            reqField.setInt(this, -1);
        } catch (Exception e) {
            Log.d("Exception Private", "ex", e);
            // TODO: handle exception
        }
    }

    @Override
    public void setSelection(int position) {
        ignoreOldSelectionByReflection();
        super.setSelection(position);
    }
}
