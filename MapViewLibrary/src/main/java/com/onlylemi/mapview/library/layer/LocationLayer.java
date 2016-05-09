package com.onlylemi.mapview.library.layer;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.view.MotionEvent;

import com.onlylemi.mapview.library.MapView;
import com.onlylemi.mapview.library.R;

/**
 * LocationLayer
 *
 */
public class LocationLayer extends MapBaseLayer {

    private boolean openCompass = false;

    // compass color
    private static final int DEFAULT_LOCATION_COLOR = 0xFF3EBFC9; //暗青蓝色
    private static final int DEFAULT_LOCATION_SHADOW_COLOR = 0xFF909090; //灰色
    private static final int DEFAULT_INDICATOR_ARC_COLOR = 0xFFFA4A8D; //粉红色
    private static final int DEFAULT_INDICATOR_CIRCLE_COLOR = 0xFF00F0FF; //亮青蓝色
    private static final float COMPASS_DELTA_ANGLE = 5.0f; //delta angle角度增量
    private float defaultLocationCircleRadius;

    private float compassLineLength;
    private float compassLineWidth;
    private float compassLocationCircleRadius;
    private float compassRadius;
    private float compassArcWidth;
    private float compassIndicatorCircleRadius;
    private float compassIndicatorGap;
    private float compassIndicatorArrowRotateDegree; //箭头旋转角度
    private float compassIndicatorCircleRotateDegree = 0;
    private float rangeIndicatorMeters; //范围指示器

    private Bitmap compassIndicatorArrowBitmap; //指南针指示箭头

    private BitmapLayer compassBitmapLayer;

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
//        locationPaint.setColor(DEFAULT_LOCATION_COLOR);
//        locationPaint.setShadowLayer(5, 3, 3, DEFAULT_LOCATION_SHADOW_COLOR);

        compassRadius = setValue(38f);
        compassLocationCircleRadius = setValue(0.5f);
        compassLineWidth = setValue(1.3f);
        compassLineLength = setValue(2.3f);
        compassArcWidth = setValue(4.0f);
        compassIndicatorCircleRadius = setValue(2.6f);
        compassIndicatorGap = setValue(15.0f);

        compassIndicatorArrowBitmap = BitmapFactory.decodeResource(mapView.getResources(),
                R.mipmap.compass);
        compassBitmapLayer = new BitmapLayer(mapView, compassIndicatorArrowBitmap, null);
    }

    @Override
    public void draw(Canvas canvas, Matrix currentMatrix, float currentZoom, float
            currentRotateDegrees) {
        if (isVisible && currentPosition != null) {
            canvas.save();
            float goal[] = {currentPosition.x, currentPosition.y};
            currentMatrix.mapPoints(goal);

            if (openCompass) {
                //画定位点
                locationPaint.setColor(mapView.getResources().getColor(R.color.ia_blue));
                canvas.drawCircle(goal[0], goal[1], defaultLocationCircleRadius, locationPaint);
                //画范围指示器
                locationPaint.setColor(mapView.getResources().getColor(R.color.ia_blue_tint));
                canvas.drawCircle(goal[0], goal[1], defaultLocationCircleRadius * rangeIndicatorMeters, locationPaint);
                //画箭头
                if (compassIndicatorArrowBitmap != null) {
                    canvas.save();
                    canvas.rotate(this.compassIndicatorArrowRotateDegree,
                            goal[0], goal[1]);
                    canvas.drawBitmap(compassIndicatorArrowBitmap,
                            goal[0] - compassIndicatorArrowBitmap.getWidth() / 2,
                            goal[1] - defaultLocationCircleRadius - compassIndicatorGap,
                            new Paint());
                    canvas.restore();
                }
            }
            canvas.restore();
        }
    }

    public boolean isOpenCompass() {
        return openCompass;
    }

    public void setOpenCompass(boolean openCompass) {
        this.openCompass = openCompass;
    }

    public PointF getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(PointF currentPosition) {
        this.currentPosition = currentPosition;
    }

    public float getCompassIndicatorCircleRotateDegree() {
        return compassIndicatorCircleRotateDegree;
    }

    public void setCompassIndicatorCircleRotateDegree(float compassIndicatorCircleRotateDegree) {
        this.compassIndicatorCircleRotateDegree = compassIndicatorCircleRotateDegree;
    }

    public float getCompassIndicatorArrowRotateDegree() {
        return compassIndicatorArrowRotateDegree;
    }

    public void setCompassIndicatorArrowRotateDegree(float compassIndicatorArrowRotateDegree) {
        this.compassIndicatorArrowRotateDegree = compassIndicatorArrowRotateDegree;
    }

    public void setRangeIndicatorMeters(float rangeIndicatorMeters) {
        this.rangeIndicatorMeters = rangeIndicatorMeters;
    }
}
