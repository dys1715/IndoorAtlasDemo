package net.winsion.www.indooratlasdemo.view;

import android.animation.TypeEvaluator;
import android.graphics.PointF;

/**
 * Created by D on 2016/5/6 0006.
 * 小点移动操作
 */
public class PointEvaluator implements TypeEvaluator {
    @Override
    public Object evaluate(float fraction, Object startValue, Object endValue) {
        PointF startPoint = (PointF) startValue;
        PointF endPoint = (PointF) endValue;
        float x = startPoint.x + fraction * (endPoint.x - startPoint.x);
        float y = startPoint.y + fraction * (endPoint.y - startPoint.y);
        PointF point = new PointF(x, y);
        return point;
    }
}
