package main;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Signature;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import m1.DrawerM1;
import m1.ParserM1;
import primary.Config;
import primary.Drawer;
import primary.MyClassReader;
import primary.MyRelation;
import primary.RelationAnalyzer;

public class Main {
	
	static Set<String> basicType;
	static int maxDepth = 3;
	
	public static void recursiveForEverything(MyClassReader mcr, List<MyClassReader> list, Set<MyRelation<MyClassReader>> fieldSet, Set<MyRelation<MyClassReader>> argumentSet, Set<MyRelation<MyClassReader>> returnSet, int depth) throws IOException {
		recursiveExtendAnalyze(mcr, list, fieldSet, argumentSet, returnSet, depth);
		recursiveInterfaceAnalyze(mcr, list, fieldSet, argumentSet, returnSet, depth);
		recursiveAssociationAnalyze(mcr, list, fieldSet, argumentSet, returnSet, depth);
		recursiveDependencyAnalyze(mcr, list, fieldSet, argumentSet, returnSet, depth);
	}
	
	public static void recursiveExtendAnalyze(MyClassReader mcr, List<MyClassReader> list, Set<MyRelation<MyClassReader>> fieldSet, Set<MyRelation<MyClassReader>> argumentSet, Set<MyRelation<MyClassReader>> returnSet, int depth) throws IOException {
		if (depth >= maxDepth) {
			return;
		}
		if (mcr.extendReader.hasExtend()) {
			String extendClassName = mcr.extendReader.getUserFriendlyNames();
			if (basicType.contains(extendClassName)) {
				return;
			}
			MyClassReader temp = new MyClassReader(new ClassReader(extendClassName));
			list.add(temp);
			recursiveForEverything(mcr, list, fieldSet, argumentSet, returnSet, depth+1);
		}
	}
	
	public static void recursiveInterfaceAnalyze(MyClassReader mcr, List<MyClassReader> list, Set<MyRelation<MyClassReader>> fieldSet, Set<MyRelation<MyClassReader>> argumentSet, Set<MyRelation<MyClassReader>> returnSet, int depth) throws IOException {
		if (depth >= maxDepth) {
			return;
		}
		List<String> interfaceClassName = mcr.interfaceReader.getUserFriendlyNames();
		for (int i = 0; i < interfaceClassName.size(); i++) {
			MyClassReader temp = new MyClassReader(new ClassReader(interfaceClassName.get(i)));
			list.add(temp);
			recursiveForEverything(mcr, list, fieldSet, argumentSet, returnSet, depth+1);
		}
	}
	
	public static void recursiveAssociationAnalyze(MyClassReader mcr, List<MyClassReader> list, Set<MyRelation<MyClassReader>> fieldSet, Set<MyRelation<MyClassReader>> argumentSet, Set<MyRelation<MyClassReader>> returnSet, int depth) throws IOException {
		if (depth >= maxDepth) {
			return;
		}
		RelationAnalyzer analyzer = new RelationAnalyzer(mcr, basicType);
		Set<MyRelation<MyClassReader>> set = analyzer.getFieldRelation();
		for (MyRelation<MyClassReader> relation : set) {
			if (!fieldSet.contains(relation)) {
				fieldSet.add(relation);
				if (!basicType.contains(relation.to))
					recursiveForEverything(relation.to, list, fieldSet, argumentSet, returnSet, depth+1);
			}
		}
	}
	
	public static void recursiveDependencyAnalyze(MyClassReader mcr, List<MyClassReader> list, Set<MyRelation<MyClassReader>> fieldSet, Set<MyRelation<MyClassReader>> argumentSet, Set<MyRelation<MyClassReader>> returnSet, int depth) throws IOException {
		if (depth >= maxDepth) {
			return;
		}
		RelationAnalyzer analyzer = new RelationAnalyzer(mcr, basicType);
		
		Set<MyRelation<MyClassReader>> retset = analyzer.getReturnRelation();
		for (MyRelation<MyClassReader> relation : retset) {
			if (!returnSet.contains(relation)) {
				returnSet.add(relation);
				if (!basicType.contains(relation.to))
					recursiveForEverything(relation.to, list, fieldSet, argumentSet, returnSet, depth+1);
			}
		}
		Set<MyRelation<MyClassReader>> argset = analyzer.getArgumentRelation();
		for (MyRelation<MyClassReader> relation : argset) {
			if (!argumentSet.contains(relation)) {
				argumentSet.add(relation);
				if (!basicType.contains(relation.to))
					recursiveForEverything(relation.to, list, fieldSet, argumentSet, returnSet, depth+1);
			}
		}
	}
	
