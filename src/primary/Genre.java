package primary;

public class Genre {
	
	public enum RelationGenre{
		Extends,Implements,Depends,HAS_A
	}
	public enum MethodGenre{
		Public,Private,Protected
	}
	public enum PatternGenre{
        SINGLETON,VIOLATECOMPOSITION,BIDIRECTION,DECORATER,ADAPTER,BadDECORATOR,DIP,COMPONENT,TARGET,ADAPTEE
    }

	public enum ClassType{
		Abstrast,Interface,Class,Component,Adaptee,Target,Decorator,Adapter,BadDecorator
	}
	
	public enum RelationType{
		HAS_A, HAS_MANY,DEPENDS,DEPENDS_MANY
		
	}
	

}
