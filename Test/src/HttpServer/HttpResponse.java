package HttpServer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

//表示一个  HTTP响应负责构造:序列化操作
public class HttpResponse {
    //首行内容
    private String version = "HTTP/1.1";    //版本号，这里就直接固定给
    private int status; //状态码
    private String message; //状态码解析
    private Map<String,String> headers = new HashMap<>();
    private StringBuilder body = new StringBuilder();   //方便构造一个易于拼接的字符串\
    //当代码需要把响应写回客户端时，就写入outputStream
    private OutputStream outputStream = null;

    //工厂方法
    public static  HttpResponse build(OutputStream outputStream){
        HttpResponse response = new HttpResponse();
        response.outputStream = outputStream;
        //除了outputStream外，其他属性的内容暂时都无法确定，要根据代码的具体业务逻辑来确定
        //是服务器“根据请求并计算相应”阶段来进行设置的
        return response;
    }
    //给类提供一些setter方法

    public void setVersion(String version) {
        this.version = version;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setHeader(String key,String value){
        headers.put(key,value);
    }

    public void writeBody(String content) {
        body.append(content);
    }
    //以上的属性设置都是在内存中，所以还需要方法把这些属性按照Http协议写到socket中
    public void flush() throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
        //构造首行操作
        bufferedWriter.write(version+" "+status+" "+message+"\n");
        //在header中加入一个特定键值对
        headers.put("Content-Length",body.toString().getBytes().length+" ");
        //循环写入每个headers
        for(Map.Entry<String,String> entry : headers.entrySet()){
            bufferedWriter.write(entry.getKey()+": "+entry.getValue()+"\n");
        }
        bufferedWriter.write("\n"); //空行
        bufferedWriter.write(body.toString());
        bufferedWriter.flush();
    }

}
