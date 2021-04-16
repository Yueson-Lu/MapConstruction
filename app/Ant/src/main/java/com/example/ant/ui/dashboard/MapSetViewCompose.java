package com.example.ant.ui.dashboard;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.example.ant.R;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class MapSetViewCompose extends SurfaceView implements SurfaceHolder.Callback {
    //    SurfaceVice类控制器
    private SurfaceHolder surfaceHolder;
    private Paint pointPaint;
    private Path pointPath;
    private Paint navigationPaint;
    private Path navigationPath;
    private Paint pointEnd;
    private Paint pointStart;

    private float startX;
    private float startY;
    private float canvasWidth;
    private float canvasHeight;
    private float pointX;
    private float pointY;


    //    绘制自适应缩放标志
    private float mapZoom;

    //图标控制大小
    private Rect mSrcRect, mDestRect;

    ArrayList points;
    ArrayList navigations;

    float x_down = 0;
    float y_down = 0;
    PointF start = new PointF();
    PointF mid = new PointF();
    float oldDist = 1f;
    float oldRotation = 0;
    Matrix matrix = new Matrix();
    Matrix matrix1 = new Matrix();
    Matrix savedMatrix = new Matrix();

    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    int mode = NONE;

    boolean matrixCheck = false;

    int widthScreen;
    int heightScreen;

    Bitmap gintama;

    public MapSetViewCompose(Activity activity) {
        //....
        super(activity);
        mapZoom = 5;
        surfaceHolder = this.getHolder();//获取holder
        surfaceHolder.addCallback(this);
        pointPaint = new Paint();
        pointPath = new Path();
        navigationPaint = new Paint();
        navigationPath = new Path();
        pointEnd = new Paint();
        pointStart = new Paint();

//        线条效果
        pointPaint.setStrokeWidth(3f * mapZoom);
        pointPaint.setStrokeJoin(Paint.Join.ROUND);
        pointPaint.setAntiAlias(true);
        pointPaint.setStyle(Paint.Style.STROKE);
        pointPaint.setColor(Color.rgb(60, 150, 200));
        pointPaint.setDither(true);
        pointPaint.setFilterBitmap(true);


        navigationPaint.setColor(Color.BLACK);
        navigationPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        navigationPaint.setTextSize(20f);
        navigationPaint.setTypeface(Typeface.DEFAULT_BOLD);
        navigationPaint.setUnderlineText(true);
        navigationPaint.setDither(true);
        navigationPaint.setFilterBitmap(true);

        pointEnd.setStrokeJoin(Paint.Join.ROUND);
        pointEnd.setAntiAlias(true);
        pointEnd.setStyle(Paint.Style.FILL);
        pointEnd.setColor(Color.rgb(200, 0, 0));
        pointEnd.setDither(true);
        pointEnd.setFilterBitmap(true);

        pointStart.setStrokeJoin(Paint.Join.ROUND);
        pointStart.setAntiAlias(true);
        pointStart.setStyle(Paint.Style.FILL);
        pointStart.setColor(Color.rgb(0, 200, 0));
        pointStart.setDither(true);
        pointStart.setFilterBitmap(true);
//画布初始化
        setZOrderOnTop(true);//使surfaceview放到最顶层
        getHolder().setFormat(PixelFormat.TRANSLUCENT);//使窗口支持透明度
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        widthScreen = dm.widthPixels;
        heightScreen = dm.heightPixels;
        matrix = new Matrix();
        points = new ArrayList();
        navigations = new ArrayList();
    }

    protected void paint(Canvas canvas, ArrayList<Float> points, ArrayList navigations) {
        if (points.size()<=2){
            mapZoom=5;
            pointPaint.setStrokeWidth(3f * mapZoom);
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);//绘制透明色
            invalidate();
        }
        //这里的代码跟继承View时OnDraw中一样
        if (!points.isEmpty() && null != points) {
            pointPath.reset();
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);//绘制透明色
            canvasWidth = canvas.getWidth();
            canvasHeight = canvas.getHeight();
            startX = canvasWidth / 2;
            startY = canvasHeight / 2;
            pointPath.moveTo(startX, startY);
            canvas.drawColor(getSolidColor());

//            图标
            for (int i = 0; i <= points.size() - 2; i = i + 2) {
                pointX = startX + points.get(i) * mapZoom;
                pointY = startY - points.get(i + 1) * mapZoom;
                pointPath.lineTo(pointX, pointY);
                if (pointX > 0.9 * canvasWidth || pointY > 0.9 * canvasHeight || pointX < 0.1 * canvasWidth || pointY < 0.1 * canvasHeight) {
                    mapZoom = 0.9f * mapZoom;
                    pointPaint.setStrokeWidth(3f * mapZoom);
                }
                if (i == 0) {
                    canvas.drawCircle(pointX, pointY, 4f * mapZoom, pointStart);
                }
                if (i == points.size() - 2) {
                    canvas.drawCircle(pointX, pointY, 4f * mapZoom, pointEnd);
                }
            }
            if (null != navigations && !navigations.isEmpty()) {
                startX = canvasWidth / 2;
                startY = canvasHeight / 2;
                for (int i = 0; i < navigations.size(); i = i + 2) {
                    navigationPaint.setTextSize(20f * mapZoom);
                    canvas.drawText((String) navigations.get(i + 1), startX + (points.get((Integer) navigations.get(i) * 2) * mapZoom), startY - (points.get((Integer) navigations.get(i) * 2 + 1) * mapZoom), navigationPaint);
                canvas.drawCircle(startX + (points.get((Integer) navigations.get(i) * 2) * mapZoom), startY - (points.get((Integer) navigations.get(i) * 2 + 1) * mapZoom), 4f*mapZoom,pointStart);
                }
            }
            canvas.drawPath(pointPath, pointPaint);
        }
        invalidate();
    }

    public void repaint(ArrayList points, ArrayList navigations) {
        Canvas canvas = null;
        this.points = points;
        this.navigations = navigations;
        try {
            canvas = surfaceHolder.lockCanvas();
            if (null != canvas) {
                canvas.setMatrix(matrix);
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
        pointPath.reset();
        navigationPath.reset();
    }

    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.drawBitmap(gintama, matrix, null);
        canvas.restore();
    }

    public boolean onTouchEvent(MotionEvent event) {
        Canvas canvas = surfaceHolder.lockCanvas();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mode = DRAG;
                x_down = event.getX();
                y_down = event.getY();
                savedMatrix.set(matrix);
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                mode = ZOOM;
                oldDist = spacing(event);
                oldRotation = rotation(event);
                savedMatrix.set(matrix);
                midPoint(mid, event);
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == ZOOM) {
                    matrix1.set(savedMatrix);
                    float rotation = rotation(event) - oldRotation;
                    float newDist = spacing(event);
                    float scale = newDist / oldDist;
                    matrix1.postScale(scale, scale, mid.x, mid.y);// 縮放
                    matrix1.postRotate(rotation, mid.x, mid.y);// 旋轉
                    matrixCheck = matrixCheck();
                    if (matrixCheck == false) {
                        matrix.set(matrix1);
                        invalidate();
                    }
                } else if (mode == DRAG) {
                    matrix1.set(savedMatrix);
                    matrix1.postTranslate(event.getX() - x_down, event.getY()
                            - y_down);// 平移
                    matrixCheck = matrixCheck();
                    matrixCheck = matrixCheck();
                    if (matrixCheck == false) {
                        matrix.set(matrix1);
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                break;
        }
        canvas.setMatrix(matrix);
        paint(canvas, points, navigations);
        surfaceHolder.unlockCanvasAndPost(canvas);
        return true;
    }

    private boolean matrixCheck() {
        float[] f = new float[9];
        matrix1.getValues(f);
        // 图片4个顶点的坐标
        float x1 = f[0] * 0 + f[1] * 0 + f[2];
        float y1 = f[3] * 0 + f[4] * 0 + f[5];
        float x2 = f[0] * canvasWidth + f[1] * 0 + f[2];
        float y2 = f[3] * canvasWidth + f[4] * 0 + f[5];
        float x3 = f[0] * 0 + f[1] * canvasHeight + f[2];
        float y3 = f[3] * 0 + f[4] * canvasHeight + f[5];
        float x4 = f[0] * canvasWidth + f[1] * canvasHeight + f[2];
        float y4 = f[3] * canvasWidth + f[4] * canvasHeight + f[5];
        // 图片现宽度
        int size = 20;
        double width = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
        if (width < widthScreen / size || width > widthScreen * size) {
            return true;
        }
        // 出界判断
        if ((x1 < widthScreen / size && x2 < widthScreen / size
                && x3 < widthScreen / size && x4 < widthScreen / size)
                || (x1 > widthScreen * 2 / size && x2 > widthScreen * 2 / size
                && x3 > widthScreen * 2 / size && x4 > widthScreen * 2 / size)
                || (y1 < heightScreen / size && y2 < heightScreen / size
                && y3 < heightScreen / size && y4 < heightScreen / size)
                || (y1 > heightScreen * 2 / size && y2 > heightScreen * 2 / size
                && y3 > heightScreen * 2 / size && y4 > heightScreen * 2 / size)) {
            return true;
        }
        return false;
    }

    // 触碰两点间距离
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    // 取手势中心点
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    // 取旋转角度
    private float rotation(MotionEvent event) {
        double delta_x = (event.getX(0) - event.getX(1));
        double delta_y = (event.getY(0) - event.getY(1));
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }

    // 将移动，缩放以及旋转后的图层保存为新图片
    // 本例中沒有用到該方法，需要保存圖片的可以參考
    public Bitmap CreatNewPhoto() {
        Bitmap bitmap = Bitmap.createBitmap(widthScreen, heightScreen,
                Bitmap.Config.ARGB_8888); // 背景图片
        Canvas canvas = new Canvas(bitmap); // 新建画布
        canvas.drawBitmap(gintama, matrix, null); // 画图片
//        Canvas.ALL_SAVE_FLAG
        canvas.save(); // 保存画布
        canvas.restore();
        return bitmap;
    }

}