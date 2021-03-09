package com.example.ant.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ant.R;
import com.example.ant.ui.dashboard.MapSetView;

import java.util.ArrayList;

public class notificationsFragment extends Fragment {
    private FrameLayout frameLayout;
    //    制图类
    private MapSetView mapSetView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        findByMe();
        listener();
    }

    private void findByMe() {
        frameLayout = getActivity().findViewById(R.id.frameLayout);
    }

    private void listener() {
        mapSetView = new MapSetView(getActivity());
        frameLayout.addView(mapSetView);
        ArrayList<Float> points = new ArrayList();
        ArrayList<Integer> navigations = new ArrayList();
        points.add(100f);
        points.add(100f);
        points.add(300f);
        points.add(300f);
        points.add(900f);
        points.add(900f);
        points.add(500f);
        points.add(400f);
        navigations.add(0);
        mapSetView.repaint(points, navigations);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
