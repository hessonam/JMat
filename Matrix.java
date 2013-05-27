/*
*  Amer Hesson
*  University of Toronto
*
*  Sept. 26, 2012
*  
*  Class Matrix creates a 2 dimensional array of values that can be manipulated
*  in useful ways, including elementary row operations and transforming the
*  matrix to reduced row echelon form.
*/

import java.text.DecimalFormat;
import java.awt.Point;
import java.util.*;

public class Matrix {
  
  private int numRows;
  private int numCols;
  private int rrefPivotRow;
  private double[][] entries;

  public Matrix(int numRows, int numCols, double[][] entries) {
    this.numRows = numRows;
    this.numCols = numCols;
    this.entries = new double[numRows][numCols];
    for (int m = 0; m < numRows; m++) {
      for (int n = 0; n < numCols; n++) {
        this.entries[m][n] = entries[m][n];
      }
    }
    this.rrefPivotRow = -1;
  }

  public Matrix(Matrix other) {
    this.numRows = other.numRows;
    this.numCols = other.numCols;
    this.entries = new double[numRows][numCols];
    for (int m = 0; m < numRows; m++) {
      for (int n = 0; n < numCols; n++) {
        this.entries[m][n] = other.entries[m][n];
      }
    }
    this.rrefPivotRow = -1;
  }

  public Matrix reduceRowEchelon() {
    if (this.isReduced()) {
      return this;
    } else {
      adjustEntries();
      //System.out.println(this);
      Point pivot = this.getPivot();
      if (pivot != null) {
        int pivotRow = (int)pivot.getX();
        int pivotCol = (int)pivot.getY();
        
        double pivotEntry = entries[pivotRow][pivotCol];
        if (pivotRow != 0 && this.isZeroRow(pivotRow - 1)) {
          this.exchange(pivotRow, pivotRow - 1);
        }
        this.multiplyRow(pivotRow, 1/pivotEntry);
        //System.out.println(this);
        for (int m = 0; m < numRows; m++) {
          if (m != pivotRow) {
            this.addMultiple(m, pivotRow, -entries[m][pivotCol]);
            //System.out.println(this);
          }
        }
      } else {
        return this;
      }
    }
    return this.reduceRowEchelon();
  }

  public boolean isReduced() {
    double[] firstNonZeroEntry = new double[numRows];
    int[] firstNonZeroEntryCol = new int[numRows];
    for (int m = 0; m < numRows; m++) {
      firstNonZeroEntry[m] = 0;
    }

    for (int m = 0; m < numRows; m++) {
      for (int n = 0; n < numCols; n++) {
        if (entries[m][n] != 0) {
	  firstNonZeroEntry[m] = entries[m][n];
	  firstNonZeroEntryCol[m] = n;
          break;
	}
      }
    }
    
    //Check that all zero rows are at the end, and all leading ones are to the right of the leading ones in the rows above it.
    boolean isAfterZeroRow = false;
    int prevLeadingOneEntryCol = -1;

    for (int m = 0; m < numRows; m++) {
      int curNonZeroEntryCol = firstNonZeroEntryCol[m];
      if (firstNonZeroEntry[m] == 0) {
        isAfterZeroRow = true;
      } else if (firstNonZeroEntry[m] != 1 || isAfterZeroRow || prevLeadingOneEntryCol >= curNonZeroEntryCol) {
        return false;
      }
      
      //Check that non-zero entry is the only such entry in its column.
      if (firstNonZeroEntry[m] == 1) {
        for (int r = 0; r < numRows; r++) {
          if (r != m && entries[r][curNonZeroEntryCol] != 0) {
              return false;
	  }
	}
	prevLeadingOneEntryCol = firstNonZeroEntryCol[m];
      }
    }
    return true;
  }
  
  private boolean isValidPivot(int row, int col) {
    boolean isReducedCol = isReducedCol(col);
    if (entries[row][col] == 0) {
      return false;
    }
    if (isReducedCol) {
      reorderReducedCol(col);
      rrefPivotRow = row;
      return false;
    }
    //if (entries[row][col] == 0) {
    //  return false;
    //}
    for (int n = 0; n < col; n++) {
      if (isReducedCol(n) && entries[row][n] == 1) {
        return false;
      }
    }
    
    if (rrefPivotRow + 1 < numRows) {
      this.exchange(row, rrefPivotRow + 1);
    }
    rrefPivotRow = row;
    return true;
  }

  private boolean isReducedCol(int col) {
    boolean existsOneInCol = false;
    boolean isReducedCol = false;
    
    for (int m = 0; m < numRows; m++) {
      double curEntry = entries[m][col];
      if (curEntry == 1 && existsOneInCol) {
        return false;
      } else if (curEntry == 1) {
        existsOneInCol = true;
      } else if (curEntry != 0) {
        return false;
      }
    }
    return true;
  }

  private boolean isZeroRow(int row) {
    for (int n = 0; n < numCols; n++) {
      if (entries[row][n] != 0) {
        return false;
      }
    }
    return true;
  }
  
  private Point getPivot() {
    for (int n = 0; n < numCols; n++) {
      for (int m = 0; m < numRows; m++) {
        if (isValidPivot(m, n)) {
          double curEntry = entries[m][n];
	  if (curEntry != 0) {
            return new Point(m, n);
	  }
        }
      }
    }
    return null;
  }

  private void reorderReducedCol(int col) {
    int oneRowNum = -1;
    for (int m = 0; m < numRows; m++) {
      if (entries[m][col] == 1) {
        oneRowNum = m;
      }
    }
    for (int m = 0; m < numRows; m++) {
      for (int n = 0; n < numCols; n++) {
        if (entries[m][n] == 1 && isReducedCol(n)) {
	  if (n > col && m < oneRowNum) {
	    this.exchange(m, oneRowNum);
	  }
	}
      }
    }
  }

