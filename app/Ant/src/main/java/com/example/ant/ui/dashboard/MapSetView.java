package com.example.ant.ui.dashboard;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class MapSetView extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder surfaceHolder;
    private Paint paint;
    private Path path;
    private float startX;
    private float startY;

    public MapSetView(Context context) {
        //....
        super(context);
        surfaceHolder = this.getHolder();//获取holder
        surfaceHolder.addCallback(this);
        paint = new Paint();
        path = new Path();
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(5f);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
    }

    protected void paint(Canvas canvas, ArrayList arrayList) {
        //这里的代码跟继承View时OnDraw中一样
        path.reset();
        startX=canvas.getWidth()/2;
        startY=canvas.getHeight()/2;
        path.moveTo(startX, startY);
        Log.i("size", arrayList.size() + "");
        for (int i = 0; i <= arrayList.size() - 2; i = i + 2) {
            path.lineTo(startX+(Float) arrayList.get(i), startY+(Float) arrayList.get(i + 1));
//            path.moveTo((Float) arrayList.get(i),(Float) arrayList.get(i+1));
            Log.i("XY", arrayList.get(i) + "    " + arrayList.get(i + 1) + "");

        }
        canvas.drawPath(path, paint);
    }

    public void repaint(ArrayList arrayList) {
        Canvas canvas = null;
        try {
            canvas = surfaceHolder.lockCanvas();
            paint(canvas, arrayList);
        } finally {
            if (canvas != null) {
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
//        初始化画布
//        Canvas canvas = holder.lockCanvas();
//
//        surfaceHolder.unlockCanvasAndPost(canvas);

    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }
}