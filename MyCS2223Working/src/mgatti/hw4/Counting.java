package mgatti.hw4;

import mgatti.hw4.AVL;
import edu.princeton.cs.algs4.SeparateChainingHashST;
import edu.princeton.cs.algs4.StdRandom;
import mgatti.hw4.RedBlackBST;

/** Copy to your USERID.hw4 package and make changes. */
public class Counting {

	/**
	 * This skeletal structure needs to be modified to solve this problem. Feel free to add code anywhere...
	 */
	public static void main(String[] args) {
		System.out.println("N\tMaxAVHt\tMaxAVDp\tMaxAVZr\tAVZero%\tMaxRBHt\tMaxRBDp\tMaxRBZr\tRBZero%");
		for (int N = 32; N <= 262144; N*= 2) {
			int maxHeightAVL = 0;
			int maxDepthAVL = 0; 
			int maxZeroAVL = 0; 
			double zeroPercentageAVL = 0.0;



			int maxHeightRB = 0;
			int maxDepthRB = 0; 
			int maxZeroRB = 0; 
			double zeroPercentageRB = 0.0; 

			int NUMTRIAL= 100;
			for (int T = 0; T < NUMTRIAL; T++) {

				// This constructs the array of N-1 values (where N is a power of 2) and 
				// it uses StdRandom.setSeed() to ensure all students will get the same result
				int[] vals = new int[N-1];
				for (int i = 0; i < N-1; i++) {
					vals[i] = i+1;
				}
				StdRandom.setSeed(T);
				StdRandom.shuffle(vals);

				// note: Insert the integers in vals into a new AVL or RedBlack Tree in order from left to right
				AVL<Integer> avl = new AVL<Integer>();
				for (int v: vals)
				{
					avl.insert(v);

				}
				//Finding the maxHeight
				if(avl.height() > maxHeightAVL) {
					maxHeightAVL = avl.height();
				}
				//Finding the maxDepth
				if((avl.height() - avl.minDepth()) > maxDepthAVL){
					maxDepthAVL = avl.height() - avl.minDepth();  
				}
				//Finding the maxZero
				SeparateChainingHashST<Integer,Integer> ht = new SeparateChainingHashST<>();
				ht = avl.counts();
				maxZeroAVL = ht.get(0);

				//Find the AVLZero Percentage
				zeroPercentageAVL = maxZeroAVL;
				zeroPercentageAVL = (zeroPercentageAVL / N)*100;
				zeroPercentageAVL= Math.round(zeroPercentageAVL *1000)/1000.0;

				//  RBTree
				RedBlackBST<Integer, Integer> rBBST = new RedBlackBST<Integer, Integer>();
				for (int v:vals) {
					rBBST.put(v, v);
				}
				//Finding the maxHeight for RB tree
				if (rBBST.height() > maxHeightRB) {
					maxHeightRB = rBBST.height();
				}
				//Finding the maxDepth for  RB tree
				if (rBBST.height() - rBBST.minDepth() > maxDepthRB ) {
					maxDepthRB = rBBST.height() - rBBST.minDepth(); 
				}
				//Finding the maxZero of RB tree 
				SeparateChainingHashST<Integer,Integer> htRB = new SeparateChainingHashST<>();
				htRB = rBBST.counts(); 
				maxZeroRB = htRB.get(0);

				//Finding the RBZero Percentage 
				zeroPercentageRB =  maxZeroRB;
				zeroPercentageRB = (zeroPercentageRB / N)*100; 
				zeroPercentageRB = Math.round(zeroPercentageRB *1000) / 1000.0; 
			}

			System.out.println(N + "\t" + maxHeightAVL +  "\t" + maxDepthAVL +"\t" + maxZeroAVL + "\t" + zeroPercentageAVL + "\t" + maxHeightRB + "\t" +maxDepthRB + "\t" +maxZeroRB+ "\t" + zeroPercentageRB);
		}
	}
}


