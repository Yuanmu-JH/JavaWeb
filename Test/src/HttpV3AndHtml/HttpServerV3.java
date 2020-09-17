package HttpV3AndHtml;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
    V3版本：支持返回一个完整的静态html文件
            解析处理cookie为键值对
            解析处理body为键值对
            实现一个完整的登录功能（session简单实现）
 */
public class HttpServerV3 {
    private ServerSocket serverSocket = null;

    static class User{
        //用一个专门的类来保存用户的相关信息
        public String userName;
        public int age;
        public String school;
    }

    //session会话：指的是同一个用户访问服务器的操作，归类到一起就是一个会话;也就是把请求和响应全部归在一起
    private Map<String,User> sessions = new HashMap<>();

    public HttpServerV3(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    public void start() throws IOException {
        System.out.println("服务器启动");
        //考虑并发操作，使用线程池
        ExecutorService executorService = Executors.newCachedThreadPool();
        while (true) {
            Socket clientSocket = serverSocket.accept();
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    process(clientSocket);
                }
            });
        }
    }

    private void process(Socket clientSocket) {
        //处理核心逻辑
        try {
            //1.获取请求并解析
            HttpRequest request = HttpRequest.build(clientSocket.getInputStream());
            HttpResponse response = HttpResponse.build(clientSocket.getOutputStream());
            //2. 根据请求计算相应
            //按照不同的HTTP方法拆成不同的逻辑
            if ("GET".equalsIgnoreCase(request.getMethod())) {
                doGet(request, response);
            } else if ("POST".equalsIgnoreCase(request.getMethod())) {
                doPost(request, response);
            } else {
                //其他方法返回405这个状态码
                response.setStatus(405);
                response.setMessage("Method Not Allowed");
            }
            //3. 把响应写回客户端
            response.flush();
        } catch (IOException | NullPointerException  e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void doGet(HttpRequest request, HttpResponse response) throws IOException {
        //1.能够支持返回一个html文件
        if (request.getUrl().startsWith("/index.html")) {
            String sessionId = request.getCookie("sessionId");
            User user = sessions.get(sessionId);
            //看cookie中是否包含username
            if (sessionId == null||user == null) {
                //用户尚未登陆，则返回一个登陆界面
                //这种情况下，就让代码读取一个index.html这样的文件，把文件内容写入到响应的body中
                //读文件要先知道文件路径，现在只知道文件名，而index.html的文件路径可以自己来约定（e:/....）专门放html
                //打开文件
                response.setStatus(200);
                response.setMessage("OK");
                response.setHeaders("Content-Type", "text/html; charset=utf-8");
                InputStream inputStream = HttpServerV3.class.getClassLoader().getResourceAsStream("index.html");
                //html为文本文件， 用字符流处理
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                //按行读取内容，把数据写入response中
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    response.writeBody(line + "\n");
                }
                bufferedReader.close();
            }else {
                //用户已经登录则无需在登陆
                response.setStatus(200);
                response.setMessage("OK");
                response.setHeaders("Content-Type", "text/html; charset=utf-8");
                response.writeBody("<html>");
                response.writeBody("<div>"+user.userName+" 您已经登陆了，无需再次登录。"+"</div>");
                response.writeBody(+user.age+"</div>");
                response.writeBody("<div>"+user.school+"</div>");
                response.writeBody("</html>");

            }
        }
    }
    private void doPost(HttpRequest request, HttpResponse response) {
        //2. 实现/login的处理
        if (request.getUrl().startsWith("/login")) {
            //读取用户提交的用户名和密码
            String userName = request.getParameter("username");
            String password = request.getParameter("password");
            //此处用户名密码写死
            if ("ym".equals(userName) && "123".equals(password)) {
                //登录成功
                response.setStatus(200);
                response.setMessage("OK");
                response.setHeaders("Content-Type", "text/html;charset=utf-8");
                //此时在HTTP的响应中加上了Set-Cookie的字段，浏览器就会自动存储下刚才这个Cookie数据
                //实现了一般登录后刷新页面依旧为登陆状态
                //response.setHeaders("Set-Cookie","username="+userName);   //会使隐私泄露

                //对于登陆成功的处理，就是给登录的用户分配了一个session，在hash中新增了一个键值对，
                //key是随机生成的，value就是用户的身份信息，身份信息保存在服务器中也就不再有泄露问题了，给浏览器返回的cookie
                // 中只包含sessionID
                String sessionId = UUID.randomUUID().toString();
                User user = new User();
                user.userName="ym";
                user.age=18;
                user.school = "科技大学";
                //将键值对加入session中
                sessions.put(sessionId,user);
                response.setHeaders("Set-Cookie", "sessionId=" + sessionId);

                //response.setHeaders("Set-Cookie","username="+userName);
                response.writeBody("<html>");
                response.writeBody("<div>欢迎您" + userName + "</div>");
                response.writeBody("</html>");
            } else {
                //登陆失败
                response.setStatus(403);
                response.setMessage("Forbidden");
                response.setHeaders("Content-Type", "text/html;charset=utf-8");
                response.writeBody("<html>");
                response.writeBody("<div>登陆失败</div>");
                response.writeBody("</html>");
            }
        }
    }
    public static void main (String[]args) throws IOException {
        HttpServerV3 serverV3 = new HttpServerV3(9090);
        serverV3.start();
    }
}


