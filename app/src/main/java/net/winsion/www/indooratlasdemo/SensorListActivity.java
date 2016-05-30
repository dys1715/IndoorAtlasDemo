package net.winsion.www.indooratlasdemo;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import java.util.List;

/**
 * Created by dys on 2016/5/30 0030.
 * 查看传感器列表
 */
public class SensorListActivity extends Activity {

    private TextView mTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_list);
        mTextView = (TextView)findViewById(R.id.tv_sensors);
        mTextView.setMovementMethod(ScrollingMovementMethod.getInstance());
        getSensorList();
    }

    private void getSensorList() {
        // 获取传感器管理器
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        // 获取全部传感器列表
        List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ALL);

        // 打印每个传感器信息
        StringBuilder strLog = new StringBuilder();
        int iIndex = 1;
        for (Sensor item : sensors) {
            strLog.append(iIndex + ".");
            strLog.append("	Sensor Type - " + item.getType() + "\r\n");
            strLog.append("	Sensor Name - " + item.getName() + "\r\n");
            strLog.append("	Sensor Version - " + item.getVersion() + "\r\n");
            strLog.append("	Sensor Vendor(供应商) - " + item.getVendor() + "\r\n");
            strLog.append("	Maximum Range(最大范围) - " + item.getMaximumRange() + "\r\n");
            strLog.append("	Minimum Delay(延迟) - " + item.getMinDelay() + "\r\n");
            strLog.append("	Power(mA) - " + item.getPower() + "\r\n");
            strLog.append("	Resolution(分辨率) - " + item.getResolution() + "\r\n");
            strLog.append("\r\n");
            iIndex++;
        }
        mTextView.setText(strLog);
    }
}
/*      TYPE_ACCELEROMETER
        1:加速度传感器，单位是m/s2，测量应用于设备X、Y、Z轴上的加速度

        TYPE_MAGNETIC_FIELD
        2:磁力传感器，单位是uT(微特斯拉)，测量设备周围三个物理轴（x，y，z）的磁场

        TYPE_ORIENTATION
        3:方向传感器,测量设备围绕三个物理轴（x，y，z）的旋转角度

        TYPE_GYROSCOPE
        4:陀螺仪传感器，单位是rad/s，测量设备x、y、z三轴的角加速度

        TYPE_LIGHT
        5:光线感应传感器，单位lx，检测周围的光线强度

        TYPE_PRESSURE
        6:压力传感器，单位是hPa(百帕斯卡)，返回当前环境下的压强

        TYPE_TEMPERATURE
        7:温度传感器，目前已被TYPE_AMBIENT_TEMPERATURE替代

        TYPE_PROXIMITY
        8:距离传感器，单位是cm，用来测量某个对象到屏幕的距离

        TYPE_GRAVITY
        9:重力传感器，单位是m/s2，测量应用于设备X、Y、Z轴上的重力

        TYPE_LINEAR_ACCELERATION
        10:线性加速度传感器，单位是m/s2，该传感器是获取加速度传感器去除重力的影响得到的数据

        TYPE_ROTATION_VECTOR
        11:旋转矢量传感器，旋转矢量代表设备的方向

        TYPE_RELATIVE_HUMIDITY
        12:湿度传感器，单位是%，来测量周围环境的相对湿度

        TYPE_AMBIENT_TEMPERATURE
        13:温度传感器，单位是℃

        TYPE_MAGNETIC_FIELD_UNCALIBRATED
        14:未校准磁力传感器，提供原始的，未校准的磁场数据

        TYPE_GAME_ROTATION_VECTOR
        15:游戏动作传感器，不收电磁干扰影响

        TYPE_GYROSCOPE_UNCALIBRATED
        16:未校准陀螺仪传感器，提供原始的，未校准、补偿的陀螺仪数据，用于后期处理和融合定位数据

        TYPE_SIGNIFICANT_MOTION
        17:特殊动作触发传感器

        TYPE_STEP_DETECTOR
        18:步行检测传感器，用户每走一步就触发一次事件

        TYPE_STEP_COUNTER
        19:计步传感器

        TYPE_GEOMAGNETIC_ROTATION_VECTOR
        20:地磁旋转矢量传感器，提供手机的旋转矢量，当手机处于休眠状态时，仍可以记录设备的方位

*/
