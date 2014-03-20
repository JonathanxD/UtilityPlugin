package me._Jonathan_xD.UtilityPlugin.FileMan;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
 
public class FReader {
    public static String[] SpaceList = {
        "\0", "\1", "\2", "\3", " "
    };
        
    public static ArrayList<String> ReadFile(String file) {
        ArrayList<String> LinesReaded = new ArrayList<String>(); 
        try (BufferedReader br = new BufferedReader(new FileReader(file)))
        {
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                LinesReaded.add(sCurrentLine);
            }
            return LinesReaded;
        } catch (IOException e) {
            return null;
        } 
    }
    public static boolean isEmpty(String file){	 
        try (BufferedReader br = new BufferedReader(new FileReader(file)))
        { 
            if(br.readLine() != null) {
                return false;
            }else{
                return false;
            }
        } catch (IOException e) {
            return true;
        }
    }
    public static String getValue(String value, String file, String equalsValue){
        if(!value.isEmpty() && !file.isEmpty()){
            ArrayList<String> read = new ArrayList<String>();
            read = ReadFile(file);
            String b = null;
            String x = null;
            for(int l = 0;l < read.size();++l){
                x = read.get(l);
                if(x != null && x.indexOf(value+equalsValue) != -1){
                    b = x.replace(value+equalsValue, "");
                    for(String space : SpaceList)b = b.replaceAll(space, "");
                    break;
                }
            }
            return b;
        }else return null;
    }
    public static ArrayList<String> getValueByFun(String value, String file, String start, String end){
        if(!value.isEmpty() && !file.isEmpty()){
            ArrayList<String> read = new ArrayList<String>();
            read = ReadFile(file);
            String x = null;
            ArrayList<String> p_p = new ArrayList<String>();
            boolean __p = false;
            for(int l=0;l < read.size();++l){
                x = read.get(l);
                if(x != null){
                    if(x.indexOf(start) != -1){
                        __p=true;
                        continue;
                    }
                    if(__p==true && x.indexOf(end) <= -1){
                        p_p.add(x);
                    }
                    if(x.indexOf(end) != -1){
                        break;
                    }
                }
            }
            return p_p;
        }else{
            return null;
        }
    }
}