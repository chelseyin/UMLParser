package m3;

import org.objectweb.asm.ClassReader;

import primary.Detector;
import primary.MyClassReader;
import primary.MyFieldReader;
import primary.MyMethodReader;

public class DecoratorDetector implements Detector{

	@Override
	public boolean detect(MyClassReader mcr) {
		if (!mcr.extendReader.hasExtend()) {
			return false;
		}
		String father = mcr.extendReader.getExtends().replaceAll("/", ".");
		if (father.equals("java/lang/Object")) {
//			System.out.println("	warning");
			return false;
		}
		boolean inConstructor = false;
		boolean inField = false;
		MyMethodReader mmr = mcr.methodReader;
		for (int i = 0; i < mmr.getMethodSize(); i++) {
			if (mmr.isConstructor(i) && mmr.getMethodArgumentTypes(i).contains(father)) {
				inConstructor = true;
			}
		}
		MyFieldReader mfr = mcr.fieldReader;
		for (int i = 0; i < mfr.getFieldSize(); i++) {
			if (mfr.getUserFriendlyType(i).equals(father)) {
				inField = true;
			}
		}
		return (inConstructor && inField);

	}
	
	public boolean isComponent(MyClassReader component, MyClassReader decorater) {
		if (!detect(decorater)) {
			return false;
		}
		if (decorater.extendReader.getExtends().replaceAll("/", ".").equals(component.getUserFriendlyName())) {
			if (detect(component)) {
				return false;
			}
			return true;
		}
				
		return false;
	}

}
