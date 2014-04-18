package structure;

public class SkipList<T extends Comparable<T>> 
{
	private SkipNode<T> head, tail;
	
	public SkipList()
	{
		head = new SkipNode<T>("HEAD");
		tail = new SkipNode<T>("TAIL");
		
		head.hlink(tail);
	}
	
	public void insert(T val)
	{
		SkipNode<T> A = head;
		SkipNode<T> B = tail;
		
		
	}
	
	public boolean delete()
	{
		return false;
	}
	
	public T find()
	{
		return null;
	}
}