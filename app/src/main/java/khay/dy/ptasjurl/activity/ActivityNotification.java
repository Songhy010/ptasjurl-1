package khay.dy.ptasjurl.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import khay.dy.ptasjurl.R;
import khay.dy.ptasjurl.adapter.AdapterNotification;
import khay.dy.ptasjurl.util.Tools;

public class ActivityNotification extends ActivityController {

    private RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        Tools.setSystemBarColor(this,R.color.colorPrimaryDark);
        initView();
    }

    private void findView() {
        recycler = findViewById(R.id.recycler);
    }

    private void initView() {
        findView();
        initToolBar();
        initRecycler();
    }

    private void initToolBar() {
        final ImageView iv_back = findViewById(R.id.iv_back);
        final TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.notification));
        iv_back.setImageDrawable(getResources().getDrawable(R.drawable.img_arrow));
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    private void initRecycler(){
        final LinearLayoutManager manager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recycler.setLayoutManager(manager);
        recycler.setAdapter(new AdapterNotification(this,null));
    }
}
