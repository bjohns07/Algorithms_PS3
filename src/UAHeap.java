import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

/*  Student Name:
 *  Username:		ua###		<--- this needs to be correct
 *  Date:			
 *  Class:          CS 3103 - Algorithms
 *  Filename:       UAHeap.java
 *  Description:    Implementation of a priority queue using a heap (by A. Mackey)
 */

public class UAHeap {

    static final int INITIAL_SIZE = 1;		//just to declare the array with the default starting point

    private int[] a;		//This will be used to store the values of the heap
    private int heapSize = 0;

    // Default, no-argument constructor
    public UAHeap() {
       this(INITIAL_SIZE);
    }

    // Construct a UAHeap with the size provided
    public UAHeap(int size) {
    	a = new int[size];
    }

    // this method simply returns the array a, so do not modify this one
    public int[] getArray() {
	    return this.a;
    }


	//The main method should be already configured to run with the appropriate settings.  Do not alter this method.
	public static void main(String[] args) {
		if ( args.length < 3 ) {
			System.out.println("Invalid syntax:   java  UAHeap  inputfile  outputfile  inputsize");
			System.exit(100);
		}

		int inputSize = 0;

		try {
			inputSize = Integer.parseInt(args[2]);
		} catch(Exception ex) {
			System.out.println("Invalid input size");
			System.exit(100);
		}

		UAHeap h = new UAHeap(inputSize);
		System.out.println("Step 1: Loading the array");
		h.loadData(args[0]);

		if (h.getArray().length < 20) {
			System.out.println("Start:  " + Arrays.toString(h.getArray()));
			h.buildMinHeap();
			System.out.println("Heap:   " + Arrays.toString(h.getArray()));
			h.getSortedHeap();
			System.out.println("Sorted:   " + Arrays.toString(h.getArray()));
			System.out.println("\n\n");

			h.saveDataToFile(args[1]);
		} else {
			int[] a1 = h.getArray();
			System.out.print("Start (first 20 only): " );
			for ( int i = 0; i < 20; i++ ) {
				System.out.print( a1[i] + " ");
			}
			System.out.println();


			h.buildMinHeap();
			int[] a2 = h.getArray();
			System.out.print("Heap (first 20 only): " );
			for ( int i = 0; i < 20; i++ ) {
				System.out.print( a2[i] + " ");
			}
			System.out.println();

			System.out.println("Step 2: Running Heapsort");

			System.out.println("\n\n");
			h.getSortedHeap();
			h.saveDataToFile(args[1]);

			int[] a3 = h.getArray();
			System.out.print("Sorted (first 20 only): " );
			for ( int i = 0; i < 20; i++ ) {
				System.out.print( a3[i] + " ");
			}
			System.out.println();
			System.out.print("Sorted (last 20 only): " );
			for ( int i = a3.length-20; i < a3.length; i++ ) {
				System.out.print( a3[i] + " ");
			}
			System.out.println();

			System.out.println("Complete. Saved output to " + args[1]);
		}



		System.out.println();
	}




	// this method should return the height of your heap (you need to calculate it)
    public int getHeight() {
    	return (int)(logBase(3, size()));
    }

    // Retrieves the min value from the heap
    public int getMinValue() {
    	return a[0];
    }

    // Removes and returns the min value from the heap while preserving the heap properties
    public int removeMinValue() {
    	int minValue = Integer.MIN_VALUE;
    	if(a.length > 0) {
    		minValue = a[0];
    		//swap the last value and the root node
    		swapValues(a, 0, size()-1);
    		heapSize--;
    		//heapify the new root to keep heap cohesive
    		heapifyDown(0);
    	} else {
    		System.out.println("No data in heap, returning Integer.MIN_VALUE.");
    	}
    	
    	return minValue;
    }

    // Builds the heap structure by starting from ((int) n/3)
    public void buildMinHeap() {
    	int index = getLastNonLeafIndex();
    	for(int i = index; i >= 0; i--) {
    		heapifyDown(i);
    	}
    }

    // Insert a value into the heap
    public void insertValue(int value) {
    	if(heapSize < a.length) {
    		a[heapSize] = value;
    		heapifyUp(heapSize, 0);
    		heapSize++;
    	}
    }

    // Returns the number of elements within the heap
    public int size() {
    	return heapSize;
    }

