package lesson_1;

import java.util.Scanner;

/**
 * лучше сокет отправлять в отдельный тредпул, не будет блокировки
 */

public class Main {

    public static void main(String[] args) {
        Server server = new Server(9090);
        new Thread(server).start();


        Scanner scanner = new Scanner(System.in);
        while (scanner.nextLine().equals("stop")){
            server.stop();
        };
    }
}
