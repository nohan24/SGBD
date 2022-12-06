package file;
import java.io.*;

public class Fichier {
    public static void ecrire(String file) throws IOException {
        BufferedWriter fis= new BufferedWriter(new FileWriter(file,true));
        fis.write("lolo");
        fis.close();
        System.out.println("vitaa");
    }
    public static String lire(String file) throws IOException {
        BufferedReader fis= new BufferedReader(new FileReader(file));
        String resp = "";
        String huhu = fis.readLine();
        while(huhu != null){
            resp = resp + huhu;
            huhu = fis.readLine();   
        }
        return resp;
    }
}