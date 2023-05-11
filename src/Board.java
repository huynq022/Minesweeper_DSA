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
    
    public Board(JLabel statusbar) {

        this.statusbar = statusbar;
        initBoard();
    }

    private void initBoard() {

        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));

        // Array to store images
        img = new Image[NUM_IMAGES];

        for (int i = 0; i < NUM_IMAGES; i++) {

            var path = "src/resources/" + i + ".png";

            // Use ImageIcon to load the image file specified by the path variable
            img[i] = (new ImageIcon(path)).getImage();
        }

        addMouseListener(new MinesAdapter());
        newGame();
    }

    private void newGame() {

        int cell;

        var random = new Random(); // generate random numbers
        inGame = true; // indicate game is in progress
        minesLeft = N_MINES; // total mines in game

        allCells = N_ROWS * N_COLS; // total number of cells
        field = new int[allCells]; // present state of a cell

        for (int i = 0; i < allCells; i++) {

            field[i] = COVER_FOR_CELL;
        }

        statusbar.setText(Integer.toString(minesLeft)); // show number of mines left

        int i = 0;

        // use while to place randomly mines
        while (i < N_MINES) {

            int position = (int) (allCells * random.nextDouble());

            // check if position is in range of field array
            // and that cell is not a mine
            if ((position < allCells)
                    && (field[position] != COVERED_MINE_CELL)) {

                int current_col = position % N_COLS; // determine neighboring cells
                field[position] = COVERED_MINE_CELL; // set as a mine
                i++;

                // check the current position is not  in the first column
                if (current_col > 0) {

                    // check diagonally above to the left
                    cell = position - 1 - N_COLS;
                    if (cell >= 0) {
                        // if not a mine -> increase by 1
                        if (field[cell] != COVERED_MINE_CELL) {
                            field[cell] += 1;
                        }
                    }

                    // check diagonally below to the left
                    cell = position - 1;
                    if (cell >= 0) {
                        // if not a mine -> increase by 1
                        if (field[cell] != COVERED_MINE_CELL) {
                            field[cell] += 1;
                        }
                    }

                    // to the left of the current position
                    cell = position + N_COLS - 1;
                    if (cell < allCells) {
                        // if not a mine -> increase by 1
                        if (field[cell] != COVERED_MINE_CELL) {
                            field[cell] += 1;
                        }
                    }
                }

                cell = position - N_COLS;
                if (cell >= 0) {
                    // if not a mine -> increase by 1
                    if (field[cell] != COVERED_MINE_CELL) {
                        field[cell] += 1;
                    }
                }

                cell = position + N_COLS;
                if (cell < allCells) {
                    // if not a mine -> increase by 1
                    if (field[cell] != COVERED_MINE_CELL) {
                        field[cell] += 1;
                    }
                }

                // check current position is not the last column of game board
                if (current_col < (N_COLS - 1)) {
                    // above
                    cell = position - N_COLS + 1;
                    if (cell >= 0) {
                        // if not a mine -> increase by 1
                        if (field[cell] != COVERED_MINE_CELL) {
                            field[cell] += 1;
                        }
                    }

                    // below
                    cell = position + N_COLS + 1;
                    if (cell < allCells) {
                        // if not a mine -> increase by 1
                        if (field[cell] != COVERED_MINE_CELL) {
                            field[cell] += 1;
                        }
                    }

                    // to the right
                    cell = position + 1;
                    if (cell < allCells) {
                        // if not a mine -> increase by 1
                        if (field[cell] != COVERED_MINE_CELL) {
                            field[cell] += 1;
                        }
                    }
                }
            }
        }
    }
    
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
            // undo part 
        }
}
