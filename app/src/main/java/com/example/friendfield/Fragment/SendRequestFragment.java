package com.example.friendfield.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.friendfield.Adapter.SendRequestAdapter;
import com.example.friendfield.R;

public class SendRequestFragment extends Fragment {

    RecyclerView send_recylerview;
    SendRequestAdapter sendRequestAdapter;
    int[] user_img = {R.drawable.ic_user,R.drawable.ic_user,R.drawable.ic_user,R.drawable.ic_user,R.drawable.ic_user};
    String[] user_name = {"John Bryan","John Bryan","John Bryan","John Bryan","John Bryan"};
    String[] user_dis = {String.valueOf(R.string.dis),String.valueOf(R.string.dis),String.valueOf(R.string.dis),String.valueOf(R.string.dis),String.valueOf(R.string.dis)};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_send_request, container, false);

        send_recylerview = view.findViewById(R.id.send_recylerview);

        sendRequestAdapter = new SendRequestAdapter(this,user_img,user_name,user_dis);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        send_recylerview.setLayoutManager(manager);
        send_recylerview.setAdapter(sendRequestAdapter);

        return view;
    }
}