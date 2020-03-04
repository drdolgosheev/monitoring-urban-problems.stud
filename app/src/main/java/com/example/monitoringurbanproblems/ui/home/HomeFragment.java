package com.example.monitoringurbanproblems.ui.home;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.monitoringurbanproblems.R;

import java.util.ArrayList;

import static androidx.core.app.ActivityCompat.finishAffinity;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private HomeViewModel homeViewModel;
    private ArrayList<String> mImagesName = new ArrayList<>();
    private ArrayList<String> mImagesUrls = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_problems, container, false);
        initImageBitmaps();
        recyclerView = (RecyclerView) root.findViewById(R.id.ResycleWiev);
        initRecyclerView();
//        final TextView textView = root.findViewById(R.id.text_home);
//        homeViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        chekGPS();

        return root;
    }

    private void initImageBitmaps(){
        mImagesUrls.add("gs://urbanstudproj.appspot.com/test_user/chelovek.jpg");
        mImagesName.add("Havasu Falls");

        mImagesUrls.add("https://i.redd.it/tpsnoz5bzo501.jpg");
        mImagesName.add("Trondheim");

        mImagesUrls.add("https://i.redd.it/qn7f9oqu7o501.jpg");
        mImagesName.add("Portugal");

        mImagesUrls.add("https://i.redd.it/j6myfqglup501.jpg");
        mImagesName.add("Rocky Mountain National Park");


        mImagesUrls.add("https://i.redd.it/0h2gm1ix6p501.jpg");
        mImagesName.add("Mahahual");

        mImagesUrls.add("https://i.redd.it/k98uzl68eh501.jpg");
        mImagesName.add("Frozen Lake");


        mImagesUrls.add("https://i.redd.it/glin0nwndo501.jpg");
        mImagesName.add("White Sands Desert");

        mImagesUrls.add("https://i.redd.it/obx4zydshg601.jpg");
        mImagesName.add("Austrailia");

        mImagesUrls.add("https://i.imgur.com/ZcLLrkY.jpg");
        mImagesName.add("Washington");


    }

    private void initRecyclerView(){
        RecycleViewAdapter adapter = new RecycleViewAdapter(mImagesName, mImagesUrls, getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public void chekGPS()
    {
        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(getContext(), "Включите доступ к GPS", Toast.LENGTH_SHORT).show();
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder
                    .setTitle("Необходимо предоставить доступ к GPS")
                    .setMessage("Если доступ не будет предоставлен, приложение будет закрыто")
                    .setIcon(android.R.drawable.stat_sys_warning)
                    .setPositiveButton("Предоставить", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(android.provider.Settings
                                    .ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton("Не предоставлять", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finishAffinity((Activity) getContext());
                        }
                    })
                    .setCancelable(false).show();
        }
    }

}