package com.example.friendfield.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.friendfield.Adapter.MessageAdapter;
import com.example.friendfield.MainActivity;
import com.example.friendfield.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class ChatingActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    CircleImageView img_user;
    ImageView hp_back_arrow, img_contact, img_video_call;
    RelativeLayout rl_user;
    ImageView img_camera, img_gallery;
    RelativeLayout btn_send;
    RecyclerView chat_recycler;
    EditText edt_chating;
    private WebSocket webSocket;
    private String SERVER_PATH = "ws://192.168.29.105:8080/socket.io/socket.io.js";
    private static final int PERMISSION_CODE = 1000;
    private int IMAGE_CAPTURE_CODE = 1001;
    Uri imageUri;
    private int IMAGE_REQUEST_ID = 1;
    private MessageAdapter messageAdapter;
    String txt_name;
    TextView u_name;
    TextView txt_online;

    RelativeLayout rel_replay;
    ImageView iv_pro_image;
    TextView txt_pro_name;
    TextView txt_pro_des;
    TextView txt_pro_price;
    LinearLayout lin_icon;

    String p_name;
    String p_des;
    String p_price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chating);

        img_user = findViewById(R.id.img_user);
        hp_back_arrow = findViewById(R.id.hp_back_arrow);
        rl_user = findViewById(R.id.rl_user);
        img_contact = findViewById(R.id.img_contact);
        img_video_call = findViewById(R.id.img_video_call);
        chat_recycler = findViewById(R.id.chat_recycler);
        btn_send = findViewById(R.id.btn_send);
        edt_chating = findViewById(R.id.edt_chating);
        img_camera = findViewById(R.id.img_camera);
        img_gallery = findViewById(R.id.img_gallery);
        u_name = findViewById(R.id.u_name);
        txt_online = findViewById(R.id.txt_online);

        rel_replay = findViewById(R.id.rel_replay);
        iv_pro_image = findViewById(R.id.iv_pro_image);
        txt_pro_name = findViewById(R.id.txt_pro_name);
        txt_pro_des = findViewById(R.id.txt_pro_des);
        txt_pro_price = findViewById(R.id.txt_pro_price);
        lin_icon = findViewById(R.id.lin_icon);

        hp_back_arrow.setOnClickListener(this);
        rl_user.setOnClickListener(this);
        img_contact.setOnClickListener(this);
        img_video_call.setOnClickListener(this);
        img_camera.setOnClickListener(this);

        txt_name = getIntent().getStringExtra("Name");

        p_name = getIntent().getStringExtra("product_name");
        p_des = getIntent().getStringExtra("product_des");
        p_price = getIntent().getStringExtra("product_price");

        if (p_name == null && p_des == null && p_price == null) {
            rel_replay.setVisibility(View.GONE);
        } else {
            txt_pro_name.setText(p_name);
            txt_pro_des.setText(p_des);
            txt_pro_price.setText(p_price);
            rel_replay.setVisibility(View.VISIBLE);
        }

        lin_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rel_replay.setVisibility(View.GONE);
            }
        });

        u_name.setText(txt_name);

        initiateSocketConnection();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.hp_back_arrow:
                startActivity(new Intent(ChatingActivity.this, MainActivity.class));
                finish();
                break;
            case R.id.rl_user:
                startActivity(new Intent(ChatingActivity.this, ChatUserProfileActivity.class));
//                finish();
                break;
            case R.id.img_contact:
                startActivity(new Intent(ChatingActivity.this, ChattingAudioCallActivity.class));
//                finish();
                break;
            case R.id.img_video_call:
                startActivity(new Intent(ChatingActivity.this, VideoCallActivity.class));
//                finish();
                break;
            case R.id.img_camera:
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    requestPermissions(permission, PERMISSION_CODE);
                } else {
                    openCamera();
                }
                break;
        }
    }

    private void initiateSocketConnection() {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(SERVER_PATH).build();
        webSocket = client.newWebSocket(request, new SocketListener());

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        String string = editable.toString().trim();

        if (string.isEmpty()) {
            resetMessageEdit();
        }
    }

    private void resetMessageEdit() {
        edt_chating.removeTextChangedListener(this);
        edt_chating.setText("");
        edt_chating.addTextChangedListener(this);
    }

    private class SocketListener extends WebSocketListener {

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            super.onOpen(webSocket, response);

            runOnUiThread(() -> {
                Toast.makeText(ChatingActivity.this, "Socket Connection Successful!", Toast.LENGTH_SHORT).show();
                initializeView();
            });

        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            super.onMessage(webSocket, text);

            runOnUiThread(() -> {

                try {
                    JSONObject jsonObject = new JSONObject(text);
                    jsonObject.put("isSent", false);

                    messageAdapter.addItem(jsonObject);

                    chat_recycler.smoothScrollToPosition(messageAdapter.getItemCount() - 1);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            });

        }
    }

    private void initializeView() {

        messageAdapter = new MessageAdapter(getLayoutInflater());
        chat_recycler.setAdapter(messageAdapter);
        chat_recycler.setLayoutManager(new LinearLayoutManager(this));

        edt_chating.addTextChangedListener(this);

        btn_send.setOnClickListener(v -> {

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("name", txt_name);
                jsonObject.put("message", edt_chating.getText().toString());

                webSocket.send(jsonObject.toString());

                jsonObject.put("isSent", true);
                messageAdapter.addItem(jsonObject);

                chat_recycler.smoothScrollToPosition(messageAdapter.getItemCount() - 1);

                resetMessageEdit();

            } catch (JSONException e) {
                e.printStackTrace();
            }

        });

        img_gallery.setOnClickListener(v -> {

            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");

            startActivityForResult(Intent.createChooser(intent, "Pick image"),
                    IMAGE_REQUEST_ID);

        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST_ID && resultCode == RESULT_OK) {

            try {
                InputStream is = getContentResolver().openInputStream(data.getData());
                Bitmap image = BitmapFactory.decodeStream(is);

                sendImage(image);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    private void sendImage(Bitmap image) {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);

        String base64String = Base64.encodeToString(outputStream.toByteArray(),
                Base64.DEFAULT);

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("name", txt_name);
            jsonObject.put("image", base64String);

            webSocket.send(jsonObject.toString());

            jsonObject.put("isSent", true);

            messageAdapter.addItem(jsonObject);

            chat_recycler.smoothScrollToPosition(messageAdapter.getItemCount() - 1);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                } else {
                    Toast.makeText(this, getResources().getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ChatingActivity.this, MainActivity.class));
        finish();
    }
}