package com.cms.util;


import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.HttpKit;
import com.jfinal.plugin.ehcache.CacheKit;
import com.jfinal.render.RenderManager;

public class WeixinUtils {
    
    /** 缓存名称 */
    public static final String CACHE_NAME = "weixin";
    
    /** 异步地址 */
    private static String notifyUrl = "/common/notify/weixinpayNotify";
    
    /** 微信公众号 */
    private static final String wechatAppId = "wx28484f7cb731589e";//微信分配的公众账号ID（企业号corpid即为此appId）
    private static final String wechatSecret = "2c45bf468816e7bdd4d98c1bcce79ac0";
    
    /** 微信小程序 */
    private static final String xcxAppId = "";   //小程序AppId
    private static final String xcxSecret = "";  //小程序Secret
    
    /** 微信开放平台 */
    private static final String openAppId = "";  //微信开放平台审核通过的应用APPID
    
    /** 微信支付平台 */
    private static final String mchId = "";      //微信支付分配的商户号
    private static final String mchAppId = "";   //微信支付分配的应用ID
    public static final String key = "";        //key设置路径：微信商户平台(pay.weixin.qq.com)-->账户设置-->API安全-->密钥设置
    
	
    //=====基本 token  和  ticket
    
	private static final String GET_TOKEN_URL="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";
	
	public static String getAccessToken(){
        String cacheKey = "accessToken";
        String cacheAccessToken = CacheKit.get(CACHE_NAME, cacheKey);
        if (StringUtils.isBlank(cacheAccessToken)) {
            //======获取开始
            String url = String.format(GET_TOKEN_URL, wechatAppId, wechatSecret);
            String content= HttpKit.get(url, null);
            JSONObject jsonObject = JSONObject.parseObject(content);
            System.out.println("======"+jsonObject.toJSONString());
            String accessToken = "";
            if (null != jsonObject) {
                try {
                    accessToken = jsonObject.getString("access_token");
                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("获取token失败 errcode:" + jsonObject.getIntValue("errcode") + " errmsg:" + jsonObject.getString("errmsg"));
                }
            }
            //======获取结束
            CacheKit.put(CACHE_NAME, cacheKey, accessToken);
            cacheAccessToken = CacheKit.get(CACHE_NAME, cacheKey);
        }
        return cacheAccessToken;
	    
	}
	
	private static final String GET_JSAPI_TICKET_URL="https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=%s&type=jsapi";
	
	public static String getJsapiTicket(){
	    String accessToken = getAccessToken();
        String cacheKey = "jsapiTicket";
        String cacheJsapiTicket = CacheKit.get(CACHE_NAME, cacheKey);
        if (StringUtils.isBlank(cacheJsapiTicket)) {
            //======获取开始
            String url = String.format(GET_JSAPI_TICKET_URL,accessToken);
            System.out.println(url);
            String content= HttpKit.get(url, null);
            JSONObject jsonObject = JSONObject.parseObject(content);
            String jsapiTicket = jsonObject.getString("ticket");
            if(StringUtils.isBlank(jsapiTicket)){
                return null;
            }
            //======获取结束
            CacheKit.put(CACHE_NAME, cacheKey, jsapiTicket);
            cacheJsapiTicket = CacheKit.get(CACHE_NAME, cacheKey);
        }
        return cacheJsapiTicket;
	}
	
	//获取用户信息
	private static final String GET_USER_INFO_URL = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=%s&openid=%s&lang=zh_CN";
	
	//{country=中国, subscribe=1, city=长沙, openid=obknuvpx-L2QwySLwCDBs6VwUuc0, tagid_list=[], sex=1, groupid=0, language=zh_CN, remark=, subscribe_time=1515471683, province=湖南, nickname=樱木, headimgurl=http://wx.qlogo.cn/mmopen/Q3auHgzwzM5N6OYHyYyICmNCHuvT1rBvB81IZ1My8ricRVg3GeHdCCmvXQ1QuGYeXSTEQSjpzXicylicOZibBKIgXw/0}
    public static Map<String,Object> getUserInfo(String openId){
        String accessToken = getAccessToken();
        String content = HttpKit.get(String.format(GET_USER_INFO_URL, accessToken,openId), null);
        System.out.println("GET_USER_INFO_URL=="+content);
        return JSONObject.parseObject(content, Map.class);
	}
	
	//获取用户auth信息
	private static final String GET_AUTHORIZE_URL="https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_userinfo&state=%s#wechat_redirect";
	
	public static String getAuthorizeUrl(String redirectUri,String state){
	    String url = String.format(GET_AUTHORIZE_URL, wechatAppId,redirectUri,state);
	    System.out.println("url=="+url);
	    return url;
	}
	
	private static final String GET_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";
	//access_token   openid
	public static Map<String,Object> getOauth2Token(String code){
	    String content = HttpKit.get(String.format(GET_ACCESS_TOKEN_URL, wechatAppId,wechatSecret,code), null);
	    System.out.println("GET_ACCESS_TOKEN_URL=="+content);
	    return JSONObject.parseObject(content, Map.class);
	}
	
	
	private static final String GET_OAUTH2_USERINFO_URL= "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=zh_CN";
	
