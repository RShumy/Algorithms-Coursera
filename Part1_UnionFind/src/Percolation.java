import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;
public class Percolation {
    private final boolean[] grid;
    private final WeightedQuickUnionUF unionFindGrid;
    private final int edgeLength;
    private final int array1DLength;
    private int openSites = 0;

    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException("Cannot construct a Matrix of length below or equal to 0");
        this.edgeLength = n;
        this.array1DLength = n*n;
        grid = new boolean[ array1DLength ];
        this.unionFindGrid = new WeightedQuickUnionUF(array1DLength+2);
        for (int i = 0; i < array1DLength; i++) {
            grid[i] = false;
        }
    }

    private void exception(int row, int column) {
        throw new IllegalArgumentException(String.format("Row %d or Column %d is out of Bounds for Length %d", row, column, edgeLength));
    }

    private int index(int row, int column) {
        if (isOutOfBounds(row, column))
            exception(row, column);
        return (row-1) * edgeLength + (column-1);
    }

    public void open(int row, int column) {
        int index = index(row, column);
        if (grid[index])
            return;
        openSites++;
        grid[index] = true;
        if ( row == 1 ) unionFindGrid.union(index(row, column)+1, 0);
        if ( row == edgeLength ) unionFindGrid.union( index(row, column)+1, array1DLength+1 );
        tryToFill(row, column, row-1, column);
        tryToFill(row, column, row, column-1);
        tryToFill(row, column, row+1, column);
        tryToFill(row, column, row, column+1);
    }

    public boolean isOpen(int row, int column) {
        return grid[index(row, column)];
    }

    public boolean isFull(int row, int column) {
        return unionFindGrid.find(index(row, column)+1) == unionFindGrid.find(0);
    }

    private boolean validToFill(int row, int column) {
        if ( isOutOfBounds(row, column) )
            return false;
        return isOpen(row, column);
    }

    private void fillSite(int flattenIndex, int flattenIndexNeighbour) {
        unionFindGrid.union(flattenIndex + 1, flattenIndexNeighbour + 1);
    }

    private void tryToFill (int previousR, int previousC, int row, int column) {
        if ( !validToFill(row, column) )
            return;
        fillSite(index(previousR, previousC), index(row, column));
    }

    public int numberOfOpenSites() {
        return this.openSites;
    }

    public boolean percolates() {
        for (int col = 1; col <= edgeLength; col++)
            if ( isFull(edgeLength,col) ) return true;
        return false;
    }

    private boolean isOutOfBounds(int row, int column) {
        return row < 1 || row > edgeLength || column < 1 || column > edgeLength;
    }

    public static void main(String[] args) {
        Percolation percolation = new Percolation(20);
        for (int i = 1; i <= percolation.array1DLength - (percolation.array1DLength / 7); i++) {
            int row = StdRandom.uniformInt(1, percolation.edgeLength + 1);
            int col = StdRandom.uniformInt(1, percolation.edgeLength + 1);
            percolation.open(row, col);
            System.out.printf("Opening [row: %d][column: %d]%n", row,col );
            percolation.print2DArray();
        }
        System.out.println(percolation.percolates());
        percolation.print2DArray();
    }

    private void print2DArray() {
        // USING ANSI escape codes to print colored text
        int row,col;
        for (row=0; row<=edgeLength; row++) {
            for (col=0; col<=edgeLength; col++) {
                String CYAN_BKGRND = "\u001B[48;5;45m";
                String WHT_BKGRND = "\u001B[48;5;195m";
                String BLCK_BKGRND = "\u001B[48;5;16m";
                if (row == 0)
                    System.out.printf("%3d", col);
                else if(col == 0)
                    System.out.printf("%3d ", row);
                else if (isFull(row, col))
                    System.out.print(CYAN_BKGRND + "   ");
                else if (!isFull(row, col) && isOpen(row, col))
                    System.out.print(WHT_BKGRND + "   ");
                else
                    System.out.print(BLCK_BKGRND + "   ");
                String ANSI_RESET = "\u001B[0m";
                System.out.print(ANSI_RESET);
            }
            System.out.println();
        }
    }
}
