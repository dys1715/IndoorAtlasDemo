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
import android.graphics.BitmapFactory;
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
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import com.onlylemi.mapview.library.MapView;
import com.onlylemi.mapview.library.MapViewListener;
import com.onlylemi.mapview.library.layer.LocationLayer;
import com.orhanobut.logger.Logger;

import net.winsion.www.indooratlasdemo.bean.Point;
import net.winsion.www.indooratlasdemo.utils.CommonMethord;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dys on 2016/5/8 0008.
 * 室内定位
 */
public class MapViewActivity extends AppCompatActivity implements View.OnClickListener, SensorEventListener {

    private static final int EMPTY_MSG = 10000;
    private static final int MAP_FIXED = 1; //地图固定
    private static final int MAP_FOLLOW = 2; //地图跟随
    private MapView mapView;
    private LocationLayer mLocationLayer;
    private IALocationManager mIALocationManager;
    private IAResourceManager mFloorPlanManager;
    private IATask<IAFloorPlan> mPendingAsyncResult;
    private IAFloorPlan mFloorPlan;
    private long mDownloadId;
    private DownloadManager mDownloadManager;
    private TextView mTextView;
    private ProgressDialog mProgressDialog;
    private SensorManager mSensorManager;
    private List<Point> mPointXYList = new ArrayList<>();
    private Button savePoints, showPoints, changeMode;
    private int mapMode;
    private float mapDegree = 85; // the rotate between reality map to northern
    private float degree = 0;
    private PointF mPointF;
    private String imgPath;

