package primary;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Config {
	
	private boolean isRecursive;
	private List<String> directories;
	private String accessMode;
	private int maxDepth;
	private String loadPropertyName;
	private String writePropertyName;
	
	
	public Config() {
		this.isRecursive = false;
		this.directories = new ArrayList<>();
		this.accessMode = "Private";
		this.maxDepth = -1;
		this.loadPropertyName = null;
		this.writePropertyName = null;
	}
	
	public void setRecursive(boolean recursive) {
		this.isRecursive = recursive;
	}
	
	public boolean isRecursive() {
		return this.isRecursive;
	}
	
	public List<String> getDirectories() {
		return this.directories;
	}

	public void addDirectory(String directory) {
		this.directories.add(directory);
	}
	
	public String getAccessMode() {
		return this.accessMode;
	}
	
	/*
	 * AccessMode: String: Private, Protected and Public
	 */
	public void setAccessMode(String access) {
		this.accessMode = access;
	}
	
	public void setMaxDepth(int maxDepth) {
		this.maxDepth = maxDepth;
	}
	
	public int getMaxDepth() {
		return maxDepth;
	}
	
	public void setPropertyName(String propertyName) {
		this.loadPropertyName = propertyName;
	}
	
	public String getPropertyName() {
		return this.loadPropertyName;
	}
	
	public void setWriteProperty(String writeProperty) {
		this.writePropertyName = writeProperty;
	}
	
	public String getWriteProperty() {
		return this.writePropertyName;
	}
	
}