	//nickname headimgurl
	public static Map<String,Object> getOauth2Userinfo(String openId){
	    String accessToken = getAccessToken();
	    String content = HttpKit.get(String.format(GET_OAUTH2_USERINFO_URL, accessToken,openId), null);
	    System.out.println("GET_OAUTH2_USERINFO_URL=="+content);
	    return JSONObject.parseObject(content, Map.class);
	}
	
	//小程序获取openid
    //获取session_key
    private static final String GET_JSCODE2SESSION_URL = "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code";
    
    //openid session_key unionid
    public static Map<String,Object> getJscode2session(String code){
        String content = HttpKit.get(String.format(GET_JSCODE2SESSION_URL, xcxAppId,xcxSecret,code), null);
        System.out.println("GET_JSCODE2SESSION_URL=="+content);
        return JSONObject.parseObject(content, Map.class);
    }
	
	//客服消息
	private static final String CUSTOM_SEND_URL="https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=%s";
	public static void customSend(String message){
	    String accessToken = getAccessToken();
	    String content = HttpKit.post(String.format(CUSTOM_SEND_URL, accessToken), message);
	    System.out.println("==CUSTOM_SEND_URL=="+content);
	}
	
    //获取文本消息内容
    public static String getTextMessage(String touser,String content){
        String message="";
        message+="{";
        message+="\"touser\":\""+touser+"\",";   //OPENID
        message+="\"msgtype\":\"text\",";
        message+="\"text\":";
        message+="{";
        message+="\"content\":\""+content+"\"";
        message+="}";
        message+="}";
        return message;
    }
    //获取图片消息内容
    public static String getImageMessage(String touser,String mediaId){
        String message="";
        message+="{";
        message+="\"touser\":\""+touser+"\",";   //OPENID
        message+="\"msgtype\":\"image\",";
        message+="\"image\":";
        message+="{";
        message+="\"media_id\":\""+mediaId+"\"";
        message+="}";
        message+="}";    
        return message;
    }
    //获取语音消息内容
    public static String getVoiceMessage(String touser,String mediaId){
        String message="";
        message+="{";
        message+="\"touser\":\""+touser+"\",";   //OPENID
        message+="\"msgtype\":\"voice\",";
        message+="\"voice\":";
        message+="{";
        message+="\"media_id\":\""+mediaId+"\"";
        message+="}";
        message+="}";   
        return message;
    }
    //获取视频消息内容
    public static String getVideoMessage(String touser,String mediaId,String thumbMediaId,String title,String description){
        String message="";
        message+="{";
        message+="\"touser\":\""+touser+"\",";   //OPENID
        message+="\"msgtype\":\"video\",";
        message+="\"video\":";
        message+="{";
        message+="\"media_id\":\""+mediaId+"\"";
        message+="\"thumb_media_id\":\""+thumbMediaId+"\"";
        message+="\"title\":\""+title+"\"";
        message+="\"description\":\""+description+"\"";
        message+="}";
        message+="}";   
        return message;
    }
    //获取音乐消息内容
    public static String getMusicMessage(String touser,String title,String description,String musicurl,String hqmusicurl,String thumbMediaId){
        String message="";
        message+="{";
        message+="\"touser\":\""+touser+"\",";   //OPENID
        message+="\"msgtype\":\"music\",";
        message+="\"music\":";
        message+="{";
        message+="\"title\":\""+title+"\"";
        message+="\"description\":\""+description+"\"";
        message+="\"musicurl\":\""+musicurl+"\"";
        message+="\"hqmusicurl\":\""+hqmusicurl+"\"";
        message+="\"thumb_media_id\":\""+thumbMediaId+"\"";
        message+="}";
        message+="}";
        return message;
    }
    
    //获取图文消息（点击跳转到外链）图文消息条数限制在8条以内，注意，如果图文数超过8，则将会无响应。
    public static String getNewsMessage(String touser,List<Map<String,String>> data){
        String message="";
        message+="{";
        message+="\"touser\":\""+touser+"\",";
        message+="\"msgtype\":\"news\",";
        message+="\"news\":{";
        message+="\"articles\": [";
        for(int i=0;i<data.size();i++){
            Map<String,String> map = new HashMap<String,String>();
            message+="{";
            message+="\"title\":\""+map.get("map")+"\",";
            message+="\"description\":\""+map.get("description")+"\",";
            message+="\"url\":\""+map.get("url")+"\",";
            message+="\"picurl\":\""+map.get("picurl")+"\"";
            message+="}";
        }    
        message+="]";
        message+="}";
        message+="}";
        return message;
    }
    //获取图文消息（点击跳转到图文消息页面） 图文消息条数限制在8条以内，注意，如果图文数超过8，则将会无响应。
    public static String getMpnewsMessage(String touser,String mediaId){
        String message="";
        message+="{";
        message+="\"touser\":\""+touser+"\",";   //OPENID
        message+="\"msgtype\":\"mpnews\",";
        message+="\"mpnews\":";
        message+="{";
        message+="\"media_id\":\""+mediaId+"\"";
        message+="}";
        message+="}";  
        return message;
    }
	
