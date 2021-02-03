package com.example.stepcount;

import android.content.Context;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.stepcount.Utils.PointSet;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Log.i("tag","1111");
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.stepcount", appContext.getPackageName());
    }
    @Test
    public void test(){
        PointSet pointSet = new PointSet(0,0,0,1);
        int[] ints = pointSet.calculatePoint(0, 0, 30);
        for (int i = 0; i <ints.length ; i++) {
            Log.i("x:",ints[0]+"");
            Log.i("y:",ints[1]+"");
        }
    }
}
