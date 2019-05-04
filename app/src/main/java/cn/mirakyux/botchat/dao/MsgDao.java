package cn.mirakyux.botchat.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cn.mirakyux.botchat.dao.dbhelper.SqliteHelper;
import cn.mirakyux.botchat.entity.Msg;

/**
 * Created by Mi on 2017/9/15.
 */

public class MsgDao {
    private SqliteHelper helper = null;

    public MsgDao(Context context)
    {
        helper = new SqliteHelper(context);
    }

    public List<Msg> getMsg()
    {
        List<Msg> msgs = new ArrayList<>();
        SQLiteDatabase readableDatabase = this.helper.getReadableDatabase();
        Cursor query = readableDatabase.query("msg", new String[]{"_id", "msg", "bot", "time"}, null, null, null, null, null);
        if(query != null && query.getCount() > 0)
        {
            while (query.moveToNext())
            {
                Log.e("Msg",query.getString(query.getColumnIndexOrThrow("msg")) + "--------" +
                        "-------" +( query.getInt(query.getColumnIndexOrThrow("bot")) == 1));
                msgs.add( new Msg(query.getString(query.getColumnIndexOrThrow("msg")),
                        query.getInt(query.getColumnIndexOrThrow("bot")) == 1));
            }
        }
        if (query != null)
        {
            query.close();
        }
        readableDatabase.close();
        return msgs;
    }

    public void insertMsg(Msg msg)
    {
        SQLiteDatabase writableDatabase = this.helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("msg", msg.getMsg());
        contentValues.put("bot", msg.isBot()?1:0);
        contentValues.put("time", System.currentTimeMillis());
        writableDatabase.insert("msg", null, contentValues);
        writableDatabase.close();
    }

    public void deleteMsg()
    {
        SQLiteDatabase writableDatabase = this.helper.getWritableDatabase();
        writableDatabase.delete("msg", null, null);
        writableDatabase.close();
    }
}
