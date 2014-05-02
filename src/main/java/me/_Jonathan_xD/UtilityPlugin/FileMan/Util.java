/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package me._Jonathan_xD.UtilityPlugin.FileMan;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jonathan
 */
public class Util {
    public static File reNew(File file){
        try {
            file.delete();
            file.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    public static File reNew(String file){
        File toRenew = new File(file);
        return toRenew;
    }
    public static boolean isNumber(String v){
    	try{
    		Integer.parseInt(v);
    		return true;
    	}catch(Exception e){
    		return false;
    	}
    }
	public static boolean containsLetter(String string) {
		for(char c : string.toCharArray()){
			if(Character.isAlphabetic(c)){
				return true;
			}
		}
		return false;
	}
    public static char[] letters = {'a','b','c','d','e','f','g','h','i',
    	'j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
	public static String lettersToNumber(String string) {
		string = string.replace("-", ".");
		StringBuilder sb = new StringBuilder();
		for(char c : string.toCharArray()){
			for(int x=0;x<letters.length;++x){
				char v = letters[x];
				if(c == v){
					c = String.valueOf(x).charAt(0);
				}
			}
			sb.append(c);
		}
		return sb.toString();
	}
	
}
