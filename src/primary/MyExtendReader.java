package primary;

import java.io.IOException;
import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;


public class MyExtendReader extends MyReader{
	
	private ClassNode node;
	private List<MethodNode> methods;

	public MyExtendReader(ClassNode node) {
		this.node = node;
		this.methods = (List<MethodNode>) node.methods;
		
		
	}
	public int getMethodSize() {
		return methods.size();
	}
	
	public String getMethodName(int index) {
		return methods.get(index).name;
	}
	
	public boolean hasExtend() {
		return !getUserFriendlyNames().equals("java.lang.Object");
	}
	
	public String getExtends() {
		return node.superName;
	}
	
	public String getUserFriendlyNames() {
		//System.out.println(Type.getObjectType(this.getExtends().toString()).getClassName());
		return Type.getObjectType(this.getExtends().toString()).getClassName();
	}
	
	public MyClassReader getReader() {
		MyClassReader mcr = null;
		try {
			ClassReader reader = new ClassReader(node.superName);
			mcr = new MyClassReader(reader);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return mcr;
	}

}
