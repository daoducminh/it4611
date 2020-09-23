package com.hust.soict.minhdao.client_server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = null;
        Scanner scanner = null;
        String inputString;
        try {
            socket = new Socket("127.0.0.1", 8080);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            System.out.println(in.readLine());
            scanner = new Scanner(System.in);

            StringBuilder builder = new StringBuilder();
            int number;

            while (true) {
                inputString = scanner.nextLine().trim();
                if (inputString.isEmpty()) {
                    out.println(builder.toString().trim());
                    break;
                } else {
                    try {
                        number = Integer.parseInt(inputString);
                        System.out.println("Received number: " + number);
                        builder.append(number);
                        builder.append(" ");
                    } catch (NumberFormatException e) {
                        System.out.println("Not a number. Please input again!");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socket.close();
            scanner.close();
        }
    }
}
