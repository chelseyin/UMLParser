package m1;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import m3.CoverIDetector;
import m3.SingletonDetector;
import m4.AdapterDetector;
import m4.BadDecoratorDetector;
import m4.DecoratorDetector;
import m4.InversionDetector;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.core.DiagramDescription;
import primary.Config;
import primary.Drawer;
import primary.Genre;
import primary.Genre.RelationGenre;
import primary.Genre.RelationType;
import primary.MyClassReader;
import primary.MyRelation;
import primary.ReaderType;

public class DrawerM1 implements Drawer {
	
	static final String outputPath = "output/";
	private HashMap<RelationGenre,String> relationMap;
	private HashSet<MyRelation> assoSet = new HashSet<>();
	private HashSet<MyRelation> depSet = new HashSet<>();
	private CoverIDetector coveri=new CoverIDetector();
	private SingletonDetector single=new SingletonDetector();
	private InversionDetector inversion=new InversionDetector();
	private DecoratorDetector decorator=new DecoratorDetector(); 
	private AdapterDetector adapter=new AdapterDetector();
	private Set<ReaderType> set=new HashSet();
	private Set<MyClassReader> setClasses=new HashSet();
	private Map<MyClassReader,MyClassReader> DecoMap=new HashMap<>();
	private Map<MyClassReader,MyClassReader> AdapMap=new HashMap<>();
	private BadDecoratorDetector badDeco=new BadDecoratorDetector();
	


	private Config config;
	private HashSet<String> collections;
	
	public DrawerM1(Set<MyRelation<MyClassReader>> fieldSet, Set<MyRelation<MyClassReader>> argumentSet,
			Set<MyRelation<MyClassReader>> returnSet){
		this.collections = new HashSet<>();
		collections.add("List");
		collections.add("Set");
		collections.add("Map");
		relationMap = new HashMap<>();
		relationMap.put(RelationGenre.Extends, " <|-- ");
		relationMap.put(RelationGenre.Implements, " <|.. ");
		relationMap.put(RelationGenre.Depends, " ..> ");
		relationMap.put(RelationGenre.HAS_A, " --> ");
	}
	

	
	@Override
	public void draw(List<MyClassReader> classes,Set<MyRelation<MyClassReader>> fieldSet, 
			Set<MyRelation<MyClassReader>> argumentSet, Set<MyRelation<MyClassReader>> returnSet) {
		StringBuilder result = new StringBuilder();
		result.append("@startuml\n");
		
		for(MyClassReader mcr: classes){
			CheckDecoAdap(mcr);
		}
		setClasses.addAll(classes);

		for(ReaderType rt: set){
			if(setClasses.contains(rt.getMCR())){
				setClasses.remove(rt.getMCR());
			}
		}
		
		for(MyClassReader mcr: setClasses){
			syncClassName(result,mcr);
			syncFields(result,mcr);
			syncMethods(result,mcr);
			result.append("}\n\n");
			
		}
		for(ReaderType rt: set){
			syncClassNameRT(result,rt);
			syncFields(result,rt.getMCR());
			syncMethods(result,rt.getMCR());
			result.append("}\n\n");
			
		}
		syncRelationM1(result,classes);
		syncRelationM2(result,fieldSet,argumentSet,returnSet);
		
		result.append("@enduml");
		finalize(result.toString());
	}







