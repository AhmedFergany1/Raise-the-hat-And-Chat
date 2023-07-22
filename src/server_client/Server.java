package server_client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable {

    public static Dictionary<String,ArrayList<String>> usersRequests= new Hashtable<>();
    private ServerSocket server;
    private static ArrayList<ConnectionHandler> connections;
    private boolean done;
    private ExecutorService pool;


    public Server() {
        connections = new ArrayList<>();
        done = false;

    }

    @Override
    public void run() {

        try {
            server = new ServerSocket(2023);
            pool = Executors.newCachedThreadPool();
            while (!done) {
                Socket client = server.accept();
                ConnectionHandler handler = new ConnectionHandler(client);
                connections.add(handler);
                pool.execute(handler);
            }
        } catch (IOException e) {
            shutdown();
        }

    }

    public static void broadCast(String req) throws IOException {
        for (ConnectionHandler ch : connections) {
            if (ch != null) {
                ch.sendMessage(req,ch.client);
            }
        }
    }

    public void shutdown() {
        try {
            done = true;
            pool.shutdown();
            if (!server.isClosed()) {
                server.close();
            }
            for (ConnectionHandler ch : connections) {
                ch.shutdown();

            }
        } catch (IOException e) {
            //ignore
        }
    }

    static class ConnectionHandler implements Runnable {

        public Socket client;
        private BufferedReader in;
        private PrintWriter out;

        private ArrayList<String> userContactList;
        private ArrayList<String> userRequestsList;

        public ConnectionHandler(Socket client) {
            this.client = client;
        }

        @Override
        public void run() {
            try {
                out = new PrintWriter(client.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(client.getInputStream()));

                //out.println("Enter a nickname");  // out for each client

                       // put the logged username here

//                System.out.println(nickname + "Connected!");
                //broadCast(nickname + " joined the chat!");
                String message ;
                while ((message = in.readLine()) != null ) {

                    String[] splitMessage = message.split("#");
                    String username = splitMessage[0];

                    String receiverUsername = splitMessage[2];

                    usersRequests.put(receiverUsername, new ArrayList<>());

                    if (splitMessage[1].equalsIgnoreCase("Add")) {
                        //out.println(line);
                        //System.out.println("ADDED");

                        ArrayList<String> eachUserRequests;
                        eachUserRequests = usersRequests.get(receiverUsername);
                        if (!eachUserRequests.contains(username)) {
                            eachUserRequests.add(username);
                        }
                        usersRequests.put(receiverUsername, eachUserRequests);

                        if (usersRequests != null) System.out.println(usersRequests);
                    }

                    Server.broadCast(message);


                }
            } catch (IOException e) {
                shutdown();
            }
        }

        public void sendMessage(String message , Socket c) throws IOException {
            out = new PrintWriter(c.getOutputStream());
            out.print(message);
        }

        public void shutdown() {
            try {
                in.close();
                out.close();
                if (!client.isClosed()) {
                    client.close();
                }
            } catch (IOException e) {
                //ignore
            }
        }
    }
    public static void main(String[] args) {
        Server server = new Server();
        server.run();
    }
}














//
//import java.io.*;
//import java.net.*;
//import java.util.*;
//
//// Server class
//public class Server {
//
//    public static Dictionary<String, ArrayList<String>> usersRequests = new Hashtable<>();
//     ArrayList<ClientHandler> connections = new ArrayList<>();
//
//
//    public void runServer() {
//        System.out.println("Start server");
//
//        ArrayList<String> names = new ArrayList<>();
//        names.add("7egz");
//
//        usersRequests.put("ferr",names);
//
//
//        ServerSocket server = null;
//
//        try {
//            // server is listening on port 2023
//            server = new ServerSocket(2023);
//            server.setReuseAddress(true);
//
//            // running infinite loop for getting
//            // client request
//
//            System.out.println("Before while ");
//            while (true) {
//
//                // socket object to receive incoming client
//                // requests
//                Socket client = server.accept();
//
//                // Displaying that new client is connected
//                // to server
//                System.out.println("New client connected"
//                        + client.getInetAddress()
//                        .getHostAddress());
//
//                // create a new thread object
//                ClientHandler clientSock
//                        = new ClientHandler(client);
//
//                connections.add(clientSock);
//
//
//                // This thread will handle the client
//                // separately
//                new Thread(clientSock).start();
//                System.out.println(usersRequests);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (server != null) {
//                try {
//                    server.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//    // ClientHandler class
//    private static class ClientHandler implements Runnable {
//        private final Socket clientSocket;
//
//
//        // Constructor
//        public ClientHandler(Socket socket) {
//            this.clientSocket = socket;
//        }
//
//        public void run() {
//            PrintWriter out = null;
//            BufferedReader in = null;
//            try {
//                // get the outputstream of client
//                out = new PrintWriter(
//                        clientSocket.getOutputStream(), true);
//
//                // get the inputstream of client
//                in = new BufferedReader(
//                        new InputStreamReader(
//                                clientSocket.getInputStream()));
//
//                // instantiate userRequests
//
////                String line = in.readLine();
////                while ((line) != null) {
////                    // writing the received message from
////                    // client
//////                    System.out.printf(
//////                            " Sent from the client: %s\n",
//////                            line);
////
//////
//////                    String[] splitMessage = line.split(",");
//////                    String username = splitMessage[0];
//////
//////                    String receiverUsername = splitMessage[2];
//////
//////                    usersRequests.put(receiverUsername, new ArrayList<>());
//////
//////                    if (splitMessage[1].equalsIgnoreCase("Add")) {
//////                        //out.println(line);
//////                        System.out.println("ADDED");
//////
//////                        ArrayList<String> eachUserRequests;
//////                        eachUserRequests = (ArrayList<String>) usersRequests.get(receiverUsername);
//////                        if (!eachUserRequests.contains(username)) {
//////                            eachUserRequests.add(username);
//////                        }
//////                        usersRequests.put(receiverUsername, eachUserRequests);
//////
//////                        if (usersRequests != null) System.out.println(usersRequests);
//////                    }
////
////                    line = null;
////
////                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//                try {
//                    if (out != null) {
//                        out.close();
//                    }
//                    if (in != null) {
//                        in.close();
//                        clientSocket.close();
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//    public static void main(String[] args) {
//        Server server = new Server();
//        server.runServer();
//        System.out.println("usersRequests");
//    }
//}