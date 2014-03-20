package me._Jonathan_xD.UtilityPlugin.FileMan;

import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;

public class BadUtils {

	public static HashMap<Integer, Character> NLL = new HashMap<Integer, Character>(); 
    public static int ignoreP = 4; 
    public static boolean itIsBadWord(Player p, String w, List<String> badWords){
		if(p != null && (p.isOp() || p.hasPermission("UtilityPlugin.speakbanword."+w))){
			return false;
		}    	
    	w = w.toLowerCase();
    	w = w.replaceAll("[^a-z0-9]", "");
    	w = convertSpace(w);
    	w = convertStr(w);
    	char s[];
    	s = w.toCharArray();
    	int maxPoints = w.length();
    	int points = 0;
    	if(w.length() >= 4){
    		maxPoints -= ignoreP; 
    	}
    	//String finish = null;
    	String b;
    	for(int x=0; x < badWords.size(); ++x){
    		b = badWords.get(x);
        	if(w.indexOf(b) != -1)
        	{
        		return true;
        	}
    		char z[] = b.toCharArray();
    		char c;
    		int ze = 0;
    		for(int za = 0; za < s.length && ze < z.length; ++za, ++ze){
    			c = s[za];
            	if(c >= '0' && c <= '9'){
            		c = convertChar(c);
            	}        	
    			if(isEChar(c))continue;
    			if(z[ze] == c){
    				++ points;
    			}/*else
    			if(points >= 1){
    				++ze;
    				continue;
    			}*/
    	    	if(points >= maxPoints){
    	    		return true;
    	    	}
        	}    		
    	}
    	return false;
    }
    public static char[] Alfabeto = {
    	'a','b','c','d','e','f','g','h','i','j','k','l','m','n',
    	'o','p','q','r','s','t','u','v','w','x','y','z'    	
    };
    private static boolean isEChar(char c) {
		for(char xc : Alfabeto)if(xc == c)return false;
		return true;
	}
	private static char convertChar(char c) {
    	
		return Character.toLowerCase(NLL.get(Character.getNumericValue(c)));
	}
	public static void setupNLL(){
    	NLL.put(0, 'O');
    	NLL.put(1, 'I');    	
    	NLL.put(2, 'S');    	
    	NLL.put(3, 'E');    	
    	NLL.put(4, 'A');
    	NLL.put(5, 'S');
    	NLL.put(6, 'G');
    	NLL.put(7, 'T');
    	NLL.put(8, 'B');
    	NLL.put(9, 'J');
    }
	private static String convertSpace(String x){
		x = x.replace('\0', ' ');
		x = x.replace('\1', ' ');
		x = x.replace('\2', ' ');
		x = x.replace('\3', ' ');
		x = x.replace('\4', ' ');
		x = x.replace('\5', ' ');
		x = x.replace('\6', ' ');
		x = x.replace('\7', ' ');
		//x.replace("\8", " ");
		//x.replace("\9", " ");
		return x;
	}	
	private static String convertStr(String c) {
    	
		// TODO Stub de método gerado automaticamente
		for(int x=0; x<10;++x){
			c = c.replace(String.valueOf(x), String.valueOf(NLL.get(x)).toLowerCase());
		}
		return c;
	}

}
