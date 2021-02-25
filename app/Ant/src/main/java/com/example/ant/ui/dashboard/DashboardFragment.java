package com.example.ant.ui.dashboard;

import android.content.Context;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ant.Login;
import com.example.ant.Main;
import com.example.ant.R;
import com.example.ant.Utils.DirectionSet;
import com.example.ant.Utils.DistanceCaculate;
import com.example.ant.Utils.PointSet;
import com.example.ant.Utils.StepCountJudgment;

import java.util.ArrayList;

public class DashboardFragment extends Fragment implements SensorEventListener, GestureDetector.OnDoubleTapListener, GestureDetector.OnGestureListener {
    //    xml组件
    private LinearLayout linearLayout;
    private FrameLayout frameLayout;
    private TextView tvStep;
    private TextView tvDirection;
    private Button btnStart;
    private ImageView compass;


    //    缩放标志
    private float zoom = 1;

    //    传感器
    private SensorManager sManager;
    private Sensor mSensorAccelerometer;
    private Sensor mSensorMagnetic;

    // 手势检测
    private GestureDetector gestureDetector;
    //    方向设定
    private float direction = 0;
    private ArrayList directions;
    //    步数设定
    private Integer step = 0;


    //状态
    //    是否开始构建
    private boolean processState = false;
    //    计步姿态 0-手握 1-平握 2-放在口袋中
    private int statu;

    //    位置设定
    private PointSet pointSet;
    //    private float currentAngle = 0;
    private float currentX = 0;
    private float currentY = 0;
    private float nextX = 0;
    private float nextY = 0;
    private ArrayList points;

    //    制图类
    private MapSetView mapSetView;

    //    计步类
    private StepCountJudgment stepCountJudgment;

    //    初始化页面
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        return view;
    }

    //    页面构建
    @Override
    public void onStart() {
        super.onStart();
        findView();
        listener();
        stepCountJudgment = new StepCountJudgment();
    }

    //注册组件
    public void findView() {
        linearLayout = getView().findViewById(R.id.linearLayout);
        frameLayout = getView().findViewById(R.id.frameLayout);
        tvStep = getView().findViewById(R.id.tv_step);
        tvDirection = getView().findViewById(R.id.tv_direction);
        btnStart = getView().findViewById(R.id.btn_start);
        compass = getView().findViewById(R.id.compass);
    }

    //监听器注册
    public void listener() {
        //        设定开始点
        pointSet = new PointSet(0, 0, 0, 30);
        points = new ArrayList<Float>();
        directions = new ArrayList<Float>();
//        手势
        gestureDetector = new GestureDetector(frameLayout.getContext(), this);
//        传感器
        sManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        mSensorAccelerometer = sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorMagnetic = sManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sManager.registerListener(this, mSensorAccelerometer, SensorManager.SENSOR_DELAY_UI);
        sManager.registerListener(this, mSensorMagnetic, SensorManager.SENSOR_DELAY_UI);
        mapSetView = new MapSetView(getActivity());
        frameLayout.addView(mapSetView);
//点击监听
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvStep.setText("本次行走距离0米");
                tvDirection.setText("当前方位");
                if (processState == true) {
                    btnStart.setText("开始构建");
                    processState = false;
                    points.clear();
                    directions.clear();
                    direction = 0;
                    step = 0;
                    currentX = 0;
                    currentY = 0;
                    zoom = 1;
                    RLtag = 0;
                    UDtag = 0;
                    tag = 0;
                    mapSetView.repaint(points, direction);

                } else {
                    btnStart.setText("停止");
                    processState = true;
                }
            }
        });
