/*
*  Amer Hesson
*  University of Toronto
*  September 26, 2012
*
*  Class JMat accepts user input to create a Matrix object that can be 
*  manipulated.
*/

import java.util.*;

public class JMat {

  public static void main (String[] args) {
    int numRows = 0, numCols = 0;
    Scanner scanner = new Scanner(System.in);
    System.out.println("\nThis calculates the reduced row echelon form, determinant, adjoint, and other useful things of an m x n matrix.\n");
    System.out.println("This program is open source - feel free to look at and modify the source code to better suit your needs!\n");
    System.out.println("Note: the inverse of the matrix is just adjA/detA, both of which are provided. Alternatively, the inverse can be found by inputting the original matrix augmented with the identity matrix of the same size\n");
    System.out.println("Author: Amer Hesson\nUniversity of Toronto\n");
    System.out.println("Enter Ctrl+C at any time to exit the program.\n");
    while (true) {
      System.out.println("Enter the number of rows (m): ");
      numRows = scanner.nextInt();
      System.out.println("Enter the number of columns (n): ");
      numCols = scanner.nextInt();
      double[][] matValues = new double[numRows][numCols];
    
      for (int m = 0; m < numRows; m++) {
        System.out.println("Enter row " + (m + 1) + " (separate entries with a space or new line): ");
        for (int n = 0; n < numCols; n++) {
          matValues[m][n] = scanner.nextDouble();
        }
      }
      Matrix matrix = new Matrix(numRows, numCols, matValues);
      Double det = matrix.determinant();
      Matrix trans = matrix.transpose();
      Matrix adj = matrix.adjoint();
      System.out.println("\nThe input matrix is:\n\n" + matrix);
      if (det != null) {
        System.out.println("The determinant of this matrix is: " + det + "\n");
	if (det == 0.0) {
	  System.out.println("This matrix is singular - it is not invertible.\n");
	} else {
	  if (matrix.isIdentity()) {
	    System.out.println("This matrix is the identity matrix I of size " + numRows +".\n");
	  }
	}
      } else {
        System.out.println("This matrix does not have a determinant - it needs to be square.\n");
      }
      System.out.println("\nThe transpose of this matrix is:\n\n" + matrix.transpose());
      System.out.println("The reduced row echelon form of this matrix is:\n\n" + matrix.reduceRowEchelon());
      //System.out.println("The transpose of this matrix is: \n\n" + trans);
      if (adj != null) {
        System.out.println("The adjoint of this matrix is: \n\n" + adj);
        System.out.println("The determinant of the adjoint matrix is: " + Math.pow(det, numRows - 1) + "\n");
      }

      /*
      //Static dot product testing
      //You can also do dot product, however this feature is disabled for now.
      double[][] dotA = {
        {1, 2, 4, 5, 6},
	{2, 4, 6, 6, 1},
	{21, 2, 7, 7, 8},
	{22, 90, 1, 2, 9}
      };

      double[][] dotB = {
        {1,6},
	{2,7},
	{3,8},
	{4,9},
	{5,10}
      };
      Matrix A = new Matrix(dotA.length, dotA[0].length, dotA);
      Matrix B = new Matrix(dotB.length, dotB[0].length, dotB);

      System.out.println(Matrix.dot(A, B));
      */

    }
  }
}