  private void exchange(int row1, int row2) {
    double[] bufferRow = new double[numCols];
    for (int n = 0; n < numCols; n++) {
      bufferRow[n] = entries[row1][n];
      entries[row1][n] = entries[row2][n];
      entries[row2][n] = bufferRow[n];
    }
  }

  private void multiplyRow(int row, double factor) {
    for (int n = 0; n < numCols; n++) {
      entries[row][n] *= factor;
    }
  }

  //row1 = row1 + factor*row2
  private void addMultiple(int row1, int row2, double factor) {
    for (int n = 0; n < numCols; n++) {
      entries[row1][n] += factor * entries[row2][n];
    }
  }

  private void adjustEntries() {
   for (int m = 0; m < numRows; m++) {
     for (int n = 0; n < numCols; n++) {
       if (Math.abs(entries[m][n]) <= 0.00001) {
         entries[m][n] = 0;
       }
     }
   }
  }

  public String toString() {
    String strMatrix = "";
    DecimalFormat formatter = new DecimalFormat("#.##");    
    for (int m = 0; m < numRows; m++) {
      strMatrix += "   ";
      for (int n = 0; n < numCols; n++) {
        if (entries[m][n] == 0) {
          entries[m][n] = 0;
        }
        
        String strEntry = formatter.format(entries[m][n]);
        int strEntryLength = strEntry.length();
        for (int x = 0; x < 4 - strEntryLength; x++) {
          strEntry += " ";
        }
        strMatrix+= strEntry + " ";
      }
      strMatrix += "\n";
    }
    return strMatrix;
  }
  
  public Double determinant() {
    if (numRows != numCols) {
      return null;
    } else if (numRows == 2) {
      return entries[0][0] * entries[1][1] - entries[0][1] * entries[1][0];
    } else {
      Double det = new Double(0);
      for (int col = 0; col < numCols; col++) {
        double[][] newEntries = new double[numRows - 1][numCols - 1];
	int newRow = 0, newCol = 0;
        for (int r = 1; r < numRows; r++) {
	  for (int c = 0; c < numCols; c++) {
	    if (c == col) continue;
            newEntries[newRow][newCol] = entries[r][c];
	    newCol++;
	  }
	  newRow++;
	  newCol = 0;
	}
        int multiplier = col % 2 == 0 ? 1 : -1;
        Matrix cofactorMatrix = new Matrix(numRows - 1, numCols - 1, newEntries);
        det += multiplier * entries[0][col] * cofactorMatrix.determinant();
      }
      return det;
    }
  }

  public Matrix transpose() {
    double[][] newEntries = new double[numCols][numRows];
    for (int r = 0; r < numRows; r++) {
      for (int c = 0; c < numCols; c++) {
        newEntries[c][r] = entries[r][c];
      }
    }
    return new Matrix(numCols, numRows, newEntries);
  }
  
  public double cofactorAt (int i, int j) {
    double[][] newEntries = new double[numRows - 1][numCols - 1];
    int newRow = 0, newCol = 0;
    for (int r = 0; r < numRows; r++) {
      if (r == i) continue;
      for (int c = 0; c < numCols; c++) {
        if (c == j) continue;
	newEntries[newRow][newCol] = entries[r][c];
	newCol++;
      }
      newRow++;
      newCol = 0;
    }
    int multiplier = (i + j) % 2 == 0 ? 1 : -1;
    Matrix smaller = new Matrix(numRows - 1, numCols - 1, newEntries);
    return multiplier * smaller.determinant();
  }

  public Matrix cofactor() {
    if (numRows != numCols) {
      return null;
    }
    double[][] newEntries = new double[numRows][numCols];
    for (int r = 0; r < numRows; r++) {
      for (int c = 0; c < numCols; c++) {
        newEntries[r][c] = cofactorAt(r, c);
      }
    }

    return new Matrix(numRows, numCols, newEntries);
  }
  
  public Matrix adjoint() {
    Matrix cofactor =  cofactor();
    if (cofactor != null) return cofactor.transpose();
    return null;
  }
  

  public static Matrix dot(Matrix a, Matrix b) {
    if (a.numCols != b.numRows) {
      return null;
    }
    double[][] newEntries = new double[a.numRows][b.numCols];
    double dotSum = 0;
    for (int ra = 0; ra < a.numRows; ra++) {
      for (int cb = 0; cb < b.numCols; cb++) {
        for (int ca = 0; ca < a.numCols; ca++) {
          dotSum += a.entries[ra][ca] * b.entries[ca][cb];
	}
	newEntries[ra][cb] = dotSum;
	dotSum = 0;
      }
    }
    return new Matrix(a.numRows, b.numCols, newEntries);
  }
  
  public boolean isIdentity() {
    if (numCols == numRows) {
      for (int r = 0; r < numRows; r++) {
        for (int c = 0; c < numCols; c++) {
	  if ((r == c && this.entries[r][c] != 1.0) || (r != c && this.entries[r][c] != 0.0))
	    return false;
	}
      }
    }
    return true;
  }

  /*
  public double[] eigenvalues() {
    Matrix rref = this.reduceRowEchelon();
  }
  */

  /*
  private double[][] concatEntries(double[][] entriesA, double[][] entriesB) {
    int rowsEntriesA = entriesA.length; 
    int rowsEntriesB = entriesB.length;
    double[][] entriesC = new double[rowsEntriesA + rowsEntriesB][entriesA[0].length];
    System.arraycopy(entriesA, 0, entriesC, 0, rowsEntriesA);
    System.arraycopy(entriesB, 0, entriesC, rowsEntriesA, rowsEntriesB);
    return entriesC;
  }
  */
}
