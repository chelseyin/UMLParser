package primary;

import java.io.IOException;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;

public class MyClassReader extends MyReader{

	public MyMethodReader methodReader;
	public MyFieldReader fieldReader;
	public MyExtendReader extendReader;
	public MyInterfaceReader interfaceReader;
	
	private ClassNode node;
	
	//public String extendClass;
	
	public MyClassReader(ClassReader cr) throws IOException {
		this.node = new ClassNode();
		cr.accept(node, ClassReader.EXPAND_FRAMES);
		
		this.methodReader = new MyMethodReader(node);
		this.fieldReader = new MyFieldReader(node);
		this.extendReader = new MyExtendReader(node);
		this.interfaceReader = new MyInterfaceReader(node);
	}
	
	public String getInnerClassName() {
		return node.name;
	}
	
	public String getUserFriendlyName() {
		return Type.getObjectType(node.name).getClassName();
	}
	
	public String getClassAccess() {
		return super.getAccess(node.access);
	}
	
	public boolean isClassAbstract() {
		return ((node.access & Opcodes.ACC_ABSTRACT) != 0);
	}
	
	public boolean isClassInterface() {
		return ((node.access & Opcodes.ACC_INTERFACE) != 0);
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof MyClassReader)) 
			return false;
		else {
			MyClassReader temp = (MyClassReader) o;
			return temp.hashCode() == this.hashCode();
		}
	}
	
	@Override
	public int hashCode() {
		return this.toString().hashCode();
	}
	
	public String toString() {
		String s = "";
		s = s + "Class's JVM internal name: " + getInnerClassName() + "\n";
		s = s + "User-friendly name: " + getUserFriendlyName() + "\n";
		s = s + "Access? " + getClassAccess() + "\n";
		s = s + "Extends: " + extendReader.getUserFriendlyNames() + "\n";
		s = s + "Implements: " + interfaceReader.getUserFriendlyNames() + "\n\n";
		return s;
	}

}
