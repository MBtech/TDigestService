package com.tdigestserver;

import com.tdunning.math.stats.TDigest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class TDigestServer {

    private static TDigest t = TDigest.createDigest(100);
    ServerSocket myServerSocket;
    boolean ServerOn = true;
    int port = 11111;

    public TDigestServer(int port){
        this.port = port;
        try {
            myServerSocket = new ServerSocket(port);
        } catch (IOException ioe) {
            System.out.println("Could not create server socket on port "+port+". Quitting.");
            System.exit(-1);
        }
        // Successfully created Server Socket. Now wait for connections. 
        while (ServerOn) {
            try {
                // Accept incoming connections. 
                Socket clientSocket = myServerSocket.accept();
                // Start a Service thread 
                ClientServiceThread cliThread = new ClientServiceThread(clientSocket);
                cliThread.start();
            } catch (IOException ioe) {
                System.out.println("Exception encountered on accept. Ignoring. Stack Trace :");
                ioe.printStackTrace();
            }
        }

        try {
            myServerSocket.close();
            System.out.println("Server Stopped");
        } catch (Exception ioe) {
            System.out.println("Problem stopping server socket");
            System.exit(-1);
        }
    }
    
    public TDigestServer() {
        this(11111);
    }

//    public static void main(String[] args) {
//        new TDigestServer();
//    }

    class ClientServiceThread extends Thread {

        Socket myClientSocket;
        boolean m_bRunThread = true;

        public ClientServiceThread() {
            super();
        }

        ClientServiceThread(Socket s) {
            myClientSocket = s;

        }

        public void run() {
            // Obtain the input stream and the output stream for the socket 
            // A good practice is to encapsulate them with a BufferedReader 
            // and a PrintWriter as shown below. 
            BufferedReader in = null;
            PrintWriter out = null;

            ObjectInputStream inStream = null;
            ObjectOutputStream outStream = null;

            // Print out details of this connection 
            System.out.println("Accepted Client Address - " + myClientSocket.getInetAddress().getHostName());
            try {
                //in = new BufferedReader(new InputStreamReader(myClientSocket.getInputStream()));
                //out = new PrintWriter(new OutputStreamWriter(myClientSocket.getOutputStream()));

                inStream = new ObjectInputStream(myClientSocket.getInputStream());
                outStream = new ObjectOutputStream(myClientSocket.getOutputStream());
                // At this point, we can read for input and reply with appropriate output. 
                // Run in a loop until m_bRunThread is set to false 
                System.out.println("Starting the server thread");
                while (m_bRunThread) {
                    // read incoming stream 
                    String clientCommand = (String) inStream.readObject();
                    //String clientCommand = in.readLine();
                    System.out.println("Client Says :" + clientCommand);

                    if (!ServerOn) {
                        // Special command. Quit this thread 
                        System.out.print("Server has already stopped");
                        out.println("Server has already stopped");
                        out.flush();
                        m_bRunThread = false;
                    }

                    if (clientCommand.equalsIgnoreCase("quit")) {
                        // Special command. Quit this thread 
                        m_bRunThread = false;
                        System.out.print("Stopping client thread for client : ");
                    } else if (clientCommand.equalsIgnoreCase("end")) {
                        // Special command. Quit this thread and Stop the Server
                        m_bRunThread = false;
                        System.out.print("Stopping client thread for client ");
                        ServerOn = false;
                    } else if (clientCommand.equalsIgnoreCase("add")) {
                        Double element = (Double) inStream.readObject();
                        synchronized (this) {
                            t.add(element);
                        }
                        System.out.println("Element read is: " + element);
                    } else if (clientCommand.equalsIgnoreCase("addArray")) {
                        ArrayList<Double> elements = (ArrayList<Double>) inStream.readObject();
                        synchronized (this) {
                            for (Double e : elements) {
                                t.add(e);
                            }
                        }
                        System.out.println("Element read is: " + elements);
                    } else if (clientCommand.equalsIgnoreCase("get")) {
                        Double percentile = (Double) inStream.readObject();
                        Double result = t.quantile(percentile);
                        //System.out.println("Element read is: " + result);
                        outStream.writeObject(result);
                        outStream.flush();
                    } else {
                        // Process it 
                        out.println("Server Says : " + clientCommand);
                        out.flush();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // Clean up 
                try {
                    in.close();
                    out.close();
                    myClientSocket.close();
                    System.out.println("...Stopped");
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                } catch (NullPointerException nex){
                    
                }
            }
        }

    }
}
