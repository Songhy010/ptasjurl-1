package khay.dy.ptasjurl.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import khay.dy.ptasjurl.R;
import khay.dy.ptasjurl.util.Tools;

public class ActivityProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Tools.setSystemBarColor(this,R.color.colorPrimaryDark);
    }
}
