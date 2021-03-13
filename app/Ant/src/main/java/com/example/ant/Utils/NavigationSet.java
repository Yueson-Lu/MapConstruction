package com.example.ant.Utils;

import java.util.ArrayList;

public class NavigationSet {
//    public NavigationSet() {
//    }

    public static boolean setStartNavigation(ArrayList navigations) {
        navigations.add(0);
        navigations.add("起点");
        return true;
    }

    public static boolean setNavigation(int i, String s, ArrayList navigations) {
        navigations.add(i);
        navigations.add(s);
        return true;
    }

    public static boolean setEndNavigation(int i, ArrayList navigations) {
        navigations.add(0);
        navigations.add("终点");
        return true;
    }
}