	private void syncClassNameRT(StringBuilder result, ReaderType rt) {
		if(rt.Type.equals(Genre.ClassType.BadDecorator)){
			if(rt.getMCR().getClassAccess()==null){
				result.append("Class "+ rt.getMCR().getUserFriendlyName()+" <<Bad Decorator>>"+"{\n");
			}else{
				if(rt.getMCR().getClassAccess()=="Public"){
					result.append("Class "+ rt.getMCR().getUserFriendlyName()+" <<Bad Decorator>>"+"{\n");
				}
				if(config.getAccessMode().equals("Protected")){
					if(rt.getMCR().getClassAccess().equals("Protected")){
						result.append("Class "+ rt.getMCR().getUserFriendlyName()+" <<Bad Decorator>>"+"{\n");
					}
				}
				if(config.getAccessMode().equals("Private")){
					if(rt.getMCR().getClassAccess()=="Private"){
						result.append("Class "+ rt.getMCR().getUserFriendlyName()+" <<Bad Decorator>>"+"{\n");
					}
				}	
			}
		}else if(rt.Type.equals(Genre.ClassType.Component)){
			if(rt.getMCR().getClassAccess()==null){
				result.append("Class "+ rt.getMCR().getUserFriendlyName()+" <<Component>> #Green"+"{\n");
			}else{
				if(rt.getMCR().getClassAccess()=="Public"){
					result.append("Class "+ rt.getMCR().getUserFriendlyName()+" <<Component>> #Green"+"{\n");
				}
				if(config.getAccessMode().equals("Protected")){
					if(rt.getMCR().getClassAccess().equals("Protected")){
						result.append("Class "+ rt.getMCR().getUserFriendlyName()+" <<Component>> #Green"+"{\n");
					}
				}
				if(config.getAccessMode().equals("Private")){
					if(rt.getMCR().getClassAccess()=="Private"){
						result.append("Class "+ rt.getMCR().getUserFriendlyName()+" <<Component>> #Green"+"{\n");
					}
				}
			}
		}
		else if(rt.Type.equals(Genre.ClassType.Decorator)){
			if(rt.getMCR().getClassAccess()==null){
				result.append("Class "+ rt.getMCR().getUserFriendlyName()+" <<Decorator>> #Green"+"{\n");
			}else{
				if(rt.getMCR().getClassAccess()=="Public"){
					result.append("Class "+ rt.getMCR().getUserFriendlyName()+" <<Decorator>> #Green"+"{\n");
				}
				if(config.getAccessMode().equals("Protected")){
					if(rt.getMCR().getClassAccess().equals("Protected")){
						result.append("Class "+ rt.getMCR().getUserFriendlyName()+" <<Decorator>> #Green"+"{\n");
					}
				}
				if(config.getAccessMode().equals("Private")){
					if(rt.getMCR().getClassAccess()=="Private"){
						result.append("Class "+ rt.getMCR().getUserFriendlyName()+" <<Decorator>> #Green"+"{\n");
					}
				}
			}
		}
		else if(rt.Type.equals(Genre.ClassType.Adapter)){
			if(rt.getMCR().getClassAccess()==null){
				result.append("Class "+ rt.getMCR().getUserFriendlyName()+" <<Adapter>> #Red"+"{\n");
			}else{
				if(rt.getMCR().getClassAccess()=="Public"){
					result.append("Class "+ rt.getMCR().getUserFriendlyName()+" <<Adapter>> #Red"+"{\n");
				}
				if(config.getAccessMode().equals("Protected")){
					if(rt.getMCR().getClassAccess().equals("Protected")){
						result.append("Class "+ rt.getMCR().getUserFriendlyName()+" <<Adapter>> #Red"+"{\n");
					}
				}
				if(config.getAccessMode().equals("Private")){
					if(rt.getMCR().getClassAccess()=="Private"){
						result.append("Class "+ rt.getMCR().getUserFriendlyName()+" <<Adapter>> #Red"+"{\n");
					}
				}
			}
		}else if(rt.Type.equals(Genre.ClassType.Adaptee)){
			if(rt.getMCR().getClassAccess()==null){
				result.append("Class "+ rt.getMCR().getUserFriendlyName()+" <<Adaptee>> #Red"+"{\n");
			}else{
				if(rt.getMCR().getClassAccess()=="Public"){
					result.append("Class "+ rt.getMCR().getUserFriendlyName()+" <<Adaptee>> #Red"+"{\n");
				}
				if(config.getAccessMode().equals("Protected")){
					if(rt.getMCR().getClassAccess().equals("Protected")){
						result.append("Class "+ rt.getMCR().getUserFriendlyName()+" <<Adaptee>> #Red"+"{\n");
					}
				}
				if(config.getAccessMode().equals("Private")){
					if(rt.getMCR().getClassAccess()=="Private"){
						result.append("Class "+ rt.getMCR().getUserFriendlyName()+" <<Adaptee>> #Red"+"{\n");
					}
				}
			}
		}else if(rt.Type.equals(Genre.ClassType.Target)){
			if(rt.getMCR().getClassAccess()==null){
				result.append("Class "+ rt.getMCR().getUserFriendlyName()+" <<Target>> #Red"+"{\n");
			}else{
				if(rt.getMCR().getClassAccess()=="Public"){
					result.append("Class "+ rt.getMCR().getUserFriendlyName()+" <<Target>> #Red"+"{\n");
				}
				if(config.getAccessMode().equals("Protected")){
					if(rt.getMCR().getClassAccess().equals("Protected")){
						result.append("Class "+ rt.getMCR().getUserFriendlyName()+" <<Target>> #Red"+"{\n");
					}
				}
				if(config.getAccessMode().equals("Private")){
					if(rt.getMCR().getClassAccess()=="Private"){
						result.append("Class "+ rt.getMCR().getUserFriendlyName()+" <<Target>> #Red"+"{\n");
					}
				}	
			}
		}
//		else if(rt.Type.equals(Genre.ClassType.BadDecorator)){
//			if(rt.getMCR().getClassAccess()==null){
//				result.append("Class "+ rt.getMCR().getUserFriendlyName()+" <<Bad Decorator>>"+"{\n");
//			}else{
//				if(rt.getMCR().getClassAccess()=="Public"){
//					result.append("Class "+ rt.getMCR().getUserFriendlyName()+" <<Bad Decorator>>"+"{\n");
//				}
//				if(config.getAccessMode().equals("Protected")){
//					if(rt.getMCR().getClassAccess().equals("Protected")){
//						result.append("Class "+ rt.getMCR().getUserFriendlyName()+" <<Bad Decorator>>"+"{\n");
//					}
//				}
//				if(config.getAccessMode().equals("Private")){
//					if(rt.getMCR().getClassAccess()=="Private"){
//						result.append("Class "+ rt.getMCR().getUserFriendlyName()+" <<Bad Decorator>>"+"{\n");
//					}
//				}	
//			}
//		}
	}



