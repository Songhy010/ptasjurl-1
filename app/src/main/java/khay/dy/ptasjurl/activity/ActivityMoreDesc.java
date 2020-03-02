package khay.dy.ptasjurl.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import khay.dy.ptasjurl.R;
import khay.dy.ptasjurl.util.Tools;

public class ActivityMoreDesc extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_desc);
        Tools.setSystemBarColor(this,R.color.colorPrimaryDark);
        initView();
    }

    private void initView() {
        findView();
        initToolBar();
    }

    private void findView() {
    }

    private void initToolBar() {
        final ImageView iv_back = findViewById(R.id.iv_back);
        final TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.more_desc));
        iv_back.setImageDrawable(getResources().getDrawable(R.drawable.img_arrow));
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
