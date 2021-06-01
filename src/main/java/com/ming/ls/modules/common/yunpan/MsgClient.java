package com.ming.ls.modules.common.yunpan;

import com.yunpian.sdk.YunpianException;

import java.util.HashMap;
import java.util.Map;

import static com.ming.ls.modules.common.sysConst.Const.*;
import static com.yunpian.sdk.util.HttpUtil.post;

public class MsgClient {
    /**
     * 批量发送短信,相同内容多个号码,智能匹配短信模板
     *
     * @param apikey 成功注册后登录云片官网,进入后台可查看
     * @param text   需要使用已审核通过的模板或者默认模板
     * @param mobile 接收的手机号,多个手机号用英文逗号隔开
     * @return json格式字符串
     */
    public static String batchSendVip(String mobile) throws YunpianException {
        Map<String, String> params = new HashMap<String, String>();//请求参数集合
        params.put("apikey", API_KEY);
        params.put("text", VIP_MSG_TEXT);
        params.put("mobile", mobile);
        return post("https://sms.yunpian.com/v2/sms/batch_send.json", params);//请自行使用post方式请求,可使用Apache HttpClient
    }

    public static String batchSendPick(String mobile) throws YunpianException {
        Map<String, String> params = new HashMap<String, String>();//请求参数集合
        params.put("apikey", API_KEY);
        params.put("text", PICK_MSG_TEXT);
        params.put("mobile", mobile);
        return post("https://sms.yunpian.com/v2/sms/batch_send.json", params);//请自行使用post方式请求,可使用Apache HttpClient
    }

    public static String batchSendMsg(String text, String mobile) throws YunpianException {
        Map<String, String> params = new HashMap<>();//请求参数集合
        params.put("apikey", API_KEY);
        params.put("text", text);
        params.put("mobile", mobile);
        return post("https://sms.yunpian.com/v2/sms/batch_send.json", params);//请自行使用post方式请求,可使用Apache HttpClient
    }
}
