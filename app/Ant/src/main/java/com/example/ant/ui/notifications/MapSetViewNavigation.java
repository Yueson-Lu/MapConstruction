package com.example.ant.ui.notifications;

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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.example.ant.R;
import com.example.ant.dto.MyMap;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

public class MapSetViewNavigation extends SurfaceView implements SurfaceHolder.Callback {
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
    //    构建的地图类型
//    0-原始地图 1-合成地图
    private int flag;

    private HashMap navigationMapPoint;
    private float disx;
    private float disy;
    //    绘制自适应缩放标志
    private float mapZoom;

    //图标控制大小
    private Rect mSrcRect, mDestRect;

    ArrayList points;
    ArrayList<Integer> navigations;

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

    public MapSetViewNavigation(Activity activity) {
        //....
        super(activity);
        mapZoom = 5;
        surfaceHolder = this.getHolder();//获取holder
        surfaceHolder.addCallback(this);
        pointPaint = new Paint();
        pointPath = new Path();
        navigationPaint = new Paint();
        navigationPath = new Path();
//        线条效果
        pointPaint.setStrokeWidth(3f * mapZoom);
        pointPaint.setStrokeJoin(Paint.Join.ROUND);
        pointPaint.setAntiAlias(true);
        pointPaint.setStyle(Paint.Style.STROKE);
        pointPaint.setColor(Color.rgb(60, 150, 200));

        navigationPaint.setStrokeWidth(3.5f);
        navigationPaint.setColor(Color.GREEN);
        navigationPaint.setStyle(Paint.Style.FILL_AND_STROKE);

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

    //    用户导航行走路段处理
    public void setPaint(float size) {
        pointPaint.setStrokeWidth(size);
    }


    protected void paint(Canvas canvas, ArrayList<Float> points, ArrayList<Integer> navigations) {
        //这里的代码跟继承View时OnDraw中一样
        if (!points.isEmpty() && null != points) {
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
            canvas.drawColor(getSolidColor());

//            图标
            for (int i = 0; i <= points.size() - 2; i = i + 2) {
                pointX = startX + points.get(i) * mapZoom;
                pointY = startY - points.get(i + 1) * mapZoom;
                pointPath.lineTo(pointX, pointY);
//            path.moveTo((Float) points.get(i),(Float) points.get(i+1));
//            Log.i("XY", points.get(i) + "    " + points.get(i + 1) + "");
                if (pointX > 0.9 * canvasWidth || pointY > 0.9 * canvasHeight || pointX < 0.1 * canvasWidth || pointY < 0.1 * canvasHeight) {
                    mapZoom = 0.9f * mapZoom;
                    pointPaint.setStrokeWidth(3f * mapZoom);
                }
            }
            if (null != navigations && !navigations.isEmpty()) {
                navigationPath.reset();
                startX = canvasWidth / 2;
                startY = canvasHeight / 2;
                for (int i = 1; i < navigations.size(); i++) {
                    if (navigations.get(i) == 0) {
                        break;
                    } else {
                        navigationPath.addCircle(startX + points.get(navigations.get(i) * 2 - 2) * mapZoom, startY - points.get(navigations.get(i) * 2 - 1) * mapZoom, 3 * mapZoom, Path.Direction.CW);
                    }
                }
            }
            canvas.drawPath(navigationPath, navigationPaint);
            canvas.drawPath(pointPath, pointPaint);
        }
        //            图标
        Bitmap bitmap = BitmapFactory.decodeResource(this.getContext().getResources(), R.mipmap.starting);
        mSrcRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        mDestRect = new Rect((int) (startX - 5 * mapZoom), (int) (startY - 8 * mapZoom), (int) (startX + 5 * mapZoom), (int) (startY + 4 * mapZoom));
        canvas.drawBitmap(bitmap, mSrcRect, mDestRect, null);
        invalidate();
    }

    //导航地图
    public void repaint(ArrayList points, ArrayList navigations, int flag) {
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
        this.flag = flag;
    }


    protected void paint(Canvas canvas, HashMap navigationMapPoint, float disx, float disy) {
        //这里的代码跟继承View时OnDraw中一样
        this.navigationMapPoint = navigationMapPoint;
        this.disx = disx;
        this.disy = disy;
        ArrayList<Float> map1Point = (ArrayList) navigationMapPoint.get("map1");
        ArrayList<Float> map2Point = (ArrayList) navigationMapPoint.get("map2");
        Log.i("map1Point", map1Point.toString());
        Log.i("map2Point", map2Point.toString());

        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);//绘制透明色
        if (!map1Point.isEmpty() && null != map1Point) {
            Paint map1PointPaint;
            Path map1PointPath;
            map1PointPaint = new Paint();
            map1PointPath = new Path();
            map1PointPaint.reset();
            map1PointPath.reset();
            map1PointPaint.setStrokeWidth(3f * mapZoom);
            map1PointPaint.setStrokeJoin(Paint.Join.ROUND);
            map1PointPaint.setAntiAlias(true);
            map1PointPaint.setStyle(Paint.Style.STROKE);
            map1PointPaint.setColor(Color.rgb(60, 150, 200));
            canvasWidth = canvas.getWidth();
            canvasHeight = canvas.getHeight();
            startX = canvasWidth / 2 + disx / 2;
            startY = canvasHeight / 2 + disy / 2;
            map1PointPath.moveTo(startX, startY);
            canvas.drawColor(getSolidColor());
//            坐标点
            for (int i = 0; i <= map1Point.size() - 2; i = i + 2) {
                pointX = startX + map1Point.get(i) * mapZoom;
                pointY = startY - map1Point.get(i + 1) * mapZoom;
                map1PointPath.lineTo(pointX, pointY);
                if (pointX > 0.9 * canvasWidth || pointY > 0.9 * canvasHeight || pointX < 0.1 * canvasWidth || pointY < 0.1 * canvasHeight) {
                    mapZoom = 0.9f * mapZoom;
                    map1PointPaint.setStrokeWidth(3f * mapZoom);
                }
            }
            canvas.drawPath(map1PointPath, map1PointPaint);
        }
        if (!map2Point.isEmpty() && null != map2Point) {
            Paint map2PointPaint;
            Path map2PointPath;
            map2PointPaint = new Paint();
            map2PointPath = new Path();
            map2PointPaint.reset();
            map2PointPath.reset();
            map2PointPaint.setStrokeWidth(3f * mapZoom);
            map2PointPaint.setStrokeJoin(Paint.Join.ROUND);
            map2PointPaint.setAntiAlias(true);
            map2PointPaint.setStyle(Paint.Style.STROKE);
            map2PointPaint.setColor(Color.rgb(0, 150, 0));
            canvasWidth = canvas.getWidth();
            canvasHeight = canvas.getHeight();
            startX = canvasWidth / 2 - disx / 2;
            startY = canvasHeight / 2 - disy / 2;
            map2PointPath.moveTo(startX, startY);
            canvas.drawColor(getSolidColor());
//            坐标点
            for (int i = 0; i <= map2Point.size() - 2; i = i + 2) {
                pointX = startX + map2Point.get(i) * mapZoom;
                pointY = startY - map2Point.get(i + 1) * mapZoom;
                map2PointPath.lineTo(pointX, pointY);
                if (pointX > 0.9 * canvasWidth || pointY > 0.9 * canvasHeight || pointX < 0.1 * canvasWidth || pointY < 0.1 * canvasHeight) {
                    mapZoom = 0.9f * mapZoom;
                    map2PointPaint.setStrokeWidth(3f * mapZoom);
                }
            }
            canvas.drawPath(map2PointPath, map2PointPaint);
        }
//                    图标
//        Bitmap bitmap = BitmapFactory.decodeResource(this.getContext().getResources(), R.mipmap.starting);
//        mSrcRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
//        mDestRect = new Rect((int) (startX - 5 * mapZoom), (int) (startY - 8 * mapZoom), (int) (startX + 5 * mapZoom), (int) (startY + 4 * mapZoom));
//        canvas.drawBitmap(bitmap, mSrcRect, mDestRect, null);
        invalidate();
    }

    //    合成地图
    public void repaint(HashMap navigationMapPoint, float disx, float disy, int flag) {
        Canvas canvas = null;
        try {
            canvas = surfaceHolder.lockCanvas();
            if (null != canvas) {
                canvas.setMatrix(matrix);
                paint(canvas, navigationMapPoint, disx, disy);
                invalidate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (canvas != null) {
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
        this.flag = flag;
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
        if (flag == 0) {
            paint(canvas, points, navigations);
        } else {
            paint(canvas, navigationMapPoint, disx, disy);
        }
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
