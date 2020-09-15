package UdpTcpTest;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpEchoServer {
    private ServerSocket serverSocket = null;
    
    public TcpEchoServer(int port) throws IOException {
        //绑定端口号
        serverSocket = new ServerSocket(port);
    }
    
    public void start() throws IOException {
        System.out.println("服务器启动");
        while (true){
            //先从内核获取TCP连接
           Socket clientsocket = serverSocket.accept();
           //处理这个连接
            processConnection(clientsocket);
        }
    }

    private void processConnection(Socket clientsocket) {
        System.out.printf("[%s:%d] 客户端上线\n", clientsocket.getInetAddress().toString(),
                clientsocket.getPort());
        //通过clientSocket来和客户端进行交互，先做好准备工作，获取到 clientSocket中的流对象
        /*
        getInputStream() 读取字节流
        InputStreamReader把字节流转换为字符流
        BufferedReader 在字符流的基础上套上缓冲区
         */
        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(clientsocket.getInputStream()));
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(clientsocket.getOutputStream()))) {
            //此处先实现一个“长连接”版本的服务器，一次连接的处理过程中需要处理多个请求和响应
            //while循环去掉就是短链接
            while (true){
                //读取请求并解析
                //此处readLine规定客户端发的数据必须是一个按行发送的数据（每条占一行）
                String request =bufferedReader.readLine();
                //根据请求计算响应
                String response = process(request);
                //把响应写回客户端（按行读）
                bufferedWriter.write(response+"\n");
                bufferedWriter.flush();
                //打印请求日志
                System.out.printf("[%s:%d] req:%s;resp: %s \n",clientsocket.getInetAddress().toString(),
                        clientsocket.getPort(),request,response);
            }
        } catch (IOException e) {
            System.out.printf("[%s:%d] 客户端下线\n", clientsocket.getInetAddress().toString(),
                    clientsocket.getPort());
        }
    }

    private String process(String request) {
        return request;
    }

    public static void main(String[] args) throws IOException {
        TcpEchoServer server = new TcpEchoServer(9090);
        server.start();
    }
}
