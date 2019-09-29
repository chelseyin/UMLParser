package m3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.ClassReader;

import primary.Detector;
import primary.MyClassReader;
import primary.MyMethodReader;

public class BadDecoratorDetector implements Detector{
	
	DecoratorDetector decoratorDetector;
	
	public BadDecoratorDetector() {
		this.decoratorDetector = new DecoratorDetector();
	}

	@Override
	public boolean detect(MyClassReader mcr) {
		List<String> rt = getMissingMethods(mcr);
		return !(rt==null || rt.isEmpty());
		/*
		if (!decoratorDetector.detect(mcr))
			return false;
		else {
			MyMethodReader mmr = mcr.methodReader;
			MyClassReader mcrFather = mcr.extendReader.getReader();
			MyMethodReader mmrFather = mcrFather.methodReader;
			for (int i = 0; i < mmrFather.getMethodSize(); i++) {
				if (!mmrFather.getMethodName(i).equals("<init>")) {
					boolean flag = false;
					for (int j = 0; j < mmr.getMethodSize(); j++) {
						if (mmr.getMethodName(j).equals(mmrFather.getMethodName(i))) {
							flag = true;
							break;
						}
					}
					if (!flag) {
						return true;
					}
				}
			}
			return false;
		}
		*/
	}
	
	public List<String> getMissingMethods(MyClassReader mcr) {
		List<String> rt = new ArrayList<>();
		if (!decoratorDetector.detect(mcr)) {
			System.out.println("Not even a decorator");
			return null;
		}
		else {
			MyMethodReader mmr = mcr.methodReader;
			MyClassReader mcrFather = mcr.extendReader.getReader();
			MyMethodReader mmrFather = mcrFather.methodReader;
			for (int i = 0; i < mmrFather.getMethodSize(); i++) {
				if (!mmrFather.getMethodName(i).equals("<init>")) {
					boolean flag = false;
					for (int j = 0; j < mmr.getMethodSize(); j++) {
						if (mmr.getMethodName(j).equals(mmrFather.getMethodName(i))) {
							flag = true;
							break;
						}
					}
					if (!flag) {
						rt.add(mmrFather.getMethodName(i));
					}
				}
			}
			return rt;
		}
		
	}

}
