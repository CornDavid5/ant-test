package com.mycompany.app;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class App {

    public static void main(String[] args) {
        try {
            String command = "ant -buildfile ./cassandra/build.xml testsome -Dtest.name=org.apache.cassandra.hints.HintsCatalogTest -Dtest.methods=deleteHintsTest -Duse.jdk11=true";
            String line = null;
            Process pro = Runtime.getRuntime().exec(command);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(pro.getInputStream()));
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            pro.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
