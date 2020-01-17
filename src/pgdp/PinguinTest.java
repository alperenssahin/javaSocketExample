package pgdp;

import java.io.*;
import java.net.*;
import java.sql.Time;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PinguinTest {
    // TODO
    public static boolean test(long expected ,String actual){
        return expected == Long.parseLong(actual);
    }

    public static boolean test(int expected ,String actual){
        return expected == Integer.parseInt(actual);
    }
    public static void main(String[] args) throws IOException {
        LinkedList<Question> questions = new LinkedList<>();
        questions.add(new Question(0, "Was ergibt die folgende Summe?", "2 + 4 + 6 + 4 + 6 + 4 + 6 + 4 + 6 + 4 + 6 + 4 + 6 + 4 + 6 + 4 + 6 + 4 + 6 + 4 + 6 + 4 + 6"));
        questions.add(new Question(1, "Zähle die Fische in", "afisch bFisch cf dfisch"));
        int selectedQuestion = 1;
        try {
            ServerSocket server = new ServerSocket(25565);
            System.out.println("-----Server Started-----");
            Socket client;
            String clientResponse;
            client = server.accept();
            System.out.println("-----Client Accepted-----");
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);
            out.println(questions.get(selectedQuestion).getQuestion());
            long time = new Date().getTime();
            out.println( questions.get(selectedQuestion).getContent());
            System.out.println("-----Question Sent-----");
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            while ((clientResponse = in.readLine()) != null) {
                if(clientResponse.contains("GoodBye")){
                    break;
                }
                if(selectedQuestion == 0){
                    System.out.println(test(112,clientResponse));
                }
                if(selectedQuestion == 1){
                    System.out.println(test(3,clientResponse));
                }
                System.out.println("request-response time: " + (new Date().getTime() - time) + "ms");
                System.out.println("-----Test Closing, say good bye-----");
                out.println("GoodBye");
            }
            out.close();
            in.close();
            client.close();
            server.close();

        } catch (Exception ignored) {
            System.out.println("Error");
        }
    }
}

class Penguin {
    public static void main(String[] args) throws IOException {
        Socket socket = null;
        PrintWriter out = null;
        BufferedReader in = null;
        String question;
        int qState = 0;
        try {
            socket = new Socket("127.0.0.1", 25565);
            System.out.println("-----Client Started-----");
        } catch (Exception e) {
            System.out.println("Port error!");
        }
        out = new PrintWriter(socket.getOutputStream(), true);

        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        while ((question = in.readLine()) != null) {
            if (qState == 0) {
//                System.out.println(question);
                System.out.println("-----Request Interpreter-----");

                if (question.contains("Was ergibt die folgende Summe?")) {
                    qState = 1;
                }
                if (question.contains("Zähle die Fische in")) {
                    qState = 2;
                }
                if(question.contains("GoodBye")){
                    System.out.println("-----Good Bye------");

                    out.println("GoodBye");
                    break;
                }
            } else {
                System.out.println("-----Question Solver-----");
                if (qState == 1) {
                    String[] arr = question.split(" \\+ ");
                    long sum = 0;
                    for (int i = 0; i < arr.length; i++) {
                        sum += Integer.parseInt(arr[i]);
                    }
                    System.out.println(sum);
                    out.println(sum);
                    qState = 0;
                }
                if (qState == 2) {
                    if(question.equals("")){
                        out.println(0);
                        qState = 0;
                    }else{
                        String[] arr = question.split(" ");
                        int count = 0;
                        for (int i = 0; i < arr.length; i++) {
                            if(arr[i].contains("fisch") || arr[i].contains("Fisch") ){
                                count++;
                            }
                        }
                        out.println(count);
                        qState = 0;
                    }
                }
            }
        }
        out.close();
        in.close();
        socket.close();
    }
}

class Question {
    private final int number;
    private final String question;
    private final String content;

    Question(int number, String question, String content) {
        this.number = number;
        this.question = question;
        this.content = content;
    }

    public int getNumber() {
        return number;
    }

    public String getQuestion() {
        return question;
    }

    public String getContent() {
        return content;
    }
}
