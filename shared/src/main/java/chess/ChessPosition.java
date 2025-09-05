package chess;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {

    private final int ROW;
    private final int COL;

    public ChessPosition(int row, int col) {
        this.ROW = row;
        this.COL = col;
    }


    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return ROW;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return COL;
    }

    @Override
    public String toString() {
        return String.format("[%d, %d]", ROW, COL);
    }

    @Override
    public boolean equals(Object obj) {
        // initial checks
        if (obj == null)
            return false;
        if (obj == this)
            return true;
        if (obj.getClass() != this.getClass())
            return false;

        ChessPosition pos = (ChessPosition) obj;

        return ((this.ROW == pos.ROW) && (this.COL == pos.COL));
    }

    @Override
    public int hashCode() {
        return(ROW ^ COL);
    }
}
