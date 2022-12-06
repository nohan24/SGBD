package serveur;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import base.Requete;
/*
 * www.codeurjava.com
 */
public class Serveur {
 
    public static void main(String[] test){ 
        ServerSocket serveurSocket ;
        Socket clientSocket ;
        BufferedReader in;
        PrintWriter out;
        Scanner sc=new Scanner(System.in);
        try {
            serveurSocket = new ServerSocket(5000);
            clientSocket = serveurSocket.accept();
            out = new PrintWriter(clientSocket.getOutputStream());
            in = new BufferedReader (new InputStreamReader (clientSocket.getInputStream()));
            int a = 0;

            String line = in.readLine();
            while(line != null){
                System.out.println("Req: "+line);
                line = in.readLine();
                Requete.requete(line,out);
                out.flush();
            }
            
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
