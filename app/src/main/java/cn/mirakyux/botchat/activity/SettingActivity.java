package cn.mirakyux.botchat.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;
import cn.mirakyux.botchat.R;
import cn.mirakyux.botchat.util.Constant;

/**
 * Created by Mi on 2019/05/05.
 */
public class SettingActivity extends AppCompatActivity{

    // UI references.
    private EditText mTlKey;
    private EditText mAppId;
    private EditText mSecurityKey;
    private Button mSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        init();
    }

    private void init(){
        mTlKey = findViewById(R.id.tl_key);
        mAppId = findViewById(R.id.app_id);
        mSecurityKey = findViewById(R.id.security_key);
        mSave = findViewById(R.id.save);
        loadKey();
    }
    private void loadKey() {
        SharedPreferences sp = getSharedPreferences(Constant.TL_KEY, Context.MODE_PRIVATE);
        final String tlkey = sp.getString(Constant.WORD_TLKEY, Constant.TULING_KEY);
        String appid = sp.getString(Constant.WORD_APPID, Constant.BAIDU_APPID);
        String seckey = sp.getString(Constant.WORD_SECKEY, Constant.BAIDU_SECURITY_KEY);
        mTlKey.setText(TextUtils.equals(tlkey, Constant.TULING_KEY) ? Constant.CHAR_BLANK : tlkey);
        mAppId.setText(TextUtils.equals(appid, Constant.BAIDU_APPID) ? Constant.CHAR_BLANK : appid);
        mSecurityKey.setText(TextUtils.equals(seckey, Constant.BAIDU_SECURITY_KEY) ? Constant.CHAR_BLANK : seckey);
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tlkey = mTlKey.getText().toString().trim();
                String appid = mAppId.getText().toString().trim();
                String seckey = mSecurityKey.getText().toString().trim();
                SharedPreferences sp = getSharedPreferences(Constant.WORD_KEY, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                if(!TextUtils.isEmpty(tlkey)){
                    if(tlkey.length() != 32) {
                        Toast.makeText(SettingActivity.this, String.format(Constant.MSG_ERR_TOAST, Constant.WORD_TLKEY, 32), Toast.LENGTH_LONG).show();
                        mTlKey.setError(String.format(Constant.MSG_ERR_INPUT, 32));
                    } else {
                        editor.putString(Constant.WORD_TLKEY, tlkey);
                    }
                }
                if(!TextUtils.isEmpty(appid)){
                    if(appid.length() != 17) {
                        Toast.makeText(SettingActivity.this, String.format(Constant.MSG_ERR_TOAST, Constant.WORD_APPID, 17), Toast.LENGTH_LONG).show();
                        mAppId.setError(String.format(Constant.MSG_ERR_INPUT, 17));
                    } else {
                        editor.putString(Constant.WORD_APPID, appid);
                    }
                }
                if(!TextUtils.isEmpty(seckey)){
                    if(seckey.length() != 20) {
                        Toast.makeText(SettingActivity.this, String.format(Constant.MSG_ERR_TOAST, Constant.WORD_SECKEY, 20), Toast.LENGTH_LONG).show();
                        mSecurityKey.setError(String.format(Constant.MSG_ERR_INPUT, 20));
                    } else {
                        editor.putString(Constant.WORD_SECKEY, seckey);
                    }
                }
                editor.apply();
            }
        });
    }


}

