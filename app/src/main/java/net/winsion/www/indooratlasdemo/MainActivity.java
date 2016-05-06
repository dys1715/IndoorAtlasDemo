package net.winsion.www.indooratlasdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.indooratlas.android.sdk.IALocation;
import com.indooratlas.android.sdk.IALocationListener;
import com.indooratlas.android.sdk.IALocationManager;
import com.indooratlas.android.sdk.IARegion;
import com.orhanobut.logger.Logger;

public class MainActivity extends AppCompatActivity {

    private TextView mTextView;
    private IALocationManager mIALocationManager;

    private IALocationListener mIaLocationListener = new IALocationListener() {
        @Override
        public void onLocationChanged(IALocation iaLocation) {
//            Logger.d("纬度=" + iaLocation.getLatitude() + ";经度=" + iaLocation.getLongitude());
            mTextView.setText("");
            mTextView.setText("latitude纬度:" + iaLocation.getLatitude() + '\n'
                    + "longitude经度:" + iaLocation.getLongitude() + '\n'
                    + "Accuracy精度:" + iaLocation.getAccuracy() + '\n'
                    + "Altitude高度:" + iaLocation.getAltitude() + '\n'
                    + "FloorLevel:" + iaLocation.getFloorLevel() + '\n'
                    + "Bearing方位:" + iaLocation.getBearing() + '\n' //Returns bearing in degrees, in range of (0.0, 360.0].
                    + "Region:" + iaLocation.getRegion().toString() + '\n');
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
//            Logger.d(s + ";status=" + i);
        }
    };

    private IARegion.Listener mRegionListener = new IARegion.Listener() {
        @Override
        public void onEnterRegion(IARegion iaRegion) {
            Logger.init("floorPlanId");
            Logger.i(iaRegion.getId());
        }

        @Override
        public void onExitRegion(IARegion iaRegion) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.text1);
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ImageViewActivity.class));
            }
        });

        mIALocationManager = IALocationManager.create(this);


    }

    @Override
    protected void onResume() {
        super.onResume();
//        mIALocationManager.requestLocationUpdates(IALocationRequest.create(), mIaLocationListener);
//        mIALocationManager.registerRegionListener(mRegionListener);

    }

    @Override
    protected void onPause() {
        super.onPause();
//        mIALocationManager.removeLocationUpdates(mIaLocationListener);
//        mIALocationManager.unregisterRegionListener(mRegionListener);
    }

    @Override
    protected void onDestroy() {
        mIALocationManager.destroy();
        super.onDestroy();
    }
}
