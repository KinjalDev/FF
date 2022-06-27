package com.example.friendfield.Adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.friendfield.Fragment.StoryDisplayFragment;

import com.example.friendfield.Model.Story.StoryUser;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

import kotlin.jvm.internal.Intrinsics;

public  class StoryPagerAdapter extends FragmentStatePagerAdapter {
    private final ArrayList storyList;

    @NotNull
    public Fragment getItem(int position) {
        Object var10002 = this.storyList.get(position);
        Intrinsics.checkExpressionValueIsNotNull(var10002, "storyList[position]");
        return StoryDisplayFragment.newInstance(position, (StoryUser)var10002);
    }

    public int getCount() {
        return this.storyList.size();
    }

    @Nullable
    public final Fragment findFragmentByPosition(@NotNull ViewPager viewPager, int position) {
        Intrinsics.checkParameterIsNotNull(viewPager, "viewPager");

        Fragment var4;
        try {
            Object var10000 = this.instantiateItem((ViewGroup)viewPager, position);
            Intrinsics.checkExpressionValueIsNotNull(var10000, "instantiateItem(viewPager, position)");
            Object f = var10000;
            var10000 = f;
            if (!(f instanceof Fragment)) {
                var10000 = null;
            }

            var4 = (Fragment)var10000;
        } finally {
            this.finishUpdate((ViewGroup)viewPager);
        }

        return var4;
    }

    public StoryPagerAdapter(@NotNull FragmentManager fragmentManager, @NotNull ArrayList storyList) {
        super(fragmentManager, 1);
        Intrinsics.checkParameterIsNotNull(fragmentManager, "fragmentManager");
        Intrinsics.checkParameterIsNotNull(storyList, "storyList");
        this.storyList = storyList;
    }
}