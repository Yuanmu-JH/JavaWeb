package UdpTcpTest;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/*
    回显服务器：客户端给服务器发送一个字符串，服务器原封不动地返回该字符串
 */
public class UdpEchoServer {
    /*
    对于一个服务器程序而言，核心流程：
    ①初始化操作（实例化Socket对象）
    ②进入主循环，接收并处理请求（主循环即死循环）
       a. 读取数据并解析
       b. 根据请求计算响应
       c. 把响应接过写回客户端
     */
    private DatagramSocket socket = null;

    public UdpEchoServer(int port) throws SocketException {
        //new这个socket对象时，就会让当前的socket对象和一个ip地址以及一个端口号关联起来（绑定端口）
        //未来客户端就按照这个ip+端口来访问服务器，如果构造socket时没写ip，则默认为0.0.0.0（特殊ip，
        // 表示会关联到这个主机所有网卡的ip）
        socket = new DatagramSocket(port);
    }
    public void start() throws IOException {
        System.out.println("服务器启动");
        //进入主循环
        while(true){
            //DatagramPacket是UDPSocket发送接收数据的基本单位
            //创建packet对象的同时也要给他关联一个缓冲区，并设置缓冲区长度
            DatagramPacket requestPacket = new DatagramPacket(new byte[4096],4096);
            /*
            读取从socket下读取的一切请求，程序启动后，马上就能执行到receive
            若客户端一直没有发送请求，则receive会一直阻塞，直至有数据过来为止，阻塞时间不可预计
            当客户端真的有数据传送过来之后，此时receive就会把收到的数据放到DatagramPacket对象的缓冲区中
             */
            socket.receive(requestPacket);
            /*
            将请求转为String类型（原本是byte[]）
            用户实际发送的数据可能远远小于4096，而getlength得到的长度就是4096，通过trim可以去掉不必要的空白，也可不用
             */
            String request = new String(requestPacket.getData(),0,requestPacket.getLength()).trim();
            //根据请求计算响应
            String response = process(request);
            //把响应写回给客户端,响应数据就是response，需要包装成一个packet对象,并指定该包最后发给谁，此处的地址就是
            //客户端的地址和端口，这两个信息就包含在requestPacket内部了；缓冲区的长度不能用response.length，这个获得
            // 的是字符数量，而我们要得到字节数。getBytes()是返回一个字节数组，String类中的方法
            DatagramPacket responsPacket = new DatagramPacket(response.getBytes(),response.getBytes().length,
                    requestPacket.getSocketAddress());
            socket.send(responsPacket);
            //打印请求日志（锦上添花）
            System.out.printf("[%s:%d] req:%s;resp:%s\n",requestPacket.getAddress().toString(),responsPacket
                    .getPort(),request,response);
        }
    }

    public String process(String request){
        /*
        由于是echo server,则内容的是啥响应就是啥
         */
        return  request;
    }

    public static void main(String[] args) throws IOException {
        UdpEchoServer server = new UdpEchoServer(9090);
        server.start();
    }

}
