package org.kkonoplev.bali.services.utils;

import java.io.BufferedReader;
import java.io.IOException;

public class ReaderUtil {
	
	public ReaderUtil(){
	}
	
	public static String getStringSource(BufferedReader bufReader) throws IOException{

		String content = "";
		String line = "";
		while ((line=bufReader.readLine())!=null){
			content += line;
		}
		return content;
	}

}
