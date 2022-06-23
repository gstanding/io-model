package bio;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class BIOServer {
    static final Logger log = LoggerFactory.getLogger(BIOServer.class);
    public static void main(String[] args) {
        // backlog连接队列的长度 比如tomcat的accpet队列
        // backlog参数就是tomcat中的accept参数，默认值100 net.core.somaxconn默认值128 高并发情况下，
        // 如果tomcat来不及处理新的连接，连接会堆积到accept队列，超出长度时，内核会向客户端发送RST
        // BIO默认是50
        try(ServerSocket serverSocket = new ServerSocket(9090, 20);) {
            Socket socket;
            while((socket = serverSocket.accept()) != null) {
                log.info("客户端连接成功，信息：{}:{}",
                        socket.getInetAddress().getHostAddress(),
                        socket.getPort()
                        );
                ExecutorService executorService = Executors.newFixedThreadPool(5);
                final Socket finalSocket = socket;
                Socket finalSocket1 = socket;
                executorService.execute(()-> {
                    // 获取socket的输入流和输出流
                    try(ObjectInputStream ois = new ObjectInputStream(finalSocket.getInputStream());
                        ObjectOutputStream oos = new ObjectOutputStream(finalSocket.getOutputStream())
                    ) {
                        // 根据定义的传输格式序列化协议得到对象并解析
                        log.info("阻塞在这里");
                        byte b = ois.readByte();
                        log.info("读到数据，结束阻塞");
                        log.info("收到客户端发来的：{}", b);
                        oos.writeByte(1);
                        oos.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
