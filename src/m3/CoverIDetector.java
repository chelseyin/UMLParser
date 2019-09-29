package m3;

import primary.Detector;
import primary.MyClassReader;

public class CoverIDetector implements Detector {

	public boolean detect(MyClassReader mcr) {
		if(mcr.extendReader!=null){
			for(int i=0;i<mcr.methodReader.getMethodSize();i++){
				String methodName=mcr.methodReader.getMethodName(i);
				for(int j=0;j<mcr.extendReader.getMethodSize();j++){
					if(mcr.extendReader.getMethodName(i).equals(methodName)){
						return true;
					}
				}
			}
		}
		
		return false;
	}
}
