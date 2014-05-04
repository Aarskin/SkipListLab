package structure;

import java.util.Random;

public class SkipList<T extends Comparable<T>> 
{
	// The starting pair used for search/insert
	private SkipNode<T> head, tail;
	
	// Manipulate during the search/insert
	private SkipNode<T> front; // of the row
	private SkipNode<T> end;   // of the col
	private SkipNode<T> A;	   // left check
	private SkipNode<T> B;	   // right check
	private SkipNode<T> value; // value being inserted
	
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
		
		// Run to the end of this row
		SkipNode<T> temp = tail;
		while(!temp.isTail)
		{
			temp = temp.right;
		}
		
		front = head;
		end = temp; // The true end of this row
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
				break; // The 'insert' is done
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
	
	/* Returns the node in the highest row containing the value 'find' */
	public int search(T find)
	{	
		int comparisons = 0;
		
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
				comparisons ++;
				if(A.compareTo(value) < 0)
				{
					comparisons ++;
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
						return comparisons;
					}
				}
				else if(A.compareTo(value) == 0)
				{ // I don't think this ever fires, but just in case
					return comparisons;
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
			else	// It's not here
				break;
		}
		
		return 0;
	}
	
	public String toString()
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
				map[i][j] = ' ';

		A = head;
		while (A.down != null)
			A = A.down;
		
		int x = 0;
		while (A.right != null) {
			map[0][x] = '.';
			
			int y = 0;
			B = A;
			while (B.up != null) {
				map[y][x] = '.';
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
			
			out += ".\n";
		}

		return "|           HEIGHT : " + height + "\n" +
		       "|  EXPECTED HEIGHT : " + Math.log(width) + "\n" +
		       "|            WIDTH : " + width + "\n" + 
		       "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n" + out;
	}

	public void print()
	{
		SkipNode<T> start = head;
		System.out.println("--------------------------------------------------------------");
		
		while(start != null)
		{
			System.out.print(start.value);
			System.out.print(" --- ");
			SkipNode<T> curr = start.right;
			
			while(!curr.isTail)
			{
				System.out.print(curr.value);
				System.out.print(" --- ");
				curr = curr.right;
			}
			
			System.out.print(curr.value);
			System.out.println();
			
			start = start.down;
		}
		System.out.println("--------------------------------------------------------------");
	}

	/* The randomized propagation upwards */
	private void propagate(SkipNode<T> A, SkipNode<T> B, 
						   SkipNode<T> front, SkipNode<T> end, 
						   SkipNode<T> insert) 
	{
		SkipNode<T> clone, rowBelow;
		Random random = new Random();
		float f = random.nextFloat();
		int flip = Math.round(f);
		boolean newLevel = false;
		
		A.linkRight(insert);
		B.linkLeft(insert);
		rowBelow = insert;
		
		while(flip != 0 && !newLevel)
		{
			clone = insert.clone();
			
			if(A.up != null)
			{ // Move up a row
				A = A.up;
				B = A.right;
				front = front.up;
				end = end.up;
			}
			else if(front.up != null)
			{ // Move up a row (more calculating)
				while(A.up == null)
				{	// Find the closest node to the left that
					// has a copy in the above level
					A = A.left;
				}
				
				A = A.up;
				B = A.right;
				front = front.up;
				end = end.up;
			}
			else // Create new top level
			{
				newLevel = true;
				
				// Make new terminal nodes
				A = new SkipNode<T>("HEAD");
				B = new SkipNode<T>("TAIL");
				// Link them to the current ones
				front.linkUp(A);
				end.linkUp(B);
				// Update current front/end
				front = A;
				end = B;
				// Link 'em
				front.linkRight(end);
			}

			// Insert node at this level
			A.linkRight(clone);
			B.linkLeft(clone);
			clone.linkDown(rowBelow);
			rowBelow = clone;
			
			f = random.nextFloat();
			flip = Math.round(f);		
		}
		
		// Don't forget to update the global head and tail for the list
		while(front.up != null)
		{
			front = front.up;
		}
		head = front;
		tail = head.right;
	}
}