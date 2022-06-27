package com.example.friendfield.Fragment;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.friendfield.Adapter.RequestAdapter;
import com.example.friendfield.R;

public class FriendRequestFragment extends Fragment {

    RequestAdapter requestAdapter;
    RecyclerView recycle_request;
    int[] user_img = {R.drawable.ic_user,R.drawable.ic_user,R.drawable.ic_user,R.drawable.ic_user,R.drawable.ic_user};
    String[] user_name = {"John Bryan","John Bryan","John Bryan","John Bryan","John Bryan"};
    String[] user_dis = {"John Bryan","John Bryan","John Bryan","John Bryan","John Bryan"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend_request, container, false);

        recycle_request = view.findViewById(R.id.recyler_request);

        requestAdapter = new RequestAdapter(this,user_img,user_name,user_dis);
        LinearLayoutManager maneger = new LinearLayoutManager(getContext());
        recycle_request.setLayoutManager(maneger);
        recycle_request.setAdapter(requestAdapter);

        return view;
    }
}