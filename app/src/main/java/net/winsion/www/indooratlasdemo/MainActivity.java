package net.winsion.www.indooratlasdemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText mEdit;
    private List<Class> mClasses = new ArrayList<>();
    private Class mClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mClasses.add(ImageViewActivity.class);
        mClasses.add(MapViewActivity.class);
//        mClass = mClasses.get(0); //默认使用ImageViewActivity
        mClass = mClasses.get(1); //默认使用MapViewActivity

        findViewById(R.id.btn_imageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClass = mClasses.get(0); //ImageViewActivity
                Toast.makeText(getApplicationContext(), "已切换为ImageView", Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.btn_MapView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClass = mClasses.get(1); //MapViewActivity
                Toast.makeText(getApplicationContext(), "已切换为MapView", Toast.LENGTH_SHORT).show();
            }
        });
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
                startActivity(new Intent(MainActivity.this, mClass)
                        .putExtra("floorPlanId", getString(R.string.indooratlas_floor_plan_id_26f)));
            }
        });
        findViewById(R.id.text2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, mClass)
                        .putExtra("floorPlanId", getString(R.string.indooratlas_floor_plan_id_27f)));
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
}
