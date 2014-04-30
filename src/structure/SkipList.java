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
		System.out.println("Insert: " + val);
		foundIt = false;
		
		// Run to the end of this row
		SkipNode<T> temp = tail;
		while(!temp.isTail)
		{
			System.out.println("Failpoint 9");
			temp = temp.right;
		}
		
		front = head;
		end = temp; // The true end of this row
		A = head;
		B = tail;
		value = new SkipNode<T>(val);
		
		while(true) // Search for the final insertion point
		{			
			System.out.println("Failpoint 5");
			do // Find the column where it belongs
			{			
				System.out.println("Failpoint 12");
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
				System.out.println("Insertion point found: " + A.value + " >-< " + B.value);
				System.out.println("Failpoint 6");
				propagate(A, B, front, end, value);
				System.out.println("Failpoint 7");
				break;
			}
		}
		
		System.out.println("Failpoint 8");
	}
	
	/* Returns the node in the highest row containing the value 'find' */
	//public SkipNode<T> search(T find)
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
			System.out.println("Failpoint 10");
			do // Find the column where it is (for this row)
			{
				System.out.println("Failpoint 11");
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
						//return B;
						return comparisons;
					}
				}
				else if(A.compareTo(value) == 0)
				{ // I don't think this ever fires, but just in case
					//return A;
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
			else
				break; // It's not here
		} 
	
		//return null;
		return 0;
	}
	
	public String toString()
	{
		System.out.println("Begin toString");
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
			
			out += "\n";
		}

		out += "HEIGHT: " + height + "\n";
		out += "WIDTH: " + width + "\n";
		System.out.println("End toString");
		return out;
	}

	/* The randomized propagation upwards */
	private void propagate(SkipNode<T> A_prop, SkipNode<T> B_prop, 
						   SkipNode<T> front_prop, SkipNode<T> end_prop, 
						   SkipNode<T> insert) 
	{
		SkipNode<T> clone, rowBelow;
		Random random = new Random();
		float f = random.nextFloat();
		int flip = Math.round(f);
		
		A_prop.linkRight(insert);
		B_prop.linkLeft(insert);
		rowBelow = insert;
		
		System.out.println("Failpoint 1");
		while(flip != 0)
		{
			System.out.println("Failpoint 2");
			clone = insert.clone();
			
			if(A_prop.up != null)
			{ // Move up a row
				A_prop = A_prop.up;
				B_prop = A_prop.right;
				front_prop = front_prop.up;
				end_prop = end_prop.up;
				System.out.println(A_prop.value);
				System.out.println("Path 1");
			}
			else if(front_prop.up != null)
			{ // Move up a row (more calculating)
				while(A_prop.up == null)
				{	// Find the closest node to the left that
					// has a copy in the above level
					A_prop = A_prop.left;
					System.out.println(A_prop.value);
				}
				
				A_prop = A_prop.up;
				B_prop = A_prop.right;
				front_prop = front_prop.up;
				end_prop = end_prop.up;
				System.out.println("Path 2");
			}
			else // Create new top level
			{
				// Make new terminal nodes
				A_prop = new SkipNode<T>("HEAD");
				B_prop = new SkipNode<T>("TAIL");
				// Link them to the current ones
				front_prop.linkUp(A_prop);
				end_prop.linkUp(B_prop);
				// Update current front/end
				front_prop = A_prop;
				end_prop = B_prop;
				// Link 'em
				front_prop.linkRight(end_prop);
				System.out.println("Path 3");
			}

			System.out.println("Insertion after: " + A_prop.value + " (" + A_prop.isTail + ")");
			// Insert node at this level
			A_prop.linkRight(clone);
			B_prop.linkLeft(clone);
			clone.linkDown(rowBelow);
			rowBelow = clone;
			
			f = random.nextFloat();
			flip = Math.round(f);		
		}
		
		// Don't forget to update the global head and tail for the list
		while(front_prop.up != null)
		{
			System.out.println("Failpoint 3");
			front_prop = front_prop.up;
		}
		head = front_prop;
		tail = head.right;
		System.out.println("Failpoint 4");
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
}