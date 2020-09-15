package UdpTcpTest;

import java.io.IOException;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

public class UDPDictServer extends UdpEchoServer {

    private Map<String,String> dict = new HashMap<>();
    public UDPDictServer(int port) throws SocketException {
        super(port);

        dict.put("cat","猫");
        dict.put("dog","狗");
    }

    @Override
    public String process(String request) {
        return dict.getOrDefault(request,"这不是我会的");

    }

    public static void main(String[] args) throws IOException {
        UDPDictServer server = new UDPDictServer(9090);
        server.start();
    }
}
