package structure;

public class SkipNode<T extends Comparable<T>> {
	SkipNode<T> left, right, up, down;
	
	T value;
	
	public SkipNode()
	{
		
	}
}