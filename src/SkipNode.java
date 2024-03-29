

public class SkipNode<T extends Comparable<T>> 
{
	Boolean isHead, isTail;
	
	SkipNode<T> left;	// Null for HEAD
	SkipNode<T> right;	// Null for TAIL
	SkipNode<T> up;		// Null for top row
	SkipNode<T> down;	// Null for bottom row
	T value; 
	
	/* Only assign booleans on a terminal node */
	public SkipNode(String terminal)
	{
		if(terminal.equals("HEAD"))
		{
			isHead = true;
			isTail = false;
		}
		else if(terminal.equals("TAIL"))
		{
			isHead = false;
			isTail = true;
		}
		else
		{
			System.err.println("Invalid Identifier -- HEAD | TAIL");
			System.exit(0);
		}
	}
	
	public SkipNode(T val)
	{
		value = val;
		isHead = false;
		isTail = false;
	}
	
	/* Creates an edge between this node and the one to the right */
	public void linkRight(SkipNode<T> other)
	{
		right = other;
		other.left = this; 
	}
	
	/* Creates an edge between this node and the one to the left */
	public void linkLeft(SkipNode<T> other)
	{
		left = other;
		other.right = this; 
	}
	
	/* Creates an edge between this node and the one to the up */
	public void linkUp(SkipNode<T> other)
	{
		up = other;
		other.down = this; 
	}
	
	/* Creates an edge between this node and the one to the down */
	public void linkDown(SkipNode<T> other)
	{
		down = other;
		other.up = this; 
	}
	
	public SkipNode<T> clone()
	{
		return new SkipNode<T>(value);
	}

	public int compareTo(SkipNode<T> other)
	{
		if(isHead || other.isTail)
		{
			return -1;
		}
		else if(other.isHead || isTail)
		{
			return 1;
		}
		else
		{
			return this.value.compareTo(other.value);
		}
	}
}