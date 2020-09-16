package HttpServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

//表示一个Http请求并进行解析
public class HttpRequest {
    private String method;
    private String url;
    private String version;
    private Map<String,String> headers = new HashMap<>();
    //解析Url中参数 ？a=10&b=20
    private Map<String,String> parameters = new HashMap<>();

    //请求的构造模式，使用工厂模式
    //此处的参数就是从Socket中获取到的 InputStream对象
    //build过程就是解析请求的过程,此过程就是反序列化，将比特流转换为一个结构化数据
    public static HttpRequest build(InputStream inputStream) throws IOException {
        HttpRequest request = new HttpRequest();
        //此处不能把BufferedReader写入try，写进去就意味着bufferedReader被关闭，会影响到clientSocket的状态
        //所以等待整个请求处理完毕再关闭
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        //1.解析首行
        String firstLine = bufferedReader.readLine();

        String[] firstLineTokens = firstLine.split(" ");
        request.method = firstLineTokens[0];
        request.url = firstLineTokens[1];
        request.version = firstLineTokens[2];
        //2.解析url
        //看Url中是否有？，没有则说明不带参数，就不需要解析了
        int pos = request.url.indexOf("?"); //indexOf:存在则返回下标，不存在返回-1
        //❓存在
        if(pos != -1){
            // /index.html?a=10&b=20
            //parameters是截取了url从？开始的部分
            String parameters = request.url.substring(pos+1);
            //进一步解析 parameters，切分的结果： key a,value 10;key b,value 20;
            parseKV(parameters,request.parameters);
        }
        //3. 解析header
        String line = "";
        while((line = bufferedReader.readLine())!= null && line.length() != 0){
            String[] headerTokens = line.split(": ");
            request.headers.put(headerTokens[0],headerTokens[1]);
        }
        //4.解析body(先不考虑)
        return request;
    }

    private static void parseKV(String input, Map<String, String> output) {
        //1.先按照&切分成若干组键值对
        String[] kvTokens = input.split("&");
        //2. 针对切分结果再按照=切分，得到键和值
        for (String kv:kvTokens) {
            String[] result = kv.split("=");
            output.put(result[0],result[1]);    //得到key和value
        }
    }
    //构造一些getter方法，因为请求对象的内容是从网络解析的所以用户不应该修改
    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public String getVersion() {
        return version;
    }

    public String getHeader(String key){
        return headers.get(key);
    }

    public String getParameters(String key){
        return headers.get(key);
    }

    @Override
    public String toString() {
        return "HttpRequest{" +
                "method='" + method + '\'' +
                ", url='" + url + '\'' +
                ", version='" + version + '\'' +
                ", headers=" + headers +
                ", parameters=" + parameters +
                '}';
    }
}
