package primary;

import java.util.List;

import org.objectweb.asm.ClassReader;

public interface Recognizer {
	void recognize(List<ClassReader> classes);
}
