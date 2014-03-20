package me._Jonathan_xD.UtilityPlugin.FileMan;

import java.util.ArrayList;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
 
public class FWriter {
	public static IOException Write(String file, ArrayList<String> toWrite) {
            try {
                File _file = new File(file);
 		if (!_file.exists()) {
			_file.createNewFile();
		}
		FileWriter fw = new FileWriter(_file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		for(int a=0;a<toWrite.size();++a){
			bw.write(toWrite.get(a));
		}			
		bw.close();
		return null;
		} catch (IOException e) {
                    return e;
		}
	}
}