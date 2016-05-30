package com.onlylemi.mapview.library.layer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Picture;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewTreeObserver;

import com.onlylemi.mapview.library.MapView;

/**
 * MapLayer
 */
public class MapLayer extends MapBaseLayer {

    private static final String TAG = "MapLayer";

    //    private Picture image = null;
    private Bitmap mBitmap = null;
    private boolean hasMeasured;
    private Paint mPaint;

    public MapLayer(MapView mapView) {
        super(mapView);
        level = MAP_LEVEL;
    }

//    public void setImage(Picture image) {
//        this.image = image;
//
//        if (mapView.getWidth() == 0) {
//            ViewTreeObserver vto = mapView.getViewTreeObserver();
//            vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//                public boolean onPreDraw() {
//                    if (!hasMeasured) {
//                        initMapLayer();
//                        hasMeasured = true;
//                    }
//                    return true;
//                }
//            });
//        } else {
//            initMapLayer();
//        }
//    }

    public void setImage(Bitmap bitmap) {
        mBitmap = bitmap;

        if (mapView.getWidth() == 0) {
            ViewTreeObserver vto = mapView.getViewTreeObserver();
            vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                public boolean onPreDraw() {
                    if (!hasMeasured) {
                        initMapLayer();
                        hasMeasured = true;
                    }
                    return true;
                }
            });
        } else {
            initMapLayer();
        }
    }

    /**
     * init map image layer
     */
    private void initMapLayer() {
        mPaint = new Paint();
//        float zoom = getInitZoom(mapView.getWidth(), mapView.getHeight(), image.getWidth(), image.getHeight());
        float zoom = getInitZoom(mapView.getWidth(), mapView.getHeight(), mBitmap.getWidth(), mBitmap.getHeight());
        Log.i(TAG, "zoom:" + zoom);
        mapView.setCurrentZoom(zoom, 0, 0);

//        float width = mapView.getWidth() - zoom * image.getWidth();
//        float height = mapView.getHeight() - zoom * image.getHeight();
        float width = mapView.getWidth() - zoom * mBitmap.getWidth();
        float height = mapView.getHeight() - zoom * mBitmap.getHeight();

        mapView.translate(width / 2, height / 2);
    }

    /**
     * calculate init zoom
     *
     * @param viewWidth
     * @param viewHeight
     * @param imageWidth
     * @param imageHeight
     * @return
     */
    private float getInitZoom(float viewWidth, float viewHeight, float imageWidth, float imageHeight) {
        float widthRatio = viewWidth / imageWidth;
        float heightRatio = viewHeight / imageHeight;

        Log.i(TAG, "widthRatio:" + widthRatio);
        Log.i(TAG, "widthRatio:" + heightRatio);

        if (widthRatio * imageHeight <= viewHeight) {
            return widthRatio;
        } else if (heightRatio * imageWidth <= viewWidth) {
            return heightRatio;
        }
        return 0;
    }

    @Override
    public void onTouch(MotionEvent event) {

    }

    @Override
    public void draw(Canvas canvas, Matrix currentMatrix, float currentZoom, float currentRotateDegrees) {
        canvas.save();
        canvas.setMatrix(currentMatrix);
        if (mBitmap != null) {
            canvas.drawBitmap(mBitmap, 0, 0, null);
        } else {
            Log.e(TAG, "mBitmap is null,load map fail");
        }
//        if (image != null) {
//            canvas.drawPicture(image);
//        }else {
//            Log.e(TAG,"image is null,load map fail");
//        }
        canvas.restore();
    }

//    public Picture getImage() {
//        return image;
//    }

    public Bitmap getImage() {
        return mBitmap;
    }
}
