package structure;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Random;

public class Testbench {
	
	//** BEGIN TEST CONFIGURATION VARIABLES **//
	static int RUNTIME_DATA_SET = 1000;   // (0, RUNTIME_DATA_SET)
	static int INSERT_COUNT     = 10;   // How many iterations to run tests
	//** END TEST CONFIGURATION VARIABLES **//
	
	//** INSTANTIATE SKIPLIST **//
	static SkipList<Integer> skipList = new SkipList<Integer>();
	
	//** AVAILABLE TESTS **//
	// Insert 1..1000 into skiplist in variety of ways
	// - Increasing order without repeats
	// - Increasing order with repeats
	// - Clustered values between 1..1000
	// - Much larger data set
	// - Random inserts
	//
	
	public static void main(String[] args) {
		PrintWriter writer;
		try {
			writer = new PrintWriter("run_report" + System.currentTimeMillis() + ".txt", "UTF-8");

			System.out.println("Running randomInsert");
			randomInsert(INSERT_COUNT);
			writer.println("TEST: randomInsert");
			writer.println("RUNTIME DATA SET: [0," + RUNTIME_DATA_SET + "]");
			writer.println("INSERT COUNT: " + INSERT_COUNT);
			writer.println(skipList.print());
			writer.println();

			System.out.println("Running clusteredValues");
			skipList = new SkipList<Integer>();
			clusteredValues(INSERT_COUNT);
			writer.println("TEST: clusteredValues");
			writer.println("RUNTIME DATA SET: [0," + RUNTIME_DATA_SET + "]");
			writer.println("INSERT COUNT: " + INSERT_COUNT);
			writer.println(skipList.print());
			writer.println();

			System.out.println("Running increasingOrder (no repeats)");
			skipList = new SkipList<Integer>();
			increasingOrder(INSERT_COUNT, false);
			writer.println("TEST: increasingOrder (no repeats)");
			writer.println("RUNTIME DATA SET: [0," + RUNTIME_DATA_SET + "]");
			writer.println("INSERT COUNT: " + INSERT_COUNT);
			writer.println(skipList.print());
			writer.println();		

			System.out.println("Running increasingOrder (repeats)");
			skipList = new SkipList<Integer>();
			increasingOrder(INSERT_COUNT, true);
			writer.println("TEST: increasingOrder (repeats)");
			writer.println("RUNTIME DATA SET: [0," + RUNTIME_DATA_SET + "]");
			writer.println("INSERT COUNT: " + INSERT_COUNT);
			writer.println(skipList.print());
			writer.println();
			
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
		
		if (repeat)
			for (int i = 0; i < count; i++)
				skipList.insert(i);
		else {
			int biggest = 0, t;
			for (int i = 0; i < count; i++) {
				do {
					t = r.nextInt(RUNTIME_DATA_SET);
				}
				while (t >= biggest);
				
				biggest = t;
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

}
