package structure;

import java.util.Random;

public class SkipList<T extends Comparable<T>> 
{
	// Always point at the starting and ending points for a search/insert
	private SkipNode<T> head, tail;
	
	// Manipulate during the search/insert
	private SkipNode<T> front;
	private SkipNode<T> end;
	private SkipNode<T> A;
	private SkipNode<T> B;
	private SkipNode<T> value;
	
	private boolean foundIt;
	
	public SkipList()
	{
		head = new SkipNode<T>("HEAD");
		tail = new SkipNode<T>("TAIL");
		
		head.linkRight(tail);
	}
	
	public void insert(T val)
	{
		foundIt = false;
		
		front = head;
		end = tail;
		A = head;
		B = tail;
		value = new SkipNode<T>(val);
		
		while(true) // Search for the final insertion point
		{
			do // Find the column where it belongs
			{
				if(A.compareTo(value) < 0)
				{
					if(value.compareTo(B) < 0) 
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
				else if(A.compareTo(value) == 0)
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
				propagate(A, B, front, end, value);
				break;
			}
		} 
	}
	
	public boolean delete()
	{
		return false;
	}
	
	/* Returns the node in the highest row containing the value 'find' */
	public SkipNode<T> search(T find)
	{	
		foundIt = false;
		
		front = head;
		end = tail;
		A = head;
		B = tail;
		value = new SkipNode<T>(find);
		
		while(true) // Until we're done
		{
			do // Find the column where it is (for this row)
			{
				if(A.compareTo(value) < 0)
				{
					if(value.compareTo(B) < 0) 
					{
						break; // A < insert < B; Need to drop down
					}
					else if(value.compareTo(B) > 0)
					{
						// Shift 'em right
						A = A.right;
						B = B.right;
					}
					else // It equals B
					{
						return B;
					}
				}
				else if(A.compareTo(value) == 0)
				{ // I don't think this ever fires, but just in case
					return A;
				}
				else
				{
					System.err.println("This shouldn't be happening...");
					System.exit(0);
				}
			} while(!B.isTail); // While B is not part of the TAIL column
			
			if(A.down != null)
			{ // If we aren't in the bottom row, drop down one
				A = A.down;
				B = A.right;
				front = front.down;
				end = end.down;
			}
			else
				break; // It's not here
		} 
	
		return null;
	}
	
	public String print()
	{
		int height = 0, width = 0;
		A = head;
		
		while (A.down != null) {
			A = A.down;
			height ++;
		}
		
		while (A.right != null) {
			A = A.right;
			width++;
		}
		
		char[][] map = new char[height][width];
		for (int i = 0; i < map.length; i++) 
			for (int j = 0; j < map[0].length; j++)
				map[i][j] = 'o';

		A = head;
		while (A.down != null)
			A = A.down;
		
		int x = 0;
		while (A.right != null) {
			map[0][x] = 'x';
			
			int y = 0;
			B = A;
			while (B.up != null) {
				map[y][x] = 'x';
				B = B.up;
				y ++;
			}
			
			A = A.right;
			x ++;
		}
		
		String out = "";
		for (int i = map.length - 1; i >= 0; i--) {
			for (int j = 0; j < map[0].length - 1; j++) {
				out += map[i][j];
			}
			
			out += "\n";
		}
		
		return out;
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
		
		// Don't forget to update the global head and tail for the list
		head = A;
		tail = B;
	}
}