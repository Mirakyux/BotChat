package cn.mirakyux.botchat.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.Toast;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import cn.mirakyux.botchat.R;
import cn.mirakyux.botchat.adapter.ListViewAdapter;
import cn.mirakyux.botchat.dao.MsgDao;
import cn.mirakyux.botchat.entity.Msg;
import cn.mirakyux.botchat.util.Constant;
import cn.mirakyux.botchat.util.Flags;
import cn.mirakyux.botchat.service.GetOnlineMsg;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {


    /**
     * 图灵api(抽出)
     */
    private String TLkey = null;
    private String AppId = null;
    private String SecKey = null;

    private boolean start = false;
    private int round = -1;
    private int gng = 0;
    private int flag = Flags.FLAG_COMPUTER;
    private int guess;
    private String text;

    private ListViewAdapter adapter;
    private ListView listView;
    private Button btn_send;
    private EditText msg;
//    private CommonDialog commonDialog;

    //private int cl;

    private Msg msgtext;
    private MsgDao msgDao;
    private Handler handler;
    private GetOnlineMsg onlineMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sp = getSharedPreferences(Constant.WORD_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        loadKey(sp, editor);
    }

    private void init() {
        // UI
        listView = findViewById(R.id.listview);
        btn_send = findViewById(R.id.btn_send);
        msg = findViewById(R.id.msg);

        btn_send.setClickable(false);
        btn_send.setOnClickListener(this);

        msgDao = new MsgDao(ChatActivity.this);


        adapter = new ListViewAdapter(this);
        listView.setAdapter(adapter);

        // KEY
        // 是否有用户key, 否则默认key
        SharedPreferences sp = getSharedPreferences(Constant.WORD_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        boolean once = sp.getBoolean(Constant.WORD_FIRST, true);
        // 第一次打开, 加载默认对话
        if (once) {
            loadOnce(editor);
        } else {
            loadAll();
        }

        //HANDLE
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
                        sendNewMsgToAdapter(msgtext);
                        break;
                    case 1:
                        break;
                }
            }
        };

    }

    private void loadKey(SharedPreferences sp, SharedPreferences.Editor editor) {
        String tlkey = sp.getString(Constant.WORD_TLKEY, Constant.TULING_KEY);
        String appid = sp.getString(Constant.WORD_APPID, Constant.BAIDU_APPID);
        String seckey = sp.getString(Constant.WORD_SECKEY, Constant.BAIDU_SECURITY_KEY);
        if(TextUtils.isEmpty(tlkey) || TextUtils.isEmpty(appid) || TextUtils.isEmpty(seckey)) {
            setting();
            Toast.makeText(ChatActivity.this, Constant.MSG_ERR_NO_KEY, Toast.LENGTH_LONG).show();
            return;
        }
        if (!TextUtils.equals(tlkey, TLkey)) {
            TLkey = tlkey;
            editor.putString(Constant.WORD_TLKEY, tlkey);
        }
        if (!TextUtils.equals(appid, AppId)) {
            AppId = appid;
            editor.putString(Constant.WORD_APPID, appid);
        }
        if (!TextUtils.equals(seckey, SecKey)) {
            SecKey = seckey;
            editor.putString(Constant.WORD_SECKEY, seckey);
        }
        editor.apply();
        onlineMsg = new GetOnlineMsg(TLkey, AppId, SecKey);
    }

    private void loadAll() {
        for (Msg msg : msgDao.getMsg()) {
            sendMsgToAdapter(msg);
            listView.smoothScrollToPosition(listView.getCount() - 1);
        }
    }

    private void loadOnce(SharedPreferences.Editor editor) {
        editor.putBoolean(Constant.WORD_FIRST, false);
        editor.commit();

        this.sendNewMsgToAdapter(new Msg(Constant.MSG_FIRST_01, true));
        this.sendNewMsgToAdapter(new Msg(Constant.MSG_FIRST_02, true));
        this.sendNewMsgToAdapter(new Msg(Constant.MSG_FIRST_03, true));
        this.sendNewMsgToAdapter(new Msg(Constant.MSG_FIRST_04, true));
        this.sendNewMsgToAdapter(new Msg(Constant.MSG_FIRST_05, true));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_key:
//                showInputKey();
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    //dialog
//    private void showInputKey() {
//        commonDialog = new CommonDialog(ChatActivity.this, R.style.dialog, "Please get your key" +
//                " on http://www.tuling123.com/member/robot/index.jhtml",
//
//                new CommonDialog.OnCloseListener() {
//                    @Override
//                    public void onClick(Dialog dialog, int confirm) {
//                        switch (confirm) {
//                            case Flags.FLAG_SUBMIT:
//                                String key = commonDialog.getInput();
//                                SharedPreferences sp = getSharedPreferences("key", Context.MODE_PRIVATE);
//                                SharedPreferences.Editor editor = sp.edit();
//                                if (key == null || key.length() != 32) {
//                                    Toast.makeText(ChatActivity.this, "Wrong length", Toast.LENGTH_SHORT).show();
//                                    commonDialog.setInput(key);
//                                    editor.putString("tlkey", TLkey);
//                                    editor.apply();
//                                } else {
//                                    editor.putString("tlkey", key);
//                                    editor.apply();
//                                }
//                                break;
//                            case Flags.FLAG_CONTENT:
//                                Intent intent = new Intent();
//                                intent.setAction("android.intent.action.VIEW");
//                                Uri uri = Uri.parse("http://www.tuling123.com/member/robot/index.jhtml");
//                                intent.setData(uri);
//                                startActivity(intent);
//                                break;
//                            default:
//                                break;
//                        }
//                    }
//                });
//        commonDialog.setTitle("Input your key");
//        commonDialog.show();
//    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_send:
                text = msg.getText().toString().trim();

                if (!TextUtils.isEmpty(text)) {
                    this.sendNewMsgToAdapter(new Msg(text, false));
                    switch (text) {
//                        case "input key":
//                            showInputKey();
//                            break;
//                        case "show key":
//                            sendNewMsgToAdapter(new Msg(TLkey, true));
//                            break;
                        case Constant.WORD_HELP:
                            sendNewMsgToAdapter(new Msg(Constant.MSG_HELP, true));
                            break;
                        case Constant.WORD_CLEAR:
                            clearMsg();
                            break;
                        case Constant.WORD_SET:
                            setting();
                            break;
                        default:
                            if (start) {
                                int mguess = findNumber(text);
                                if (mguess > 0) {
                                    guess = mguess;
                                    flag = Flags.FLAG_COMPUTER;
                                    btn_send.setClickable(false);
                                } else if (text.equals(Constant.WORD_END)) {
                                    start = false;
                                    sendNewMsgToAdapter(new Msg(Constant.MSG_EXIT_GAME, true));
                                } else {
                                    new OnlineThread().start();
                                }
                            } else if (TextUtils.equals(Constant.WORD_GUESS, text) && round == -1) {
                                flag = Flags.FLAG_COMPUTER;
                                this.openGuess();
                            } else {
                                new OnlineThread().start();
                            }
                    }

                }
                break;
        }
    }

    private void setting() {
        Intent intent = new Intent();
        intent.setClass(ChatActivity.this, SettingActivity.class);
        startActivity(intent);
    }

    private int findNumber(String text) {
        String num = "";
        if (text != null && !"".equals(text))
            for (int i = 0; i < text.length(); i++) {
                char c = text.charAt(i);
                if (!"".equals(num) && (c < 48 || c > 57)) {
                    break;
                }
                if (c >= 48 && c <= 57) {
                    num += c;
                }
            }
        if ("".equals(num)) {
            return -1;
        }
        return Integer.valueOf(num);
    }

    private void sendNewMsgToAdapter(Msg newmsg) {
        sendMsgToAdapter(newmsg);
        msgDao.insertMsg(newmsg);
    }

    private void sendMsgToAdapter(Msg newmsg) {
        adapter.addMsgToAdapter(newmsg);
        adapter.notifyDataSetChanged();
        listView.smoothScrollToPosition(listView.getCount() - 1);
        msg.setText(Constant.CHAR_BLANK);
    }

    private void openGuess() {
        flag = Flags.FLAG_COMPUTER;
        new BotThread().start();
        new UserThread().start();
    }

    private void clearMsg() {
        msgDao.deleteMsg();
        restartApplication();
    }

    private void restartApplication() {
        final Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    private class BotThread extends Thread {
        int mynum = 0;
        int win = 0;
        int ng;
        String wrong = "Wrong! ";

        public void run() {
            start = true;
            Log.e("Bot", "Running");
            mynum = 0;
            round = 0;
            win = 0;

            try {

                sendMsg(new Msg("Are you ready?...ummm...GO!", true));
                sleep(500);

                while (round++ < 10) {
                    ng = 0;
                    guess = 200;

                    sendMsg(new Msg("Round " + round, true));
                    sleep(100);

                    mynum = (int) (Math.random() * 100) + 1;

                    sendMsg(new Msg("Give me your answer.", true));
                    sleep(500);


                    sendMsg(new Msg("or chat with me...", true));
                    sleep(500);

                    int x = 3;
                    flag = Flags.FLAG_HUMAN;
                    while (x > 0) {

                        if (flag == Flags.FLAG_HUMAN) {
                            try {
                                sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            continue;
                        }


                        if (ng > 5) {
                            sendMsg(new Msg("Do not try my patience archer, You lose this round because of your stupid!", true));
                            sleep(1000);
                            break;
                        }

                        if (gng > 25) {
                            round = 15;
                            x = -1;
                            sleep(1000);
                            sendMsg(new Msg("Listen to me archer! You've lost my patience and your opportunity!", true));
                            sleep(3000);
                            handler.sendEmptyMessage(1);
                            continue;
                        }


                        if (guess > 1000) {
                            flag = Flags.FLAG_HUMAN;
                            gng += 2;
                            ng += 2;
                            sendMsg(new Msg("I think your ambition is soooooooooo large, why don't we ...a little smaller...please... ", true));
                            sleep(500);
                            continue;

                        } else if (guess > 100) {
                            flag = Flags.FLAG_HUMAN;
                            gng++;
                            ng++;
                            sendMsg(new Msg("Ahaha, Just a little less...", true));
                            sleep(500);
                            continue;

                        } else if (guess < 1) {
                            flag = Flags.FLAG_HUMAN;
                            gng++;
                            ng++;
                            sendMsg(new Msg("Ummmmmm...Calm down! Do you know what my mean? ... I think So...", true));
                            sleep(500);
                            continue;

                        } else if (guess == mynum) {
                            win++;
                            sendMsg(new Msg("Lucky! You are right!", true));
                            sleep(500);

                            break;
                        }
                        if (--x < 0) {
                            sendMsg(new Msg("Lose! My number is " + mynum + "! ", true));
                            sleep(500);

                        } else {
                            String text;
                            switch (Math.abs(mynum - guess)) {
                                case 1:
                                    text = "Hot!";
                                    break;
                                case 2:
                                    text = "Warm!";
                                    break;
                                case 3:
                                    text = "Cold!";
                                    break;
                                default:
                                    text = "Keep on going!";
                                    break;
                            }
                            sendMsg(new Msg(wrong + text, true));
                            sleep(100);

                            if (guess > mynum) {
                                sendMsg(new Msg("Your guess is bigger than mine", true));
                                sleep(100);
                            } else {
                                sendMsg(new Msg("My number is bigger than yours", true));
                                sleep(100);
                            }


                            if (x > 0) {
                                sendMsg(new Msg("Give me your answer.", true));
                                sleep(500);
                                sendMsg(new Msg("or chat with me...", true));
                                sleep(500);
                            } else {
                                sendMsg(new Msg("Lose! My number is " + mynum + ", please try again!", true));
                                sleep(500);
                            }

                        }
                        flag = Flags.FLAG_HUMAN;
                    }

                    sendMsg(new Msg("You have won " + win + " out of " + round + " rounds.", true));
                    sleep(100);

                }

                sendMsg(new Msg("ummmm......", true));
                sleep(100);

                String rating = "I don't want to blow your selfesteem...";
                switch (win) {
                    case 10:
                        rating = "Hackers！God like! Welcome to our fimaly! My new deputy!";
                        break;
                    case 9:
                        rating = "Professionals! Give you an elder! I believe you will be the Deputy!(#laugh)";
                        break;
                    case 8:
                        rating = "Advanced! Give you an elder!";
                        break;
                    case 7:
                        rating = "Amateurs! I think you should try again!";
                    default:
                        break;
                }

                start = false;
                sendMsg(new Msg(rating, true));
                sleep(500);

                sendMsg(new Msg("If you want to play again", true));
                sleep(500);
                sendMsg(new Msg("Please tell me 'guess'", true));
                sleep(500);
                round = -1;

            } catch (Exception e) {
                Toast.makeText(ChatActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        private void sendMsg(Msg msg) {
            msgtext = msg;
            handler.sendEmptyMessage(0);
        }

    }

    private class UserThread extends Thread {
        public void run() {
            while (start) {
                if (flag == Flags.FLAG_COMPUTER) {
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    btn_send.setClickable(true);
                }
            }
        }
    }

    private class OnlineThread extends Thread {
        @Override
        public void run() {
            sendMsg(new Msg(onlineMsg.getMsg(text), true));
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private void sendMsg(Msg msg) {
            msgtext = msg;
            handler.sendEmptyMessage(0);
        }
    }
}
