package khay.dy.ptasjurl.listener;

import android.content.DialogInterface;
import android.view.View;

import androidx.appcompat.app.AlertDialog;

public interface CustomAlertListnner {
    void findView(View view, AlertDialog.Builder builder);
    void submit(DialogInterface dialogInterface);
    void itemOnClick(AlertDialog dialog, View view);
}
