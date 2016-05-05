package net.winsion.www.indooratlasdemo;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.PointF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.indooratlas.android.sdk.IALocation;
import com.indooratlas.android.sdk.IALocationListener;
import com.indooratlas.android.sdk.IALocationManager;
import com.indooratlas.android.sdk.IALocationRequest;
import com.indooratlas.android.sdk.IARegion;
import com.indooratlas.android.sdk.resources.IAFloorPlan;
import com.indooratlas.android.sdk.resources.IALatLng;
import com.indooratlas.android.sdk.resources.IALocationListenerSupport;
import com.indooratlas.android.sdk.resources.IAResourceManager;
import com.indooratlas.android.sdk.resources.IAResult;
import com.indooratlas.android.sdk.resources.IAResultCallback;
import com.indooratlas.android.sdk.resources.IATask;
import com.orhanobut.logger.Logger;

import net.winsion.www.indooratlasdemo.bean.PointXY;
import net.winsion.www.indooratlasdemo.imageview.BlueDotView;
import net.winsion.www.indooratlasdemo.utils.CommonMethord;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImageViewActivity extends FragmentActivity implements SensorEventListener {

    private static final String TAG = "IndoorAtlasExample";
    private static final int EMPTY_MSG = 10000;

    // blue dot radius in meters
    private static final float dotRadius = 0.25f;

    private IALocationManager mIALocationManager;
    private IAResourceManager mFloorPlanManager;
    private IATask<IAFloorPlan> mPendingAsyncResult;
    private IAFloorPlan mFloorPlan;
    private BlueDotView mImageView;
    private long mDownloadId;
    private DownloadManager mDownloadManager;
    private TextView mTextView;
    private ProgressDialog mProgressDialog;
    private SensorManager mSensorManager;
    private List<PointXY> mPointXYList = new ArrayList<>();
    private Button savePoints, showPoints;

    private IALocationListener mLocationListener = new IALocationListenerSupport() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onLocationChanged(IALocation location) {
            if (mFloorPlan != null) {
                Logger.i(mFloorPlan.toString());
                IALatLng latLng = new IALatLng(location.getLatitude(), location.getLongitude());
                PointF point = mFloorPlan.coordinateToPoint(latLng);
                mPointXYList.add(new PointXY(point.x, point.y, location.getLatitude(), location.getLongitude()));

                mTextView.setText("latitude纬度:" + location.getLatitude() + '\n'
                        + "longitude经度:" + location.getLongitude() + '\n'
                        + "Accuracy精度:" + location.getAccuracy() + '\n'
                        + "Altitude高度:" + location.getAltitude() + '\n'
                        + "FloorLevel:" + location.getFloorLevel() + '\n'
                        + "Bearing方位:" + location.getBearing() + '\n'
                        + "Region:" + location.getRegion().toString()
                        + "pointX:" + point.x + "|pointY:" + point.y + '\n'
                        + "bitmapWidth&Height:" + mFloorPlan.getBitmapWidth() + "*" + mFloorPlan.getBitmapHeight());

                if (mImageView != null && mImageView.isReady()) {
                    mImageView.setDotCenter(point);
                    mImageView.setRangeIndicatorMeters(location.getAccuracy());
                    mImageView.postInvalidate();
                    if (mProgressDialog.isShowing()) {
                        mHandler.sendEmptyMessage(EMPTY_MSG);
                    }
                }
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            super.onStatusChanged(provider, status, extras);
        }
    };

    private IARegion.Listener mRegionListener = new IARegion.Listener() {

        @Override
        public void onEnterRegion(IARegion region) {
            Logger.i(region.toString());
            //获取配置的floorId
            if (region.getType() == IARegion.TYPE_FLOOR_PLAN) {
                String id = region.getId();
                Log.i(TAG, "floorPlan changed to " + id);
                Toast.makeText(ImageViewActivity.this, id, Toast.LENGTH_SHORT).show();
                fetchFloorPlan(id);
            }

        }

        @Override
        public void onExitRegion(IARegion region) {
            // leaving a previously entered region
        }

    };

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mProgressDialog.dismiss();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);
        // prevent the screen going to sleep while app is on foreground
        findViewById(android.R.id.content).setKeepScreenOn(true);

        mTextView = (TextView) findViewById(R.id.text_img);
        mImageView = (BlueDotView) findViewById(R.id.imageView);
        savePoints = (Button) findViewById(R.id.btn_save_point);
        showPoints = (Button) findViewById(R.id.btn_get_points);

        savePoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //把坐标信息保存到根目录points.txt文件
                boolean isSave = CommonMethord.saveFile(CommonMethord.ListToStr(mPointXYList));
                if (isSave) {
                    Toast.makeText(getApplicationContext(), "坐标点保存成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "坐标点保存失败", Toast.LENGTH_SHORT).show();
                }
            }
        });

        showPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pointsData = CommonMethord.getFile(CommonMethord.getCurrentTime());
                new AlertDialog.Builder(ImageViewActivity.this)
                        .setTitle("points data")
                        .setMessage(pointsData)
                        .create()
                        .show();
            }
        });

        mProgressDialog = new ProgressDialog(ImageViewActivity.this);
        mProgressDialog.show();
        mProgressDialog.setMessage("初始化地图数据...");

        mDownloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        mIALocationManager = IALocationManager.create(this);
        mFloorPlanManager = IAResourceManager.create(this);

        //配置属性参数（然而并没有用，都是云端决定的。。。）
        (new IALocation.Builder())
                .withAccuracy(0.1f)
                .withAltitude(1000)
                .build();

        //获取传感器服务
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        
        /* optional setup of floor plan id
           if setLocation is not called, then location manager tries to find
           location automatically */
        setLocation(getString(R.string.indooratlas_floor_plan_id));

    }

    private void setLocation(String floorPlanId) {
        if (!TextUtils.isEmpty(floorPlanId)) {
            final IALocation location = IALocation.from(IARegion.floorPlan(floorPlanId));
            mIALocationManager.setLocation(location);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // starts receiving location updates
        mIALocationManager.requestLocationUpdates(IALocationRequest.create(), mLocationListener);
        mIALocationManager.registerRegionListener(mRegionListener);
        //注册方向传感器
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_NORMAL);
        registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    @Override
    protected void onPause() {
        super.onPause();
        mIALocationManager.removeLocationUpdates(mLocationListener);
        mIALocationManager.unregisterRegionListener(mRegionListener);
        mSensorManager.unregisterListener(this);
        unregisterReceiver(onComplete);
        Toast.makeText(getApplicationContext(), "定位中止", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mIALocationManager.destroy();
    }

    /**
     * Methods for fetching floor plan data and bitmap image.
     * Method {@link #fetchFloorPlan(String id)} fetches floor plan data including URL to bitmap
     */

     /*  Broadcast receiver for floor plan image download */
    private BroadcastReceiver onComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0L);
            if (id != mDownloadId) {
                Log.w(TAG, "Ignore unrelated download");
                return;
            }
            Log.w(TAG, "Image download completed");
            Bundle extras = intent.getExtras();
            DownloadManager.Query q = new DownloadManager.Query();
            q.setFilterById(extras.getLong(DownloadManager.EXTRA_DOWNLOAD_ID));
            Cursor c = mDownloadManager.query(q);

            if (c.moveToFirst()) {
                int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
                if (status == DownloadManager.STATUS_SUCCESSFUL) {
                    // process download
                    String filePath = c.getString(c.getColumnIndex(
                            DownloadManager.COLUMN_LOCAL_FILENAME));
                    showFloorPlanImage(filePath);
                }
            }
            c.close();
        }
    };

    private void showFloorPlanImage(String filePath) {
        Log.w(TAG, "showFloorPlanImage: " + filePath + "; MetersToPixels=" + mFloorPlan.getMetersToPixels());
        mImageView.setRadius(mFloorPlan.getMetersToPixels() * dotRadius);
        mImageView.setImage(ImageSource.uri(filePath));
//        mImageView.setCompassIndicatorArrowRotateDegree(60);
//        mImageView.setDotCenter(new PointF(500, 500));
        mProgressDialog.setMessage("请移动方位以完成初始化操作");
    }

    /**
     * Fetches floor plan data from IndoorAtlas server. Some room for cleaning up!!
     */
    private void fetchFloorPlan(String id) {
        cancelPendingNetworkCalls();
        final IATask<IAFloorPlan> asyncResult = mFloorPlanManager.fetchFloorPlanWithId(id);
        mPendingAsyncResult = asyncResult;
        if (mPendingAsyncResult != null) {
            mPendingAsyncResult.setCallback(new IAResultCallback<IAFloorPlan>() {
                @Override
                public void onResult(IAResult<IAFloorPlan> result) {
                    Log.e(TAG, "fetch floor plan result:" + result);
                    if (result.isSuccess() && result.getResult() != null) {
                        mFloorPlan = result.getResult();
                        String fileName = mFloorPlan.getId() + ".jpg";
                        String filePath = Environment.getExternalStorageDirectory() + "/"
                                + Environment.DIRECTORY_DOWNLOADS + "/" + fileName;
                        File file = new File(filePath);
                        if (!file.exists()) {
                            DownloadManager.Request request =
                                    new DownloadManager.Request(Uri.parse(mFloorPlan.getUrl()));
                            request.setDescription("IndoorAtlas floor plan");
                            request.setTitle("Floor plan");
                            // requires android 3.2 or later to compile
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                request.allowScanningByMediaScanner();
                                request.setNotificationVisibility(DownloadManager.
                                        Request.VISIBILITY_HIDDEN);
                            }
                            request.setDestinationInExternalPublicDir(Environment.
                                    DIRECTORY_DOWNLOADS, fileName);

                            mDownloadId = mDownloadManager.enqueue(request);
                        } else {
                            showFloorPlanImage(filePath);
                        }
                    } else {
                        // do something with error
                        if (!asyncResult.isCancelled()) {
                            Toast.makeText(ImageViewActivity.this,
                                    (result.getError() != null
                                            ? "error loading floor plan: " + result.getError()
                                            : "access to floor plan denied"),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }, Looper.getMainLooper()); // deliver callbacks in main thread
        }
    }

    private void cancelPendingNetworkCalls() {
        if (mPendingAsyncResult != null && !mPendingAsyncResult.isCancelled()) {
            mPendingAsyncResult.cancel();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (mImageView != null && mImageView.isReady()) {
            float mapDegree = 85; // the rotate between reality map to northern
            float degree = 0;
            if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
                degree = event.values[0];
            }

            mImageView.setCompassIndicatorArrowRotateDegree(mapDegree + degree);
            mImageView.postInvalidate();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}

