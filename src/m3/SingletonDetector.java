package m3;

import primary.Detector;
import primary.MyClassReader;
import primary.MyMethodReader;

public class SingletonDetector implements Detector{
	
	public boolean detect(MyClassReader mcr) {
		MyMethodReader mmr = mcr.methodReader;
		boolean privateConstructor = false, staticGetter = false;
		for (int i = 0; i < mmr.getMethodSize(); i++) {
			if (mmr.isConstructor(i) && (mmr.isMethodPrivate(i) || mmr.isMethodProtected(i))) {
				privateConstructor = true;
			}
			if (mmr.isMethodStatic(i) && mmr.getMethodReturnType(i).equals(mcr.getUserFriendlyName())) {
				staticGetter = true;
			}
		}
		return privateConstructor&&staticGetter;
	}

}
