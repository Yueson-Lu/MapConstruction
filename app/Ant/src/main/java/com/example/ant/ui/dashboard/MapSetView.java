package com.example.ant.ui.dashboard;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.example.ant.R;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class MapSetView extends SurfaceView implements SurfaceHolder.Callback {
    //    SurfaceVice类控制器
    private SurfaceHolder surfaceHolder;
    private Paint pointPaint;
    private Path pointPath;
    private Paint navigationPaint;
    private Path navigationPath;
    private float startX;
    private float startY;
    private float canvasWidth;
    private float canvasHeight;
    private float pointX;
    private float pointY;


    //    绘制自适应缩放标志
    private float mapZoom ;
    //图标控制大小
    private Rect mSrcRect, mDestRect;

    public MapSetView(Context context) {
        //....
        super(context);
        surfaceHolder = this.getHolder();//获取holder
//        Log.i(surfaceHolder.toString(),surfaceHolder.toString()+"p======================================");
        surfaceHolder.addCallback(this);
        pointPaint = new Paint();
        pointPath = new Path();
        navigationPaint = new Paint();
        navigationPath = new Path();
//        线条效果
//        PathEffect pathEffect = new DashPathEffect(new float[]{20f, 15f, 10f, 5f}, 0);
//        paint.setPathEffect(pathEffect);
        pointPaint.setStrokeWidth(8f);
        pointPaint.setStrokeJoin(Paint.Join.ROUND);
        pointPaint.setAntiAlias(true);
        pointPaint.setStyle(Paint.Style.STROKE);
        pointPaint.setColor(Color.rgb(60, 150, 200));

        navigationPaint.setStrokeWidth(3f);
        navigationPaint.setColor(Color.GREEN);
        navigationPaint.setStyle(Paint.Style.STROKE);
//画布初始化
        setZOrderOnTop(true);//使surfaceview放到最顶层
        getHolder().setFormat(PixelFormat.TRANSLUCENT);//使窗口支持透明度

        mapZoom = 5;
    }

    //    用户导航行走路段处理
    public void setPaint(float size) {
        pointPaint.setStrokeWidth(size);
//        paint.setColor(Color.RED);
        //        PathEffect pathEffect = new DashPathEffect(new float[]{20f, 15f, 10f, 5f}, 0);
//        paint.setPathEffect(pathEffect);
    }


    protected void paint(Canvas canvas, ArrayList<Float> points, ArrayList<Integer> navigations) {
        //这里的代码跟继承View时OnDraw中一样
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
//        path.rewind();
        if (!points.isEmpty() && null != points) {
//            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
//            canvas.drawPaint(paint);
//            invalidate();
//            paint.setStrokeWidth(10f);
//            paint.setStrokeJoin(Paint.Join.ROUND);
//            paint.setAntiAlias(true);
//            paint.setStyle(Paint.Style.STROKE);
//            paint.setColor(Color.rgb(60, 150, 200));

            pointPath.reset();
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);//绘制透明色
            canvasWidth = canvas.getWidth();
            canvasHeight = canvas.getHeight();
            startX = canvasWidth / 2;
            startY = canvasHeight / 2;
            pointPath.moveTo(startX, startY);
//        Log.i("size", points.size() + "");
            canvas.drawColor(getSolidColor());

//            图标
            for (int i = 0; i <= points.size() - 2; i = i + 2) {
                pointX = startX + points.get(i) * mapZoom;
                pointY = startY - points.get(i + 1) * mapZoom;
                pointPath.lineTo(pointX, pointY);
//            path.moveTo((Float) points.get(i),(Float) points.get(i+1));
//            Log.i("XY", points.get(i) + "    " + points.get(i + 1) + "");
                if (pointX > 0.9 * canvasWidth || pointY > 0.9 * canvasHeight || pointX < 0.1 * canvasWidth || pointY < 0.1 * canvasHeight) {
                    mapZoom = mapZoom * 0.6f;
                    canvas.scale(mapZoom,mapZoom,canvasWidth/2,canvasHeight/2);
                }
//                平移
//                canvas.translate(30,0);
//                Log.i("canvas", canvasWidth+"   "+canvasHeight+"")
//                canvas.drawBitmap(bitmap,matrix,null);
            }
            if (null != navigations && !navigations.isEmpty()) {
                navigationPath.reset();
                startX = canvasWidth / 2;
                startY = canvasHeight / 2;
//                Log.i("points", points.toString() + "  " + points.size());
//                Log.i("navigation", navigations.toString());
                for (int i = 1; i < navigations.size(); i++) {
                    if (navigations.get(i) == 0) {
                        break;
                    } else {
                        navigationPath.addCircle(startX + points.get(navigations.get(i) * 2 - 2) * mapZoom, startY - points.get(navigations.get(i) * 2 - 1) * mapZoom, 5 * mapZoom, Path.Direction.CW);
                    }
                }
            }
//            canvas.rotate(-direction,startX,startY);
            canvas.drawPath(navigationPath, navigationPaint);
            canvas.drawPath(pointPath, pointPaint);
        }
        //            图标
        Bitmap bitmap = BitmapFactory.decodeResource(this.getContext().getResources(), R.mipmap.starting);
        mSrcRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        mDestRect = new Rect((int) startX - 30, (int) startY - 40, (int) startX + 30, (int) startY + 20);
        canvas.drawBitmap(bitmap, mSrcRect, mDestRect, null);
        invalidate();
    }

    public void repaint(ArrayList points, ArrayList navigations) {
        Canvas canvas = null;
        try {
            canvas = surfaceHolder.lockCanvas();
            if (null != canvas) {
                paint(canvas, points, navigations);
                invalidate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (canvas != null) {
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }


    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }

}