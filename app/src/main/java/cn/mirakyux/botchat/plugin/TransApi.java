package cn.mirakyux.botchat.plugin;

import cn.mirakyux.botchat.util.Constant;

import java.util.HashMap;
import java.util.Map;

public class TransApi {

    private String appid;
    private String securityKey;

    public TransApi(String appid, String securityKey) {
        this.appid = appid;
        this.securityKey = securityKey;
    }

    public String getTransResult(String query, String from, String to) {
        Map<String, String> params = buildParams(query, from, to);
        return HttpGet.get(Constant.BD_HOST, params);
    }

    private Map<String, String> buildParams(String query, String from, String to) {
        Map<String, String> params = new HashMap<>();
        params.put(Constant.BD_Q, query);
        params.put(Constant.BD_FROM, from);
        params.put(Constant.BD_TO, to);

        params.put(Constant.BD_APPID, appid);

        // 随机数
        String salt = String.valueOf(System.currentTimeMillis());
        params.put(Constant.BD_SALT, salt);

        // 签名
        String src = appid + query + salt + securityKey; // 加密前的原文
        params.put(Constant.BD_SIGN, MD5.md5(src));

        return params;
    }

}
