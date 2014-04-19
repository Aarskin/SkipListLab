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
		System.out.println("------Start Insert------");
		foundIt = false;
		
		front = head;
		end = tail;
		A = head;
		B = tail;
		value = new SkipNode<T>(val);
		
		while(true) // Search for the final insertion point
		{
			System.out.println("LOOP");
			
			do // Find the column where it belongs
			{
				System.out.println("A is: " + A.value);
				System.out.println("Value is: " + value.value);
				System.out.println("B is: " + B.value);
				
				if(A.compareTo(value) < 0)
				{					
					if(value.compareTo(B) > 0) 
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
				System.out.println("DROP");
				A = A.down;
				B = A.right;
				front = front.down;
				end = end.down;
			}
			else // Between A and B is where the node will be inserted
			{
				System.out.println("FOUND INSERTION POINT");
				propagate(A, B, front, end, value);
				break;
			}
		}

		System.out.println("------Finish Insert------");
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
		return "";
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
		
		System.out.println("Flip = " + flip);
		while(flip != 0)
		{
			System.out.println("HEADS");
			clone = insert.clone();

			// Insert node at this level
			A_prop.linkRight(clone);
			B_prop.linkLeft(clone);
			clone.linkDown(rowBelow);
			
			System.out.println("After Link");
			System.out.println(A_prop.right);
			System.out.println(insert);
			System.out.println(insert.right);
			System.out.println(B_prop);
			
			if(A_prop.up != null)
			{ // Move up a row
				System.out.println("A up");
				A_prop = A_prop.up;
				B_prop = A_prop.right;
				front_prop = front_prop.up;
				end_prop = end_prop.up;
				rowBelow = clone;
			}
			else if(front_prop.up != null)
			{
				System.out.println("Front up");
				while(A_prop.up == null)
				{	// Find the closest node to the left that
					// has a copy in the above level
					A_prop = A_prop.left;
				}
				
				A_prop = A_prop.up;
				B_prop = A_prop.right;
				front_prop = front_prop.up;
				end_prop = end_prop.up;
				rowBelow = clone;
			}
			else // Create new top level
			{
				System.out.println("New row");
				// Make new terminal nodes
				A_prop = new SkipNode<T>("HEAD");
				B_prop = new SkipNode<T>("TAIL");
				// Link them to the current ones
				front_prop.linkUp(A_prop);
				end_prop.linkUp(B_prop);
				// Update current
				front_prop = front_prop.up;
				end_prop = end_prop.up;
				// Link 'em
				front_prop.linkRight(end_prop);
				// To be sure
				A_prop = front_prop;
				B_prop = end_prop;
				
				rowBelow = clone;
			}
			
			f = random.nextFloat();
			flip = Math.round(f);		
		}
		
		// Don't forget to update the global head and tail for the list
		head = front_prop;
		tail = end_prop;
	}
}