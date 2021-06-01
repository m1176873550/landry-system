package com.ming.ls.config;

import org.springframework.context.annotation.Configuration;

import java.io.FileWriter;
import java.io.IOException;

@Configuration
public class AlipayConfig {

    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号,开发时使用沙箱提供的APPID，生产环境改成自己的APPID
    public static String APP_ID = "2016102100733077";

    // 商户私钥，您的PKCS8格式RSA2私钥
    public static String APP_PRIVATE_KEY = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCAdUJDG4VHu7/8rGhRACE2kKS+DHV0Kcx52TL5A4JigGeDY9uk0ruzG8X7eLczzFi0vB6iCz92AcsA7zn7dKUmHu+lMdeLYwRyzSh50RJ80oToBW3PSirnbsrDcfrQIyF5M5eFksgr56pfQ9XY/nWkjYnjfuzIpaDZA5/WsmjgTmpGzC5CPkcqgk8vpsLdOUvGmyKZVn/DDRK/XBDjMYb8cXUxClfaBAqFcmMTgpOwvoo0CaPsy9HZ6X4hSUjvo17oMptFZ7E4CCJpSZGB32PqZ3oGi8tQqZ/a+SAxZQz6BdQQselQIQjQRH28mphrdPddbWjDLUsiQWUhFXwB+pZNAgMBAAECggEAIuu9j7CzksSfdHkKArseTE8V+fQ5GdMjGxzIlpk1oMF3YmiLFUuUayRptesjIpmyo69gRHz6S4JBRYfDI3CaVLqkOyxJhELuuqa7EiKb/ALezpRrPabT8wZTaL5biKmEOAamclFsyUK5NhyizdKe+JThn06p4AHmsBgkI6FskU1dNP0OtBsiSd5Yg1PrP5L3jETrySkxtLqv6p2Ev4sAKrAg+gQtSxaKoHax7NTQM7qFbtOeN8xvJUm2Wx15EKlPAP4lDHZJjNqKw1i/8Lc3R6TdVUqslmPWAZ4G+1xmtejLNwjZ4Zglr2uTo3d5pmXaQRy7N3HqOFlmSmQ57cVOoQKBgQC9n3t2g94ke4nPZgcEevRDh+GTB1LBNeQ5sD+JALzqV1b7kJZ+YgXk58y3AS8OV2flo7qaoXQiWAFxqUcVqPtkjk6EjXZbOkXzdFCEgB//jr84jaDCQwHcHyyr8mkxcpsvwBRi+fvrIn1SN2XHBupQk69J6pAE6El00qDYb4Hg7wKBgQCtbKgk0TOOjWU12U8RNvVEfcUOKTiU2nWvQUSLJT8puaz8wdRreCrlfIWQ5Fz9Whqe6WHQ6xbAnFVRBrma4BWIZljLRaGzv2UxK96t63V6NyhbTkU/EN4wZo+FPTX+cQIEw0ekTdbX6J2gyTay2IlDQPaddtSoXReR/ZuLv4tEgwKBgBA2KVUJkx1lXnPX0jPE20tGu7dZQe74z4lLEzOAV6ZlTy90vhkSAlT+qFbhCAfe2ygb5mE/+nDRO1ZfVY95gfyn2filK4BDLCXTROr/FT1BFAIewaa6Gdea5rPQpsOue6wKbPPFUY9ZxIMZ716jrbaz2aGlJyj0Pwt40n2OkvkLAoGBAIg+GoKCD3zoPCDnzaP6hwj9vXd4jsjyLw41ukGOS2beRbZ0GAst5pVPFCdWIG+w/8TkGEJmzgMXTjdPjzTK4l+79/oJhBWQQVE148TCfBI9jujcPq5i9AsWJ55HDkoMQce4GmpL9IK+bkdIjD5Iut0pBhWCq2Jxb/jzQeWx3xk9AoGBAKsgcHxToSKVv8uC4XqmzYl/iuNyAOuMaxIblwX4EcJOK2WtkktYpRu7TWYpQ/YorkFYAAvWU5WSJPav0G1mW5xpiX49kSp7H0fFG/uUCTkCMZs2R41Vumy2wOJz9QSASPP1Fm33zV85KPUxll2ZHiqadZRRE69NoEROtToh4NuM";

    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAj+Hc+7j3jGwBPDw5LifFGZ35/SQt1FFgG4rJgbQww4HyladGTc4gAw5su7+Wt9e7uhexY4hohTuObdQHAk1C3LjSW/jw8ExvPf30O6lfR1CCrKt48oI6p10xZHdFTZuqP5veLOFq/fbpmUhXU+9xviuoEr9s2YQhvIiDtV17Rc2jloV996FwvyWtuYnJTeTUv+UWCPHC5oKfTe3Ed5/KfIresdrFwJWIlyvoNRwRPp8blYH7fvRykrZsSw65y956SL3jlIaRkorU0l0os7XdVWjT6rVxckqJilVdG5NOsG9+5GoA7ZRBxMreIjXFQtg2yO0xcuYUcoBMEQtSS8awdQIDAQAB";

    // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = "http://localhost:8081/alipay.trade.page.pay-JAVA-UTF-8/notify_url.jsp";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问(其实就是支付成功后返回的页面)
    public static String return_url = "http://localhost:8081/view/home/home.htm";

    // 签名方式
    public static String sign_type = "RSA2";

    // 字符编码格式
    public static String CHARSET = "utf-8";

    // 支付宝网关，这是沙箱的网关
    public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

    // 日志
    public static String log_path = "C:\\";


//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    /**
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     * @param sWord 要写入日志里的文本内容
     */
    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis()+".txt");
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }



}