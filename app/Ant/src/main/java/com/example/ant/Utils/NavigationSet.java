package com.example.ant.Utils;

import java.util.ArrayList;

public class NavigationSet {
//    public NavigationSet() {
//    }

    public static boolean setStartNavigation(ArrayList navigations) {
        navigations.add(0);
        return true;
    }

    public static boolean setNavigation(int i, ArrayList navigations) {
        navigations.add(i);
        return true;
    }

    public static boolean setEndNavigation(int i, ArrayList navigations) {
        navigations.add(i);
        return true;
    }
}