    // Reorganizes an element at the given index moving downward (if needed)
    public void heapifyDown(int index) {
    	int leftChildIndex = getLeftChildIndex(index);
    	int centerChildIndex = getCenterChildIndex(index);
    	int rightChildIndex = getRightChildIndex(index);
    	boolean leftInBounds = leftChildIndex < size();
    	boolean centerInBounds = centerChildIndex < size();
    	boolean rightInBounds = rightChildIndex < size();
    	
    	if((leftInBounds && a[index] > a[leftChildIndex]) || (centerInBounds && a[index] > a[centerChildIndex]) || (rightInBounds && a[index] > a[rightChildIndex])) {
    		//swap with smallest value
    		//set smallest to left child or current moving value
    		int smallestChildIndex = getIndexWithSmallestValue(a, index, leftChildIndex);
    		if(centerInBounds) { //if center index is in bounds, check against current smallest
    			smallestChildIndex = getIndexWithSmallestValue(a, smallestChildIndex, centerChildIndex);
    			if(rightInBounds) { //if right index is in bounds, check against current smallest
    				smallestChildIndex = getIndexWithSmallestValue(a, smallestChildIndex, rightChildIndex);
    			}
    		}
    		
    		//smallestChildIndex is now correct
    		if(smallestChildIndex != index) {
	    		//swap
	    		swapValues(a, index, smallestChildIndex);
	    		//smallestChildIndex now points to our moving-value's location in the array, recurse down
	    		heapifyDown(smallestChildIndex);
    		}
    	}
    }

    // Decreases the value at the specified index and moves it upward (if needed).
    public void heapifyUp(int index, int value) {
    	a[index] -= value;
    	int parentIndex = getParentIndex(index);
    	if(a[parentIndex] > a[index] && parentIndex >= 0 && parentIndex != index) {
    		swapValues(a, index, parentIndex);
    		heapifyUp(parentIndex, 0);
    	}
    }

    // Loads data into the heap from a file (line-delimited)
    public void loadData(String filename) {
    	File infile = new File(System.getProperty("user.dir") + "/" + filename);
    	if(infile.exists()) {
    		try {
    			FileReader fr = new FileReader(infile);
    			BufferedReader br = new BufferedReader(fr);
    			String line;
    			int index = 0;
    			while((line = br.readLine()) != null && index < a.length) {
    				try {
    					int lineNum = Integer.parseInt(line);
    					a[index] = lineNum;
    					index++;
    				} catch (NumberFormatException e) {
    					e.printStackTrace();
    				}
    			}
    			heapSize = index;
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    	} else {
    		System.out.println("No file with name " + filename + " exists in running directory " + System.getProperty("user.dir"));
    	}
    }

    // Writes the contents of the heap into the specified file (space-delimited)
    public void saveDataToFile(String filename) {
    	File outfile = new File(System.getProperty("user.dir") + "/" + filename);
    	try {
			FileWriter fr = new FileWriter(outfile);
			BufferedWriter br = new BufferedWriter(fr);
			for(int i = 0; i < size(); i++) {
				br.write(a[i] + " ");
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    // Returns the values from the heap in ascending order (and saves it to the array a)
    public int[] getSortedHeap() {
    	int[] sortedHeap = new int[size()];
    	int heapSizeBackup = size();
    	int index = 0;
    	while(heapSize > 0) {
    		int minValueInHeap = removeMinValue();
    		sortedHeap[index] = minValueInHeap;
    		index++;
    	}
    	a = sortedHeap;
    	heapSize = heapSizeBackup;
    	
    	return sortedHeap;
    }

    /***********************************************************************
     * Location for additional methods needed to complete this problem set.
     ***********************************************************************/

    private int getIndexWithSmallestValue(int[] array, int indexA, int indexB) {
    	return array[indexA] < array[indexB] ? indexA : indexB;
    }
    
    private void swapValues(int[] array, int indexA, int indexB) {
    	int temp = array[indexA];
    	array[indexA] = array[indexB];
    	array[indexB] = temp;
    	
    	return;
    }
    
    private int getLastNonLeafIndex() {
    	return getParentIndex(size()-1);
    }
    
    private int getLayerByIndex(int i) {
    	return (int)logBase(3, i);
    }
    
    private int getLeftChildIndex(int i) {
    	return 3 * i + 1;
    }
    
    private int getCenterChildIndex(int i) {
    	return 3 * i + 2;
    }
    
    private int getRightChildIndex(int i) {
    	return 3 * i + 3;
    }
    
    private int getParentIndex(int childi) {
    	return (int)(childi / 3);
    }
    
    private static double logBase(double base, double x) {
    	return Math.log(x) / Math.log(base);
    }
}
