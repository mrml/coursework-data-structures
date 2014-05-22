/*
 * 	CM2303 Algorithms & Data Structures Coursework
 * 	Author: Matthew Lloyd
 * 	Lecturer: Dr. P. L. Rosin
 * 	
 * 	Sparse.java
 */
import java.util.Random;

public class Sparse {

	private static int size, seed, limit;
	private static int[] sparse1D;

	public static ArrayStack arrayStack = new ArrayStack();


    public void printValue(int value){
    // This function enables each value to be output as a suitable on screen value
        if(value < 10) System.out.print(" "+value+"  ");
        else System.out.print(value+"  ");
    }

    public int[][] generateArray(int seed, int size) {
    // Function to generate the array, given the command-line arguments 
        Random rand = new Random(seed);
        int[][] matrix2d = new int[size][size];
        System.out.println("initial 2D non-sparse matrix:");
        for(int y = 0; y < size; y++) {
            for(int x = 0; x < size; x++) {
                matrix2d[x][y] = rand.nextInt(100);
                printValue(matrix2d[x][y]);
            }
            System.out.println(""); 
        }
        System.out.println(""); 

        return matrix2d;
    }

    public int[] sparseConvert(int[][] matrix2d, int limit) {
    // Function to convert the inputted 2D matrix to a sparse matrix given the limit argument
		Sparse sparse = new Sparse();
		ArrayStack arrayStack = new ArrayStack();
        int nonZeroCount = 0;
        int sizeMatrix = matrix2d.length;
        System.out.println("initial 2D sparse matrix:");

        for(int y = 0; y < sizeMatrix; y++) {
            for(int x = 0; x < sizeMatrix; x++) {
                if(matrix2d[x][y] > limit | matrix2d[x][y]==0) {
                    matrix2d[x][y] = 0;
                }
                else{
                    nonZeroCount += 1;
                }
                printValue(matrix2d[x][y]);
            }
            System.out.println(""); 
        }
        System.out.println(""); 


        int[] sparseMatrix = new int[nonZeroCount*2];
        int index = 0;

        for(int y = 0; y < sizeMatrix; y++){
            for(int x = 0; x < sizeMatrix; x++){
                if(matrix2d[x][y] > 0){
                    sparseMatrix[index*2] = x + (y*sizeMatrix);
                    sparseMatrix[(index*2)+1] = matrix2d[x][y];
                    index++;
                }
            }
        }
        
        System.out.print("sequence: ");
        for(int i = 0; i < index; i++){
            int position = i*2;
            int value = position + 1;
            System.out.print(sparseMatrix[position] + " " + sparseMatrix[value] + "; ");
        }
        System.out.println();
        sparse.arrayStack.print();
        System.out.println();

        return sparseMatrix;
    }


