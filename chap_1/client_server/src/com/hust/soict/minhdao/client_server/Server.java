package com.hust.soict.minhdao.client_server;

import com.hust.soict.minhdao.helper.*;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {
    public static void main(String[] args) {
        System.out.println("The Sorter is running!");
        int clientNumber = 0;
        NumberSorter selectionSorter = new SelectionSort(),
                bubbleSorter = new BubbleSort(),
                insertionSorter = new InsertionSort(),
                shellSorter = new ShellSort();

        try {
            ServerSocket listener = new ServerSocket(8080);
            while (true) {
                switch (clientNumber % 4) {
                    case 1: {
                        new Sorter(listener.accept(), clientNumber++, bubbleSorter).start();
                        break;
                    }
                    case 2: {
                        new Sorter(listener.accept(), clientNumber++, insertionSorter).start();
                        break;
                    }
                    case 3: {
                        new Sorter(listener.accept(), clientNumber++, shellSorter).start();
                        break;
                    }
                    default: {
                        new Sorter(listener.accept(), clientNumber++, selectionSorter).start();
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
