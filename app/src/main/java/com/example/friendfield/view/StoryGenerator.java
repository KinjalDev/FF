package com.example.friendfield.view;

import com.example.friendfield.Model.Story.Story;
import com.example.friendfield.Model.Story.StoryUser;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import kotlin.jvm.internal.Intrinsics;
import kotlin.random.Random;

public class StoryGenerator {
    @NotNull
    public static final StoryGenerator INSTANCE;

    @NotNull
//    public final ArrayList<StoryUser> generateStories(int[][] images_array, String[] username_array) {
    public final ArrayList<StoryUser> generateStories() {
        ArrayList<String> storyUrls = new ArrayList<String>();
        storyUrls.add("https://player.vimeo.com/external/403295268.sd.mp4?s=3446f787cefa52e7824d6ce6501db5261074d479&profile_id=165&oauth2_token_id=57447761");
        storyUrls.add("https://player.vimeo.com/external/409206405.sd.mp4?s=0bc456b6ff355d9907f285368747bf54323e5532&profile_id=165&oauth2_token_id=57447761");
        storyUrls.add("https://player.vimeo.com/external/403295710.sd.mp4?s=788b046826f92983ada6e5caf067113fdb49e209&profile_id=165&oauth2_token_id=57447761");
        storyUrls.add("https://player.vimeo.com/external/394678700.sd.mp4?s=353646e34d7bde02ad638c7308a198786e0dff8f&profile_id=165&oauth2_token_id=57447761");
        storyUrls.add("https://player.vimeo.com/external/405333429.sd.mp4?s=dcc3bdec31c93d87c938fc6c3ef76b7b1b188580&profile_id=165&oauth2_token_id=57447761");
        storyUrls.add("https://player.vimeo.com/external/363465031.sd.mp4?s=15b706ccd3c0e1d9dc9290487ccadc7b20fff7f1&profile_id=165&oauth2_token_id=57447761");
        storyUrls.add("https://player.vimeo.com/external/422787651.sd.mp4?s=ec96f3190373937071ba56955b2f8481eaa10cce&profile_id=165&oauth2_token_id=57447761");
        storyUrls.add("https://images.pexels.com/photos/1433052/pexels-photo-1433052.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
        storyUrls.add("https://images.pexels.com/photos/1366630/pexels-photo-1366630.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
        storyUrls.add("https://images.pexels.com/photos/1067333/pexels-photo-1067333.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
        storyUrls.add("https://images.pexels.com/photos/1122868/pexels-photo-1122868.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
        storyUrls.add("https://images.pexels.com/photos/1837603/pexels-photo-1837603.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
        storyUrls.add("https://images.pexels.com/photos/1612461/pexels-photo-1612461.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
        storyUrls.add("https://images.pexels.com/photos/1591382/pexels-photo-1591382.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
        storyUrls.add("https://images.pexels.com/photos/53757/pexels-photo-53757.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500");
        storyUrls.add("https://images.pexels.com/photos/134020/pexels-photo-134020.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
        storyUrls.add("https://images.pexels.com/photos/1367067/pexels-photo-1367067.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
        storyUrls.add("https://images.pexels.com/photos/1420226/pexels-photo-1420226.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500");
        storyUrls.add("https://images.pexels.com/photos/2217365/pexels-photo-2217365.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
        storyUrls.add("https://images.pexels.com/photos/2260800/pexels-photo-2260800.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
        storyUrls.add("https://images.pexels.com/photos/1719344/pexels-photo-1719344.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
        storyUrls.add("https://images.pexels.com/photos/364096/pexels-photo-364096.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
        storyUrls.add("https://images.pexels.com/photos/3849168/pexels-photo-3849168.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
        storyUrls.add("https://images.pexels.com/photos/2953901/pexels-photo-2953901.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
        storyUrls.add("https://images.pexels.com/photos/3538558/pexels-photo-3538558.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
        storyUrls.add("https://images.pexels.com/photos/2458400/pexels-photo-2458400.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");

        ArrayList<String> userProfileUrls = new ArrayList<String>();
        userProfileUrls.add("https://randomuser.me/api/portraits/women/1.jpg");
        userProfileUrls.add("https://randomuser.me/api/portraits/men/1.jpg");
        userProfileUrls.add("https://randomuser.me/api/portraits/women/2.jpg");
        userProfileUrls.add("https://randomuser.me/api/portraits/men/2.jpg");
        userProfileUrls.add("https://randomuser.me/api/portraits/women/3.jpg");
        userProfileUrls.add("https://randomuser.me/api/portraits/men/3.jpg");
        userProfileUrls.add("https://randomuser.me/api/portraits/women/4.jpg");
        userProfileUrls.add("https://randomuser.me/api/portraits/men/4.jpg");
        userProfileUrls.add("https://randomuser.me/api/portraits/women/5.jpg");
        userProfileUrls.add("https://randomuser.me/api/portraits/men/5.jpg");
        userProfileUrls.add("https://randomuser.me/api/portraits/women/6.jpg");
        userProfileUrls.add("https://randomuser.me/api/portraits/men/6.jpg");
        userProfileUrls.add("https://randomuser.me/api/portraits/women/7.jpg");
        userProfileUrls.add("https://randomuser.me/api/portraits/men/7.jpg");
        userProfileUrls.add("https://randomuser.me/api/portraits/women/8.jpg");
        userProfileUrls.add("https://randomuser.me/api/portraits/men/8.jpg");
        userProfileUrls.add("https://randomuser.me/api/portraits/women/9.jpg");
        userProfileUrls.add("https://randomuser.me/api/portraits/men/9.jpg");
        userProfileUrls.add("https://randomuser.me/api/portraits/women/10.jpg");
        userProfileUrls.add("https://randomuser.me/api/portraits/men/10.jpg");
        userProfileUrls.add("https://randomuser.me/api/portraits/women/11.jpg");
        userProfileUrls.add("https://randomuser.me/api/portraits/men/11.jpg");
        ArrayList<StoryUser> storyUserList = new ArrayList<StoryUser>();
        int i = 1;

        for (byte var5 = 10; i <= var5; ++i) {
//        for (byte var5 = (byte) images_array.length; i <= var5; ++i) {
            ArrayList stories = new ArrayList();
            int storySize = Random.Default.nextInt(1, 5);
            int j = 0;

            for (int var9 = storySize; j < var9; ++j) {
                Object var10003 = storyUrls.get(Random.Default.nextInt(storyUrls.size()));
//                Object var10003 = images_array[Random.Default.nextInt(storyUrls.size());
//                Intrinsics.checkExpressionValueIsNotNull(var10003, "storyUrls[Random.nextInt(storyUrls.size)]");
                stories.add(new Story((String) var10003, System.currentTimeMillis() - (long) (1 * (24 - j) * 60 * 60 * 1000)));
            }

            String var10 = "username" + i;
            Object var10004 = userProfileUrls.get(Random.Default.nextInt(userProfileUrls.size()));
//            Object var10004 = username_array[Random.Default.nextInt(username_array.length)];
//            Intrinsics.checkExpressionValueIsNotNull(var10004, "userProfileUrls[Random.n…nt(userProfileUrls.size)]");
            storyUserList.add(new StoryUser(var10, (String) var10004, stories));
        }

        return storyUserList;
    }

    @NotNull
    public final ArrayList<StoryUser> generateStories(String[][] images_array, String[] username_array, String[] UserProfile,int position) {
        ArrayList<StoryUser> storyUserList = new ArrayList<StoryUser>();

//        for (int k = 0; k < images_array.length; k++) {
        for (int k = position; k < images_array.length; k++) {
            ArrayList stories = new ArrayList();

            for (int l = 0; l < images_array[k].length; l++) {
                stories.add(new Story(images_array[k][l], System.currentTimeMillis() - (long) (1 * (24 - l) * 60 * 60 * 1000)));
            }

            storyUserList.add(new StoryUser(username_array[k], (String) UserProfile[k], stories));

        }

        return storyUserList;
    }

    private StoryGenerator() {
    }

    static {
        StoryGenerator var0 = new StoryGenerator();
        INSTANCE = var0;
    }
}
