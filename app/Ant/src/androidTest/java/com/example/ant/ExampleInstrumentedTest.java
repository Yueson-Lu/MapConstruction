package com.example.ant;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.ant.Utils.BlobUtil;
import com.example.ant.Utils.Tips;
import com.example.ant.dao.impl.MapPointDaoImpl;
import com.example.ant.dto.MyMap;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.sql.SQLException;

import static android.os.Looper.getMainLooper;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    public static Handler mainHandler;
    public static MapPointDaoImpl mapPointDao;

    @Test
    public void useAppContext() throws SQLException, ClassNotFoundException {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.login", appContext.getPackageName());

        mainHandler = new Handler(getMainLooper());
        mapPointDao = new MapPointDaoImpl();

        new Thread(new Runnable() {
            @Override
            public void run() {
                MyMap selectmap = mapPointDao.selectmap(135520359);
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                       Log.i("selectmap",selectmap.toString());
                       Log.i("points", BlobUtil.getObject(selectmap.getPoints()).toString());
                       Log.i("navigation", BlobUtil.getObject(selectmap.getNavigation()).toString());
                    }
                });
            }
        }).start();
    }
}
