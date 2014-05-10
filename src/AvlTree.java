public class AvlTree {
	private AvlNode head, curr;
	private int count = 0;
	private boolean DEBUG = false;
	public AvlTree(int value) {
		head = new AvlNode(value);
	}

	public AvlNode getHead() {
		return head;
	}

	/* This function inserts the desired value into the AVL tree and then returns the new root of the tree
	 * The root of the tree only changes due to rotations when re-balancing the tree
	 * */
	public synchronized AvlNode insert(int value) {
		curr = head;
		head = insertHelper(curr, value);
		return head;
	}

	private synchronized AvlNode insertHelper(AvlNode curr, int value) {
		if (curr == null) {
			curr = new AvlNode(value);
		} else if (value == curr.getValue()) {
			return curr; //node already exists in tree
		} else if (value < curr.getValue()) {
			curr.setLeft(insertHelper(curr.getLeft(), value));
			curr.getLeft().setParent(curr);
		} else if (value > curr.getValue()) {
			curr.setRight(insertHelper(curr.getRight(), value));
			curr.getRight().setParent(curr);
		}

		getHead().setLock(); //obtain the lock of the tree when rebalancing
		count++;
		
		
		if (getBalanceFactor(curr) >= 2) {
			if (DEBUG)
				System.out.println("right BF imbalanced");

			if (getBalanceFactor(curr.getRight()) >= 1) {
				curr = singleLeftRotation(curr);
			} else {
				curr = rightLeftRotation(curr);
			}
		} else if (getBalanceFactor(curr) <= -2) {
			if (DEBUG)
				System.out.println("left BF imbalanced");

			if (getBalanceFactor(curr.getLeft()) <= -1) {
				curr = singleRightRotation(curr);
			} else {
				curr = leftRightRotation(curr);
			}
		}
		curr.setHeight(getMax(curr.getLeft(), curr.getRight()) + 1); //update height of node as it traverses tree
		getHead().unLock(); //Free up the lock so other threads can use the tree

		return curr; //The newly added node
	}

	public int getHeight(AvlNode curr) {
		if (curr == null) {
			return 0;
		} else {
			getHeight(curr.getLeft());
			getHeight(curr.getRight());
			return getMax(curr.getLeft(), curr.getRight()) + 1;
		}
	}
	public int getCount()
	{
		return count;
	}
	
	private int getMax(AvlNode left, AvlNode right) {
		if (left == null && right == null) {
			return 0;
		} else if (left == null) {
			return right.getHeight();
		} else if (right == null) {
			return left.getHeight();
		}
		if (left.getHeight() > right.getHeight()) {
			return left.getHeight();
		} else {
			return right.getHeight();
		}
	}

	public int getBalanceFactor(AvlNode curr) {
		if (curr.getLeft() == null) {
			if (curr.getRight() != null) {
				return curr.getRight().getHeight();
			} else {
				return 0;
			}
		} else if (curr.getRight() == null) {
			if (curr.getLeft() != null) {
				return -1 * curr.getLeft().getHeight();
			} else {
				return 0;
			}
		} else {
			return curr.getRight().getHeight() - curr.getLeft().getHeight();
		}
	}

	private AvlNode singleLeftRotation(AvlNode curr) {
		AvlNode temp = curr.getRight();
		temp.setParent(curr.getParent());

		curr.setRight(temp.getLeft());
		temp.setLeft(curr);

		if (curr.getRight() != null) {
			curr.getRight().setParent(curr);
		}
		curr.setParent(temp);

		temp.getLeft().setHeight(getMax(temp.getLeft().getLeft(), temp.getLeft().getRight()) + 1);
		return temp;
	}

	private AvlNode leftRightRotation(AvlNode curr) {
		curr.setLeft(singleLeftRotation(curr.getLeft()));
		curr = singleRightRotation(curr);
		return curr;
	}

	private AvlNode singleRightRotation(AvlNode curr) {
		AvlNode temp = curr.getLeft();

		temp.setParent(curr.getParent());

		curr.setLeft(temp.getRight());
		temp.setRight(curr);

		if (curr.getLeft() != null) {
			curr.getLeft().setParent(curr);
		}
		curr.setParent(temp);

		temp.getRight().setHeight(getMax(temp.getRight().getLeft(), temp.getRight().getRight()) + 1);
		return temp;
	}

	private AvlNode rightLeftRotation(AvlNode curr) {
		curr.setRight(singleRightRotation(curr.getRight()));
		curr = singleLeftRotation(curr);
		return curr;
	}

	public void inorderPrint() {
		curr = head;
		if (curr != null) {
			inorderHelper(curr);
		}
	}

	private void inorderHelper(AvlNode curr) {
		if (curr != null) {
			inorderHelper(curr.getLeft());
			System.out.println(curr.getValue());
			inorderHelper(curr.getRight());
		}

	}

	public AvlNode findValue(int value) {
		curr = head;
		while (curr != null) {
			if (value == curr.getValue())
				return curr;
			else if (value < curr.getValue())
				curr = curr.getLeft();
			else if (value > curr.getValue())
				curr = curr.getRight();
		}
		return null; //Reached bottom of tree and didn't find value
	}

	public boolean contains(int value) {
		if (findValue(value) != null)
			return true;
		else
			return false;
	}
}