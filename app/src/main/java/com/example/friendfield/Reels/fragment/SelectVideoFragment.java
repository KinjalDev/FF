package com.example.friendfield.Reels.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bokecc.shortvideo.combineimages.model.SelectVideoInfo;

import java.util.List;

import com.example.friendfield.R;
import com.example.friendfield.Reels.adapter.SelectVideoAdapter;
import com.example.friendfield.Reels.util.MultiUtils;

public class SelectVideoFragment extends Fragment {

    private View view;
    private Activity activity;
    private RecyclerView rv_select_video;
    private SelectVideoAdapter selectVideoAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_select_video, null);
        activity = getActivity();

        rv_select_video = view.findViewById(R.id.rv_select_video);
        rv_select_video.getItemAnimator().setChangeDuration(0);
        rv_select_video.getItemAnimator().setAddDuration(0);

        List<SelectVideoInfo> videoDatas = MultiUtils.getVideoDatas(activity);
        GridLayoutManager layoutManager = new GridLayoutManager(activity, 4);
        rv_select_video.setLayoutManager(layoutManager);
        selectVideoAdapter = new SelectVideoAdapter(videoDatas);
        rv_select_video.setAdapter(selectVideoAdapter);

        selectVideoAdapter.setOnItemClickListener(new SelectVideoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(SelectVideoInfo item, int position) {
                String path = item.getPath();
                Intent intent = new Intent();
                intent.putExtra("path", path);
                activity.setResult(-1,intent);
                activity.finish();
            }
        });
        return view;
    }
}
