package primary;

import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.Method;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public class MyMethodReader extends MyReader{
	
	private List<MethodNode> methods;

	public MyMethodReader(ClassNode node) {
		this.methods = (List<MethodNode>) node.methods;
	}
	
	public int getMethodSize() {
		/*
		 * It doesn't make sense for size / 2, but it prevents duplicate prints. 
		 */
		return methods.size();
	}
	
	public String getMethodName(int index) {
		return methods.get(index).name;
	}
	
	public String getMethodSignature(int index) {
		return methods.get(index).signature;
	}
	
	public String getMethodDesc(int index) {
		return methods.get(index).desc;
	}
	
	public String getMethodReturnType(int index) {
		return Type.getReturnType(getMethodDesc(index)).getClassName();
	}
	
	public List<String> getMethodArgumentTypes(int index) {
		List<String> rt = new ArrayList<>();
		for (Type argType : Type.getArgumentTypes(methods.get(index).desc)) {
			rt.add(argType.getClassName());
		}
		return rt;
	}
	
	public List<String> getMethodArgumentNames(int index) {
		/*
		 * FIXME: It is not tested. It may have some problems. I am not sure if getInternalName gets method's names. 
		 */
		return null;
	}
	
	public int getMethodArgumentSize(int index) {
		return Type.getArgumentTypes(methods.get(index).desc).length;
	}
	
	public String getMethodAccess(int index) {
		return getAccess(this.methods.get(index).access);
	}
	
	public boolean isMethodPrivate(int index) {
		return super.isPrivate(methods.get(index).access);
	}
	
	public boolean isMethodProtected(int index) {
		return super.isProtected(methods.get(index).access);
	}
	
	public boolean isMethodPublic(int index) {
		return super.isPublic(methods.get(index).access);
	}
	
	public boolean isMethodStatic(int index) {
		return ((methods.get(index).access & Opcodes.ACC_STATIC) != 0);
	}
	
	public boolean isConstructor(int index) {
		return methods.get(index).name.equals("<init>");
	}
	
	public String toString() {
		String s = "";
		for (int index = 0; index < getMethodSize(); index++) {
			s = s + "	Method: " + getMethodName(index) + "\n";
			s = s + "	Internal JVM method signature: " + getMethodDesc(index) + "\n";
			s = s + "	Return type: " + getMethodReturnType(index) + "\n";
			s = s + "	Args: " + getMethodArgumentTypes(index) + "\n";
			s = s + "	access? " + getMethodAccess(index) + "\n";
			s = s + "	static? " + isMethodStatic(index) + "\n";
			s = s + "\n";
		}

		return s;
	}

}
