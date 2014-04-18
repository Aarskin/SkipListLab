package structure;

public class SkipList<T extends Comparable<T>> {
	SkipNode<T> root, tail;
	
	public SkipList()
	{
		root = new SkipNode<T>();
		tail = new SkipNode<T>();
	}
}