package com.example.ant.Utils;

public class DirectionSet  {

    public static float directionSet(float[] value){
        float direction=0;
        float x = value[0];
        float y = value[1];
        if (x <= 0) {
            if (y >= 0) {
                direction = (float) (-Math.tanh(x / y) * 90);
            } else {
                direction = (float) ((-Math.tanh(x / y) * 90) + 180);
            }
        } else {
            if (y >= 0) {
                direction = (float) ((-Math.tanh(x / y) * 90) + 360);
            } else {
                direction = (float) ((-Math.tanh(x / y) * 90) + 180);
            }
        }
        return direction;
    }
}
