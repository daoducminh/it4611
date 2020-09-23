package com.hust.soict.minhdao.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;

public class Sorter extends Thread {
    private Socket socket;
    private int clientNumber;
    private NumberSorter sorter;

    public Sorter(Socket socket, int clientNumber, NumberSorter sorter) {
        this.socket = socket;
        this.clientNumber = clientNumber;
        this.sorter = sorter;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println("Hello, you are client #" + clientNumber);
            while (true) {
                String input = in.readLine();
                if (input == null || input.isEmpty()) {
                    break;
                }
                String[] nums = input.split(" ");
                int[] inputArray = new int[nums.length];
                int i = 0;
                for (String textValue : nums) {
                    try {
                        inputArray[i] = Integer.parseInt(textValue);
                        i++;
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }

                this.sorter.sort(inputArray);

                System.out.println("input: " + Arrays.toString(inputArray));

                String[] strArray = Arrays.stream(inputArray)
                        .mapToObj(String::valueOf)
                        .toArray(String[]::new);
                out.println(Arrays.toString(strArray));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Connection with client # " + clientNumber + " closed");
        }
    }
}
