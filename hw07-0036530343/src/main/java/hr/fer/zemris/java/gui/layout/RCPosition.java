package hr.fer.zemris.java.gui.layout;

/**
 * This class is used for positioning elements on layout
 */
public class RCPosition {

    /**
     * Row index.
     */
    private int row;
    /**
     * Column index.
     */
    private int column;

    /**
     * Constructor.
     * @param row row index
     * @param column column index
     */
    public RCPosition(int row, int column) {
        if(row < 1 || row > 5 || column < 1 || column > 7) {
            throw new CalcLayoutException();
        } else if(row == 1 && (column >= 2 && column <= 5)) {
            throw new CalcLayoutException();
        }
        this.row = row;
        this.column = column;
    }

    /**
     * Row getter.
     * @return
     */
    public int getRow() {
        return row;
    }
    /**
     * Column getter.
     * @return
     */
    public int getColumn() {
        return column;
    }

    /**
     * Method used for parsing if input is String
     * @param text input
     * @return new RCPosition
     */
    public static RCPosition parse(String text) {
        if(text.length() != 3){
            throw new CalcLayoutException();
        }
        int row = Integer.parseInt(text.split(",")[0]);
        int column = Integer.parseInt(text.split(",")[1]);
        return new RCPosition(row, column);
    }
}
