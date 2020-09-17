package HttpV3AndHtml;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    private String version = "HTTP/1.1";
    private int status;
    private String message;
    private Map<String,String> headers = new HashMap<>();
    private StringBuilder body = new StringBuilder();
    private OutputStream outputStream = null;   //输出流，随时往文件中写数据

    public static HttpResponse build(OutputStream outputStream){
        HttpResponse response = new HttpResponse();
        response.outputStream = outputStream;
        return response;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setHeaders(String key,String value) {
        headers.put(key, value);
    }

    public void writeBody(String content) {
        body.append(content);
    }


    public void flush() throws IOException {
        //手动刷新，将内容写到outputstream中
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
        bufferedWriter.write(version+" "+status+" "+message+"\n");
        headers.put("Content-Length",body.toString().getBytes().length+""); //后面加字符串自动转为字符串
        for(Map.Entry<String,String> entry : headers.entrySet()) {
            bufferedWriter.write(entry.getKey() + ": " + entry.getValue() + "\n");
        }
            bufferedWriter.write("\n"); //空行
            bufferedWriter.write(body.toString());
            bufferedWriter.flush();
    }
}
