package primary;

import java.util.List;

import org.objectweb.asm.ClassReader;

public interface Drawer {
	void draw(List<MyClassReader> classes);
	void setConfig(Config config);
}
