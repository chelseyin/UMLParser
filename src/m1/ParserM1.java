package m1;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

import primary.Config;
import primary.Parser;

public class ParserM1 implements Parser{
	
	public ParserM1(String[] args, Config config) {
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-load")) {
				config.setPropertyName(args[i+1]);
				return;
			}
		}
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-write")) {
				config.setWriteProperty(args[i+1]);
				break;
			}
		}
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-recursive")) {
				config.setRecursive(true);
				break;
			}
		}
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-dir")) {
				String path = args[i+1];
				File file = new File(path);
				//System.out.println(path+"\n");
				//System.out.println(file.getName()+"\n");
				if (file!=null) {
					File[] files = file.listFiles();
					for (File f : files) {
						if (f.getName().contains(".java")) {
							String fileName = f.getName().replace(".java", "");
							//System.out.println(file.getName() + "." + fileName);
							config.addDirectory(file.getName() + "." + fileName);
						}
					}
				}
			}
		}
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-class")) {
				config.addDirectory(args[i+1]);
			}
		}
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-Private") || args[i].equals("-private")) {
				config.setAccessMode("Private");
			}
			else if (args[i].equals("-Protected") || args[i].equals("-protected")) {
				config.setAccessMode("Protected");
			}
			else if (args[i].equals("-Public") || args[i].equals("-public")) {
				config.setAccessMode("Public");
			}
		}
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-depth")) {
				config.setMaxDepth(Integer.getInteger(args[i+1]));
			}
		}
	}
	
	public void storeProperty(String[] args, String outputFileName) throws Exception {
		Properties prop = new Properties();
		FileOutputStream oFile = new FileOutputStream(outputFileName, false);
		
		int i = 0;
		prop.setProperty(Integer.toString(i), Integer.toString(args.length));
		i++;
		for (String s : args) {
			prop.setProperty(Integer.toString(i), s);
			i++;
		}
		prop.store(oFile, "Input file");
		oFile.close();
	}
	
	public String[] loadProperty(String inputFileName) {
		try {
			Properties prop = new Properties();
			InputStream in = new BufferedInputStream(new FileInputStream(inputFileName));
			prop.load(in);
			Iterator<String> it=prop.stringPropertyNames().iterator();
			String[] rt = null;
			
			int length = Integer.valueOf(prop.getProperty("0"));
			rt = new String[length];
			
			while (it.hasNext()) {
				String key = it.next();
				if (Integer.valueOf(key)!=0) {
					rt[Integer.valueOf(key)-1] = prop.getProperty(key);
					
				}
				
			}
			return rt;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}

}
