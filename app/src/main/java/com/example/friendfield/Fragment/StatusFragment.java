package com.example.friendfield.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.devlomi.circularstatusview.CircularStatusView;
import com.example.friendfield.Activity.AllStoryViewActivity;
import com.example.friendfield.Activity.TakePhotoActivity;
import com.example.friendfield.Activity.ViewStoryActivity;
import com.example.friendfield.Adapter.RecentStatusAdapter;
import com.example.friendfield.MainActivity;
import com.example.friendfield.Model.Story.ViewStoryModel;
import com.example.friendfield.R;
import com.example.friendfield.Reels.CreateReelsActivity;
import com.example.friendfield.Utils.DBHelper;
import com.example.friendfield.Utils.FileUtils;
import com.example.friendfield.status.pix.Options;
import com.example.friendfield.status.pix.Pix;
import com.example.friendfield.status.utility.PermUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class StatusFragment extends Fragment {
    RecyclerView recent_status_recyclerview;
    SearchView searchView;
    LinearLayout lin_my_status;
    CircleImageView cir_status;
    RecyclerView viewed_status_recyclerview;
    CircularStatusView circular_status_view;
    FloatingActionButton btn_camera;
    int RequestCode = 1;
    ArrayList<ViewStoryModel> arrayList = new ArrayList<>();
    DBHelper dbHelper;

    //    int[][] images = {R.drawable.img_1,
//            R.drawable.girl_image,
//            R.drawable.img_3,
//            R.drawable.img_7,
//            R.drawable.img_6,
//            R.drawable.img_2};

    int[][] images = {{R.drawable.img_1, R.drawable.img_2, R.drawable.img_3},
            {R.drawable.img_4, R.drawable.img_6},
            {R.drawable.img_8, R.drawable.img_4, R.drawable.img_6},
            {R.drawable.img_12},
            {R.drawable.img_9, R.drawable.img_11, R.drawable.img_1, R.drawable.img_3},
            {R.drawable.img_7, R.drawable.img_10}};

    String[][] images_array = {{"https://images.pexels.com/photos/1433052/pexels-photo-1433052.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260",
            "https://player.vimeo.com/external/403295268.sd.mp4?s=3446f787cefa52e7824d6ce6501db5261074d479&profile_id=165&oauth2_token_id=57447761",
            "https://images.pexels.com/photos/53757/pexels-photo-53757.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500"},

            {"https://images.pexels.com/photos/1366630/pexels-photo-1366630.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260",
                    "https://images.pexels.com/photos/1367067/pexels-photo-1367067.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260"},

            {"https://images.pexels.com/photos/2458400/pexels-photo-2458400.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260",
                    "https://player.vimeo.com/external/405333429.sd.mp4?s=dcc3bdec31c93d87c938fc6c3ef76b7b1b188580&profile_id=165&oauth2_token_id=57447761",
                    "https://images.pexels.com/photos/1420226/pexels-photo-1420226.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500"},

            {"https://images.pexels.com/photos/1591382/pexels-photo-1591382.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260"},

            {"https://player.vimeo.com/external/409206405.sd.mp4?s=0bc456b6ff355d9907f285368747bf54323e5532&profile_id=165&oauth2_token_id=57447761",
                    "https://images.pexels.com/photos/134020/pexels-photo-134020.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260",
                    "https://images.pexels.com/photos/1719344/pexels-photo-1719344.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260",
                    "https://player.vimeo.com/external/394678700.sd.mp4?s=353646e34d7bde02ad638c7308a198786e0dff8f&profile_id=165&oauth2_token_id=57447761"},

            {"https://images.pexels.com/photos/364096/pexels-photo-364096.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260",
                    "https://images.pexels.com/photos/3849168/pexels-photo-3849168.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260"}};

    ArrayList<String> imagesArrayList = new ArrayList<>();

    ArrayList<String> imagesArrayList1 = new ArrayList<>();
    ArrayList<String> imagesArrayList2 = new ArrayList<>();
    ArrayList<String> imagesArrayList3 = new ArrayList<>();
    ArrayList<String> imagesArrayList4 = new ArrayList<>();
    ArrayList<String> imagesArrayList5 = new ArrayList<>();
    ArrayList<String> imagesArrayList6 = new ArrayList<>();


    String[] username = {"Kinjal", "Arti", "Dipti", "Rani", "Tanvi", "Sweta"};

    String[] user_profile = {"https://randomuser.me/api/portraits/women/11.jpg", "https://randomuser.me/api/portraits/women/10.jpg",
            "https://randomuser.me/api/portraits/women/8.jpg", "https://randomuser.me/api/portraits/women/5.jpg",
            "https://randomuser.me/api/portraits/women/3.jpg", "https://randomuser.me/api/portraits/women/2.jpg"};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_status, container, false);

        searchView = view.findViewById(R.id.searchView);
        lin_my_status = view.findViewById(R.id.lin_my_status);
        cir_status = view.findViewById(R.id.cir_status);
        circular_status_view = view.findViewById(R.id.circular_status_view);
        btn_camera = view.findViewById(R.id.btn_camera);

        dbHelper = new DBHelper(getContext());
        dbHelper.open();

        recent_status_recyclerview = view.findViewById(R.id.recent_status_recyclerview);
        viewed_status_recyclerview = view.findViewById(R.id.viewed_status_recyclerview);

        searchView.setQueryHint("Search");

        arrayList.addAll(dbHelper.retreiveimagefromdb1());
        Log.e("LLL_status_list-->", String.valueOf(arrayList.size()));

        recent_status_recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        recent_status_recyclerview.setNestedScrollingEnabled(false);

        viewed_status_recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        viewed_status_recyclerview.setNestedScrollingEnabled(false);

        circular_status_view.setPortionsCount(arrayList.size());

        if (arrayList.size() > 0) {
            cir_status.setImageURI(Uri.parse(arrayList.get(0).getImage()));
        }

