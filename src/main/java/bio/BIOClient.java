package bio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class BIOClient {
    static final Logger log = LoggerFactory.getLogger(BIOClient.class);

    public static void main(String[] args) {
        try (Socket socket = new Socket("127.0.0.1", 9090)) {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeByte(1);
            oos.flush();
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            byte b = ois.readByte();
            log.info("从服务器收到：{}", b);

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