	private void CheckDecoAdap(MyClassReader mcr) {
		if(badDeco.detect(mcr)){
			ReaderType rt=new ReaderType(mcr);
			rt.setRelationType(Genre.ClassType.BadDecorator);
			set.add(rt);
		}else if(decorator.detect(mcr)){
			ReaderType rt=new ReaderType(mcr);
			rt.setRelationType(Genre.ClassType.Decorator);
			set.add(rt);
			
			if(decorator.isComponent(mcr.extendReader.getReader(), mcr)){
				ReaderType rt1=new ReaderType(mcr.extendReader.getReader());
				rt1.setRelationType(Genre.ClassType.Component);
				set.add(rt1);
			}
		}else if(adapter.detect(mcr)){
			ReaderType rt=new ReaderType(mcr);
			rt.setRelationType(Genre.ClassType.Adapter);
			set.add(rt);
			List<MyClassReader> interfaceR=mcr.interfaceReader.getReaders();
			for(int i=0;i<interfaceR.size();i++){
				if(adapter.isAdaptee(interfaceR.get(i),mcr)){
					ReaderType rt1=new ReaderType(interfaceR.get(i));
					rt1.setRelationType(Genre.ClassType.Adaptee);
					set.add(rt1);
				}else if(adapter.isTarget(interfaceR.get(i),mcr)){
					ReaderType rt1=new ReaderType(interfaceR.get(i));
					rt1.setRelationType(Genre.ClassType.Target);
					set.add(rt1);
				}
			}
		}
	}


