package net.winsion.www.indooratlasdemo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.anthonycr.grant.PermissionsManager;
import com.anthonycr.grant.PermissionsResultAction;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText mEdit;
    private List<Class> mClasses = new ArrayList<>();
    private Class mClass;
    boolean locationPermission;
    boolean storagePermission;
    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mClasses.add(ImageViewActivity.class);
        mClasses.add(MapViewActivity.class);
//        mClass = mClasses.get(0); //默认使用ImageViewActivity
        mClass = mClasses.get(1); //默认使用MapViewActivity
        locationPermission = PermissionsManager.hasPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        storagePermission = PermissionsManager.hasPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

//        findViewById(R.id.btn_imageView).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mClass = mClasses.get(0); //ImageViewActivity
//                Toast.makeText(getApplicationContext(), "已切换为ImageView", Toast.LENGTH_SHORT).show();
//            }
//        });
//        findViewById(R.id.btn_MapView).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mClass = mClasses.get(1); //MapViewActivity
//                Toast.makeText(getApplicationContext(), "已切换为MapView", Toast.LENGTH_SHORT).show();
//            }
//        });
        findViewById(R.id.btn_get_sensor_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SensorListActivity.class));
            }
        });

        mEdit = (EditText) findViewById(R.id.et_input);
        findViewById(R.id.text1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    startActivity(new Intent(MainActivity.this, mClass)
                            .putExtra("floorPlanId", getString(R.string.indooratlas_floor_plan_id_26f)));
                }
            }
        });
        findViewById(R.id.text2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, mClass)
                        .putExtra("floorPlanId", getString(R.string.indooratlas_floor_plan_id_27f)));
            }
        });
        findViewById(R.id.text3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, mClass)
                        .putExtra("floorPlanId", getString(R.string.indooratlas_floor_plan_id_bjxz2f)));
            }
        });
        findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEdit.getText().length() < 36) {
                    Toast.makeText(getApplicationContext(), "id格式有误", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(MainActivity.this, mClass)
                            .putExtra("floorPlanId", mEdit.getText().toString().trim()));
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private boolean checkPermission() {
        Logger.i("Has " + Manifest.permission.ACCESS_FINE_LOCATION + " & "
                + Manifest.permission.WRITE_EXTERNAL_STORAGE + " permission: "
                + locationPermission + " & " + storagePermission);
        if (!locationPermission || !storagePermission) {
            PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(this,
                    new String[]{
                            Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    new PermissionsResultAction() {
                        @Override
                        public void onGranted() {
                            Logger.e("all permissions granted");
                        }

                        @Override
                        public void onDenied(String permission) {
                            String message = String.format(Locale.getDefault(), "Permission \\\"%1$s\\\" has been denied.", permission);
                            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    });
            return false;
        } else {
            return true;
        }
    }
}
