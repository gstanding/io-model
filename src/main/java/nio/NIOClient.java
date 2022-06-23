package nio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NIOClient {
    static final Logger log = LoggerFactory.getLogger(NIOClient.class);

    public static void main(String[] args) {
        InetSocketAddress serverAddr = new InetSocketAddress("127.0.0.1", 9090);
        try(SocketChannel socketChannel = SocketChannel.open();) {
            socketChannel.bind(new InetSocketAddress(45513));
            socketChannel.connect(serverAddr);
            ByteBuffer buffer = ByteBuffer.allocateDirect(1);
            buffer.put(new byte[]{1});
            socketChannel.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
