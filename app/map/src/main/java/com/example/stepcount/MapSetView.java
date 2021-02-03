package com.example.stepcount;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.view.View;


import java.util.ArrayList;

public class MapSetView extends View {
    private Canvas canvas;
    private Path path;
    private Paint paint;


    public MapSetView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvas = canvas;
        path=new Path();
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
//        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.STROKE);
        path.moveTo(0,0);
        path.lineTo(50,50);
        path.moveTo(50,50);
        path.lineTo(90,80);
        canvas.drawPath(path, paint);
    }

    protected void draw(ArrayList point) {
//        清除画布内容
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        invalidate();
        path.reset();
        path.moveTo(0,0);
        path.lineTo(50,50);
        path.moveTo(50,50);
        path.lineTo(90,80);
        canvas.drawPath(path, paint);
//        postInvalidateDelayed(100);
    }

    @Override
    public void refreshDrawableState() {
        super.refreshDrawableState();
    }

}
