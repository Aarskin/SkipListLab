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
				System.out.println("insertison point found: " + A.value + " >-< " + B.value);
				propagate(A, B, front, end, value);
				print();
				break;
			}
		}
	}
	
	/* Returns the node in the highest row containing the value 'find' */
	public boolean search(T find)
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
						return true;
					}
				}
				else if(A.compareTo(value) == 0)
				{ // I don't think this ever fires, but just in case
					return true;
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
				return false; // It's not here
		} 
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
		
		while(flip != 0)
		{
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

			System.out.println("Instertion after : " + A_prop.value + "(" + A_prop.isTail + ")");
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
			front_prop = front_prop.up;
		}
		head = front_prop;
		tail = head.right;
	}
	
	public String toString()
	{
		String list = "";
		
		return list;
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