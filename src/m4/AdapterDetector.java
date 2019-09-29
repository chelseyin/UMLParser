package m3;

import java.beans.MethodDescriptor;

import primary.Detector;
import primary.MyClassReader;
import primary.MyFieldReader;
import primary.MyMethodReader;

public class AdapterDetector implements Detector{

	@Override
	public boolean detect(MyClassReader mcr) {
		if (mcr.interfaceReader.getInterfaces().isEmpty()) {
//			System.out.println(" NO INTERFACE");
			return false;
		}
		MyMethodReader mmr = mcr.methodReader;
		MyFieldReader mfr = mcr.fieldReader;
		for (int i = 0; i < mmr.getMethodSize(); i++) {
			if (mmr.isConstructor(i)) {
				for (int j = 0; j < mfr.getFieldSize(); j++) {
					if (mmr.getMethodArgumentTypes(i).contains(mfr.getUserFriendlyType(j))) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public boolean isTarget(MyClassReader target, MyClassReader adaptor) {
		if (!detect(adaptor)) {
			return false;
		}
		System.out.println(adaptor.interfaceReader.getInterfaces());
		System.out.println(target.getUserFriendlyName().replaceAll("\\.", "/"));
		if (adaptor.interfaceReader.getInterfaces().contains(target.getUserFriendlyName().replaceAll("\\.", "/"))) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean isAdaptee(MyClassReader adaptee, MyClassReader adaptor) {
		MyFieldReader mfr = adaptor.fieldReader;
		MyMethodReader mmr = adaptor.methodReader;
		for (int i = 0; i < mfr.getFieldSize(); i++) {
			if (mfr.getUserFriendlyType(i).equals(adaptee.getUserFriendlyName())) {
				for (int j = 0; j < mmr.getMethodSize(); j++) {
					if (mmr.isConstructor(j) && mmr.getMethodArgumentTypes(j).contains(adaptee.getUserFriendlyName())) {
						return true;
					}
				}
			}
		}
		return false;
	}

}
