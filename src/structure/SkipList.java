package structure;

import java.util.Random;

public class SkipList<T extends Comparable<T>> 
{
	private SkipNode<T> head, tail;
	
	public SkipList()
	{
		head = new SkipNode<T>("HEAD");
		tail = new SkipNode<T>("TAIL");
		
		head.linkRight(tail);
	}
	
	public void insert(T val)
	{
		boolean foundIt = false;
		
		SkipNode<T> front = head;
		SkipNode<T> end = tail;
		SkipNode<T> A = head;
		SkipNode<T> B = tail;
		SkipNode<T> insert = new SkipNode<T>(val);
		
		while(true) // Search for the final insertion point
		{
			do // Find the column where it belongs
			{
				if(A.compareTo(insert) < 0)
				{
					if(insert.compareTo(B) < 0) 
					{
						break; // A < insert < B
					}
					else
					{
						// Shift 'em right
						A = A.right;
						B = B.right;
					}
				}
				else if(A.compareTo(insert) == 0)
				{
					foundIt = true;
					break; // It's already here
				}
				else
				{
					System.err.println("This shouldn't be happening...");
					System.exit(0);
				}
			} while(!B.isTail); // While B is not part of the TAIL column
			
			if(foundIt)
				break;
			else if(A.down != null)
			{ // If we aren't in the bottom row, drop down one
				A = A.down;
				B = A.right;
				front = front.down;
				end = end.down;
			}
			else // Between A and B is where the node will be inserted
			{
				propagate(A, B, front, end, insert);
				break;
			}
		} 
	}
	
	public boolean delete()
	{
		return false;
	}
	
	public T find()
	{
		return null;
	}
	
	public String print()
	{
		return "";
	}

	/* The randomized propagation upwards */
	private void propagate(SkipNode<T> A, SkipNode<T> B, 
						   SkipNode<T> front, SkipNode<T> end, SkipNode<T> insert) 
	{
		float f;
		int flip;
		
		Random random = new Random();
		
		do // Because we insert the first time regardless of the coin flip
		{
			f = random.nextFloat();
			flip = Math.round(f);
			
			// Insert node at this level
			A.linkRight(insert);
			B.linkLeft(insert);
			
			if(front.up != null)
			{ // Move up a row
				A = A.up;
				B = A.right;
				front = front.up;
				end = end.up;
			}
			else // Create new top level
			{
				A = new SkipNode<T>("HEAD");
				B = new SkipNode<T>("TAIL");
				front.linkUp(A);
				end.linkUp(B);
			}
						
		} while(flip != 0);
	}
}