package com.example.ant.ui.dashboard;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class MapSetView extends SurfaceView implements SurfaceHolder.Callback {
    //    SurfaceVice类控制器
    private SurfaceHolder surfaceHolder;
    private Paint paint;
    private Path path;
    private float startX;
    private float startY;
    private float canvasWidth;
    private float canvasHeight;
    private float pointX;
    private float pointY;

    //    绘制自适应缩放标志
    private float mapZoom = 1;


    public MapSetView(Context context) {
        //....
        super(context);
        surfaceHolder = this.getHolder();//获取holder
        surfaceHolder.addCallback(this);
        paint = new Paint();
        path = new Path();
//        线条效果
//        PathEffect pathEffect = new DashPathEffect(new float[]{20f, 15f, 10f, 5f}, 0);
//        paint.setPathEffect(pathEffect);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(10f);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.RED);
//画布初始化
        setZOrderOnTop(true);//使surfaceview放到最顶层
        getHolder().setFormat(PixelFormat.TRANSLUCENT);//使窗口支持透明度

        mapZoom=1;
    }

    protected void paint(Canvas canvas, ArrayList<Float> arrayList, float direction) {
        //这里的代码跟继承View时OnDraw中一样
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

//        path.rewind();
        if (!arrayList.isEmpty()) {
            path.reset();
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);//绘制透明色
            canvasWidth = canvas.getWidth();
            canvasHeight = canvas.getHeight();
            startX = canvasWidth / 2;
            startY = canvasHeight / 2;
            path.moveTo(startX, startY);
//        Log.i("size", arrayList.size() + "");
            canvas.drawColor(getSolidColor());
            for (int i = 0; i <= arrayList.size() - 2; i = i + 2) {
                pointX = startX + arrayList.get(i) * mapZoom;
                pointY = startY - arrayList.get(i + 1) * mapZoom;
                path.lineTo(pointX, pointY);
//            path.moveTo((Float) arrayList.get(i),(Float) arrayList.get(i+1));
//            Log.i("XY", arrayList.get(i) + "    " + arrayList.get(i + 1) + "");
                if (pointX > 0.9 * canvasWidth || pointY > 0.9 * canvasHeight || pointX < 0.1 * canvasWidth || pointY < 0.1 * canvasHeight) {
                    mapZoom = mapZoom * 0.6f;
                }
//                平移
//                canvas.translate(30,0);
//                Log.i("canvas", canvasWidth+"   "+canvasHeight+"");
            }
//            canvas.rotate(-direction,startX,startY);
            canvas.drawPath(path, paint);
        }
        invalidate();
    }

    public void repaint(ArrayList arrayList, float direction) {
        Canvas canvas = null;
        try {
            canvas = surfaceHolder.lockCanvas();
            paint(canvas, arrayList, direction);
            invalidate();
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