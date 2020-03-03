package khay.dy.ptasjurl.activity;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;

import khay.dy.ptasjurl.R;
import khay.dy.ptasjurl.adapter.AdapterPick;
import khay.dy.ptasjurl.util.MyFunction;
import khay.dy.ptasjurl.util.PathUtil;
import khay.dy.ptasjurl.util.Tools;

public class ActivityMoreDesc extends AppCompatActivity {

    private ImageView iv_pick;
    private RecyclerView recycler;
    private final int PICK_CODE = 1;
    ArrayList<Bitmap> mArrayUri = new ArrayList<Bitmap>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_desc);
        Tools.setSystemBarColor(this, R.color.colorPrimaryDark);
        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            if (requestCode == PICK_CODE && resultCode == RESULT_OK && null != data) {
                if (data.getData() != null) {//on Single image selected
                    Uri imageUri = data.getData();
                    String realPath = PathUtil.getPath(ActivityMoreDesc.this, imageUri);
                    Log.e("path", realPath);
                    final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    final Bitmap newBitmap = MyFunction.getInstance().createScaledBit(selectedImage);
                    mArrayUri.add(newBitmap);
                    iv_pick.setImageBitmap(newBitmap);
                } else {//on multiple image selected
                    if (data.getClipData() != null) {
                        ClipData mClipData = data.getClipData();
                        for (int i = 0; i < mClipData.getItemCount(); i++) {
                            ClipData.Item item = mClipData.getItemAt(i);
                            Uri imageUri = item.getUri();
                            final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                            final Bitmap newBitmap = MyFunction.getInstance().createScaledBit(selectedImage);
                            mArrayUri.add(newBitmap);
                        }
                        iv_pick.setImageBitmap(mArrayUri.get(0));
                        initRecycler(mArrayUri);
                    }
                }
            } else {
                Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }


        super.onActivityResult(requestCode, resultCode, data);


    }

    private void initView() {
        findView();
        initToolBar();
        initPickImage();
    }

    private void initPickImage() {
        final int height = MyFunction.getInstance().getBannerHeight(this);
        iv_pick.getLayoutParams().height = height - 200;
        iv_pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage(PICK_CODE);
            }
        });
    }

    private void pickImage(final int requestCode) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), requestCode);
    }

    private void initRecycler(ArrayList<Bitmap> mArrayUri) {
        final LinearLayoutManager manager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        recycler.setLayoutManager(manager);
        recycler.setAdapter(new AdapterPick(this, mArrayUri));
    }

    private void findView() {
        iv_pick = findViewById(R.id.iv_pick);

        recycler = findViewById(R.id.recycler);
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
