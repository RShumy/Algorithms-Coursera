import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    // I will try to flatten the 2d array into a 1d array
    //
    // if we define a 2d array by totalRows(height) and totalColumns(width)
    //      --------------------
    //      |                  | <-height
    //      ------width---------
    // making a row, column index check transposed into 1d:
    // 1D array index = row * totalColumns + column

    private boolean[] grid;
    private WeightedQuickUnionUF unionFindGrid;
    private int edgeLength;
    private int array1DLength;
    private int openSites = 0;

    // percolation would work in the context of a 2d square matrix,
    // but we will work with a flattened version
    public Percolation(int n) {
        if (n <= 0) exception("Cont construct a matrix of length below or equal to 0");
        this.edgeLength = n;
        this.array1DLength = n*n;
        grid = new boolean[ array1DLength ];
        for (int i = 0; i < array1DLength; i++) {
            // initializing the grid to 0
            grid[i] = false;
            this.unionFindGrid = new WeightedQuickUnionUF(array1DLength+1);
        }
    }

    private void exception(String message) {
        throw new IllegalArgumentException(message);
    }
    // made to apply to natural index assignment from 1 to n, not from 0 to n-1
    private int index(int row, int column) {
        // subtracting 1 from row to adjust for flatten index
        if (isOutOfBounds(row, column))
            exception(String.format("Row %d or Column %d is out of Bounds for Length %d", row, column, edgeLength));
        return (row-1) * edgeLength + (column-1);
    }

    public void open(int row, int column) {
        int index = index(row, column);
        if (!grid[index]) openSites++;
        grid[index] = true;
        if (row == 1 && column <= edgeLength)
            fillSite(0,index);
    }

    public boolean isOpen(int row, int column) {
        return isOpen(index(row, column));
    }

    private boolean isOpen (int flattenIndex) {
        if (flattenIndex == array1DLength) return grid[flattenIndex-1];
        return grid[flattenIndex];
    }
    
    public boolean isFull( int row, int column) {
        if (!isOutOfBounds(row, column))
            return isFull(index(row, column));
        else return false;
    }

    private boolean isFull(int flattenIndex) {
        if (flattenIndex == array1DLength) return unionFindGrid.find(flattenIndex) == 0;
        return unionFindGrid.find(flattenIndex+1) == 0;
    }

    private boolean validToFill(int previousFlattenIndex, int row, int column) {
        if (previousFlattenIndex == array1DLength) previousFlattenIndex--;
        if (!isOutOfBounds(row, column))
            return isFull(previousFlattenIndex) && isOpen(row, column) && !isFull(row, column);
        return false;
    }

    private void fillSite( int flattenIndex, int flattenIndexNeighbour ) {
        if (flattenIndex != 0)
            unionFindGrid.union(flattenIndex+1, flattenIndexNeighbour+1);
        else unionFindGrid.union(0,flattenIndexNeighbour+1);
    }

    private void tryToFill (int previousFlatIndex, int row, int column) {
        int index = index(row, column);
        if (validToFill(previousFlatIndex, row, column)) {
            fillSite(previousFlatIndex, index);
        }
        if ( validToFill(index, row+1, column) ) tryToFill(index, row+1, column);
        if ( validToFill(index, row-1, column) ) tryToFill(index, row-1, column);
        if ( validToFill(index, row,column-1 )) tryToFill(index, row, column-1);
        if ( validToFill(index, row,column+1) ) tryToFill(index, row, column+1);
    }

    /** returns true if index is out of bounds */
    private boolean isOutOfBounds(int row, int column) {
        return row < 1 || row > edgeLength || column < 1 || column > edgeLength;
    }

    public int numberOfOpenSites() {
        return this.openSites;
    }

    public boolean percolates() {
        for (int col=1; col<=edgeLength;col++)
            if(isFull(1,col))
                tryToFill(index(1, col), 2, col);
        for (int col=1; col<=edgeLength; col++)
            if (isFull(edgeLength, col)) return true;
        return false;
    }


    public static void main(String[] args) {

        Percolation p = new Percolation(25);
        for (int i = 1; i <= p.array1DLength - (p.array1DLength / 7); i++)
            p.open(StdRandom.uniformInt(1, p.edgeLength + 1), StdRandom.uniformInt(1, p.edgeLength + 1));
        System.out.println(p.percolates());
        p.print2DArray();
    }
    private void print2DArray() {
        // USING ANSI escape codes to print colored text
        String CYAN = "\u001B[36m";
        String RED = "\u001B[31m";
        String ANSI_RESET = "\u001B[0m";
        String CYAN_BKGRND = "\u001B[48;5;45m";
        String WHT_BKGRND = "\u001B[48;5;195m";
        String BLCK_BKGRND = "\u001B[48;5;16m";
        for (int row=1; row<=edgeLength; row++) {
            for (int col=1; col<=edgeLength; col++) {
                if (isFull(row, col)) System.out.print( CYAN_BKGRND + "   " );
                else if (!isFull(row, col) && isOpen(row, col)) System.out.print( WHT_BKGRND + "   " );
                else System.out.print( BLCK_BKGRND + "   " );
                System.out.print(ANSI_RESET);
            }
            System.out.println("");
        }
    }
}
