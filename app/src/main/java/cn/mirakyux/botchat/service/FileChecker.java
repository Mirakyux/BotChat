package cn.mirakyux.botchat.service;

import android.content.Context;

import java.io.File;
import java.io.IOException;

/**
 * Created by Mi on 2017/9/12.
 */

public class FileChecker{
    Context context = null;
    String filepath = "";

    public FileChecker(Context context)
    {
        this.context = context;
        filepath = context.getFilesDir() + "/.angry_queen";
    }

    public void writeFile() throws IOException {
        File f = new File(filepath);
        if(!fileIsExists())
        {
            f.createNewFile();
        }
    }

    public boolean fileIsExists(){
        File f = new File(filepath);
        if (!f.exists())
        {
            return false;
        }
        return true;
    }

}