	private void syncClassName(StringBuilder result, MyClassReader mcr) {
		if(single.detect(mcr)){
			if(mcr.getClassAccess()==null){
				result.append("Class "+ mcr.getUserFriendlyName()+" <<Singleton>> #Blue"+"{\n");
			}else{
				if(mcr.getClassAccess()=="Public"){
					result.append("Class "+ mcr.getUserFriendlyName()+" <<Singleton>> #Blue"+"{\n");
				}
				if(config.getAccessMode().equals("Protected")){
					if(mcr.getClassAccess().equals("Protected")){
						result.append("Class "+ mcr.getUserFriendlyName()+" <<Singleton>> #Blue"+"{\n");
					}
				}
				if(config.getAccessMode().equals("Private")){
					if(mcr.getClassAccess()=="Private"){
						result.append("Class "+ mcr.getUserFriendlyName()+" <<Singleton>> #Blue"+"{\n");
					}
				}
			}
		}
//		else if(inversion.detect(mcr)){
//			if(mcr.getClassAccess()==null){
//				result.append("Class "+ mcr.getUserFriendlyName()+" <<Inversion>> #Yellow"+"{\n");
//			}else{
//				if(mcr.getClassAccess()=="Public"){
//					result.append("Class "+ mcr.getUserFriendlyName()+" <<Inversion>> #Yellow"+"{\n");
//				}
//				if(config.getAccessMode().equals("Protected")){
//					if(mcr.getClassAccess().equals("Protected")){
//						result.append("Class "+ mcr.getUserFriendlyName()+" <<Inversion>> #Yellow"+"{\n");
//					}
//				}
//				if(config.getAccessMode().equals("Private")){
//					if(mcr.getClassAccess()=="Private"){
//						result.append("Class "+ mcr.getUserFriendlyName()+" <<Inversion>> #Yellow"+"{\n");
//					}
//				}
//			}
//		}
		
//		else if(coveri.detect(mcr)){
//			if(mcr.getClassAccess()==null){
//				result.append("Class "+ mcr.getUserFriendlyName()+" #Orange"+"{\n");
//			}else{
//				if(mcr.getClassAccess()=="Public"){
//					result.append("Class "+ mcr.getUserFriendlyName()+" #Orange"+"{\n");
//				}
//				if(config.getAccessMode().equals("Protected")){
//					if(mcr.getClassAccess().equals("Protected")){
//						result.append("Class "+ mcr.getUserFriendlyName()+" #Orange"+"{\n");
//					}
//				}
//				if(config.getAccessMode().equals("Private")){
//					if(mcr.getClassAccess()=="Private"){
//						result.append("Class "+ mcr.getUserFriendlyName()+" #Orange"+"{\n");
//					}
//				}
//			}
//		}
		else{
			if(mcr.getClassAccess()==null){
				result.append("Class "+ mcr.getUserFriendlyName()+"{\n");
			}else{
				if(mcr.getClassAccess()=="Public"){
					result.append("Class "+ mcr.getUserFriendlyName()+"{\n");
				}
				if(config.getAccessMode().equals("Protected")){
					if(mcr.getClassAccess().equals("Protected")){
						result.append("Class "+ mcr.getUserFriendlyName()+"{\n");
					}
				}
				if(config.getAccessMode().equals("Private")){
					if(mcr.getClassAccess()=="Private"){
						result.append("Class "+ mcr.getUserFriendlyName()+"{\n");
					}
				}
			}
		}
	}
	
	private void syncFields(StringBuilder result, MyClassReader mcr) {
		for(int i=0;i<mcr.fieldReader.getFieldSize();i++){
			String fieldAccess = mcr.fieldReader.getFieldAccess(i);
			if(fieldAccess==null){
				result.append(mcr.fieldReader.getUserFriendlyType(i)+" "
						+mcr.fieldReader.getFieldName(i));
				result.append("\n");
			}else{
				if (fieldAccess!=null&&fieldAccess.equals("Public")) {
				result.append(mcr.fieldReader.getFieldAccess(i)+" "
						+mcr.fieldReader.getUserFriendlyType(i)+" "
						+mcr.fieldReader.getFieldName(i));
				result.append("\n");

				}
				if ((fieldAccess==null||fieldAccess.equals("Protected")) && !config.getAccessMode().equals("Public")) {
				result.append(mcr.fieldReader.getFieldAccess(i)+" "
						+mcr.fieldReader.getUserFriendlyType(i)+" "
						+mcr.fieldReader.getFieldName(i));
				result.append("\n");
				}
				if ((fieldAccess!=null&&fieldAccess.equals("Private")) && !config.getAccessMode().equals("Protected") && !config.getAccessMode().equals("Public")){
				result.append(mcr.fieldReader.getFieldAccess(i)+" "
						+mcr.fieldReader.getUserFriendlyType(i)+" "
						+mcr.fieldReader.getFieldName(i));
				result.append("\n");
				}
			}
			
		}
	}
	