//        手势
        ((Main) this.getActivity()).registerMyTouchListener(mTouchListener);
    }

    //注销
    @Override
    public void onDestroy() {
        super.onDestroy();
        sManager.unregisterListener(this);
        ((Main) this.getActivity()).unRegisterMyTouchListener(mTouchListener);
        Log.i("distory", "Dash");
    }


    //    传感器监听器
    @Override
    public void onSensorChanged(SensorEvent event) {
        statu = 1;
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float[] value = event.values;
            step = stepCountJudgment.judgment(statu, value, processState);
            if (null != step && processState) {
//                Log.i(step +"", step +"");
                tvStep.setText("本次行走距离" + String.format("%.2f",(DistanceCaculate.diatance(points.size()/2)))+"米");
//                tvDirection.setText("方位：" + String.format("%.2f", direction) + "°");

            }
        }
        if (step != null) {
            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD && processState) {
                float[] value1 = event.values;
                direction = DirectionSet.directionSet(value1);
                directions.add(direction);
                float[] floats = pointSet.calculatePoint(currentX, currentY, direction);
//                格式化 位置参数保留小数点后一位
                nextX = (float) (Math.round(floats[0] * 10)) / 10;
                nextY = (float) (Math.round(floats[1] * 10)) / 10;
                currentX = nextX;
                currentY = nextY;
                points.add(nextX);
                points.add(nextY);
                if (null != points && points.size() >= 2) {
                    mapSetView.repaint(points, direction);
                }
//                Log.i("t","+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
//                for (int i = 0; i < points.size() - 2; i=i+2) {
//                    Log.i("XY",points.get(i)+"    "+points.get(i+1)+"");
//                }
//                Log.i("XY",nextX+"    "+nextY+"");
//                mapSetView.getHolder().setFixedSize(mapSetView.getWidth()*2, mapSetView.getHeight()*2);
//               frameLayout.layout(frameLayout.getLeft()/2,frameLayout.getTop()/2,frameLayout.getRight()/2,frameLayout.getBottom()/2);
            }
        }
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            float[] value1 = event.values;
            float direction = DirectionSet.directionSet(value1);
            direction = (float) (Math.round(direction * 100)) / 100;
            compass.setPivotX(compass.getWidth() / 2);
            compass.setPivotY(compass.getHeight() / 2);//支点在图片中心
            compass.setRotation(direction);
            tvDirection.setText("方位：" + String.format("%.2f", direction) + "°");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    private Main.MyTouchListener mTouchListener = new Main.MyTouchListener() {
        @Override
        public boolean onTouchEvent(MotionEvent event) {
            // TODO Auto-generated method stub
//            Log.i("e", event.getPointerCount() + "");
//            this.onTouchEvent(event);
            return gestureDetector.onTouchEvent(event);
        }
    };

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
//        Canvas canvas=null;
        Log.i("tag", "放大");
        if (zoom<5) {
            zoom = zoom+1;
            mapSetView.setPaint(zoom*10f);
//           canvas=mapSetView.getHolder().lockCanvas();
//           canvas.scale(canvas.getWidth()*zoom,canvas.getHeight()*zoom);
//            Log.i("canvas.getWidth()*zoom",canvas.getWidth()*zoom+"");
//           mapSetView.getHolder().unlockCanvasAndPost(canvas);
//            mapSetView.getHolder().setFixedSize((int) (mapSetView.getWidth() * zoom), (int) (mapSetView.getHeight() * zoom));
        }
        mapSetView.repaint(points, direction);
        mapSetView.invalidate();
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
//        Log.i("手势检测", "true");
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }


    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }


    @Override
    public void onLongPress(MotionEvent e) {
//        Canvas canvas=null;
        if (zoom>0) {
            zoom = zoom-1;
            Log.i("tag", "缩小");
//            canvas=mapSetView.getHolder().lockCanvas();
//            canvas.scale(canvas.getWidth()*zoom,canvas.getHeight()*zoom);
//            mapSetView.getHolder().unlockCanvasAndPost(canvas);
            mapSetView.setPaint(zoom*10f);
//            mapSetView.getHolder().setFixedSize((int) (mapSetView.getWidth() * zoom), (int) (mapSetView.getHeight() * zoom));
        }
        mapSetView.repaint(points, direction);
        mapSetView.invalidate();
    }

    int RLtag = 0;
    int UDtag = 0;
    int tag = 0;

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//                Log.i("e1",e1.getX()+"            "+e1.getY());
//                Log.i("e2",e2.getX()+"            "+e2.getY());
//                Log.i("v",velocityX+"            "+velocityY);
        tag = (int) (Math.abs(velocityX) + Math.abs(velocityY) / 1000);
        Log.i("tag", Math.abs(velocityX) + Math.abs(velocityY) / 1000 + "");
        if (tag <= 0) {
            tag = 1;
        } else if (tag > 15) {
            tag = 10;
        }
        if (velocityX > 1500 && Math.abs(velocityY) < 1500) {
//            右移
            RLtag++;
            mapSetView.setTranslationX(10 * RLtag * tag);
            Log.i("tag", "右移");
        } else if (velocityX < -1500 && Math.abs(velocityY) < 1500) {
            //            左移
            RLtag--;
            mapSetView.setTranslationX(10 * RLtag * tag);
            Log.i("tag", "左移");
        } else if (velocityY < -1500 && Math.abs(velocityX) < 4000) {
//            上移
            UDtag--;
            mapSetView.setTranslationY(10 * UDtag * tag);
            Log.i("tag", "上移");
        } else if (velocityY > 1500 && Math.abs(velocityX) < 1500) {
            //            下移
            UDtag++;
            mapSetView.setTranslationY(10 * UDtag * tag);
            Log.i("tag", "下移");
        }
        mapSetView.repaint(points, direction);
        mapSetView.invalidate();
        return true;
    }
}
