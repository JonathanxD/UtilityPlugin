package me._Jonathan_xD.UtilityPlugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me._Jonathan_xD.UtilityPlugin.FileMan.Util;

import org.bukkit.Bukkit;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * This class is a barebones example of how to use the BukkitDev ServerMods API to check for file updates.
 * <br>
 * See the README file for further information of use.
 */
//72492/
public class Update {

    // The project's unique ID
    private final int projectID;

    // An optional API key to use, will be null if not submitted
    private final String apiKey;

    // Keys for extracting file information from JSON response
    private static final String API_NAME_VALUE = "name";
    private static final String API_LINK_VALUE = "downloadUrl";
    private static final String API_RELEASE_TYPE_VALUE = "releaseType";
    private static final String API_FILE_NAME_VALUE = "fileName";
    private static final String API_GAME_VERSION_VALUE = "gameVersion";

    // Static information for querying the API
    private static final String API_QUERY = "/servermods/files?projectIds=";
    private static final String API_HOST = "https://api.curseforge.com";

    /**
     * Check for updates anonymously (keyless)
     *
     * @param projectID The BukkitDev Project ID, found in the "Facts" panel on the right-side of your project page.
     */
    public Update(int projectID) {
        this(projectID, null);
    }

    /**
     * Check for updates using your Curse account (with key)
     *
     * @param projectID The BukkitDev Project ID, found in the "Facts" panel on the right-side of your project page.
     * @param apiKey Your ServerMods API key, found at https://dev.bukkit.org/home/servermods-apikey/
     */
    public Update(int projectID, String apiKey) {
        this.projectID = projectID;
        this.apiKey = apiKey;

        query(null, null, null);
    }
    public Update(int projectID, String apiKey, String ver, String type, String mcVer) {
        this.projectID = projectID;
        this.apiKey = apiKey;

        query(ver, type, mcVer);
    }

    /**
     * Query the API to find the latest approved file's details.
     */
    public static String[] verType = {
    	"Alpha", "Beta", "Release"
    };
    public void query(String ver, String type, String mcVer) {
        URL url = null;

        try {
            // Create the URL to query using the project's ID
            url = new URL(API_HOST + API_QUERY + projectID);
        } catch (MalformedURLException e) {

        	System.out.println("[UtilityPlugin] Error on check updates: "+e.toString());
            return;
        }

        try {
            URLConnection conn = url.openConnection();

            if (apiKey != null) {
                conn.addRequestProperty("X-API-Key", apiKey);
            }

            conn.addRequestProperty("JonathanScripter", "UtilityPlugin");
            final BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String response = reader.readLine();

            JSONArray array = (JSONArray) JSONValue.parse(response);

            if (array.size() > 0) {
                JSONObject latest = (JSONObject) array.get(array.size() - 1);

                String versionName = (String) latest.get(API_NAME_VALUE);

                String versionLink = (String) latest.get(API_LINK_VALUE);

                String versionType = (String) latest.get(API_RELEASE_TYPE_VALUE);

                String versionFileName = (String) latest.get(API_FILE_NAME_VALUE);

                String versionGameVersion = (String) latest.get(API_GAME_VERSION_VALUE);

                int major = majorVersion(ver, verByFileName(versionName));
            	if(versionGameVersion.equalsIgnoreCase(getBukkitVersion()))
            	{
                    if((major == 1) || (major == 2) && supType(versionType,type) == 0){
                        System.out.println(
                                "[UtilityPlugin] Have Updates, That plugin version: "+ver+" ,The latest version of " + versionFileName +
                                        " is " + verByFileName(versionName) +
                                        ", a " + versionType.toUpperCase() +
                                        " for " + versionGameVersion +
                                        ", available at: " + versionLink
                        );                	
                    }else if(major == 0){
                        System.out.println("[UtilityPlugin] Oh, that plugin version is newer than the latest posted, That's bug?, your version " + ver + " lastest posted: " +verByFileName(versionName)+".");
                    }            		
            	}
            } else {
                //System.out.println("There are no files for this project");
            }
        } catch (IOException e) {
        	System.out.println("[UtilityPlugin] Error on check updates: "+e.toString());
            return;
        }
    }
    
	public static String getBukkitVersion() {
		String version = Bukkit.getVersion();
		Pattern pattern = Pattern.compile("(\\((MC: )([0-9]+)(.)([0-9]+)(.)([0-9]+)\\))");
		Matcher matcher = pattern.matcher(version);
		StringBuilder sb = new StringBuilder();		
		if (matcher.find()) {
			for(int i = 3; i < 8; ++i)sb.append(matcher.group(i));
		}
		return sb.toString();
	}

	private String verByFileName(String fileName) {		
		char[] separators = {'_', '-'};	
		for(char c : separators){
			if(fileName.indexOf(String.valueOf(c)) != -1){
				return fileName.substring(fileName.indexOf(c)+1);
			}
		}
		return null;
	}
    public static int supType(String type1, String type2){
    	if(type1.equalsIgnoreCase(type2))return 0;
    	for(int a = 0; a < verType.length; ++a){
    		if(type1.equalsIgnoreCase(verType[a])){
    			return 1;
    		}
    		if(type2.equalsIgnoreCase(verType[a])){
    			return 0;
    		}
    	}
    	
    	return -1;
    }
    public int majorVersion(String a, String b){
    	a = Util.lettersToNumber(a);
    	b = Util.lettersToNumber(b);
    	String[] tmp = a.split(java.util.regex.Pattern.quote("."));
    	String[] tmp2 = b.split(java.util.regex.Pattern.quote("."));    	
    	int max = 0;
    	if(tmp.length > tmp2.length){
    		max = tmp2.length;    				
    	}else
    	if(tmp.length < tmp2.length){
    		max = tmp.length;    				
    	}
    	if(tmp.length == tmp2.length){
    		max=tmp.length;
    	}
		if(a == b){
			return 2;
		}
    	for(int k=0;k<max;++k){
    		if(Integer.parseInt(tmp2[k]) > Integer.parseInt(tmp[k])){
    			return 1;
    		}
    		if(Integer.parseInt(tmp[k]) > Integer.parseInt(tmp2[k])){
    			return 0;
    		}
    	}
    	return -1;
    }
    public static int IndexOfIn(String a, String x){
        for(int k=a.length();k!=-1;--k){
            if(a.indexOf(x, k) != -1){
                return k;//a.substring(k+1);
            }
        }
        return -1;
    }
    
    
}