package net.winsion.www.indooratlasdemo.imageview;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.animation.TranslateAnimation;

import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import net.winsion.www.indooratlasdemo.R;

/**
 * Extends great ImageView library by Dave Morrissey. See more:
 * https://github.com/davemorrissey/subsampling-scale-image-view.
 */
public class BlueDotView extends SubsamplingScaleImageView {

    private float radius = 1.0f;
    private PointF dotCenter = null;
    private Bitmap compassIndicatorArrowBitmap; //指南针箭头
    private float compassIndicatorArrowRotateDegree; //箭头旋转角度
    private float rangeIndicatorMeters; //范围指示器
    private float defaultLocationCircleRadius;
    private float compassIndicatorGap;
    private float compassIndicatorCircleRotateDegree = 0;
    private float compassRadius;
    private float compassArcWidth;
    private float compassIndicatorCircleRadius;
    private Paint indicatorArcPaint;
    private Paint indicatorCirclePaint;
    private Canvas mCanvas;

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public void setDotCenter(PointF dotCenter) {
        this.dotCenter = dotCenter;
    }

    public BlueDotView(Context context) {
        this(context, null);
    }

    public BlueDotView(Context context, AttributeSet attr) {
        super(context, attr);
        initialise();
    }

    private void initialise() {
        setWillNotDraw(false);
        setPanLimit(SubsamplingScaleImageView.PAN_LIMIT_CENTER);
        compassIndicatorArrowBitmap = BitmapFactory.decodeResource(this.getContext().getResources(), R.mipmap.compass);
        // setting dufault values
        defaultLocationCircleRadius = setValue(8f);
        compassIndicatorGap = setValue(15.0f);
        compassRadius = setValue(38f);
        compassArcWidth = setValue(4.0f);
        compassIndicatorCircleRadius = setValue(2.6f);
        // default indicatorArcPaint
        indicatorArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        indicatorArcPaint.setStyle(Paint.Style.STROKE);
        indicatorArcPaint.setColor(0xFFFA4A8D);
        indicatorArcPaint.setStrokeWidth(compassArcWidth);
        // default indicatorCirclePaint
        indicatorCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        indicatorCirclePaint.setAntiAlias(true);
        indicatorCirclePaint.setStyle(Paint.Style.FILL);
        indicatorCirclePaint.setShadowLayer(3, 1, 1, 0xFF909090);
        indicatorCirclePaint.setColor(0xFF00F0FF);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!isReady()) {
            return;
        }

        if (dotCenter != null) {
            PointF vPoint = sourceToViewCoord(dotCenter);
            float scaledRadius = getScale() * radius;
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.FILL);
            //画范围指示器
            paint.setColor(getResources().getColor(R.color.ia_blue_tint));
            canvas.drawCircle(vPoint.x, vPoint.y, scaledRadius * rangeIndicatorMeters, paint);
            //画定位点
            paint.setColor(getResources().getColor(R.color.ia_blue));
            canvas.drawCircle(vPoint.x, vPoint.y, scaledRadius, paint);
            //画箭头
            if (compassIndicatorArrowBitmap != null) {
                canvas.save();
                canvas.rotate(this.compassIndicatorArrowRotateDegree,
                        vPoint.x, vPoint.y);
                canvas.drawBitmap(compassIndicatorArrowBitmap,
                        vPoint.x - compassIndicatorArrowBitmap.getWidth() / 2,
                        vPoint.y - defaultLocationCircleRadius - compassIndicatorGap,
                        new Paint());
                canvas.restore();
                //画圆弧线
//                if (360 - (this.compassIndicatorArrowRotateDegree - this.compassIndicatorCircleRotateDegree) > 180) {
//                    canvas.drawArc(new RectF(vPoint.x - compassRadius,
//                                    vPoint.y - compassRadius,
//                                    vPoint.x + compassRadius,
//                                    vPoint.y + compassRadius),
//                            -90 + this.compassIndicatorCircleRotateDegree,
//                            (this.compassIndicatorArrowRotateDegree - this.compassIndicatorCircleRotateDegree),
//                            false, indicatorArcPaint);
//                } else {
//                    canvas.drawArc(new RectF(vPoint.x - compassRadius,
//                                    vPoint.y - compassRadius,
//                                    vPoint.x + compassRadius,
//                                    vPoint.y + compassRadius),
//                            -90 + this.compassIndicatorArrowRotateDegree,
//                            360 - (this.compassIndicatorArrowRotateDegree - this
//                                    .compassIndicatorCircleRotateDegree),
//                            false, indicatorArcPaint);
//                }

            }
        }
    }

    protected float setValue(float value) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, getResources().getDisplayMetrics());
    }

    public void setCompassIndicatorArrowRotateDegree(float compassIndicatorArrowRotateDegree) {
        this.compassIndicatorArrowRotateDegree = compassIndicatorArrowRotateDegree;
    }

    public void setRangeIndicatorMeters(float rangeIndicatorMeters){
        this.rangeIndicatorMeters = rangeIndicatorMeters;
    }
}