    /** 临时二维码 url */
    public static final String GET_QRCODE_CREATE_URL="https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=%s";
    /** 临时二维码下载url */
    public static final String GET_SHOWQRCODE_URL="https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=%s";
	
    //ticket
    public static String getQrcodeCreate(String qrcodeContent){
        String accessToken = getAccessToken();
        String content = HttpKit.post(String.format(GET_QRCODE_CREATE_URL, accessToken), qrcodeContent);
        System.out.println("GET_QRCODE_CREATE_URL=="+content);
        JSONObject jsonObject = JSONObject.parseObject(content);
        return jsonObject.getString("ticket");
    }
    
    //url
    public static String getShowqrcodeUrl(String ticket){
        return String.format(GET_SHOWQRCODE_URL, ticket);
    }
    
    //获取Qrcode内容
    public static String getQrcodeContent(Long sceneId){
        return "{\"expire_seconds\": 2592000, \"action_name\": \"QR_SCENE\", \"action_info\": {\"scene\": {\"scene_id\": "+sceneId+"}}}";
    }
    
    //素材
    /** 新增临时素材 url */
    public static final String UPLOAD_URL="https://api.weixin.qq.com/cgi-bin/media/upload?access_token=%s&type=%s";
    
    //type  media_id   created_at
    public static String uploadImage(File file){
        String accessToken = getAccessToken();
        String content = WebUtils.post(String.format(UPLOAD_URL, accessToken,"image"),file);
        JSONObject jsonObject = JSONObject.parseObject(content);
        return jsonObject.getString("media_id");
    }
    
    
    //菜单
    public static final String MENU_CREATE_URL="https://api.weixin.qq.com/cgi-bin/menu/create?access_token=%s";
    
    public static void menuCreate(String data){
        String accessToken = getAccessToken();
        String content = HttpKit.post(String.format(MENU_CREATE_URL, accessToken), data);
        System.out.println(content);
    }
    
    
    //公众号支付
    /**
     * 
     * @param amount    订单金额
     * @param subject   商品信息
     * @param openId    用户openId
     * @param httpRequest
     * @param httpResponse
     * @return
     */
    public static Map<String,String> wechatPay(BigDecimal amount,String subject,String openId,String orderId,HttpServletRequest httpRequest,HttpServletResponse httpResponse){
      Map<String,String> params = wechatUnifiedorder(openId, subject, amount.multiply(new BigDecimal(100)).intValue(), IPUtils.getIpAddress(httpRequest),orderId);
      String prepay_id = params.get("prepay_id");
      Map<String,String> result = new HashMap<String,String>();
      result.put("prepay_id", prepay_id);
      result.put("appId", wechatAppId);
      result.put("timeStamp", getCurrentTimestamp()+"");
      result.put("nonceStr", generateNonceStr());
      result.put("signType", SignType.MD5.toString());
      String sign="";
         try {
             sign = generateSignature(result, wechatAppId,SignType.MD5);
         } catch (Exception e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
         }
         result.put("paySign",sign);
         return result;
         
    }
    
    
    //h5支付
    public static Map<String,String> h5Pay(BigDecimal amount,String subject,String orderId,HttpServletRequest httpRequest,HttpServletResponse httpResponse){
        Map<String,String> params = h5Unifiedorder(subject, amount.multiply(new BigDecimal(100)).intValue(), IPUtils.getIpAddress(httpRequest),orderId);
        String mweb_url = params.get("mweb_url");
        Map<String,String> result = new HashMap<String,String>();
        result.put("mweb_url", mweb_url);
        return result;
    }
   
    
    /**
     * 扫码支付是第一种模式
     * 1.生成二维码  getBizpayurl
     * 2.微信回调数据过来。调用下订单接口，返回prepay_id给微信
     * 
     * 扫码支付是第二种模式
     * 1.下订单接口返回二维码地址
     */
    
