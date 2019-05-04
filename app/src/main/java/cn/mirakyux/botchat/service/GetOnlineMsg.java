package cn.mirakyux.botchat.service;


import android.content.Context;
import android.text.TextUtils;
import cn.mirakyux.botchat.plugin.TransApi;
import cn.mirakyux.botchat.util.Constant;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mi on 2017/9/13.
 * 获取在线消息(图灵api)
 */

public final class GetOnlineMsg {


    /**
     * 百度翻译api
     */
    private TransApi transApi;

    private String TLkey ;

    public GetOnlineMsg(String tlkey, String appid, String seckey)
    {
        TLkey = tlkey;
        transApi = new TransApi(appid, seckey);
    }

    public String getMsg(String str)
    {
        //翻译成中文
        str = transApi.getTransResult(str, Constant.BD_AUTO, Constant.BD_ZH);
        str = TextUtils.isEmpty(str) ? Constant.MSG_NETWORK_EXCEPTION : str;
        str = str.replace(Constant.CHAR_SPACE, Constant.CHAR_BLANK)
                .replace(Constant.CHAR_LF, Constant.CHAR_BLANK).trim();
        //获取图灵msg
        String res = doPost(setParams(str));
        return res;
    }

    private  Map<String, String> setParams(String str)
    {
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constant.TL_KEY, TLkey);
        params.put(Constant.TL_INFO, str);
        params.put(Constant.TL_USERID, Constant.TL_USER_ID);
        return params;
    }

    private String doPost(Map<String, String> params)
    {
        URL url = null;
        HttpURLConnection urlConnection = null;
        try {
            StringBuilder tempParams = new StringBuilder();
            int pos = 0;
            for (String key: params.keySet())
            {
                if(pos > 0)
                {
                    tempParams.append(Constant.CHAR_AND);
                }
                tempParams.append(String.format(Constant.STR_FORMAT, key, URLEncoder.encode(params.get(key), Constant.UTF8)));
                pos++;
            }
            byte[] data = tempParams.toString().getBytes();
            url = new URL(Constant.TL_HOST);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setUseCaches(false);
            urlConnection.setRequestMethod(Constant.POST);
            DataOutputStream dataOutputStream = new DataOutputStream(urlConnection.getOutputStream());
            dataOutputStream.write(data);
            dataOutputStream.flush();
            dataOutputStream.close();
            Map<String, String> resultMap = null;
            if(urlConnection.getResponseCode() == 200)
            {
                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line = null;
                while ((line = bufferedReader.readLine()) != null)
                {
                    stringBuilder.append(line);
                }
                String response = stringBuilder.toString();
                resultMap = new Gson().fromJson(response, Map.class);
                if (Constant.TL_CODE_40004.equals(resultMap.get(Constant.TL_CODE)))
                {
                    return Constant.MSG_CODE_40004;
                }
            }
            if (resultMap == null)
            {
                return Constant.MSG_NETWORK_EXCEPTION;
            }

            return transApi.getTransResult(resultMap.get(Constant.TL_TEXT), Constant.BD_AUTO, Constant.BD_EN);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return Constant.MSG_NETWORK_EXCEPTION;

    }

}
