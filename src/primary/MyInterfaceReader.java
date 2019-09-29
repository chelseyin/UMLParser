package primary;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;

public class MyInterfaceReader extends MyReader{
	
	private ClassNode node;

	public MyInterfaceReader(ClassNode node) {
		this.node = node;
	}

	public List<Object> getInterfaces() {
		/*
		 * FIXME: It needs modifications. 
		 */
		return node.interfaces;
	}
	
	public List<String> getUserFriendlyNames() {
		List<String> rt = new ArrayList<>();
		for (int i = 0; i < node.interfaces.size(); i++) {
			rt.add(Type.getObjectType(this.getInterfaces().get(i).toString()).getClassName());
		}
		return rt;
	}
	
	public List<MyClassReader> getReaders() {
		List<MyClassReader> rt = new ArrayList<>();
		MyClassReader mcr = null;
		try {
			for (String name : getUserFriendlyNames()) {
				ClassReader reader = new ClassReader(node.superName);
				mcr = new MyClassReader(reader);
				rt.add(mcr);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return rt;
		
	}
}