    private IALocationListener mLocationListener = new IALocationListenerSupport() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onLocationChanged(IALocation location) {
            if (mFloorPlan != null) {
//                Logger.i(mFloorPlan.toString());
                IALatLng latLng = new IALatLng(location.getLatitude(), location.getLongitude());
                mPointF = mFloorPlan.coordinateToPoint(latLng);
//                mPointXYList.add(new Point(point.x, point.y, location.getLatitude(), location.getLongitude()));

                mTextView.setText("FloorName:" + mFloorPlan.getName() + '\n'
                        + "latitude纬度:" + location.getLatitude() + '\n'
                        + "longitude经度:" + location.getLongitude() + '\n'
                        + "Accuracy精度:" + location.getAccuracy() + '\t'
                        + "| Bearing方位:" + location.getBearing() + '\n'
                        + "Region:" + location.getRegion().toString()
                        + "pointX:" + mPointF.x + " | pointY:" + mPointF.y + '\n'
                        + "bitmapWidth&Height:" + mFloorPlan.getBitmapWidth() + "*" + mFloorPlan.getBitmapHeight());

                if (mLocationLayer != null) {
                    mLocationLayer.setCurrentPosition(mPointF);
                    mLocationLayer.setRangeIndicatorMeters(location.getAccuracy());
                    mapView.refresh();
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
                Toast.makeText(getApplicationContext(), id, Toast.LENGTH_SHORT).show();
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);

        initView();
        mDownloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        mIALocationManager = IALocationManager.create(this);
        mFloorPlanManager = IAResourceManager.create(this);
        //获取传感器服务
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        setFloorPlanId(getIntent().getStringExtra("floorPlanId"));
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
     * 设置floorPlanId
     *
     * @param floorPlanId 楼层id
     */
    private void setFloorPlanId(String floorPlanId) {
        if (!TextUtils.isEmpty(floorPlanId)) {
            final IALocation location = IALocation.from(IARegion.floorPlan(floorPlanId));
            mIALocationManager.setLocation(location);
        }
    }

    private void initView() {
        Logger.init(this.getClass().getName());
        mapView = (MapView) findViewById(R.id.map_view);
        mTextView = (TextView) findViewById(R.id.text_img);
        savePoints = (Button) findViewById(R.id.btn_save_point);
        showPoints = (Button) findViewById(R.id.btn_get_points);
        changeMode = (Button) findViewById(R.id.btn_change_display_mode);
        mTextView.setOnClickListener(this);
        savePoints.setOnClickListener(this);
        showPoints.setOnClickListener(this);
        changeMode.setOnClickListener(this);
        //默认地图固定
        mapMode = MAP_FIXED;
        mProgressDialog = new ProgressDialog(MapViewActivity.this);
        mProgressDialog.setMessage("初始化地图数据...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_img:
//                mapView.loadMap(BitmapFactory.decodeFile(imgPath));
                break;
            case R.id.btn_save_point:
                //把坐标信息保存到根目录points.txt文件
                boolean isSave = CommonMethord.saveFile(CommonMethord.ListToStr(mPointXYList));
                if (isSave) {
                    Toast.makeText(getApplicationContext(), "坐标点保存成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "坐标点保存失败", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_get_points:
                String pointsData = CommonMethord.getFile(CommonMethord.getCurrentTime());
                new AlertDialog.Builder(MapViewActivity.this)
                        .setTitle("points data")
                        .setMessage(pointsData)
                        .create()
                        .show();
                break;
            case R.id.btn_change_display_mode:
                if (mapMode == MAP_FIXED) {
                    mapMode = MAP_FOLLOW;
                    changeMode.setText("切换为地图固定");
                } else {
                    mapMode = MAP_FIXED;
                    mapView.setCurrentRotateDegrees(0);
                    changeMode.setText("切换为地图跟随");
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (mapView != null && mLocationLayer != null) {

            if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
                degree = event.values[0];
            }
            showPoints.setText(degree + "");

            if (mapMode == MAP_FIXED) {
                //设置指示器旋转角度
                mLocationLayer.setCompassIndicatorArrowRotateDegree(mapDegree + degree);
            }
            if (mapMode == MAP_FOLLOW) {
                //指针不动
                mLocationLayer.setCompassIndicatorArrowRotateDegree(0);
                //设置图片旋转
                mapView.setCurrentRotateDegrees(getTargetDircetion(degree) - mapDegree);

            }
            mapView.refresh();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /**
     * 修正方向传感器旋转角度
     *
     * @param degree
     * @return
     */
    private float getTargetDircetion(float degree) {
        return (degree * -1.0f + 720) % 360;
    }

    /**
     * Broadcast receiver for floor plan image download
     */
    private BroadcastReceiver onComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0L);
            if (id != mDownloadId) {
                Logger.w("Ignore unrelated download");
                return;
            }
            Logger.w("Image download completed");
            Bundle extras = intent.getExtras();
            DownloadManager.Query q = new DownloadManager.Query();
            q.setFilterById(extras.getLong(DownloadManager.EXTRA_DOWNLOAD_ID));
            Cursor c = mDownloadManager.query(q);

            if (c.moveToFirst()) {
                int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
                if (status == DownloadManager.STATUS_SUCCESSFUL) {
                    // process download
                    String filePath = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
                    showFloorPlanImage(filePath);
                }
            }
            c.close();
        }
    };

    /**
     * 加载地图
     *
     * @param filePath
     */
    private void showFloorPlanImage(String filePath) {
//        Logger.w("showFloorPlanImage: " + filePath + "; MetersToPixels=" + mFloorPlan.getMetersToPixels());
        if (mapView.getLayers()!= null && mapView.getLayers().size() < 2) {
            mapView.loadMap(BitmapFactory.decodeFile(filePath));
            mapView.setMapViewListener(new MapViewListener() {
                @Override
                public void onMapLoadSuccess() {

                    mLocationLayer = new LocationLayer(mapView);
                    mLocationLayer.setCompassIndicatorArrowRotateDegree(0);
                    mapView.addLayer(mLocationLayer);
//                    mapView.refresh();
                }

                @Override
                public void onMapLoadFail() {
                    Logger.e(">>>>>>>>>onMapLoadFail>>>>>");
                }
            });
        }
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
                    Logger.i("fetch floor plan result:" + result);
                    if (result.isSuccess() && result.getResult() != null) {
                        mFloorPlan = result.getResult();
                        String fileName = mFloorPlan.getId() + ".jpg";
                        String filePath = Environment.getExternalStorageDirectory() + "/"
                                + Environment.DIRECTORY_DOWNLOADS + "/" + fileName;
                        imgPath = filePath;
                        File file = new File(filePath);
                        if (!file.exists()) {
                            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(mFloorPlan.getUrl()));
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
                            Toast.makeText(getApplicationContext(),
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
}
