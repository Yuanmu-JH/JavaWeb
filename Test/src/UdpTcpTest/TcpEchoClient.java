package UdpTcpTest;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class TcpEchoClient {
    private Socket socket = null;

    public TcpEchoClient(String serverIp,int serverPort) throws IOException {
        //此处实例化Socket的过程就是在建立TCP连接
        socket = new Socket(serverIp,serverPort);
    }

    public void start(){
        System.out.println("客户端启动");
        Scanner scanner = new Scanner(System.in);
        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {
            while(true){
                //读取用户输入
                System.out.print("->");
                String request = scanner.nextLine();
                if("exit".equals(request)){
                    break;
                }
                //构造请求并发送,此处的\n是为了和服务器中的readLine相对应
                bufferedWriter.write(request+"\n");
                bufferedWriter.flush();
                //客户端读取响应数据
                String response = bufferedReader.readLine();
                //把响应数据显示到界面上
                System.out.println(response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        TcpEchoClient client = new TcpEchoClient("127.0.0.1",9090);
        client.start();

    }
}
