package mgatti.hw4;

// Copy To USERID.hw4 package. Start changing this file at line 177

import edu.princeton.cs.algs4.SeparateChainingHashST;

/**
 * Minimum implementation of AVL balanced binary tree.
 * 
 * Note this does not offer SymbolTable behavior, but rather just stores Key objects.
 *
 * Worth comparing against Red-Black BST.
 * 
 * @param <Key>
 */
public class AVL<Key extends Comparable<Key>> {

	Node root;               // root of the tree

	class Node {
		Key    key;        
		Node   left, right;  // left and right subtrees
		int    height;       // need to know how TALL this subtree is (from leaf, not root).

		public Node(Key key) {
			this.key = key;
		}

		public String toString() {
			String leftS = "";
			if (left != null) leftS = "left:" + left.key;
			String rightS = "";
			if (right != null) rightS = "right:" + right.key;

			return "[" + key + " "+ leftS + " " + rightS + "]";
		}
	}

	public boolean isEmpty() { return root == null; }

	public String toString() { return "<bst: root=" + root +">"; }

	/**
	 * Determine height difference for a given node by subtracting leftH - rightH
	 */
	int heightDifference(Node node) {
		int leftTarget = 0;
		int rightTarget = 0;
		if (node.left != null) {
			leftTarget = 1 + node.left.height;
		}
		if (node.right != null) {
			rightTarget = 1 + node.right.height;
		}

		return leftTarget - rightTarget;
	}

	/** Update height for node. */
	void computeHeight(Node node) {
		int height = -1;
		if (node.left != null) {
			height = Math.max(height, node.left.height);
		}
		if (node.right != null) {
			height = Math.max(height, node.right.height);
		}

		node.height = height + 1;
	}

	// One-line method for containment. 
	public boolean contains(Key key) { return get(root, key); }
	private boolean get(Node parent, Key key) {
		if (parent == null) return false;

		int cmp = key.compareTo(parent.key);

		if      (cmp < 0) return get(parent.left, key);
		else if (cmp > 0) return get(parent.right, key);
		else              return true;
	}

	/** Invoke put on parent, should it exist. */
	public void insert(Key key) {
		root = insert(root, key);
	}

	private Node insert(Node parent, Key key) {
		if (parent == null) return new Node(key);

		int cmp = key.compareTo(parent.key);
		if (cmp <= 0) {
			parent.left  = insert(parent.left,  key);
			if (heightDifference(parent) == 2) {
				if (key.compareTo(parent.left.key) <= 0) {
					parent = rotateRight(parent);
				} else {
					parent = rotateLeftRight(parent);
				}
			}
		} else {
			parent.right = insert(parent.right, key);
			if (heightDifference(parent) == -2) {
				if (key.compareTo(parent.right.key) > 0) {
					parent = rotateLeft(parent);
				} else {
					parent = rotateRightLeft(parent);
				}
			}
		}

		computeHeight(parent);
		return parent;
	}

	/** Perform right rotation around given node. */
	private Node rotateRight(Node parent) {
		Node newRoot = parent.left;
		Node grandson = newRoot.right;
		parent.left = grandson;
		newRoot.right = parent;

		computeHeight(parent);
		return newRoot;
	}

	/** Perform left rotation around given node. */
	private Node rotateLeft(Node parent) {
		Node newRoot = parent.right;
		Node grandson = newRoot.left;
		parent.right = grandson;
		newRoot.left = parent;

		computeHeight(parent);
		return newRoot;
	}

	/** Perform left, then right rotation around given node. */
	private Node rotateLeftRight(Node parent) {
		Node child = parent.left;
		Node newRoot = child.right;
		Node grand1  = newRoot.left;
		Node grand2  = newRoot.right;
		child.right = grand1;
		parent.left = grand2;

		newRoot.left = child;
		newRoot.right = parent;

		computeHeight(child);
		computeHeight(parent);
		return newRoot;
	}

	/** Perform right, then left rotation around given node. */
	private Node rotateRightLeft(Node parent) {
		Node child = parent.right;
		Node newRoot = child.left;
		Node grand1  = newRoot.left;
		Node grand2  = newRoot.right;
		child.left = grand2;
		parent.right = grand1;

		newRoot.left = parent;
		newRoot.right = child;

		computeHeight(child);
		computeHeight(parent);
		return newRoot;
	}

	/// =================================================================================
	/// NO NEED TO MODIFY ANYTHING ABOVE THIS LINE. ONLY COMPLETE THE METHODS BELOW.....
	/// =================================================================================

	/**
	 * Return a Symbol Table (in this case, SeparateChainingHashST) which records the counts of nodes
	 * in the AVL tree that have a height differential of -1, 0 or 1. This means there will be just
	 * three keys in this symbol table, and the sum of the values associated with these keys will be N.
	 */
	public SeparateChainingHashST<Integer,Integer> counts() {
		SeparateChainingHashST<Integer,Integer> ht = new SeparateChainingHashST<>();
		counts(root, ht);
		return ht;

	}

	/**
	 * Helper method that seeks to update the given symbol table, ht, with the height differentials
	 * in the sub-tree rooted at parent.
	 */
	private void counts(Node parent, SeparateChainingHashST<Integer,Integer> ht) { 
		if (parent == null) {
			return;
		}
		if(ht.get(heightDifference(parent))== null) {
			ht.put(heightDifference(parent), (Integer) 1);
		}

		ht.put(heightDifference(parent), (ht.get(heightDifference(parent))+1));
		if(parent.left != null) {
			counts(parent.left, ht);

		}
		if(parent.right != null) {
			counts(parent.right, ht);
		}
	}


	/**
	 * Public facing API call that returns the smallest depth of a leaf node in the AVL tree.
	 * 
	 * Recall that the depth of the root is 0 (and similarly, the depth of an empty AVL tree is -1).
	 * 
	 * The depth of a leaf node is the number of edges required to traverse to that node from the root.
	 */
	public int minDepth() {
		return minDepth(root, 0, 0);
	}

	/**
	 * Helper method that seeks to find the leaf with minimum depth from the root in the sub-tree rooted
	 * at parent, assuming that the parent node is at depth "currentDepth" and that the lowest depth
	 * so far recorded for a leaf node is "lowestSoFar."
	 */
	Node current = root;
	private int minDepth(Node parent, int currentDepth, int lowestSoFar) {

		if ( this.isEmpty()) {
			throw new RuntimeException("Tree is empty");
		}
		if(parent == null) {
			return currentDepth;
		}

		return Math.min(minDepth(parent.left, currentDepth+1, lowestSoFar+1), minDepth(parent.right, currentDepth+1, lowestSoFar+1));
	}
	/** Public facing API call to return the height of the AVL tree.
	 *  */
	public int height() {
		return height(root);
	}

	/** Helper method to return the height of the subtree rooted at parent. */
	private int height(Node parent) {
		if (this.isEmpty()) {
			throw new RuntimeException("THE TREE IS EMPTY");
		}

		if (parent == null) { 
			return -1;
		}

		return 1 + Math.max(height(parent.left), height(parent.right));
	}

}
