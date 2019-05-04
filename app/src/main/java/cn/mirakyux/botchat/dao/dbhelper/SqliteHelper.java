package cn.mirakyux.botchat.dao.dbhelper;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Mi on 2017/9/15.
 */

public class SqliteHelper extends SQLiteOpenHelper {

    private final static String DB_NAME = "msg.db";
    private final static int VERSION = 1;

    public SqliteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public SqliteHelper(Context context)
    {
        this(context, DB_NAME, null, VERSION);
    }


    public SqliteHelper(Context context, int version)
    {
        this(context, DB_NAME, null, version);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        StringBuilder sb = new StringBuilder()
                .append("CREATE TABLE msg(")
                .append("   _id INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append("   msg TEXT, ")
                .append("   bot INTEGER, ")
                .append("   time INTEGER")
                .append(")");
        sqLiteDatabase.execSQL(sb.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
