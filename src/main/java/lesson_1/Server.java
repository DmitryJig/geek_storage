package lesson_1;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Server implements Runnable {

    private final int port;
    private ServerSocket serverSocket;
    private boolean isStopped;

    public Server(int port) {
        this.port = port;
    }

    public void stop(){
        isStopped = true;
        try {
            serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        Socket socket;

        openServerSocket();
        System.out.println("Server started");

        while (!isStopped){
            try{
                socket = serverSocket.accept();
            } catch (IOException e) {
                if (isStopped){
                    System.out.println("Server stopped");
                    return;
                }
                throw new RuntimeException(e);
            }

            try {
                process(socket);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void openServerSocket(){
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException("Error open server socket! ", e);
        }
    }

    private void process(Socket socket) throws IOException {
        DataInput inputStream = new DataInputStream(socket.getInputStream());
        DataOutput outputStream = new DataOutputStream(socket.getOutputStream());

        int size = inputStream.readInt();
        byte[] data = new byte[size];
        inputStream.readFully(data); // cool method for full read stream

        System.out.println("Recived: " + new String(data));

        String responce = "Hello client !!!";
        byte[] output = responce.getBytes(StandardCharsets.UTF_8);
        outputStream.writeInt(output.length);
        outputStream.write(output);

        System.out.println("Responsing done");
    }
}
