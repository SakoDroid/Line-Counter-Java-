package Analyzers;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;

public class Type {

    private HashMap<String,String> types;
    private static final Type tp = new Type();

    private Type(){
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream("/var/lib/AKO/assets/types.sak"))){
            types = (HashMap<String, String>) ois.readObject();
        }catch (Exception ex){
            System.out.println(ex.toString());
        }
    }

    public static Type getInstance(){
        return tp;
    }

    public String getType(String ext){
        if (ext.isEmpty())
            return "Other";
        String type = this.types.get(ext);
        return (type == null) ? "Other" : type ;
    }
}
