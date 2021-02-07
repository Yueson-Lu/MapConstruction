package com.example.stepcount;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;


import androidx.annotation.NonNull;

import java.util.ArrayList;

public class MapSetView extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder surfaceHolder;
    private Paint paint;
    private Path path;
    private boolean init;

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
        this.init=true;
    }

    protected void paint(Canvas canvas,ArrayList arrayList) {
        //这里的代码跟继承View时OnDraw中一样
        path.reset();
        if (init){
            path.moveTo((Float) arrayList.get(0),(Float) arrayList.get(1));
            Log.i("XY",arrayList.get(0)+"    "+arrayList.get(1)+"");
            init=false;
        }
        Log.i("size",arrayList.size()+"");
        for (int i = 0; i <=arrayList.size()-2; i=i+2) {
            path.lineTo((Float) arrayList.get(i),(Float) arrayList.get(i+1));
//            path.moveTo((Float) arrayList.get(i),(Float) arrayList.get(i+1));
            Log.i("XY",arrayList.get(i)+"    "+arrayList.get(i+1)+"");

        }
        canvas.drawPath(path,paint);
    }

    public void repaint(ArrayList arrayList) {
        Canvas canvas = null;
        try {
            canvas = surfaceHolder.lockCanvas();
            paint(canvas,arrayList);
        } finally {
            if (canvas != null) {
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        Canvas canvas = holder.lockCanvas();
        canvas
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }
}