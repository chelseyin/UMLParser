package main;

import java.io.File;

public class TestProgram {

	public static void main(String[] args) {
		String path = "C:\\Users\\xiek\\Desktop\\MyDestop\\study\\CSSE374\\Java\\CSSE374 Project\\src\\primary";
		File file = new File(path);
		File[] files = file.listFiles();
		System.out.println(file.getName()+"\n");
		
		if (file != null) {
			for (File f : files) {
				if (f.getName().contains(".java")) {
					String fileName = f.getName().replace(".java", "");
					System.out.println(file.getName() + "." + fileName);
				}
			}
		}
	}

}
