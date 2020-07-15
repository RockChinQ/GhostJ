package com.ghostj.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Test {
    public static void main(String[] args) throws IOException, InterruptedException {
        Socket socket=new Socket("39.100.5.139",1033);
        BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
        bufferedReader.readLine();
        new Thread().sleep(30000);
    }
}
