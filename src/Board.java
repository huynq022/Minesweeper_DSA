public class Board{
    private final int NUM_IMAGES = 13;
    private final int CELL_SIZE = 15;

    private final int COVER_FOR_CELL = 10;
    private final int MARK_FOR_CELL = 10;
    private final int EMPTY_CELL = 0;
    private final int MINE_CELL = 9;
    private final int COVERED_MINE_CELL = MINE_CELL + COVER_FOR_CELL;
    private final int MARKED_MINE_CELL = COVERED_MINE_CELL + MARK_FOR_CELL;

    private final int DRAW_MINE = 9;
    private final int DRAW_COVER = 10;
    private final int DRAW_MARK = 11;
    private final int DRAW_WRONG_MARK = 12;

    private final int N_MINES = 40;
    private final int N_ROWS = 16;
    private final int N_COLS = 16;
    
     private class MinesAdapter extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {

            int x = e.getX();
            int y = e.getY();

            // calculates the column and row of the corresponding cell
            int cCol = x / CELL_SIZE;
            int cRow = y / CELL_SIZE;

            boolean doRepaint = false;

            // game í not in progress
            if (!inGame) {
                newGame();
                repaint();
            }

            // check that the mouse click coordinates are within the bounds of the board
            if ((x < N_COLS * CELL_SIZE) && (y < N_ROWS * CELL_SIZE)) {
                // checks whether the left or right mouse button was clicked
                if (e.getButton() == MouseEvent.BUTTON3) {
                    // right mouse button was clicked and not a mine
                    if (field[(cRow * N_COLS) + cCol] > MINE_CELL) {

                        doRepaint = true;

                        if (field[(cRow * N_COLS) + cCol] <= COVERED_MINE_CELL) {

                            if (minesLeft > 0) {
                                field[(cRow * N_COLS) + cCol] += MARK_FOR_CELL;
                                minesLeft--;
                                String msg = Integer.toString(minesLeft);
                                statusbar.setText(msg);
                            } else {
                                statusbar.setText("No marks left");
                            }

                        } else {

                            field[(cRow * N_COLS) + cCol] -= MARK_FOR_CELL;
                            minesLeft++;
                            String msg = Integer.toString(minesLeft);
                            statusbar.setText(msg);
                        }
                    }

                } else { //the left mouse button was clicked

                    if (field[(cRow * N_COLS) + cCol] > COVERED_MINE_CELL) {

                        return; // do nothing
                    }

                    //checks whether the clicked location is not a mine cell and is not already marked as a mine
                    if ((field[(cRow * N_COLS) + cCol] > MINE_CELL)
                            && (field[(cRow * N_COLS) + cCol] < MARKED_MINE_CELL)) {

                        field[(cRow * N_COLS) + cCol] -= COVER_FOR_CELL;    // uncovers the cell
                        doRepaint = true;

                        //checks whether the uncovered cell is a mine cell
                        if (field[(cRow * N_COLS) + cCol] == MINE_CELL) {
                            inGame = false;
                        }

                        //uncovered cell is not a mine cell -> checks whether it is an empty cell
                        if (field[(cRow * N_COLS) + cCol] == EMPTY_CELL) {
                            find_empty_cells((cRow * N_COLS) + cCol);      // uncover all adjacent empty cells
                        }
                    }
                }

                if (doRepaint) {
                    repaint();
                }
            }
        }
}