	private void syncMethods(StringBuilder result, MyClassReader mcr) {
		if(badDeco.detect(mcr)){
			List<String> missing=badDeco.getMissingMethods(mcr);
			for(int i=0;i<missing.size();i++){
				result.append("<font color=”#FF0000”>"+missing.get(i)+"()"+"</font>"+"\n");
			}
			for(int i=0;i<mcr.methodReader.getMethodSize();i++){
				if(!missing.contains(mcr.methodReader.getMethodName(i))){
					String methodAccess = mcr.methodReader.getMethodAccess(i);
					
					if (methodAccess!=null&&methodAccess.equals("Public")) {
						System.out.println("Public:"+methodAccess);
						result.append(mcr.methodReader.getMethodAccess(i)+" "
								+mcr.methodReader.getMethodReturnType(i)+" "
								+mcr.methodReader.getMethodName(i)+"()");
						result.append("\n");
					}
					
					if ((methodAccess==null||methodAccess.equals("Protected")) && !config.getAccessMode().equals("Public")) {
						System.out.println("Protected:"+methodAccess);
						result.append(mcr.methodReader.getMethodAccess(i)+" "
								+mcr.methodReader.getMethodReturnType(i)+" "
								+mcr.methodReader.getMethodName(i)+"()");
						result.append("\n");
					}
					
					if ((methodAccess!=null&&methodAccess.equals("Private")) && !config.getAccessMode().equals("Protected") && !config.getAccessMode().equals("Public")) {
						result.append(mcr.methodReader.getMethodAccess(i)+" "
								+mcr.methodReader.getMethodReturnType(i)+" "
								+mcr.methodReader.getMethodName(i)+"()");
						
					}
				}
				
			}
		}else{
			for(int i=0;i<mcr.methodReader.getMethodSize();i++){
				String methodAccess = mcr.methodReader.getMethodAccess(i);
				
				if (methodAccess!=null&&methodAccess.equals("Public")) {
					System.out.println("Public:"+methodAccess);
					result.append(mcr.methodReader.getMethodAccess(i)+" "
							+mcr.methodReader.getMethodReturnType(i)+" "
							+mcr.methodReader.getMethodName(i)+"()");
					result.append("\n");
				}
				
				if ((methodAccess==null||methodAccess.equals("Protected")) && !config.getAccessMode().equals("Public")) {
					System.out.println("Protected:"+methodAccess);
					result.append(mcr.methodReader.getMethodAccess(i)+" "
							+mcr.methodReader.getMethodReturnType(i)+" "
							+mcr.methodReader.getMethodName(i)+"()");
					result.append("\n");
				}
				
				if ((methodAccess!=null&&methodAccess.equals("Private")) && !config.getAccessMode().equals("Protected") && !config.getAccessMode().equals("Public")) {
					result.append(mcr.methodReader.getMethodAccess(i)+" "
							+mcr.methodReader.getMethodReturnType(i)+" "
							+mcr.methodReader.getMethodName(i)+"()");
					
				}

			}
		}
		
	}
	
	
	private void syncRelationM1(StringBuilder result, List<MyClassReader> classes) {
		for(MyClassReader mcr: classes){

			if(mcr.extendReader!=null){
				if(mcr.extendReader.hasExtend()){
					if(coveri.detect(mcr)){
						result.append(mcr.extendReader.getUserFriendlyNames()+" "
								+relationMap.get(RelationGenre.Extends)+" "
								+mcr.getUserFriendlyName()+" #Orange"+"\n");
					}else{
						result.append(mcr.extendReader.getUserFriendlyNames()+" "
							+relationMap.get(RelationGenre.Extends)+" "
							+mcr.getUserFriendlyName()+"\n");
					}
				}
			}
			if(mcr.interfaceReader!=null){
				for(String mir:mcr.interfaceReader.getUserFriendlyNames()){
					result.append(mir+" "+relationMap.get(RelationGenre.Implements)
							+" "+mcr.getUserFriendlyName()+"\n");
				}
			}
		}		
	}
	
