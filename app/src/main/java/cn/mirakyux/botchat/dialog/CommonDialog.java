package cn.mirakyux.botchat.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import cn.mirakyux.botchat.R;
import cn.mirakyux.botchat.util.Flags;

/**
 * Created by Mi on 2017/9/15.
 * https://github.com/xiaoxiaoqingyi/mine-android-repository
 */

public class CommonDialog extends Dialog implements View.OnClickListener {

    private TextView contentText;
    private TextView titleText;
    private TextView submitText;
    private TextView cancelText;
    private EditText inputText;

    private OnCloseListener listener;
    private Context context;
    private String content;
    private String positiveName;
    private String negativeName;
    private String title;
    private String input;

    public CommonDialog(Context context, int themeResId, String content, OnCloseListener listener) {
        super(context, themeResId);
        this.context = context;
        this.content = content;
        this.listener = listener;
    }

    public CommonDialog setTitle(String title){
        this.title = title;
        return this;
    }

    public CommonDialog setInput(String input){
        this.input = input;
        this.inputText.setText(input);
        return this;
    }

    public String getInput()
    {
        return this.inputText.getText().toString().trim();
    }

    public CommonDialog setPositiveButton(String name){
        this.positiveName = name;
        return this;
    }



    public CommonDialog setNegativeButton(String name){
        this.negativeName = name;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_dialog);
        setCanceledOnTouchOutside(false);
        initView();
    }



    private void initView(){
        titleText = (TextView)findViewById(R.id.title);
        contentText = (TextView)findViewById(R.id.content);
        inputText = (EditText) findViewById(R.id.input);
        submitText = (TextView)findViewById(R.id.submit);
        cancelText = (TextView)findViewById(R.id.cancel);
        contentText.setOnClickListener(this);
        submitText.setOnClickListener(this);
        cancelText.setOnClickListener(this);

        contentText.setText(content);
        if(!TextUtils.isEmpty(positiveName)){
            submitText.setText(positiveName);
        }
        if(!TextUtils.isEmpty(negativeName)){
            cancelText.setText(negativeName);
        }
        if(!TextUtils.isEmpty(title)){
            titleText.setText(title);
        }
        if(!TextUtils.isEmpty(input)){
            inputText.setText(input);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel:
                if(listener != null){
                    listener.onClick(this, Flags.FLAG_CANCEL);
                }
                this.dismiss();
                break;
            case R.id.submit:
                if(listener != null){
                    listener.onClick(this, Flags.FLAG_SUBMIT);
                }
                this.dismiss();
                break;
            case R.id.content:
                if(listener != null){
                    listener.onClick(this, Flags.FLAG_CONTENT);
                }
                break;
        }
    }

    public interface OnCloseListener{
        void onClick(Dialog dialog, int confirm);
    }

}
