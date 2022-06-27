package com.example.friendfield.Utils;

public class Constans {

    public static String CHAT_SERVER_URL = "ws://192.168.29.105:8080/socket.io/socket.io.js";
    public static String BASE_URL = "http://192.168.29.105:8080/v1/";

    public static String send_otp = BASE_URL + "user/send-otp";
    public static String verify_otp = BASE_URL + "user/verify-otp";
    public static String profile_register = BASE_URL + "profile/personal/register";
    public static String business_register = BASE_URL + "profile/business/register";
    public static String fetch_personal_info = BASE_URL + "profile/personal";
    public static String fetch_business_info = BASE_URL + "profile/business";
    public static String add_product = BASE_URL + "product";
    public static String fetch_single_product = BASE_URL + "product/single/";
    public static String marketing_notification = BASE_URL + "marketing/notification";
    public static String story_create = BASE_URL + "story";
    public static String story_increase_view_count = BASE_URL + "story/increase-view-count/";

}
