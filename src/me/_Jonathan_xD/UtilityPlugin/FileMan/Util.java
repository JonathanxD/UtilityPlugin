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
}
