package pgdp;

import java.io.*;
import java.net.*;

public class PinguinTest {
    // TODO

    public static void main(String[] args) throws IOException {
        ServerSocket server  = new ServerSocket(25565);
        Socket client;
        String clientGelen;
        client = server.accept();
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        while((clientGelen = in.readLine()) != null) {
            System.out.println("Client = " + clientGelen);
            out.println("this is response of " + clientGelen);
        }
        out.close();
        in.close();
        client.close();
        server.close();
    }
}

class Penguin {
    public static void main(String[] args) throws IOException {
        Socket socket = null;
        PrintWriter out = null;
        BufferedReader in = null;
        String deger;
        try {
            socket = new Socket("127.0.0.1", 25565);
        } catch (Exception e) {
            System.out.println("Port error!");
        }
        out = new PrintWriter(socket.getOutputStream(), true);

        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out.println("ask me\ntest-test\nbu bir test");
        System.out.println("Server response = " + in.readLine());

        out.close();
        in.close();
        socket.close();
    }
}