	public void reset(int x, int y, int value) {
		Sparse sparse = new Sparse();
		int[] temp = sparse.sparse1D;
        int position = xyToPos(x,y);
        int oldValue = 0;
		int posFound = -1;
		int counter = 0;
	
		if(value == 0){
			for(int i = 0; i < sparse.sparse1D.length-1; i += 2){
				if(sparse.sparse1D[i] == position){
					posFound = i;
				}
			}
			if(posFound > -1){
				oldValue = temp[posFound+1];
				sparse.sparse1D = new int[sparse.sparse1D.length-2];
				for(int k = 0; k < temp.length; k += 2){				
					if(k != posFound && counter < sparse.sparse1D.length-1){
						sparse.sparse1D[counter] = temp[k];
						sparse.sparse1D[counter + 1] = temp[k + 1];
						counter += 2;
					}			
				}
			}
		}
		else{
			if(sparse.sparse1D.length == null){
            // catch for java Sparse 0 0 n arguements
				sparse.sparse1D = new int[2];
				posFound = 0;
				counter += 2;
				sparse.sparse1D[0] = position;
				sparse.sparse1D[1] = value;
			}
			else{
				for(int i = 0; i < sparse.sparse1D.length-1; i += 2){
					if(sparse.sparse1D[i] == position){
						posFound = i;
					}
				}
				if(posFound > -1){
					for(int k = 0; k < sparse.sparse1D.length; k += 2){				
						if(k == posFound){
							oldValue = temp[posFound+1];
							sparse.sparse1D[k+1] = value;
						}			
					}
				}
				else{
					sparse.sparse1D = new int[sparse.sparse1D.length+2];
					boolean posInserted = false;
					for(int j = 0; j < temp.length-1; j += 2){
						if(position < temp[j] && !posInserted){
							sparse.sparse1D[counter] = position;
							sparse.sparse1D[counter+1] = value;
							counter += 2;
							posInserted = true;
						}
						else if(position > temp[j] && !posInserted){
							sparse.sparse1D[counter] = temp[j];
							sparse.sparse1D[counter+1] = temp[j+1];
							counter += 2;
						}
						if(posInserted){
							sparse.sparse1D[counter] = temp[j];
							sparse.sparse1D[counter+1] = temp[j+1];
							counter += 2;
						}
						if(!posInserted && j == temp.length-2){
							sparse.sparse1D[counter] = position;
							sparse.sparse1D[counter+1] = value;
						}
					}
				}
			}
		}
		System.out.println("reset ("+x+","+y+") to "+value);
		sparse.printSequence();
		sparse.arrayStack.push(position);
		sparse.arrayStack.push(oldValue);
		System.out.println();
		sparse.arrayStack.print();
		System.out.println();
	}

	public void undo() {
		Sparse sparse = new Sparse();
		ArrayStack arrayStack = new ArrayStack();
		int value = (Integer) sparse.arrayStack.pop();
		int position = (Integer) sparse.arrayStack.pop();
		int x = PosToXy(position)[0];
		int y = PosToXy(position)[1];
		int posFound = -1;
		int counter = 0;
		int[] temp = sparse.sparse1D;


		if(value == 0){
			for(int i = 0; i < sparse.sparse1D.length-1; i += 2){
				if(sparse.sparse1D[i] == position){
					posFound = i;
				}
			}
			if(posFound > -1){
				sparse.sparse1D = new int[sparse.sparse1D.length-2];
				for(int k = 0; k < temp.length; k += 2){				
					if(k != posFound && counter < sparse.sparse1D.length-1){
						sparse.sparse1D[counter] = temp[k];
						sparse.sparse1D[counter + 1] = temp[k + 1];
						counter += 2;
					}			
				}
			}
		}
		else{
			for(int i = 0; i < sparse.sparse1D.length-1; i += 2){
				if(sparse.sparse1D[i] == position){
					posFound = i;
				}
			}
			if(posFound > -1){
				for(int k = 0; k < sparse.sparse1D.length; k += 2){				
					if(k == posFound){
						sparse.sparse1D[k+1] = value;
					}			
				}
			}
			else{
				sparse.sparse1D = new int[sparse.sparse1D.length+2];
				boolean posInserted = false;
				for(int j = 0; j < temp.length-1; j += 2){
					if(position < temp[j] && !posInserted){
						sparse.sparse1D[counter] = position;
						sparse.sparse1D[counter+1] = value;
						counter += 2;
						posInserted = true;
					}
					else if(position > temp[j] && !posInserted){
						sparse.sparse1D[counter] = temp[j];
						sparse.sparse1D[counter+1] = temp[j+1];
						counter += 2;
					}
					if(posInserted){
						sparse.sparse1D[counter] = temp[j];
						sparse.sparse1D[counter+1] = temp[j+1];
						counter += 2;
					}
					if(!posInserted && j == temp.length-2){
						sparse.sparse1D[counter] = position;
						sparse.sparse1D[counter+1] = value;
					}
				}
			}
		}
		System.out.println("undo ("+x+","+y+") to "+value);
		sparse.printSequence();
		System.out.println();
		sparse.arrayStack.print();
		System.out.println();
	}

