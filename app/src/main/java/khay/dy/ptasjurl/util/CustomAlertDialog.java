package khay.dy.ptasjurl.util;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnKeyListener;
import android.os.Build;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.TextView;

import khay.dy.ptasjurl.R;


public class CustomAlertDialog {
    private AlertDialog dialog;
    private Context context;
    private int fontType;
    public CustomAlertDialog(final Context context, final String title, final String message, final String positive_label, final String negative_label, final OnClickListener positive_listener, final OnClickListener negative_listener, final int fontType) {
        this.context = context;
        this.fontType = fontType;
        Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new Builder(context, R.style.AppCompatAlertDialogStyle);
        } else {
            builder = new Builder(context);
        }
        builder.setTitle(title);
        builder.setMessage(message);
        if (positive_listener != null) {
            dialog = builder.setPositiveButton(positive_label, positive_listener).create();
        }
        if (negative_listener != null) {
            dialog = builder.setNegativeButton(negative_label, negative_listener).create();
        }
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface di) {
                if (positive_label != null) {
                    Button btn_positive = dialog.getButton(Dialog.BUTTON_POSITIVE);
                    MyFont.getInstance().setFont(context, btn_positive,fontType);
                    btn_positive.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.text_size_normal));
                }
                if (negative_label != null) {
                    Button btn_negative = dialog.getButton(Dialog.BUTTON_NEGATIVE);
                    MyFont.getInstance().setFont(context, btn_negative,fontType);
                    btn_negative.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.text_size_normal));
                }
            }
        });
    }

    public CustomAlertDialog(final Context context, String title, String message, boolean end_activity,int fontType) {
        this(context, title, message, context.getString(android.R.string.ok), null, new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }, null,fontType);

        if (end_activity) {
            dialog.setOnDismissListener(new OnDismissListener() {

                @Override
                public void onDismiss(DialogInterface dialog) {
                    MyFunction.getInstance().finishActivity(context);
                }
            });
        }
    }

    public CustomAlertDialog(final Context context, String title, String message,int fontType) {
        this(context, title, message, context.getString(android.R.string.ok), null, new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }, null,fontType);
    }

    public void setFinishOnBackKey() {
        dialog.setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                    MyFunction.getInstance().finishActivity(context);
                }
                return false;
            }
        });
    }

    public void setFinishOnBackKeyDismiss() {
        dialog.setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                    dialog.dismiss();
                }
                return false;
            }
        });
    }

    public void show() {
        try {
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            final TextView tv_message =  dialog.findViewById(android.R.id.message);
            tv_message.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.text_size_normal));
            MyFont.getInstance().setFont(context, tv_message,this.fontType);

            final TextView tv_title = dialog.findViewById(context.getResources().getIdentifier("alertTitle", "id", "android"));
            tv_title.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.text_size_normal));
            MyFont.getInstance().setFont(context, tv_title,this.fontType);
        } catch (Exception e) {
            Log.e("Err", e.getMessage() + "");
        }
    }

    public void addDismissListener1(OnDismissListener listener) {
        dialog.setOnDismissListener(listener);
    }

    public boolean isShowing() {
        return dialog.isShowing();
    }

    public void dismiss() {
        dialog.dismiss();
    }
}
