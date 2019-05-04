package cn.mirakyux.botchat.entity;

/**
 * Created by Mi on 2017/9/11.
 */

public class Msg {

    private String msg;
    private boolean bot;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isBot() {
        return bot;
    }

    public void setBot(boolean bot) {
        this.bot = bot;
    }

    public Msg(String msg, boolean bot)
    {
        this.msg = msg;
        this.bot = bot;
    }


}
