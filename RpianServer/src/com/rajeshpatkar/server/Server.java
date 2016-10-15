package com.rajeshpatkar.server;

import javax.swing.*;
import java.awt.*;
import java.io.PrintWriter;
import java.net.*;
import java.util.ArrayList;

public class Server {
    public static MessageQueue<String> q = new MessageQueue<>();
    public static ArrayList<PrintWriter> noslist = new ArrayList<>();
    public static JTextArea serverText;
    public static void main(String[] args) throws Exception {
        JFrame serverFrame = new JFrame("server");
        serverText = new JTextArea(20,20);
        serverFrame.add(BorderLayout.CENTER,serverText);
        serverFrame.setSize(400,400);
        serverFrame.setVisible(true);
        serverFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        serverText.append("Server Signing ON\n");
        System.out.println("Server Signing ON");
        ServerSocket ss = new ServerSocket(8096);
        MessageDispatcher md = new MessageDispatcher();
        md.setDaemon(true);
        md.start();
        for( int i = 0 ; i < 10 ; i++)
        {
            Socket soc = ss.accept();
            System.out.println("Connection established");
            Conversation c = new Conversation( soc );
            c.start();
        }
        System.out.println("Server Signing OFF");
    }
    
}

 class MessageQueue<T> {

    ArrayList<T> al = new ArrayList<>();

    synchronized public void enqueue(T i) {
        al.add(i);
        notify();
    }

   synchronized public T dequeue() {
        if ( al.isEmpty()) {
            try {
                wait();
            } catch (Exception ex) {}
        }
        return al.remove(0);
      }
   
    synchronized public void print() {
        for (T i : al) {
            System.out.println("-->" + i);
        }

    }

    @Override
    synchronized public String toString() {
        String str = null;
        for (T s : al) {
            str += "::" + s;
        }
        return str;
    }
}