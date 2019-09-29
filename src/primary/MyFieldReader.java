package primary;

import java.util.List;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

public class MyFieldReader extends MyReader{
	
	List<FieldNode> fields;

	public MyFieldReader(ClassNode node) {
		this.fields = (List<FieldNode>) node.fields;
	}

	public int getFieldSize() {
		/*
		 * It doesn't make sense for size / 2, but it prevents duplicate prints. 
		 */
		return fields.size();
	}
	
	public String getFieldName(int index) {
		return fields.get(index).name;
	}
	
	public String getFieldSignature(int index) {
		return fields.get(index).signature;
	}
	
	public String getFieldDesc(int index) {
		return fields.get(index).desc;
	}
	
	public String getUserFriendlyType(int index) {
		return Type.getType(fields.get(index).desc).getClassName().toString();
	}
	
	public String getFieldAccess(int index) {
		return super.getAccess(fields.get(index).access);
	}
	
	public String toString() {
		String s = "";
		for (int index = 0; index < getFieldSize(); index++) {
			// System.out.println(index);
			s = s + "	Field: " + getFieldName(index) + "\n";
			s = s + "	Internal JVM type: " + getFieldDesc(index) + "\n";
			s = s + "	User-friendly type: " + getUserFriendlyType(index) + "\n";
			s = s + "	access? " + getFieldAccess(index) + "\n";
			s = s + "\n";
		}
		return s;
	}
}
