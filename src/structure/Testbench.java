package structure;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Random;

public class Testbench {
	
	//** BEGIN TEST CONFIGURATION VARIABLES **//
	static int RUNTIME_DATA_SET = 1000000;   // (0, RUNTIME_DATA_SET)
	static int INSERT_COUNT     = 10000;   // How many iterations to run tests
	//** END TEST CONFIGURATION VARIABLES **//
	
	//** INSTANTIATE SKIPLIST **//
	static SkipList<Integer> skipList = new SkipList<Integer>();
	
	//** AVAILABLE TESTS **//
	//INSERT
	// - Increasing order without repeats
	// - Increasing order with repeats
	// - Decreasing order without repeats
	// - Decreasing order with repeats
	// - Clustered values
	// - Random
	//
	//SEARCH
	// - Get number of comparisons used in search
	//     for ~every~ number in data set
	// 
	
	public static void main(String[] args) {
		PrintWriter writer;
		try {
			writer = new PrintWriter("run_report" + System.currentTimeMillis() + ".txt", "UTF-8");
			
			System.out.println("Begin Testbench");
			
			System.out.println("Start - randomInsert");
			randomInsert(INSERT_COUNT);
			writer.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			writer.println("|             TEST : randomInsert");
			writer.println("| RUNTIME DATA SET : [0," + RUNTIME_DATA_SET + "]");
			writer.println("|     INSERT COUNT : " + INSERT_COUNT);
			writer.println("| AVG. COMPARISONS : " + searchComparisons());
			writer.println(skipList.toString());
			System.out.println("Finish - randomInsert");

			System.out.println("Start -  clusteredValues");
			skipList = new SkipList<Integer>();
			clusteredValues(INSERT_COUNT);
			writer.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			writer.println("|             TEST : clusteredValues");
			writer.println("| RUNTIME DATA SET : [0," + RUNTIME_DATA_SET + "]");
			writer.println("|     INSERT COUNT : " + INSERT_COUNT);
			writer.println("| AVG. COMPARISONS : " + searchComparisons());
			writer.println(skipList.toString());
			System.out.println("Finish -  clusteredValues");

			System.out.println("Start - increasingOrder (no repeats)");
			skipList = new SkipList<Integer>();
			increasingOrder(INSERT_COUNT, false);
			writer.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			writer.println("|             TEST : increasingOrder (no repeats)");
			writer.println("| RUNTIME DATA SET : [0," + RUNTIME_DATA_SET + "]");
			writer.println("|     INSERT COUNT : " + INSERT_COUNT);
			writer.println("| AVG. COMPARISONS : " + searchComparisons());
			System.out.println("Failpoint 13");
			writer.println(skipList.toString());
			System.out.println("Finish - increasingOrder (no repeats)");

			System.out.println("Start - increasingOrder (repeats)");
			skipList = new SkipList<Integer>();
			increasingOrder(INSERT_COUNT, true);
			writer.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			writer.println("|             TEST : increasingOrder (repeats)");
			writer.println("| RUNTIME DATA SET : [0," + RUNTIME_DATA_SET + "]");
			writer.println("|     INSERT COUNT : " + INSERT_COUNT);
			writer.println("| AVG. COMPARISONS : " + searchComparisons());
			writer.println(skipList.toString());
			System.out.println("Finish - increasingOrder (repeats)");

			System.out.println("Start - decreasingOrder (no repeats)");
			skipList = new SkipList<Integer>();
			decreasingOrder(INSERT_COUNT, false);
			writer.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			writer.println("|             TEST : decreasingOrder (no repeats)");
			writer.println("| RUNTIME DATA SET : [0," + RUNTIME_DATA_SET + "]");
			writer.println("|     INSERT COUNT : " + INSERT_COUNT);
			writer.println("| AVG. COMPARISONS : " + searchComparisons());
			writer.println(skipList.toString());
			System.out.println("Finish - decreasingOrder (no repeats)");

			System.out.println("Start - decreasingOrder (repeats)");
			skipList = new SkipList<Integer>();
			decreasingOrder(INSERT_COUNT, true);
			writer.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			writer.println("|             TEST : decreasingOrder (repeats)");
			writer.println("| RUNTIME DATA SET : [0," + RUNTIME_DATA_SET + "]");
			writer.println("|     INSERT COUNT : " + INSERT_COUNT);
			writer.println("| AVG. COMPARISONS : " + searchComparisons());
			writer.println(skipList.toString());
			System.out.println("Finish - decreasingOrder (repeats)");
			
			System.out.println("Finish Testbench");
			
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void randomInsert(int count) {
		Random r = new Random();
		
		for (int i = 0; i < count; i++)
			skipList.insert(r.nextInt(RUNTIME_DATA_SET));
	}
	
	public static void increasingOrder(int count, boolean repeat) {
		Random r = new Random();
		
		if (!repeat)
			for (int i = 0; i < count; i++)
				skipList.insert(i);
		else {
			int biggest = 0, t;
			for (int i = 0; i < count; i++) {
				do {
					t = r.nextInt(RUNTIME_DATA_SET);
				} while (t < biggest);
				
				biggest = t;
				skipList.insert(t);
			}
		}
	}
	
	public static void decreasingOrder(int count, boolean repeat) {
		Random r = new Random();
		
		if (!repeat)
			for (int i = count; i > 0; i--)
				skipList.insert(i);
		else {
			int smallest = RUNTIME_DATA_SET, t;
			for (int i = count; i > 0; i--) {
				do {
					t = r.nextInt(RUNTIME_DATA_SET);
				} while (t > smallest);
				
				smallest = t;
				skipList.insert(t);
			}
		}
	}
	
	public static void clusteredValues(int count) {
		Random r = new Random();
		int clusterVal;
		
		for (int i = 0; i < count; i++) {
			clusterVal = r.nextInt(RUNTIME_DATA_SET);
			
			for (int j = 0; j < r.nextInt(count / 50); j++) {
				skipList.insert(clusterVal);
				i ++;
			}
		}
	}
	
	public static int searchComparisons() {
		ArrayList<Integer> searchTimes = new ArrayList<Integer>();
		for (int i = 0; i < RUNTIME_DATA_SET; i++) {
			searchTimes.add(skipList.search(i));
		}
		
		int sum = 0;
		for (int time : searchTimes) {
			sum += time;
		}
		
		return (sum / searchTimes.size());
	}

}