//        RecentStatusAdapter recentStatusAdapter = new RecentStatusAdapter(getContext(), images, username);
        RecentStatusAdapter recentStatusAdapter = new RecentStatusAdapter(getContext(), images_array, username, user_profile);
        recent_status_recyclerview.setAdapter(recentStatusAdapter);
        recentStatusAdapter.setOnStatusClickListener(new RecentStatusAdapter.OnStatusClickListener() {
            @Override
            public void onStatusClick(CircularStatusView circularStatusView, int pos) {
//                circularStatusView.setPortionColorForIndex(pos, Color.BLACK);

                Intent intent = new Intent(getContext(), AllStoryViewActivity.class);
//                intent.putExtra("Total_Images", images[position]);
                intent.putExtra("Total_Images", images_array);
//                intent.putExtra("UserName", username[position]);
                intent.putExtra("UserName", username);
                intent.putExtra("UserProfile", user_profile);
                intent.putExtra("currentPosition", pos);
                startActivity(intent);

            }
        });

        lin_my_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                startActivity(new Intent(getActivity(), TakePhotoActivity.class));
                if (arrayList.size() > 0) {
                    startActivity(new Intent(getActivity(), ViewStoryActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), Pix.class));

                }
            }
        });

        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(getActivity(), Pix.class));
                startActivity(new Intent(getActivity(), CreateReelsActivity.class));

            }
        });

        circular_status_view.setPortionsColor(getResources().getColor(R.color.colorAccent));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        FileUtils.hideKeyboard(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!arrayList.isEmpty()) {
            arrayList.clear();
        }
        dbHelper = new DBHelper(getContext());
        dbHelper.open();

        arrayList.addAll(dbHelper.retreiveimagefromdb1());
        circular_status_view.setPortionsCount(arrayList.size());
        if (arrayList.size() > 0) {
            cir_status.setImageURI(Uri.parse(arrayList.get(0).getImage()));
        }

    }

}