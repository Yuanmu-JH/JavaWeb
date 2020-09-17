package HttpV3AndHtml;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

//解析HTTP请求
public class HttpRequest {

    private String method;
    private String url;
    private String version;

    private Map<String,String> headers = new HashMap<>();
    //url中的参数和body中的参数都放在parameter这个哈希表中
    private Map<String,String> parameters = new HashMap<>();    //参数列表
    private Map<String,String> cookies = new HashMap<>();   //cookies的解析结果
    private String body;

    public static HttpRequest build(InputStream inputStream) throws IOException {
        //先创建一个request对象
        HttpRequest request = new HttpRequest();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        //1. 处理首行
        String firstLine = bufferedReader.readLine();
        String[] firstLineStokens = firstLine.split(" ");
        request.method = firstLineStokens[0];
        request.url = firstLineStokens[1];
        request.version = firstLineStokens[2];
        //2. 解析url
        int pos = request.url.indexOf("?");
        if(pos != -1){
            String queryString = request.url.substring(pos+1);
            parseKV(queryString,request.parameters);

        }
        //3.循环处理header部分
        String line = "";
        while ((line = bufferedReader.readLine())!= null && line.length() != 0){
            String[] headerTokens = line.split(": ");
            request.headers.put(headerTokens[0],headerTokens[1]);
        }
        //4.解析cookie
        String cookie = request.headers.get("Cookie");
        if (cookie != null){
            //把cookie进行解析
            parseCookie(cookie,request.cookies);
        }
        //5. 解析body（get方法不需要解析）
        if("POST".equalsIgnoreCase(request.method) || "PUT".equalsIgnoreCase(request.method)) {
            //这两个方法需要处理body 其他方法暂时不考虑
            //先将body读取出来
            //需要先知道body的长度，从headers读出content-Length并转换为整数，长度单位为字节
            int contentLength = Integer.parseInt(request.headers.get("Content-Length"));
            //contentLength为100则body中就有100个字节，而缓冲区的长度是100个char即200个字节，这样创建的缓冲区
            // 才能保证长度足够进行操作
            char[] buffer = new char[contentLength];
            int length = bufferedReader.read(buffer);  //因为bufferedReader读的是字符流，所以要放在char的数组中
            request.body = new String(buffer,0,length); //将缓冲区的内容构造成了字符串
            //将body的解析结果放在parameters中；body形如：a=2&b=3
            parseKV(request.body,request.parameters);
        }
        return request;
    }

    private static void parseCookie(String cookie, Map<String, String> cookies) {
        //1. 按照; 拆分成多个键值对
        String[] kvTokens = cookie.split("; ");
        //按照=拆分成每个键和值
        for (String kv : kvTokens) {
            String[] result = kv. split("=");
            cookies.put(result[0],result[1]);
        }
    }

    private static void parseKV(String queryString, Map<String, String> parameters) {
        //1. 按照&拆分成多个键值对
        String[] kvTokens = queryString.split("&");
        //按照=拆分成每个键和值
        for (String kv : kvTokens) {
            String[] result = kv. split("=");
            parameters.put(result[0],result[1]);
        }
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public String getVersion() {
        return version;
    }

    public String getBody() {
        return body;
    }

    public String getParameter(String key){
        return parameters.get(key);
    }

    public String getHeader(String key) {
        return headers.get(key);
    }

    public String getCookie(String key){
        return cookies.get(key);
    }
}
