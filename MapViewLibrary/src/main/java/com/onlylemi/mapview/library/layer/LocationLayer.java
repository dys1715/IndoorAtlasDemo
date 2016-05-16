package com.onlylemi.mapview.library.layer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.MotionEvent;

import com.onlylemi.mapview.library.MapView;
import com.onlylemi.mapview.library.R;

/**
 * LocationLayer
 */
public class LocationLayer extends MapBaseLayer {

    private boolean openCompass = false;

    // compass color
    private static final int DEFAULT_LOCATION_COLOR = 0xFF3EBFC9; //暗青蓝色
    private static final int DEFAULT_LOCATION_SHADOW_COLOR = 0xFF909090; //灰色
    private static final int DEFAULT_INDICATOR_ARC_COLOR = 0xFFFA4A8D; //粉红色
    private static final int DEFAULT_INDICATOR_CIRCLE_COLOR = 0xFF00F0FF; //亮青蓝色
    private static final float COMPASS_DELTA_ANGLE = 5.0f; //delta angle角度增量
    private static final int BLUE_DOT = 0xff00a5f5; //定位点蓝色
    private static final int RANGE_INDICATOR = 0x3809c5f1; //范围指示器蓝色

    private float defaultLocationCircleRadius;

    private float compassIndicatorGap;
    private float compassIndicatorArrowRotateDegree; //箭头旋转角度
    private float rangeIndicatorMeters; //范围指示器
    private float[] goal = new float[2];

    private Bitmap compassIndicatorArrowBitmap; //指南针指示箭头


    private Paint locationPaint;

    private PointF currentPosition = null;

    public LocationLayer(MapView mapView) {
        this(mapView, null);
    }

    @Override
    public void onTouch(MotionEvent event) {

    }

    public LocationLayer(MapView mapView, PointF currentPosition) {
        this(mapView, currentPosition, true); //默认打开
    }

    public LocationLayer(MapView mapView, PointF currentPosition, boolean openCompass) {
        super(mapView);
        this.currentPosition = currentPosition;
        this.openCompass = openCompass;

        level = LOCATION_LEVEL;
        initLayer();
    }

    private void initLayer() {
        // setting dufault values
        defaultLocationCircleRadius = setValue(5f);

        // default locationPaint
        locationPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        locationPaint.setAntiAlias(true);
        locationPaint.setStyle(Paint.Style.FILL);

        compassIndicatorGap = setValue(15.0f);

        compassIndicatorArrowBitmap = BitmapFactory.decodeResource(mapView.getResources(), R.mipmap.compass);
    }

    @Override
    public void draw(Canvas canvas, Matrix currentMatrix, float currentZoom, float currentRotateDegrees) {
        if (isVisible && currentPosition != null) {
            canvas.save();
            goal[0] = currentPosition.x;
            goal[1] = currentPosition.y;
            currentMatrix.mapPoints(goal);

            if (openCompass) {
                //画定位点
                locationPaint.setColor(BLUE_DOT);
                canvas.drawCircle(goal[0], goal[1], defaultLocationCircleRadius, locationPaint);
                //画范围指示器
                locationPaint.setColor(RANGE_INDICATOR);
                canvas.drawCircle(goal[0], goal[1], defaultLocationCircleRadius * rangeIndicatorMeters, locationPaint);
                //画箭头
                if (compassIndicatorArrowBitmap != null) {
                    canvas.save();
                    canvas.rotate(this.compassIndicatorArrowRotateDegree, goal[0], goal[1]);
                    locationPaint.setColor(BLUE_DOT);
                    canvas.drawBitmap(compassIndicatorArrowBitmap,
                            goal[0] - compassIndicatorArrowBitmap.getWidth() / 2,
                            goal[1] - defaultLocationCircleRadius - compassIndicatorGap,
                            locationPaint);
                    canvas.restore();
                }
            }
            canvas.restore();
        }
    }

    public void setCurrentPosition(PointF currentPosition) {
        this.currentPosition = currentPosition;
    }

    public void setCompassIndicatorArrowRotateDegree(float compassIndicatorArrowRotateDegree) {
        this.compassIndicatorArrowRotateDegree = compassIndicatorArrowRotateDegree;
    }

    public void setRangeIndicatorMeters(float rangeIndicatorMeters) {
        this.rangeIndicatorMeters = rangeIndicatorMeters;
    }
}
