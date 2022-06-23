package nio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;

public class NIOServer {
    static final Logger log = LoggerFactory.getLogger(NIOServer.class);

    public static void main(String[] args) {
        LinkedList<SocketChannel> channels = new LinkedList<SocketChannel>();
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();) {
            serverSocketChannel.bind(new InetSocketAddress(9090));
            serverSocketChannel.configureBlocking(false);
            while (true) {
                SocketChannel socketChannel = serverSocketChannel.accept();
                if(socketChannel == null) {
                    Thread.sleep(2000);
                } else {
                    socketChannel.configureBlocking(false); //重点  socket（服务端的listen socket<连接请求三次握手后，往我这里扔，我去通过accept 得到  连接的socket>，连接socket<连接后的数据读写使用的> ）
                    log.info("客户端连接，端口号：{}", socketChannel.socket().getPort());
                    channels.add(socketChannel);
                }

                ByteBuffer readBuffer = ByteBuffer.allocateDirect(10);

                for (SocketChannel c : channels) {
                    int num = c.read(readBuffer);  // >0  -1  0   //不会阻塞
                    if (num > 0) {
                        readBuffer.flip();
                        byte[] aaa = new byte[readBuffer.limit()];
                        readBuffer.get(aaa);
                        String msg = new String(aaa);
                        log.info("端口号：{}，消息：{}", c.socket().getPort(), msg);

                        readBuffer.clear();
                    }


                }

            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
