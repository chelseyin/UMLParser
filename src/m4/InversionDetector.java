package m3;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

import primary.Detector;
import primary.MyClassReader;
import primary.MyFieldReader;
import primary.MyMethodReader;
import primary.MyRelation;
import primary.RelationAnalyzer;

public class InversionDetector implements Detector{
	
	static Set<String> basicType;

	@Override
	public boolean detect(MyClassReader mcr) {
		MyMethodReader mmr = mcr.methodReader;
		MyFieldReader mfr = mcr.fieldReader;
		RelationAnalyzer analyzer = new RelationAnalyzer(mcr);
		
		try {
			for (MyRelation<MyClassReader> relation : analyzer.getArgumentRelation()) {
				if (!(relation.to.isClassAbstract() || relation.to.isClassInterface()))
					return true;
			}
			for (MyRelation<MyClassReader> relation : analyzer.getFieldRelation()) {
				if (!(relation.to.isClassAbstract() || relation.to.isClassInterface()))
					return true;
			}
			for (MyRelation<MyClassReader> relation : analyzer.getReturnRelation()) {
				if (!(relation.to.isClassAbstract() || relation.to.isClassInterface()))
					return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}


}
