package primary;

public class MyRelation<E> implements Comparable<MyRelation>{
	public E from;
	public E to;
	public Genre.RelationType RelationType;
	
	
	public MyRelation(E from, E to) {
		super();
		this.from = from;
		this.to = to;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 * Input: 
	 * 			target: target that you want to compare to
	 * Return:	
	 * 			0 is equal, -1 is unequal
	 */
	@Override
	public int compareTo(MyRelation target) {
		if (target.from.equals(this.from) && target.to.equals(this.to))
			return 0;
		return -1;
	}
	
	@Override
	public boolean equals(Object target) {
		if (this == target) {
			return true;
		}
		else if (! (target instanceof MyRelation))
			return false;
		else 
			return compareTo((MyRelation) target)==0;
	}
	
	@Override
	public int hashCode() {
        return this.toString().hashCode();
    }
	
	public String toString() {
		return "["+from+", "+to+"]";
	}
	
	public E getFrom(){
		return this.from;
	}
	
	public E getTo(){
		return this.to;
	}
	
	public MyRelation<E> reverse(){
		return new MyRelation(to,from);
		
	}
	
	public void setRelationType(Genre.RelationType str){
		this.RelationType=str;
	}
	
}