	public int xyToPos(int x, int y){			// Function to easily convert from x, y coordinates to a position value
		Sparse sparse = new Sparse();
		int p = x + (y * sparse.size);
		return p;
	}

	public int[] PosToXy(int position){			// Function to easily convert from a position value to x, y coordinates
		Sparse sparse = new Sparse();
		int[] coord = new int[2];
		int x = position % sparse.size;
		int y = position / sparse.size;
		coord[0] = x;
		coord[1] = y;
		return coord;
	}


	public void printfullMatrix() {
		Sparse sparse = new Sparse();

		System.out.println("reconstructed 2D sparse matrix:");
		int index = 0;
		for(int py = 0; py < sparse.size; py++){
			for(int px = 0; px < sparse.size; px++){
				if(index>sparse.sparse1D.length-1){
					printValue(0);
				}
				else if(sparse.sparse1D[index] == xyToPos(px, py)){
					printValue(sparse.sparse1D[index+1]);
					index += 2;
				}
				else{
					printValue(0);
				}
			}
			System.out.println();
		}
		System.out.println();
	}

	public void printSequence() {
    // function to print the current 1D array sequence
		Sparse sparse = new Sparse();
		int index = sparse.sparse1D.length;
        System.out.print("sequence: ");
        for(int i = 0; i < index-1; i += 2){
            System.out.print(sparse.sparse1D[i] + " " + sparse.sparse1D[i+1] + "; ");
        }
	}

	public void transposeSequence() {
    // Function that transposes the matrix in O(n) [non nested for loops]
		Sparse sparse = new Sparse();
		ArrayStack arrayStack = new ArrayStack();
		int[] temp = sparse.sparse1D;
		sparse.sparse1D = new int[sparse.sparse1D.length];
		System.out.println("transposing");
		System.out.print("cumulative column sizes: ");
		int[] colCount = new int[sparse.size];
		int[] culCol = new int[sparse.size];
		int counter = 0;		

		for(int i = 0; i < temp.length-1; i += 2){
			int xPos = PosToXy(temp[i])[0];
			colCount[xPos]++;
		}
		for(int j = 0; j < colCount.length; j++){
			counter += colCount[j];
			culCol[j] = counter;
			System.out.print(culCol[j]+" ");
		}
		for(int k = 0; k < temp.length-1; k+=2){
			int xPos = PosToXy(temp[k])[0];
			int yPos = PosToXy(temp[k])[1];
			int newPosition = xyToPos(yPos,xPos);
			int insertPosition = culCol[xPos] - colCount[xPos];
			colCount[xPos]--;
			sparse.sparse1D[(insertPosition*2)] = newPosition;
			sparse.sparse1D[(insertPosition*2)+1] = temp[k+1];
		} 
		System.out.println();
		sparse.printSequence();
		System.out.println();
		
		while(!sparse.arrayStack.isEmpty()){
			sparse.arrayStack.pop();
		}
		
		sparse.arrayStack.print();
		System.out.println();
	}

	public static void main(String[] args) {

        Sparse sparse = new Sparse();
        sparse.seed = Integer.parseInt(args[0]);
        sparse.limit = Integer.parseInt(args[1]);
        sparse.size = Integer.parseInt(args[2]);

      	int[][] create2D = sparse.generateArray(sparse.seed, sparse.size);
        sparse.sparse1D = sparse.sparseConvert(create2D, sparse.limit);

        // Tests required for the coursework
		sparse.reset(4,0,99);
        sparse.printfullMatrix();
        sparse.reset(2,0,35);
        sparse.reset(2,1,77);
        sparse.undo();
        sparse.printfullMatrix();
        sparse.reset(2,1,0);
        sparse.reset(0,2,22);
        sparse.undo();
        sparse.reset(5,2,55);
        sparse.reset(3,3,63);
        sparse.undo();
        sparse.printfullMatrix();
        sparse.transposeSequence();
        sparse.printfullMatrix();

    }

}
