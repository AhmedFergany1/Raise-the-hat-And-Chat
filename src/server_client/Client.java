package server_client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.ArrayList;

public class Client implements Runnable {

    public String message = "h#h#h";

    public String innerMessage = "";

    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private boolean done;

    private ArrayList<String> usersRequests = new ArrayList<>();

    @Override
    public void run() {
        try {
            client = new Socket("127.0.0.1",2023);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(client.getOutputStream(),true);

            innerMessage = in.readLine();

            InputHandler inHandler = new InputHandler();
            Thread task = new Thread(inHandler);
            task.start();

//            String inMessage;
//            while ((inMessage = in.readLine()) !=null){
//                System.out.println(inMessage);
//            }
        }catch (IOException e){
            System.out.println("Going to shut");
            shutdown();
        }

    }

    public void shutdown(){
        done = true;
        try {
            //in.close();
            out.close();
            if (!client.isClosed()){
                client.close();
            }
        }catch (IOException e){
            //ignore
        }
    }

    class InputHandler implements Runnable{

        @Override
        public void run() {
            try {
                BufferedReader inReader = new BufferedReader(new InputStreamReader(System.in));
                while (!done){
                    //String message = inReader.readLine();
                    if (message.equalsIgnoreCase("/quit")){
                        out.println(message);
                        inReader.close();
                        shutdown();
                    }else {
                        out.println(message);
                    }
                }

            }catch (IOException e){
                shutdown();
            }
        }
    }
}


