package client;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
/*
 * www.codeurjava.com
 */
public class Client {

    public static void main(String[] args) {
    
        Socket clientSocket;
        BufferedReader in;
        PrintWriter out;
        Scanner sc = new Scanner(System.in);
        
        try {
            clientSocket = new Socket("127.0.0.1",5000);
            
            out = new PrintWriter(clientSocket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            System.out.println("Client connecte");
            Thread envoyer = new Thread(new Runnable() {
                String msg;
                @Override
                public void run() {
                    while(true){
                        msg = sc.nextLine();
                        out.println(msg);
                        out.flush();
                    }
                }
            });
            envoyer.start();
            
            Thread recevoir = new Thread(new Runnable() {
                String msg;
                @Override
                public void run() {
                    try {
                        msg = in.readLine();
                        while(msg!=null){
                            System.out.println(msg);
                            msg = in.readLine();
                        }
                        System.out.println("Serveur déconecté");
                        out.close();
                        clientSocket.close();
                    } catch (Exception e) {
                        // e.printStackTrace();
                        System.out.println(e.getMessage());
                    }
                }
            });
            recevoir.start();
        } catch (Exception e) {
            // e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}
