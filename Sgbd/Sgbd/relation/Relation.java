package relation;

import java.io.PrintWriter;
import java.util.Vector;


public class Relation {
    Vector<String> colonne;
    Vector<Vector<String>> value;

    public Relation(Vector<String> col,Vector<Vector<String>> val){ 
        setColonne(col);
        setValue(noDouDoublant(val));
    }
    
///fonction
    public void showAll() {
        for (String col : getColonne()) System.out.print(col + "          ");
        System.out.println();
        for (int i = 0; i < getValue().size(); i++) {
            for (int j = 0; j < getColonne().size(); j++) {
                System.out.print(getValue().get(i).get(j)+"          ");
            } 
            System.out.println();
        }
    }
    public void showAll(PrintWriter out) {
        for (String col : getColonne()) out.print(col + "          ");
        out.println();
        for (int i = 0; i < getValue().size(); i++) {
            for (int j = 0; j < getColonne().size(); j++) {
                out.print(getValue().get(i).get(j)+"          ");
            } 
            out.println();
        }
    }
    //union
    public Relation union(Relation r1) throws Exception{
        if(getColonne().size() != r1.getColonne().size()) throw new Exception("Nombres de colonnes differents");
        Vector<Vector<String>> v = getValue();
        Vector<Vector<String>> v1 = r1.getValue();
        Vector<Vector<String>> resp = new Vector<>();
        for (Vector<String> vector : v) resp.add(vector);
        for (Vector<String> vector : v1) resp.add(vector);
        Relation vaovao = new Relation(getColonne(),resp);
        return vaovao;
    }
    //projection
    public Relation projection(String colonne) throws Exception{
        String[] col = colonne.split(",");
        Vector<Vector<String>> donne = new Vector<>();
        int[] indice = new int[col.length];
        for (int i = 0; i < indice.length; i++) {
            indice[i] = getColonne().indexOf(col[i]);
            if(indice[i] == -1) throw new Exception("colonne inexistante");
        } 
        for (int i = 0; i < getValue().size(); i++) {
            Vector<String> proj = new Vector<>();
            for (int j : indice) {
                proj.add(getValue().get(i).get(j));
            }
            donne.add(proj);
        }
        Relation resp = new Relation(Relation.toVec(col),donne);
        return resp; 
    }

    //intersection
    public Relation intersection(Relation r1) throws Exception{
        if(getColonne().size() != r1.getColonne().size()) throw new Exception("Nombres de colonnes differents");
        Vector<Vector<String>> v = getValue();
        Vector<Vector<String>> v1 = r1.getValue();
        Vector<Vector<String>> data = new Vector<>();
        for (Vector<String> vector : v) {
            for (Vector<String> vector2 : v1) {
                if(cmpStringdim1(vector,vector2)) data.add(vector);
            }
        }
        Relation resp = new Relation(getColonne(), data);
        return resp;
    }

    //difference
    public Relation difference(Relation r1) throws Exception{
        if(getColonne().size() != r1.getColonne().size()) throw new Exception("Nombres de colonnes differents");
        Vector<Vector<String>> v = getValue();
        Vector<Vector<String>> v1 = r1.getValue();
        Vector<Vector<String>> dif = new Vector<>();
        for (Vector<String> vector : v) {
            boolean resp = true;
            for (Vector<String> vector2 : v1) {
                if(cmpStringdim1(vector, vector2)) resp = false;
            }
            if(resp) dif.add(vector);
        }
        Relation resp = new Relation(getColonne(), dif);
        return resp;
    }

    //produit cartesien
    public Relation produit(Relation r1){
        Vector<Vector<String>> v = getValue();
        Vector<Vector<String>> v1 = r1.getValue();
        Vector<Vector<String>> newVal = new Vector<>();
        Vector<String> newCol = new Vector<>();
        newCol.addAll(getColonne());newCol.addAll(r1.getColonne());
        int newSize = v.size() * v1.size();
        int a = 0, b = 0;
        for (int i = 0; i < newSize; i++) {
            if(i % v.size() == 0) a = 0;
            if(i % v.size() == 0 && i > 0) b++;
            Vector<String> tmp = new Vector<>();
            for (int j2 = 0; j2 < v.get(a).size(); j2++) tmp.add(v.get(a).get(j2));
            for (int k2 = 0; k2 < v1.get(b).size(); k2++) tmp.add(v1.get(b).get(k2)); 
            a++;
            newVal.add(tmp);
        }
        Relation resp = new Relation(newCol, newVal);
        return resp;
    }