	public static void printOnConsole(MyClassReader mcr) {
		System.out.println(mcr.toString());
		System.out.println(mcr.methodReader.toString());
		System.out.println(mcr.fieldReader.toString());
	}

	public static void main(String[] args) throws Exception {
		
		//initialize map
		buildBasicTypeSet();
		
		Config config = new Config();
		ParserM1 m1Parser;
		
		m1Parser = new ParserM1(args, config);
		if (config.getMaxDepth() != -1) {
			maxDepth = config.getMaxDepth();
		}
		if (config.getPropertyName()!=null) {
			args = m1Parser.loadProperty(config.getPropertyName());
			m1Parser = new ParserM1(args, config);
		}
		if (config.getWriteProperty()!=null) {
			m1Parser.storeProperty(args, config.getWriteProperty());
		}
		
		List<String> directories = config.getDirectories();
		List<MyClassReader> classReaders = new ArrayList<>();
		Set<MyRelation<MyClassReader>> fieldSet = new HashSet<>();
		Set<MyRelation<MyClassReader>> argumentSet = new HashSet<>();
		Set<MyRelation<MyClassReader>> returnSet = new HashSet<>();

		
		for (String className : directories) {
			if (config.isRecursive()) {

				ClassReader reader = new ClassReader(className);
//				ClassNode classNode = new ClassNode();
//				reader.accept(classNode, ClassReader.EXPAND_FRAMES);

				MyClassReader mcr = new MyClassReader(reader);
				classReaders.add(mcr);
				
				recursiveForEverything(mcr, classReaders, fieldSet, argumentSet, returnSet, 0);
				System.out.println(fieldSet);
			}
			else {
				System.out.println(className);
				ClassReader reader = new ClassReader(className);
				ClassNode classNode = new ClassNode();
				reader.accept(classNode, ClassReader.EXPAND_FRAMES);

				MyClassReader mcr = new MyClassReader(reader);
				classReaders.add(mcr);
				
				recursiveForEverything(mcr, classReaders, fieldSet, argumentSet, returnSet, maxDepth-1);
				
				//System.out.println(dependencyReaders);
				//printOnConsole(mcr);
			}
			
			
		}
		//System.out.println(classReaders.size());
//		Drawer drawerM1=new DrawerM1();
//		drawerM1.setConfig(config);
//		drawerM1.draw(classReaders);
        Drawer drawerM1=new DrawerM1(fieldSet, argumentSet, returnSet);
		drawerM1.setConfig(config);
		drawerM1.draw(classReaders,fieldSet, argumentSet, returnSet);
		

	}
	
	private static void buildBasicTypeSet() {
		basicType = new HashSet<>();
		basicType.add("I");
		basicType.add("int");
		basicType.add("Z");
		basicType.add("boolean");
		basicType.add("C");
		basicType.add("char");
		basicType.add("B");
		basicType.add("byte");
		basicType.add("S");
		basicType.add("short");
		basicType.add("F");
		basicType.add("float");
		basicType.add("J");
		basicType.add("long");
		basicType.add("D");
		basicType.add("double");
		basicType.add("V");
		basicType.add("void");
		basicType.add("*");
		basicType.add("**");
		basicType.add("java/lang/Object");
		basicType.add("java/lang/String");
		basicType.add("java/lang/Integer");
		basicType.add("java/lang/Long");
		basicType.add("java/lang/Boolean");
		basicType.add("java/lang/Byte");
		basicType.add("java/lang/Short");
		basicType.add("java/lang/Double");
		basicType.add("java/lang/Float");
		basicType.add("java/lang/Character");
		basicType.add("java/lang/Class");
//		basicType.add("java/util/Map");
//		basicType.add("java/util/HashMap");
//		basicType.add("java/util/List");
//		basicType.add("java/util/ArrayList");
	}
}
