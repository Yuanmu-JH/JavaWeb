package HttpServer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServerV1 {
    private ServerSocket serverSocket = null;

    public HttpServerV1(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }
     public void start() throws IOException {
         System.out.println("服务器启动");
         //为了完成并发操作引入线程池
         ExecutorService executorService = Executors.newCachedThreadPool();
         while (true){
             //获取连接
             Socket clientSocket = serverSocket.accept();
             //处理连接,使用短链接的方式
             executorService.execute(new Runnable() {
                 @Override
                 public void run() {
                     process(clientSocket);
                 }
             });
         }
     }

    private void process(Socket clientSocket) {
        //获取流对象
        //由于http协议是文本协议 依旧用字符流来处理
        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))) {
            //下面操作都按照http协议进行操作
            //1.读取请求并解析
            //a.解析首行:三个部分由空格切分
            String firstLine = bufferedReader.readLine();
            String[] firstLineTikens = firstLine.split(" ");    //以空格切分
            String method = firstLineTikens[0];     //方法
            String url = firstLineTikens[1];        //url
            String version = firstLineTikens[2];    //版本号
            //b.解析header:按行读取，用冒号空格来分割键值对（键值对用HashMap）
            Map<String,String> headers = new HashMap<>();
            String line = "";
            //readLine读取一行的内容，会自动去掉换行符，对于空行而言去掉换行符就是空字符串，所以可以用line.equals("")
            //判断，也可以用 line.length()！=0;不为null则说明没有数据可读了
            while((line = bufferedReader.readLine()) != null && line.length() != 0){
                String[] headerTokens = line.split(": ");   //用冒号空格来区分
                headers.put(headerTokens[0],headerTokens[1]);
            }
            //请求解析完毕，添加一个日志，观察请求内容是否正确
            System.out.printf("%s %s %s\n",method,url,version);
            for(Map.Entry<String,String> entry : headers.entrySet()){
                System.out.println(entry.getKey()+": "+entry.getValue()+"\n");
            }
            System.out.println();   //打印空行
            //2.根据请求计算响应
            // 不管是何种请求都返回一个hello这样的html
           String resp = "";
           if(url.equals("/ok")){
               //首行
               bufferedWriter.write(version+" "+"200 OK\n");
               resp = "<h1>hello</h1>";
           }else if(url.equals("/notfound")){
               bufferedWriter.write(version+" "+"404 Not Found\n");
               resp = "<h1>not found</h1>";
           }else if(url.equals("/seeother")){
               //重定向
               bufferedWriter.write(version+" "+"303 See Other\n");
               bufferedWriter.write("Location:http://www.baidu.com\n");
               resp = "";

            }else {
               bufferedWriter.write(version+" "+"200 OK\n");
               resp = "<h1>default</h1>";

           }
            //3.把响应写回客户端
            bufferedWriter.write("Content-Type : text/html\n");
            bufferedWriter.write("Content-Length:"+resp.getBytes().length+"\n");//长度单位为字节
            bufferedWriter.write("\n"); //空行
            bufferedWriter.write(resp);
            bufferedWriter.flush();//此处可以没有，因为bufferedWriter在try中，执行完自动关闭的同时也会触发刷新操作


        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                clientSocket.close();//短连接，只进行一次交互，交互完毕关闭它
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        HttpServerV1 serverV1 = new HttpServerV1(9090);
        serverV1.start();
        //打开浏览器 输入 http://127.0.0.1:9090/ab
    }
}
