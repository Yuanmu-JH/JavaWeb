package UdpTcpTest;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class UdpEchoClient {
    /*
    客户端的主要流程有四步：
    1. 从用户这里读取数据
    2.构造请求发送给服务器
    3.从服务器读取响应
    4.把响应写回客户端
     */
    private DatagramSocket socket = null;
    private  String serverIp;
    private int serverPort;

    //需要在启动客户端时指定需要连接哪个服务器
    public UdpEchoClient(String serverIp, int serverPort) throws SocketException {
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        //构造客户端socket时不需要绑定端口号，是由操作系统自动分配一个空闲端口
        //一个端口通常情况下只能被一个进程绑定，服务器绑定了端口后，客户端才能访问。
        //若客户端绑定的话，一个主机只能启动一个客户端
        socket = new DatagramSocket();
    }

    public void start() throws IOException {
        Scanner scanner = new Scanner(System.in);
        while(true){
            //1. 从用户这里读取数据

            System.out.println(">");
            String request = scanner.nextLine();
            if(request.equals("exit")){
                break;
            }
            //    2.构造请求发送给服务器
            DatagramPacket requestPacket = new DatagramPacket(request.getBytes(),request.getBytes().length,
                    InetAddress.getByName(serverIp),serverPort);
            socket.send(requestPacket);
            //    3.从服务器读取响应
            DatagramPacket responsePacket = new DatagramPacket(new byte[4096],4096);
            socket.receive(responsePacket);
            String response = new String(responsePacket.getData(),0,responsePacket.getLength()).trim();
            //    4.把响应写回客户端
            System.out.println(response);
        }
    }

    public static void main(String[] args) throws IOException {
        //127.0.0.1特殊IP；环回ip，自己访问自己。若客户端与服务器在一台主机上则为环回ip，若不在则要写成服务器的ip
        //端口要和服务器绑定的端口相匹配
        UdpEchoClient client = new UdpEchoClient("127.0.0.1",9090);
        client.start();
    }
}