    //扫码支付
    public static Map<String,String> nativePay(BigDecimal amount,String subject,String orderId,HttpServletRequest httpRequest,HttpServletResponse httpResponse) {
        Map<String,String> params = nativeUnifiedorder(subject, amount.multiply(new BigDecimal(100)).intValue(), IPUtils.getIpAddress(httpRequest),orderId);
        String prepay_id = params.get("prepay_id");
        String code_url = params.get("code_url");
        Map<String,String> result = new HashMap<String,String>();
        result.put("prepay_id", prepay_id);
        result.put("code_url", code_url);
        return result;
    }
    
    
    //扫码支付二维码地址
    public static String getBizpayurl(String orderId){
        Map<String,String> params = new HashMap<String,String>();
        params.put("appid", wechatAppId);
        params.put("mch_id", mchId);
        params.put("time_stamp", getCurrentTimestamp()+"");
        params.put("nonce_str", generateNonceStr());
        params.put("product_id", orderId);
        try{
            String sign = generateSignature(params, key, SignType.MD5);
            params.put(FIELD_SIGN, sign);
            String bizpayurl = "weixin://wxpay/bizpayurl?sign=#(sign)&appid=#(appid)&mch_id=#(appid)&product_id=#(product_id)&time_stamp=#(time_stamp)&nonce_str=#(nonce_str)";
            return RenderManager.me().getEngine().getTemplateByString(bizpayurl).renderToString(params);
        }catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return null;
        }
    }
    
    //小程序支付
    /**
     * 
     * @param amount    订单金额
     * @param subject   商品信息
     * @param openId    用户openId
     * @param httpRequest
     * @param httpResponse
     * @return
     */
    public static Map<String,String> xcxPay(String orderId,BigDecimal amount,String subject,HttpServletRequest httpRequest,HttpServletResponse httpResponse){
      Map<String,String> params = xcxUnifiedorder(subject, amount.multiply(new BigDecimal(100)).intValue(), IPUtils.getIpAddress(httpRequest),orderId);
      String prepay_id = params.get("prepay_id");
      Map<String,String> result = new HashMap<String,String>();
      result.put("package", "prepay_id="+prepay_id);
      result.put("appId", xcxAppId);
      result.put("timeStamp", getCurrentTimestamp()+"");
      result.put("nonceStr", generateNonceStr());
      result.put("signType", SignType.MD5.toString());
      String sign="";
         try {
             sign = generateSignature(result, key,SignType.MD5);
         } catch (Exception e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
         }
         result.put("paySign",sign);
         return result;
         
    }
    
   //APP支付
    /**
     * 
     * @param amount    订单金额
     * @param subject   商品信息
     * @param openId    用户openId
     * @param httpRequest
     * @param httpResponse
     * @return
     */
    public static Map<String,String> appPay(String orderSn,BigDecimal amount,String subject,HttpServletRequest httpRequest,HttpServletResponse httpResponse){
      Map<String,String> params = appUnifiedorder(subject, amount.multiply(new BigDecimal(100)).intValue(), IPUtils.getIpAddress(httpRequest),orderSn);
      String prepay_id = params.get("prepay_id");
      Map<String,String> result = new HashMap<String,String>();
      result.put("appId", openAppId);//微信开放平台审核通过的应用APPID
      result.put("partnerid", mchId);//微信支付分配的商户号
      result.put("prepayid", prepay_id);//微信返回的支付交易会话ID
      result.put("package", "Sign=WXPay");//暂填写固定值Sign=WXPay
      result.put("nonceStr", generateNonceStr());//随机字符串，不长于32位。
      result.put("timestamp", getCurrentTimestamp()+"");//时间戳
      result.put("signType", SignType.MD5.toString());//时间戳
      String sign="";
         try {
             sign = generateSignature(result, key,SignType.MD5);// key设置路径：微信商户平台(pay.weixin.qq.com)-->账户设置-->API安全-->密钥设置
         } catch (Exception e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
         }
         result.put("paySign",sign);
         return result;
         
    }
    
    //企业付款
    /**
     * 
     * @param mch_appid  申请商户号的appid或商户号绑定的appid
     * @param mchid   微信支付分配的商户号
     * @param openid  商户appid下，某用户的openid 
     * @param amount  企业付款金额，单位为分
     * @param desc    企业付款操作说明信息。必填。
     * @return
     * @throws Exception
     */
    public static Map<String,String> transfers(String openid,int amount,String desc) throws Exception{
        KeyStore keyStore  = KeyStore.getInstance("PKCS12");
        FileInputStream instream = new FileInputStream(new File("D:/10016225.p12"));  //证书
        try {
            keyStore.load(instream, "10016225".toCharArray());  //证书密码
        } finally {
            instream.close();
        }

        // Trust own CA and all self-signed certs
        SSLContext sslcontext = SSLContexts.custom()
                .loadKeyMaterial(keyStore, "10016225".toCharArray()) //证书密码
                .build();
        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext,
                new String[] { "TLSv1" },
                null,
                SSLConnectionSocketFactory.getDefaultHostnameVerifier());
        CloseableHttpClient httpclient = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .build();
        try {
            Map<String,String> params = new HashMap<String,String>();
            params.put("mch_appid", mchAppId);
            params.put("mchid", mchId);
            params.put("nonce_str", generateNonceStr());
            params.put("partner_trade_no", generateUUID());
            params.put("openid", openid);
            params.put("check_name", "NO_CHECK");
            params.put("amount", amount+"");
            params.put("desc", desc);
            params.put("spbill_create_ip", IPUtils.getLocalAddress());
            
            String xml = generateSignedXml(params, key);  //API密钥
            
            HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers");
            StringEntity entity = new StringEntity(xml,"UTF-8");
            httpPost.setEntity(entity);  
            CloseableHttpResponse response = httpclient.execute(httpPost);
            try {
                HttpEntity httpEntity = response.getEntity();
                String result = EntityUtils.toString(httpEntity);
                EntityUtils.consume(httpEntity);
                EntityUtils.consume(entity);
                return xmlToMap(result);
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
    }
    
    //发放普通红包
    /**
     * 
     * @param mch_id   微信支付分配的商户号
     * @param wxappid  微信分配的公众账号ID
     * @param send_name  红包发送者名称
     * @param re_openid  接受红包的用户
     * @param total_amount  付款金额，单位分
     * @param total_num  红包发放总人数
     * @param wishing  红包祝福语
     * @param act_name  活动名称
     * @param remark  备注信息
     * @return
     * @throws Exception
     */
    public static Map<String,String> sendredpack(String send_name,String re_openid,int total_amount,int total_num,String wishing,String act_name,String remark) throws Exception{
        KeyStore keyStore  = KeyStore.getInstance("PKCS12");
        FileInputStream instream = new FileInputStream(new File("D:/10016225.p12"));  //证书
        try {
            keyStore.load(instream, "10016225".toCharArray());  //证书密码
        } finally {
            instream.close();
        }

        // Trust own CA and all self-signed certs
        SSLContext sslcontext = SSLContexts.custom()
                .loadKeyMaterial(keyStore, "10016225".toCharArray()) //证书密码
                .build();
        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext,
                new String[] { "TLSv1" },
                null,
                SSLConnectionSocketFactory.getDefaultHostnameVerifier());
        CloseableHttpClient httpclient = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .build();
        try {
            Map<String,String> params = new HashMap<String,String>();
            params.put("mch_id", mchId);
            params.put("wxappid", wechatAppId);
            params.put("nonce_str", generateNonceStr());
            params.put("mch_billno", generateUUID());
            params.put("send_name", send_name);
            params.put("re_openid", re_openid);
            params.put("total_amount", total_amount+"");
            params.put("total_num", total_num+"");
            params.put("wishing", wishing);
            params.put("client_ip", IPUtils.getLocalAddress());
            params.put("act_name",act_name);
            params.put("remark",remark);
            
            String xml = generateSignedXml(params, key);  //API密钥
            
            HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/mmpaymkttransfers/sendredpack");
            StringEntity entity = new StringEntity(xml,"UTF-8");
            httpPost.setEntity(entity);  
            CloseableHttpResponse response = httpclient.execute(httpPost);
            try {
                HttpEntity httpEntity = response.getEntity();
                String result = EntityUtils.toString(httpEntity);
                EntityUtils.consume(httpEntity);
                EntityUtils.consume(entity);
                return xmlToMap(result);
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
    }
    
    
    //发放裂变红包
    /**
     * 
     * @param mch_id   微信支付分配的商户号
     * @param wxappid   微信分配的公众账号ID
     * @param send_name  红包发送者名称
     * @param re_openid  接收红包的种子用户（首个用户）
     * @param total_amount  红包发放总金额
     * @param total_num  红包发放总人数
     * @param wishing   红包祝福语
     * @param act_name  活动名称
     * @param remark  备注信息
     * @return
     */
    public static Map<String,String> sendgroupredpack(String send_name,String re_openid,int total_amount,int total_num,String wishing,String act_name,String remark) throws Exception{
        KeyStore keyStore  = KeyStore.getInstance("PKCS12");
        FileInputStream instream = new FileInputStream(new File("D:/10016225.p12"));  //证书
        try {
            keyStore.load(instream, "10016225".toCharArray());  //证书密码
        } finally {
            instream.close();
        }

        // Trust own CA and all self-signed certs
        SSLContext sslcontext = SSLContexts.custom()
                .loadKeyMaterial(keyStore, "10016225".toCharArray()) //证书密码
                .build();
        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext,
                new String[] { "TLSv1" },
                null,
                SSLConnectionSocketFactory.getDefaultHostnameVerifier());
        CloseableHttpClient httpclient = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .build();
        try {
            Map<String,String> params = new HashMap<String,String>();
            params.put("mch_id", mchId);
            params.put("wxappid", wechatAppId);
            params.put("nonce_str", generateNonceStr());
            params.put("mch_billno", generateUUID());
            params.put("send_name", send_name);
            params.put("re_openid", re_openid);
            params.put("total_amount", total_amount+"");
            params.put("total_num", total_num+"");
            params.put("amt_type", "ALL_RAND");
            params.put("wishing", wishing);
            params.put("client_ip", IPUtils.getLocalAddress());
            params.put("act_name",act_name);
            params.put("remark",remark);
            
            String xml = generateSignedXml(params, key);  //API密钥
            
            HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/mmpaymkttransfers/sendgroupredpack");
            StringEntity entity = new StringEntity(xml,"UTF-8");
            httpPost.setEntity(entity);  
            CloseableHttpResponse response = httpclient.execute(httpPost);
            try {
                HttpEntity httpEntity = response.getEntity();
                String result = EntityUtils.toString(httpEntity);
                EntityUtils.consume(httpEntity);
                EntityUtils.consume(entity);
                return xmlToMap(result);
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
    }
    
    //公众号支付统一下单
    /**
     * 
     * @param appid 微信支付分配的公众账号ID（企业号corpid即为此appId）
     * @param mch_id  微信支付分配的商户号
     * @param openid  某用户的openid 
     * @param body   商品简单描述
     * @param total_fee  订单总金额，单位为分
     * @param spbill_create_ip APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP。
     * @return
     */
    public static Map<String,String> wechatUnifiedorder(String openid,String body,int total_fee,String spbill_create_ip,String orderId){
        Map<String,String> params = new HashMap<String,String>();
        params.put("appid", wechatAppId);
        params.put("mch_id", mchId);
        params.put("nonce_str", generateNonceStr());
        params.put("sign_type", SignType.MD5.toString());
        params.put("body", body);
        params.put("out_trade_no", generateUUID());
        params.put("total_fee", total_fee+"");
        params.put("spbill_create_ip", spbill_create_ip);
        params.put("notify_url", notifyUrl); //异步回调地址
        params.put("trade_type", "JSAPI");
        params.put("openid", openid);
        params.put("attach", orderId);  //附加参数-订单ID
        
        try{
            String xml = generateSignedXml(params, key);  //API密钥
            
            String content = HttpKit.post("https://api.mch.weixin.qq.com/pay/unifiedorder", xml);
            return xmlToMap(content); 
        }catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return null;
        }
    }
    
    
    //h5支付统一下单
    public static Map<String,String> h5Unifiedorder(String body,int total_fee,String spbill_create_ip,String orderId){
        Map<String,String> params = new HashMap<String,String>();
        params.put("appid", wechatAppId);
        params.put("mch_id", mchId);
        params.put("nonce_str", generateNonceStr());
        params.put("sign_type", SignType.MD5.toString());
        params.put("body", body);
        params.put("out_trade_no", generateUUID());
        params.put("total_fee", total_fee+"");
        params.put("spbill_create_ip", spbill_create_ip);
        params.put("notify_url", notifyUrl);  //异步回调地址
        params.put("trade_type", "MWEB");
        params.put("scene_info", "{\"h5_info\": {\"type\":\"Wap\",\"wap_url\": \"http://www.test.com\",\"wap_name\": \"测试\"}}"); ////场景类型 //WAP网站URL地址 //WAP 网站名
        params.put("attach", orderId);  //附加参数-订单ID
        
        try{
            String xml = generateSignedXml(params, key);  //API密钥
            
            String content = HttpKit.post("https://api.mch.weixin.qq.com/pay/unifiedorder", xml);
            return xmlToMap(content); 
        }catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return null;
        }
    }
    
    //扫码支付支付统一下单
    public static Map<String,String> nativeUnifiedorder(String body,int total_fee,String spbill_create_ip,String orderId){
        Map<String,String> params = new HashMap<String,String>();
        params.put("appid", wechatAppId);
        params.put("mch_id", mchId);
        params.put("nonce_str", generateNonceStr());
        params.put("sign_type", SignType.MD5.toString());
        params.put("body", body);
        params.put("out_trade_no", generateUUID());
        params.put("total_fee", total_fee+"");
        params.put("spbill_create_ip", spbill_create_ip);
        params.put("notify_url", notifyUrl); //异步回调地址
        params.put("trade_type", "NATIVE"); 
        params.put("product_id", orderId);  //商品ID
        params.put("attach", orderId);  //附加参数-订单ID
        
        try{
            String xml = generateSignedXml(params, key);  //API密钥
            
            String content = HttpKit.post("https://api.mch.weixin.qq.com/pay/unifiedorder", xml);
            return xmlToMap(content); 
        }catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return null;
        }
    }
    
    //小程序支付统一下单
    public static Map<String,String> xcxUnifiedorder(String body,int total_fee,String spbill_create_ip,String attach){
        Map<String,String> params = new HashMap<String,String>();
        params.put("appid", xcxAppId);
        params.put("mch_id", mchId);
        params.put("nonce_str", generateNonceStr());
        params.put("sign_type", SignType.MD5.toString());
        params.put("body", body);
        params.put("attach", attach);
        params.put("out_trade_no", generateUUID());
        params.put("total_fee", total_fee+"");
        params.put("spbill_create_ip", spbill_create_ip);
        params.put("notify_url", notifyUrl);
        params.put("trade_type", "JSAPI");
        
        try{
            String xml = generateSignedXml(params, key);  //API密钥
            
            String content = HttpKit.post("https://api.mch.weixin.qq.com/pay/unifiedorder", xml);
            return xmlToMap(content); 
        }catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return null;
        }
    }
    
    //APP支付统一下单
    public static Map<String,String> appUnifiedorder(String body,int total_fee,String spbill_create_ip,String attach){
        Map<String,String> params = new HashMap<String,String>();
        params.put("appid", openAppId); //微信开放平台审核通过的应用APPID（请登录open.weixin.qq.com查看，注意与公众号的APPID不同）
        params.put("mch_id", mchId);//微信支付分配的商户号
        params.put("nonce_str", generateNonceStr());//随机字符串，不长于32位。
        params.put("sign_type", SignType.MD5.toString());//签名类型，目前支持HMAC-SHA256和MD5，默认为MD5
        params.put("body", body);//商品描述交易字段格式根据不同的应用场景按照以下格式：APP——需传入应用市场上的APP名字-实际商品名称，天天爱消除-游戏充值。
        params.put("attach", attach);//附加数据，在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据
        params.put("out_trade_no", generateUUID());//商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*且在同一个商户号下唯一
        params.put("total_fee", total_fee+"");//订单总金额，单位为分，
        params.put("spbill_create_ip", spbill_create_ip);//支持IPV4和IPV6两种格式的IP地址。调用微信支付API的机器IP
        params.put("notify_url", notifyUrl);//接收微信支付异步通知回调地址，通知url必须为直接可访问的url，不能携带参数。
        params.put("trade_type", "APP");//支付类型
        
        try{
            String xml = generateSignedXml(params, key);  //API密钥
            
            String content = HttpKit.post("https://api.mch.weixin.qq.com/pay/unifiedorder", xml);
            return xmlToMap(content); 
        }catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return null;
        }
    }
    
    public static final String FIELD_SIGN = "sign";
    
    public enum SignType {
        MD5, HMACSHA256
    }
    
    /**
     * XML格式字符串转换为Map
     *
     * @param strXML XML字符串
     * @return XML数据转换后的Map
     * @throws Exception
     */
    public static Map<String, String> xmlToMap(String strXML) throws Exception {
        try {
            Map<String, String> data = new HashMap<String, String>();
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            InputStream stream = new ByteArrayInputStream(strXML.getBytes("UTF-8"));
            org.w3c.dom.Document doc = documentBuilder.parse(stream);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getDocumentElement().getChildNodes();
            for (int idx = 0; idx < nodeList.getLength(); ++idx) {
                Node node = nodeList.item(idx);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    org.w3c.dom.Element element = (org.w3c.dom.Element) node;
                    data.put(element.getNodeName(), element.getTextContent());
                }
            }
            try {
                stream.close();
            } catch (Exception ex) {
                // do nothing
            }
            return data;
        } catch (Exception ex) {
            throw ex;
        }

    }
    
    /**
     * 将Map转换为XML格式的字符串
     *
     * @param data Map类型数据
     * @return XML格式的字符串
     * @throws Exception
     */
    public static String mapToXml(Map<String, String> data) throws Exception {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder= documentBuilderFactory.newDocumentBuilder();
        org.w3c.dom.Document document = documentBuilder.newDocument();
        org.w3c.dom.Element root = document.createElement("xml");
        document.appendChild(root);
        for (String key: data.keySet()) {
            String value = data.get(key);
            if (value == null) {
                value = "";
            }
            value = value.trim();
            org.w3c.dom.Element filed = document.createElement(key);
            filed.appendChild(document.createTextNode(value));
            root.appendChild(filed);
        }
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        DOMSource source = new DOMSource(document);
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        transformer.transform(source, result);
        String output = writer.getBuffer().toString(); //.replaceAll("\n|\r", "");
        try {
            writer.close();
        }
        catch (Exception ex) {
        }
        return output;
    }
    
    /**
     * 生成带有 sign 的 XML 格式字符串
     *
     * @param data Map类型数据
     * @param key API密钥
     * @return 含有sign字段的XML
     */
    public static String generateSignedXml(final Map<String, String> data, String key) throws Exception {
        return generateSignedXml(data, key, SignType.MD5);
    }

    /**
     * 生成带有 sign 的 XML 格式字符串
     *
     * @param data Map类型数据
     * @param key API密钥
     * @param signType 签名类型
     * @return 含有sign字段的XML
     */
    public static String generateSignedXml(final Map<String, String> data, String key, SignType signType) throws Exception {
        String sign = generateSignature(data, key, signType);
        data.put(FIELD_SIGN, sign);
        return mapToXml(data);
    }


    /**
     * 判断签名是否正确
     *
     * @param xmlStr XML格式数据
     * @param key API密钥
     * @return 签名是否正确
     * @throws Exception
     */
    public static boolean isSignatureValid(String xmlStr, String key) throws Exception {
        Map<String, String> data = xmlToMap(xmlStr);
        if (!data.containsKey(FIELD_SIGN) ) {
            return false;
        }
        String sign = data.get(FIELD_SIGN);
        return generateSignature(data, key).equals(sign);
    }

    /**
     * 判断签名是否正确，必须包含sign字段，否则返回false。使用MD5签名。
     *
     * @param data Map类型数据
     * @param key API密钥
     * @return 签名是否正确
     * @throws Exception
     */
    public static boolean isSignatureValid(Map<String, String> data, String key) throws Exception {
        return isSignatureValid(data, key, SignType.MD5);
    }

    /**
     * 判断签名是否正确，必须包含sign字段，否则返回false。
     *
     * @param data Map类型数据
     * @param key API密钥
     * @param signType 签名方式
     * @return 签名是否正确
     * @throws Exception
     */
    public static boolean isSignatureValid(Map<String, String> data, String key, SignType signType) throws Exception {
        if (!data.containsKey(FIELD_SIGN) ) {
            return false;
        }
        String sign = data.get(FIELD_SIGN);
        return generateSignature(data, key, signType).equals(sign);
    }
    
    /**
     * 生成签名
     *
     * @param data 待签名数据
     * @param key API密钥
     * @return 签名
     */
    public static String generateSignature(final Map<String, String> data, String key) throws Exception {
        return generateSignature(data, key, SignType.MD5);
    }

    /**
     * 生成签名. 注意，若含有sign_type字段，必须和signType参数保持一致。
     *
     * @param data 待签名数据
     * @param key API密钥
     * @param signType 签名方式
     * @return 签名
     */
    public static String generateSignature(final Map<String, String> data, String key, SignType signType) throws Exception {
        Set<String> keySet = data.keySet();
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        Arrays.sort(keyArray);
        StringBuilder sb = new StringBuilder();
        for (String k : keyArray) {
            if (k.equals(FIELD_SIGN)) {
                continue;
            }
            if (data.get(k).trim().length() > 0) // 参数值为空，则不参与签名
                sb.append(k).append("=").append(data.get(k).trim()).append("&");
        }
        sb.append("key=").append(key);
        if (SignType.MD5.equals(signType)) {
            return MD5(sb.toString()).toUpperCase();
        }
        else if (SignType.HMACSHA256.equals(signType)) {
            return HMACSHA256(sb.toString(), key);
        }
        else {
            throw new Exception(String.format("Invalid sign_type: %s", signType));
        }
    }
    
    
    /**
     * 分享
     * @param httpRequest
     */
    public static Map<String,String> share(HttpServletRequest httpRequest){
        /** share */
        String queryString = httpRequest.getQueryString();
        System.out.println("queryString:"+queryString);
        String rquestUrl = httpRequest.getRequestURL().toString();
        System.out.println("rquestUrl"+rquestUrl);
        // 注意 URL 一定要动态获取，不能 hardcode
        String url = rquestUrl;
        if(StringUtils.isNotBlank(queryString)){
            url = url + "?" + queryString;
        }
        return sign(url);
    }
    
    /**
     * js sdk签名,微信分享签名
     * @param jsapi_ticket
     * @param url
     * @return
     */
    public static Map<String,String> sign(String url) {
        String nonce_str = generateNonceStr();
        String timestamp = String.valueOf(getCurrentTimestamp());
        String string1;
        String signature = "";
        String jsapi_ticket = getJsapiTicket();

        //注意这里参数名必须全部小写
        string1 = "jsapi_ticket=" + jsapi_ticket +
                  "&noncestr=" + nonce_str +
                  "&timestamp=" + timestamp +
                  "&url=" + url;
        System.out.println(string1);

        try
        {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(string1.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        Map<String,String> map = new HashMap<>();
        map.put("url", url);
        map.put("nonceStr", nonce_str);
        map.put("timestamp", timestamp);
        map.put("signature", signature);
        map.put("appId", wechatAppId);
        return map;
    }
    
    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }
    
    /**
     * 获取随机字符串 Nonce Str
     *
     * @return String 随机字符串
     */
    public static String generateNonceStr() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32);
    }
    
    /**
     * 生成 MD5
     *
     * @param data 待处理数据
     * @return MD5结果
     */
    public static String MD5(String data) throws Exception {
        java.security.MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] array = md.digest(data.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte item : array) {
            sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString().toUpperCase();
    }

    /**
     * 生成 HMACSHA256
     * @param data 待处理数据
     * @param key 密钥
     * @return 加密结果
     * @throws Exception
     */
    public static String HMACSHA256(String data, String key) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        byte[] array = sha256_HMAC.doFinal(data.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte item : array) {
            sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString().toUpperCase();
    }
    
    /**
     * 获取当前时间戳，单位秒
     * @return
     */
    public static long getCurrentTimestamp() {
        return System.currentTimeMillis()/1000;
    }

    /**
     * 获取当前时间戳，单位毫秒
     * @return
     */
    public static long getCurrentTimestampMs() {
        return System.currentTimeMillis();
    }

    /**
     * 生成 uuid， 即用来标识一笔单，也用做 nonce_str
     * @return
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32);
    }
 
}
