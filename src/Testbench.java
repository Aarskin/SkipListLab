import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Random;

public class Testbench {
	
	//** BEGIN TEST CONFIGURATION VARIABLES **//
	static int RUNTIME_DATA_SET = 1000000;   // (0, RUNTIME_DATA_SET)
	static int INSERT_COUNT     = 10000;   // How many iterations to run tests
	
	//** GLOBAL VARIABLES **//
	static PrintWriter writer;
	
	//** INSTANTIATE DATA STRUCTURES**//
	static SkipList<Integer> skipList = new SkipList<Integer>();
	static AvlTree avlTree = new AvlTree(0);
	
	//** AVAILABLE TESTS **//
	private enum Test {
		INCREASING_NO_REPEATS, // - Increasing order without repeats
		INCREASING_REPEATS,    // - Increasing order with repeats
		DECREASING_NO_REPEATS, // - Decreasing order without repeats
		DECREASING_REPEATS,    // - Decreasing order with repeats
		CLUSTERED_VALUES,      // - Clustered values
		RANDOM,                // - Random
	}
	
	public static void main(String[] args) {
		try {
			writer = new PrintWriter("run_report" + System.currentTimeMillis() + ".txt", "UTF-8"); {
				System.out.println("Begin Testbench"); {
					for (Test test : Test.values())
						run(test);
				} System.out.println("Finish Testbench");
			} writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	public static void run(Test test){
		System.out.println("Start - " + test.name());
		skipList = new SkipList<Integer>();
		avlTree = new AvlTree(0);
		
		switch (test) {
		case INCREASING_NO_REPEATS:
			increasingOrder(INSERT_COUNT, false, true);
			break;
		case INCREASING_REPEATS: 
			increasingOrder(INSERT_COUNT, true, true);
			break;
		case DECREASING_NO_REPEATS: 
			decreasingOrder(INSERT_COUNT, false, true);
			break;
		case DECREASING_REPEATS: 
			decreasingOrder(INSERT_COUNT, true, true);
			break;
		case CLUSTERED_VALUES: 
			clusteredValues(INSERT_COUNT, true);
			break;
		case RANDOM: 
			randomInsert(INSERT_COUNT, true);
			break;
		}
		
		writer.println("|~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~|");
		writer.println("|             TEST : " + test.name());		
		writer.println("| RUNTIME DATA SET : [0," + RUNTIME_DATA_SET + "]");
		writer.println("|     INSERT COUNT : " + INSERT_COUNT);
		writer.println("|~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~|");
		writer.println("| SKIPLIST DATA");
		writer.println("|    AVERAGE SKIPS : " + (skipList.numSkips() / INSERT_COUNT));
		writer.println("|      TOTAL SKIPS : " + skipList.numSkips());
		writer.println("|    AVERAGE DROPS : " + (skipList.numDrops() / INSERT_COUNT));
		writer.println("|      TOTAL DROPS : " + skipList.numDrops());
		writer.println("|    AVERAGE LOCKS : " + (skipList.numLocks() / INSERT_COUNT));
		writer.println("|      TOTAL LOCKS : " + skipList.numLocks());
		writer.println(skipList.toString());
		writer.println("|~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~|");
		writer.println("| RED-BLACK TREE DATA");
		writer.println("|           HEIGHT : " + avlTree.getHeight(avlTree.getHead()));
		writer.println("|    AVERAGE LOCKS : " + (avlTree.getCount() / INSERT_COUNT));
		writer.println("|      TOTAL LOCKS : " + avlTree.getCount());
		writer.println("|~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~|\n");
		
		System.out.println("Finish - " + test.name());
	}
	
	public static void randomInsert(int count, boolean increased) {
		Random r = new Random();
		
		for (int i = 0; i < count; i++) {
			skipList.insert(r.nextInt(RUNTIME_DATA_SET), increased);
			avlTree.insert(i);
		}
	}
	
	public static void increasingOrder(int count, boolean repeat, boolean increased) {
		Random r = new Random();
		
		if (!repeat)
			for (int i = 0; i < count; i++) {
				skipList.insert(i, increased);
				avlTree.insert(i);
			}
		else {
			for (int i = 0; i < count; i++) {
				int randomRepeat = r.nextInt(10);
				for (int j = 0; j < randomRepeat; j++) {
					skipList.insert(i, increased);
					avlTree.insert(i);
				}
				
				i += randomRepeat;
			}
		}
	}
	
	public static void decreasingOrder(int count, boolean repeat, boolean increased) {
		Random r = new Random();
		
		if (!repeat)
			for (int i = count; i > 0; i--) {
				skipList.insert(i, increased);
				avlTree.insert(i);
			}
		else {
			for (int i = count; i > 0; i--) {
				int randomRepeat = r.nextInt(10);
				for (int j = 0; j < randomRepeat; j++) {
					skipList.insert(i, increased);
					avlTree.insert(i);
				}
				
				i -= randomRepeat;
			}
		}
	}
	
	public static void clusteredValues(int count, boolean increased) {
		Random r = new Random();
		int clusterVal;
		
		for (int i = 0; i < count; i++) {
			clusterVal = r.nextInt(RUNTIME_DATA_SET);
			
			for (int j = 0; j < r.nextInt(count / 50); j++) {
				skipList.insert(clusterVal, increased);
				avlTree.insert(clusterVal);
				i ++;
			}
		}
	}

}