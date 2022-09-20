package lesson_1;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

public class Client {

    public static void main(String[] args) {
        Client client = new Client("localhost", 9090);
        String responce = client.send("Hello server!");
        System.out.println("responce = " + responce);

    }

    private final String host;
    private final int port;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String send(String msg){
        try(Socket socket = new Socket(host, port)){
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();

            byte[] data = msg.getBytes(StandardCharsets.UTF_8);
            DataOutput dataOutput = new DataOutputStream(outputStream);
            dataOutput.writeInt(data.length);

            outputStream.write(data);
            System.out.println("Message send !");

            DataInput dataInput = new DataInputStream(inputStream);
            int size = dataInput.readInt();
            byte[] bytes = new byte[size];
            dataInput.readFully(bytes);

            return new String(bytes);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
