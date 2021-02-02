package com.example.stepcount;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.view.View;

public class MapSetView extends View {

    public MapSetView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint=new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.STROKE);
        Path path=new Path();
        path.moveTo(0,0);
        path.lineTo(100,100);
        path.moveTo(50,0);
        path.lineTo(100,70);
        canvas.drawPath(path,paint);
        super.onDraw(canvas);
    }
}
