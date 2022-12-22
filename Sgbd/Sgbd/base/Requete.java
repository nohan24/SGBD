package base;

import java.io.PrintWriter;

import file.Fichier;
import relation.Relation;

public class Requete{
    static String[] syntax = {"select","*","from"};

///fonction
    public static boolean testSyntax(String req){
        boolean resp = true;
        String[] tabReq = req.split(" ");
        if(tabReq.length == getSyntax().length + 1){
            for(int i=0;i<getSyntax().length;i++){
                if(i == 1)continue;
                if(!tabReq[i].equals(getSyntax()[i]))resp = false;
            }
        } else resp = false;
        return resp;
    }

    public static void requete(String req) throws Exception{
        String[] tabReq = req.split(" ");
        if(testSyntax(req)){
            try{
                String result = Fichier.lire("data/" + tabReq[3]);
                String[] col = getCol(result);
                String[][] element = getElmt(result); 
                Relation relation = new Relation(Relation.toVec(col), Relation.toVecVec(element));
                if(tabReq[1].contains(",")) relation.projection(tabReq[1]).showAll();
                else if(tabReq[1].equals("*")) relation.showAll();
                else if(tabReq[1].contains(":")){
                   String[] getSecondT = tabReq[1].split(":");
                   String result1 = Fichier.lire("data/" + getSecondT[1]);
                   Relation relation1 = new Relation(Relation.toVec(getCol(result1)), Relation.toVecVec(getElmt(result1)));                 
                   if(tabReq[1].contains("union")) relation.union(relation1).showAll();
                   else if(tabReq[1].contains("intersection")) relation.intersection(relation1).showAll();
                   else if(tabReq[1].contains("difference")) relation.difference(relation1).showAll();
                   else if(tabReq[1].contains("produit")) relation.produit(relation1).showAll();
                   else if(tabReq[1].contains("division")) relation.division(relation1).showAll();
                   else if(tabReq[1].contains("join")) relation.join(relation1,getSecondT[2]).showAll();
                } else System.out.println("Erreur de requete");
            } catch(Exception ex){
                System.out.println(ex.getMessage());
            }
        }else System.out.println("Erreur de requete"); 
    }

    public static void requete(String req,PrintWriter out) throws Exception{
        String[] tabReq = req.split(" ");
        if(testSyntax(req)){
            try{
                String result = Fichier.lire("data/" + tabReq[3]);
                String[] col = getCol(result);
                String[][] element = getElmt(result); 
                Relation relation = new Relation(Relation.toVec(col), Relation.toVecVec(element));
                if(tabReq[1].contains(",")) relation.projection(tabReq[1]).showAll(out);
                else if(tabReq[1].equals("*")) relation.showAll(out);
                else if(tabReq[1].contains(":")){
                   String[] getSecondT = tabReq[1].split(":");
                   String result1 = Fichier.lire("data/" + getSecondT[1]);
                   Relation relation1 = new Relation(Relation.toVec(getCol(result1)), Relation.toVecVec(getElmt(result1)));                 
                   if(tabReq[1].contains("union")) relation.union(relation1).showAll(out);
                   else if(tabReq[1].contains("intersection")) relation.intersection(relation1).showAll(out);
                   else if(tabReq[1].contains("difference")) relation.difference(relation1).showAll(out);
                   else if(tabReq[1].contains("produit")) relation.produit(relation1).showAll(out);
                   else if(tabReq[1].contains("division")) relation.division(relation1).showAll(out);
                   else if(tabReq[1].contains("join")) relation.join(relation1,getSecondT[2]).showAll(out);
                } else out.println("Erreur de requete");
            } catch(Exception ex){
                out.println(ex.getMessage());
            }
        }else out.println("Erreur de requete"); 
    }

    public static String[] getCol(String res){
        String[] re = res.split("::");
        String[] col = re[0].split(";");
        return col;
    }

    public static String[][] getElmt(String res){
        String[] re = res.split("::");
        String[] ligne = re[1].split("//");
        String[][] elmt = new String[ligne.length][getCol(res).length];
        for(int i=0;i<elmt.length;i++){
            elmt[i] = ligne[i].split(";");
        }
        return elmt;
    }

///getters setters
    public static void setSyntax(String[] stx){
        syntax = stx;
    }
    public static String[] getSyntax(){
        return syntax;
    }
}