	private void syncRelationM2(StringBuilder result,Set<MyRelation<MyClassReader>> fieldSet, 
			Set<MyRelation<MyClassReader>> argumentSet, Set<MyRelation<MyClassReader>> returnSet) {
		for(MyRelation<MyClassReader> relation:fieldSet){
			argumentSet.remove(relation);
			returnSet.remove(relation);
//			for(MyRelation<MyClassReader> r:argumentSet){
//				if(r.compareTo(relation)==0){
//					argumentSet.remove(r);
//				}
//			}
//			for(MyRelation<MyClassReader> r:returnSet){
//				if(r.compareTo(relation)==0){
//					returnSet.remove(r);
//				}
//			}

		}
		
		
		for(MyRelation<MyClassReader> relation: fieldSet){
			
			if(relation.equals(relation.reverse())){
				result.append(relation.from.getUserFriendlyName()+" <--> "+relation.to.getUserFriendlyName()+" #Purple"+'\n');
			}else if(relation.RelationType.equals(RelationType.HAS_A)){
				result.append(relation.from.getUserFriendlyName()+" --> "+relation.to.getUserFriendlyName()+'\n');
			}else if(relation.RelationType.equals(RelationType.HAS_MANY)){
				result.append(relation.from.getUserFriendlyName()+" --> "+"\"1..*\" "+relation.to.getUserFriendlyName()+'\n');
			}continue;
		}
		
		Set<MyRelation<MyClassReader>> depSet=new HashSet<>();
		depSet.addAll(argumentSet);
		depSet.addAll(returnSet);
		
		for(MyRelation<MyClassReader> relation: depSet){

				if(relation.equals(relation.reverse())){
					result.append(relation.from.getUserFriendlyName()+" <..> "+relation.to.getUserFriendlyName()+" #Purple"+'\n');
				}else if(relation.RelationType.equals(RelationType.DEPENDS)){
					result.append(relation.from.getUserFriendlyName()+" ..> "+relation.to.getUserFriendlyName()+'\n');
				}else if(relation.RelationType.equals(RelationType.DEPENDS_MANY)){
					result.append(relation.from.getUserFriendlyName()+" ..> "+"\"1..*\" "+relation.to.getUserFriendlyName()+'\n');
					
			}
		}
		
//		for(MyRelation<MyClassReader> relation: returnSet){
//
//				if(relation.equals(relation.reverse())){
//					result.append(relation.from.getUserFriendlyName()+" <..> "+relation.to.getUserFriendlyName()+" #Purple"+'\n');
//				}else if(relation.RelationType.equals(RelationType.DEPENDS)){
//					result.append(relation.from.getUserFriendlyName()+" ..> "+relation.to.getUserFriendlyName()+'\n');
//				}else if(relation.RelationType.equals(RelationType.DEPENDS_MANY)){
//					result.append(relation.from.getUserFriendlyName()+" ..> "+"\"1..*\" "+relation.to.getUserFriendlyName()+'\n');
//				}
//		}
//		for(MyClassReader mcr: assoReaders){
//			for(int i=0;i<mcr.fieldReader.getFieldSize();i++){
//				
//				if(mcr.fieldReader.getUserFriendlyType(i).contains("[]")){
//					int length = mcr.fieldReader.getUserFriendlyType(i).length()-2;
//					String s = mcr.fieldReader.getUserFriendlyType(i).substring(0, length);
//					//result.append(mcr.getUserFriendlyName()+" --> "+"\"1..*\" "+s+"\n");
//					MyRelation relation = new MyRelation(mcr.getUserFriendlyName(),s);
//					relation.setRelationType(Genre.RelationType.HAS_MANY);
//					assoSet.add(relation);
//				}else{
//					MyRelation relation = new MyRelation(mcr.getUserFriendlyName(),
//							mcr.fieldReader.getUserFriendlyType(i));
//					relation.setRelationType(Genre.RelationType.HAS_A);
//					assoSet.add(relation);
//				}		
//			}
//			
//			for(int i=0;i<mcr.methodReader.getMethodSize();i++){
//				String return_type = mcr.methodReader.getMethodReturnType(i); 
//				//return type
//				if(!return_type.equals("void")){
//					String name = mcr.methodReader.getMethodName(i);
//					String sig = mcr.methodReader.getMethodSignature(i);
//					if(return_type.contains("[]")){
//						return_type = return_type.substring(0, return_type.length()-2);
//						MyRelation relation = new MyRelation(mcr.getUserFriendlyName(), return_type);
//						
//						relation.setRelationType(Genre.RelationType.DEPENDS_MANY);
//						assoSet.add(relation);
//					}else if(isCollection(return_type)){
//						System.out.println("hi"+return_type);
//						sig = this.parseSignature(sig);
//						MyRelation relation = new MyRelation(mcr.getUserFriendlyName(), sig);
//						relation.setRelationType(Genre.RelationType.DEPENDS_MANY);
//						assoSet.add(relation);
//					}else{
//						MyRelation relation = new MyRelation(mcr.getUserFriendlyName(), return_type);
//						relation.setRelationType(Genre.RelationType.DEPENDS);
//						assoSet.add(relation);
//					}
//					
//				}
//				
//				//argument type
//				List<String> argsType = mcr.methodReader.getMethodArgumentTypes(i);
//				for(String arg: argsType){
//					if(arg.contains("[]")){
//						arg = arg.substring(0, arg.length()-2);
//						MyRelation relation = new MyRelation(mcr.getUserFriendlyName(), arg);
//						relation.setRelationType(Genre.RelationType.DEPENDS_MANY);
//						assoSet.add(relation);
//					}else{
//						MyRelation relation = new MyRelation(mcr.getUserFriendlyName(), arg);
//						relation.setRelationType(Genre.RelationType.DEPENDS);
//						assoSet.add(relation);
//					}
//				}
//					
//			}
//			
//		}
	}
	