    //jointure
    public Relation join(Relation r1,String condition) throws Exception{
        Vector<Vector<String>> v = getValue();
        Vector<Vector<String>> v1 = r1.getValue();
        Vector<Vector<String>> newVal = new Vector<>();
        Vector<String> newCol = new Vector<>();
        newCol.addAll(getColonne());newCol.addAll(r1.getColonne());
        String[] cond = condition.split("=");
        if(cond.length < 2) throw new Exception("Erreur de calcul de condition");
        Relation exept = projection(cond[0]);
        exept = r1.projection(cond[1]);
        int indiceTest = getColonne().indexOf(cond[0]);
        int indiceTest1 = r1.getColonne().indexOf(cond[1]); 
        for (Vector<String> string : v) {
            for (Vector<String> string2 : v1) {
                if(string.get(indiceTest).equals(string2.get(indiceTest1))) {
                    Vector<String> temp = new Vector<>();
                    temp.addAll(string);
                    temp.addAll(string2);
                    newVal.add(temp);
                }             
            }
        }
        Relation resp = new Relation(newCol, newVal);
        return resp;
    }

    //division
    public String difColonne(Vector<String> s1,Vector<String> s2){
        String difC = "";
        for (int i = 0; i < s1.size(); i++) if(s2.indexOf(s1.get(i)) == -1) difC = s1.get(i);
        return difC;
    }

    public Relation division(Relation r1) throws Exception{
        Relation re1 = projection(difColonne(getColonne(), r1.getColonne()));
        Relation re2 = re1.produit(r1);
        Relation re3 = re2.difference(this);
        Relation re4 = re3.projection(difColonne(getColonne(), r1.getColonne()));
        Relation re5 = re1.difference(re4);
        return re5;
    }

    public static Vector<String> toVec(String[] ty){
        Vector<String> resp = new Vector<>();
        for (int i = 0; i < ty.length; i++) resp.add(ty[i]);
        return resp;
    }

    public static Vector<Vector<String>> toVecVec(String[][] ty){
        Vector<Vector<String>> resp = new Vector<>();
        for (int i = 0; i < ty.length; i++) resp.add(Relation.toVec(ty[i]));
        return resp;
    }

    public Vector<String> noDoublant(Vector<String> ty){
        for (int i = 0; i < ty.size(); i++) {
            for (int j = i+1; j < ty.size(); j++) {
                if(ty.get(i).equals(ty.get(j))){
                    ty.remove(j);
                    ty = noDoublant(ty);
                }
            }
        }
        return ty;
    }

    public Vector<Vector<String>> noDouDoublant(Vector<Vector<String>> ty){
        for (int i = 0; i < ty.size(); i++) {
            for (int j = i+1; j < ty.size(); j++) {
                if(cmpStringdim1(ty.get(i), ty.get(j))){
                    ty.remove(j);
                    ty = noDouDoublant(ty);
                }
            }
        }
        return ty;
    }

    public boolean cmpStringdim1(Vector<String> s1,Vector<String> s2){
        boolean resp = true;
        if(s1.size() != s2.size()) resp = false;
        for (int i = 0; i < s2.size(); i++) {
            resp = resp && (s1.get(i).equals(s2.get(i)));
        }
        return resp;
    }


///getters setters 
    public Vector<String> getColonne() {
        return colonne;
    }

    public void setColonne(Vector<String> colonne) {
        this.colonne = colonne;
    }

    public Vector<Vector<String>> getValue() {
        return value;
    }

    public void setValue(Vector<Vector<String>> value) {
        this.value = value;
    }


}
