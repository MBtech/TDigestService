/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tdigestclient;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import static java.lang.Math.random;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mb
 */
public class TDigestClient {

    Socket MyClient;
    ObjectInputStream inStream = null;
    ObjectOutputStream outStream = null;

    public TDigestClient() {
       this("127.0.0.1",11113);
    }
    
    public TDigestClient(String ip, int port){
         try {
            MyClient = new Socket(ip, port);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void connection() {
        System.out.println("Entering the connection block");
        try {
            //
            outStream = new ObjectOutputStream(MyClient.getOutputStream());
            inStream = new ObjectInputStream(MyClient.getInputStream());
//            ArrayList <Double> array = new ArrayList<>();
//            System.out.println("Creating the array");
//            for (int i = 0; i < 10000; i++) {
//                array.add(random());
//                //outStream.writeObject("add");
//                System.out.println(i);
//                //outStream.writeObject(random());
//            }
//            System.out.println("Sending the array");
//            outStream.writeObject("addArray");
//            outStream.writeObject(array);
//            outStream.writeObject("get");
//            outStream.writeObject(0.90);
//            Double percentile = (Double)inStream.readObject();
//            System.out.println(percentile);
//            outStream.writeObject("quit");
        } catch (IOException e) {
            System.out.println(e);
        } 
    }

    public void addElement(Double element){
        try {
            outStream.writeObject("add");
            outStream.writeObject(element);
        } catch (IOException ex) {
            Logger.getLogger(TDigestClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void addElements(ArrayList<Double> array){
        try {
            outStream.writeObject("addArray");
            outStream.writeObject(array);
        } catch (IOException ex) {
            Logger.getLogger(TDigestClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void getPercentile(Double percentile){
        try {
            outStream.writeObject("get");
            outStream.writeObject(0.90);
            System.out.println(inStream.readObject());
        } catch (IOException ex) {
            Logger.getLogger(TDigestClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TDigestClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void quitConnection(){
        try {
            outStream.writeObject("quit");
        } catch (IOException ex) {
            Logger.getLogger(TDigestClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
//    public static void main(String[] args) {
//        TDigestClient client = new TDigestClient();
//        client.connection();
//        
//
//    }
}