	private boolean isCollection(String type){
		if(type.contains("Iterator")) return false;
		if(type.contains("Ljava")){
			return true;
		}
		return false;
		
	}
	
	private String parseSignature(String sig){
		String[] temp = sig.split("<");
		
		String type = temp[temp.length-1];
		temp = type.split(";");
		type = temp[0];
		type = type.substring(1).replaceAll("/",".");
		return type;
	}
	
	private void drawRelationM2(StringBuilder result){
		for(MyRelation relation: this.assoSet){
			if(relation.RelationType==RelationType.HAS_A||
					relation.RelationType==RelationType.HAS_MANY){
				if(relation.equals(relation.reverse())){
					result.append(relation.from+" <--> "+relation.to+" #Purple"+'\n');
				}else if(relation.RelationType.equals(RelationType.HAS_A)){
					result.append(relation.from+" --> "+relation.to+'\n');
				}else if(relation.RelationType.equals(RelationType.HAS_MANY)){
					result.append(relation.from+" --> "+"\"1..*\" "+relation.to+'\n');
				}continue;
			}
			else if(relation.RelationType==RelationType.DEPENDS||
					relation.RelationType==RelationType.DEPENDS_MANY){
				if(relation.equals(relation.reverse())){
					result.append(relation.from+" <..> "+relation.to+" #Purple"+'\n');
				}else if(relation.RelationType.equals(RelationType.DEPENDS)){
					result.append(relation.from+" ..> "+relation.to+'\n');
				}else if(relation.RelationType.equals(RelationType.DEPENDS_MANY)){
					result.append(relation.from+" ..> "+"\"1..*\" "+relation.to+'\n');
				}
			}
			
		}
	}
	
	public void setConfig(Config config){
		this.config=config;
		System.out.println(this.config.getAccessMode());
	}
	
	
	
	@SuppressWarnings("deprecation")
	private void finalize(String result) {
        try {
        	System.out.println(result);
        	PrintWriter writer = new PrintWriter(this.outputPath+"uml.txt","UTF-8");
        	
        	writer.print(result);
        	writer.close();	
        	
        	SourceStringReader reader = new SourceStringReader(result);
        	final ByteArrayOutputStream os = new ByteArrayOutputStream();
        	// Write the first image to "os"
        	String desc=reader.generateImage(os, new FileFormatOption(FileFormat.SVG));
        	os.close();

        	// The XML is stored into svg
        	final String svg = new String(os.toByteArray(), Charset.forName("UTF-8"));
        	Path filePath = Paths.get(this.outputPath+"uml.svg");
        	OutputStream outStream = new FileOutputStream(filePath.toFile());
            FileFormatOption option = new FileFormatOption(FileFormat.SVG, false);
            DiagramDescription description = reader.outputImage(outStream, option);
            
        } catch (Exception e) {
        	e.printStackTrace();
        }
		
	}






}
