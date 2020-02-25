package khay.dy.ptasjurl.util;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

public class MyFont {
    private static MyFont myfont;
    private Typeface type_face;
    /* fontType = 1 english */
    /* fontType = 2 khmer */
    /* fontType = 3 chinese */

    public static MyFont getInstance() {
        if (myfont == null) {
            myfont = new MyFont();
        }
        return myfont;
    }

    private void initFont(final Context context, final int type) {
        try {
            type_face = Typeface.createFromAsset(context.getAssets(), getFontPath(type));
        } catch (Exception e) {
            Log.e("Err", e.getMessage() + "");
        }
    }

    /** type:
     * 1 = km.ttf
     * 2 = km_btb.ttf */
    private String getFontPath(int type) {
        int asc[];
        if (type == 1) {
            asc = new int[]{107, 109, 46, 116, 116, 102};
        } else if(type == 2) {
            asc = new int[]{101, 110, 46, 116, 116, 102};
        }else if(type == 3){
            asc = new int[]{99, 110, 46, 116, 116, 102};
        }else {
            asc = new int[]{98, 110, 46, 116, 116, 102};
        }

        String text = "";
        for (int i = 0; i < asc.length; i++) {
            text += (char) asc[i];
        }
        return text;
    }

    public Typeface getFont(final Context context, final int type) {
        if (type_face == null) {
            type_face = Typeface.createFromAsset(context.getAssets(), getFontPath(type));
        }
        return type_face;
    }

    public void setFont(final Context context, final View view, final int type) {
        try {
            initFont(context, type);
            if (view instanceof ViewGroup) {
                setFontView((ViewGroup) view, type_face);
            } else {
                setFontView(view, type_face);
            }
        } catch (Exception e) {
            Log.e("Err", e.getMessage() + "");
        }
    }

    private void setFontView(final View view, final Typeface tf) {
        if (view instanceof TextView) {
            ((TextView) view).setTypeface(tf);
        } else if (view instanceof EditText) {
            ((EditText) view).setTypeface(tf);
        } else if (view instanceof Button) {
            ((Button) view).setTypeface(tf);
        } else if (view instanceof CheckBox) {
            ((CheckBox) view).setTypeface(tf);
        } else if (view instanceof RadioButton) {
            ((RadioButton) view).setTypeface(tf);
        }
    }

    private void setFontView(final ViewGroup vg, final Typeface tf) {
        final int childCount = vg.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = vg.getChildAt(i);
            if (child instanceof ViewGroup) {
                setFontView((ViewGroup) child, tf);
            } else {
                setFontView(child, tf);
            }
        }
    }
}
