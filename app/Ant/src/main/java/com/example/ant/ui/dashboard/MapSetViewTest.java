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
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.widget.ImageView;


import com.example.ant.R;

import java.util.ArrayList;


public class MapSetViewTest extends androidx.appcompat.widget.AppCompatImageView {
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

    //    SurfaceVice类控制器
    private SurfaceHolder surfaceHolder;
    private Paint paint;
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
    //图标控制大小
    private Rect mSrcRect, mDestRect;
    private Canvas myCanvas = null;

    public MapSetViewTest(Activity activity) {
        super(activity);
        gintama = BitmapFactory.decodeResource(getResources(), R.mipmap.compass);
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        widthScreen = dm.widthPixels;
        heightScreen = dm.heightPixels;
        matrix = new Matrix();

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

//        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        paint.setColor(Color.GREEN);
//        paint.setStyle(Paint.Style.STROKE);
//        paint.setStrokeWidth(6f);
//        paint.setAlpha(180);


//画布初始化
//        setZOrderOnTop(true);//使surfaceview放到最顶层
//        getHolder().setFormat(PixelFormat.TRANSLUCENT);//使窗口支持透明度

    }

    protected void onDraw(Canvas canvas) {
        myCanvas = canvas;
        canvas.save();
        myCanvas.setMatrix(matrix);
        Path path=new Path();
        path.moveTo(canvasWidth,canvasWidth);
        path.lineTo(30,30);
        path.lineTo(120,600);
        myCanvas.drawPath(path,pointPaint);
        canvas.restore();
        invalidate();
    }

    public boolean onTouchEvent(MotionEvent event) {
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
        return true;
    }

    private boolean matrixCheck() {
        float[] f = new float[9];
        matrix1.getValues(f);
        // 图片4个顶点的坐标
        float x1 = f[0] * 0 + f[1] * 0 + f[2];
        float y1 = f[3] * 0 + f[4] * 0 + f[5];
        float x2 = f[0] * gintama.getWidth() + f[1] * 0 + f[2];
        float y2 = f[3] * gintama.getWidth() + f[4] * 0 + f[5];
        float x3 = f[0] * 0 + f[1] * gintama.getHeight() + f[2];
        float y3 = f[3] * 0 + f[4] * gintama.getHeight() + f[5];
        float x4 = f[0] * gintama.getWidth() + f[1] * gintama.getHeight() + f[2];
        float y4 = f[3] * gintama.getWidth() + f[4] * gintama.getHeight() + f[5];
        // 图片现宽度
        double width = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
        // 缩放比率判断
        if (width < widthScreen / 3 || width > widthScreen * 3) {
            return true;
        }
        // 出界判断
        if ((x1 < widthScreen / 3 && x2 < widthScreen / 3
                && x3 < widthScreen / 3 && x4 < widthScreen / 3)
                || (x1 > widthScreen * 2 / 3 && x2 > widthScreen * 2 / 3
                && x3 > widthScreen * 2 / 3 && x4 > widthScreen * 2 / 3)
                || (y1 < heightScreen / 3 && y2 < heightScreen / 3
                && y3 < heightScreen / 3 && y4 < heightScreen / 3)
                || (y1 > heightScreen * 2 / 3 && y2 > heightScreen * 2 / 3
                && y3 > heightScreen * 2 / 3 && y4 > heightScreen * 2 / 3)) {
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

    protected void paint(ArrayList<Float> points, ArrayList<Integer> navigations) {
        //这里的代码跟继承View时OnDraw中一样
        myCanvas.save();
        myCanvas.setMatrix(matrix);
        myCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
//        path.rewind();
        if (!points.isEmpty() && null != points) {
//            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
//            myCanvas.drawPaint(paint);
//            invalidate();
//            paint.setStrokeWidth(10f);
//            paint.setStrokeJoin(Paint.Join.ROUND);
//            paint.setAntiAlias(true);
//            paint.setStyle(Paint.Style.STROKE);
//            paint.setColor(Color.rgb(60, 150, 200));
            pointPath.reset();
            myCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);//绘制透明色
            canvasWidth = myCanvas.getWidth();
            canvasHeight = myCanvas.getHeight();
            startX = canvasWidth / 2;
            startY = canvasHeight / 2;
            pointPath.moveTo(startX, startY);
//        Log.i("size", points.size() + "");
            myCanvas.drawColor(getSolidColor());

//            图标
            for (int i = 0; i <= points.size() - 2; i = i + 2) {
                pointX = startX + points.get(i);
                pointY = startY - points.get(i + 1);
                pointPath.lineTo(pointX, pointY);
//            path.moveTo((Float) points.get(i),(Float) points.get(i+1));
//            Log.i("XY", points.get(i) + "    " + points.get(i + 1) + "");
//                平移
//                myCanvas.translate(30,0);
//                Log.i("myCanvas", canvasWidth+"   "+canvasHeight+"")
//                myCanvas.drawBitmap(bitmap,matrix,null);
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
                        navigationPath.addCircle(startX + points.get(navigations.get(i) * 2 - 2), startY - points.get(navigations.get(i) * 2 - 1), 5, Path.Direction.CW);
                    }
                }
            }
            myCanvas.drawPath(navigationPath, navigationPaint);
            myCanvas.drawPath(pointPath, pointPaint);
        }
        //            图标
        Bitmap bitmap = BitmapFactory.decodeResource(this.getContext().getResources(), R.mipmap.starting);
        mSrcRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        mDestRect = new Rect((int) startX - 30, (int) startY - 40, (int) startX + 30, (int) startY + 20);
        myCanvas.drawBitmap(bitmap, mSrcRect, mDestRect, null);
        myCanvas.restore();
        invalidate();
    }

//    public void repaint(ArrayList points, ArrayList navigations) {
//        Canvas canvas = null;
//        try {
//            canvas = surfaceHolder.lockCanvas();
//            if (null != canvas) {
//                paint(canvas, points, navigations);
//                invalidate();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (canvas != null) {
//                surfaceHolder.unlockCanvasAndPost(canvas);
//            }
//        }
//    }
}
