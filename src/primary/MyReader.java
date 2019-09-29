package primary;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

public abstract class MyReader {

	
	public MyReader() {

	}
	
	protected boolean isPublic(int access) {
		return ((access & Opcodes.ACC_PUBLIC) != 0);
	}
	
	protected boolean isPrivate(int access) {
		return ((access & Opcodes.ACC_PRIVATE) != 0);
	}
	
	protected boolean isProtected(int access) {
		return ((access & Opcodes.ACC_PROTECTED) != 0);
	}
	
	public String getAccess(int access) {
		if (isPublic(access))
			return "Public";
		if (isPrivate(access))
			return "Private";
		if (isProtected(access)) 
			return "Protected";
		return null;
	}
}
