package com.example.friendfield.Utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.example.friendfield.R;

public class GenericTextWatcher implements TextWatcher {

    private View etPrev;
    private View etNext;

    public GenericTextWatcher(View etNext, View etPrev) {
        this.etPrev = etPrev;
        this.etNext = etNext;
    }

    @Override
    public void afterTextChanged(Editable editable) {
        // TODO Auto-generated method stub
        String text = editable.toString();
//        switch (etNext.getId()) {
//
//            case R.id.btn_1:
//                etNext.setBackgroundResource(R.drawable.select_phone_number_bg);
//
//                if (text.length() == 1) {
//                    etPrev.requestFocus();
//                    etPrev.setBackgroundResource(R.drawable.select_phone_number_bg);
//                }
//                break;
//            case R.id.btn_2:
//                if (text.length() == 1) {
//                    etPrev.requestFocus();
//                    etPrev.setBackgroundResource(R.drawable.select_phone_number_bg);
//
//                } else if (text.length() == 0) {
//                    etNext.requestFocus();
//                }
//                break;
//            case R.id.btn_3:
//                if (text.length() == 1) {
//                    etPrev.requestFocus();
//                    etPrev.setBackgroundResource(R.drawable.select_phone_number_bg);
//                } else if (text.length() == 0) {
//                    etNext.requestFocus();
//                }
//                break;
//            case R.id.btn_4:
//                if (text.length() == 0)
//                    etNext.requestFocus();
//
//                break;
//        }
    }

    @Override
    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        // TODO Auto-generated method stub
    }

}
