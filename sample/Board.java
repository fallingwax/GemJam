package sample;


import java.util.ArrayList;
import java.util.List;

public class Board {

    final static int BOARD_WIDTH = 6;
    final static int BOARD_HEIGHT = 13;
    final static int GEM_SIZE = 80;

    static Gem[][] grid = new Gem[BOARD_WIDTH][BOARD_HEIGHT];

    public Board() {
        fillArray();
    }

    /**
     * fill grid with empty Gems
     */
    private static void fillArray() {
        for (int x = 0; x < grid.length; x++)
            for (int y = 0; y < grid[x].length; y++)
                grid[x][y] = new Gem(0, 0, 0);
    }

    /**
     * A method to set the current gems into the current grid positions
     *
     * @param gemList
     */
    public static void setGridPositions(List<Gem> gemList) {
        Gem gem1 = null;
        Gem gem2 = null;
        Gem gem3 = null;
        for (Gem gem : gemList) {
            if (gem.getInitialPosition() == 2) {
                gem1 = gem;
            }
            if (gem.getInitialPosition() == 1) {
                gem2 = gem;
            }
            if (gem.getInitialPosition() == 0) {
                gem3 = gem;
            }
        }
        //gem1 is at the
        if (gem1.getPosition() == 0) {
            grid[(int) gem1.imageView.getLayoutX() / GEM_SIZE][(int) gem1.imageView.getLayoutY() / GEM_SIZE] = gem1;
            grid[(int) gem2.imageView.getLayoutX() / GEM_SIZE][(int) gem2.imageView.getLayoutY() / GEM_SIZE - 1] = gem2;
            grid[(int) gem3.imageView.getLayoutX() / GEM_SIZE][(int) gem3.imageView.getLayoutY() / GEM_SIZE - 2] = gem3;
        }
        //gem1 is in the middle
        else if (gem1.getPosition() == 1) {
            grid[(int) gem1.imageView.getLayoutX() / GEM_SIZE][(int) gem1.imageView.getLayoutY() / GEM_SIZE] = gem1;
            grid[(int) gem2.imageView.getLayoutX() / GEM_SIZE][(int) gem2.imageView.getLayoutY() / GEM_SIZE - 1] = gem2;
            grid[(int) gem3.imageView.getLayoutX() / GEM_SIZE][(int) gem3.imageView.getLayoutY() / GEM_SIZE - 2] = gem3;
        } else
        //gem1 is at the bottom
        {
            System.out.println((int) gem2.imageView.getLayoutY() / GEM_SIZE);
            grid[(int) gem1.imageView.getLayoutX() / GEM_SIZE][(int) gem1.imageView.getLayoutY() / GEM_SIZE] = gem1;
            grid[(int) gem2.imageView.getLayoutX() / GEM_SIZE][(int) gem2.imageView.getLayoutY() / GEM_SIZE - 2] = gem2;
            grid[(int) gem3.imageView.getLayoutX() / GEM_SIZE][(int) gem3.imageView.getLayoutY() / GEM_SIZE - 3] = gem3;
        }
    }

    /**
     * A method used to check if the space below is the end of our array or if element below is filled
     *
     * @param x
     * @param y
     * @return true if the element below is empty
     */
    public static boolean checkDown(int x, int y) {
        if (y + 1 == BOARD_HEIGHT) {
            return false;
        } else if (grid[x][y + 1].getColorId() > 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * A method used to check if the space to the left is the beginning of our array or if element is filled
     *
     * @param x
     * @param y
     * @return true if the element to the left is empty
     */
    public static boolean checkLeft(int x, int y) {
        if (x - 1 < 0) {
            return false;
        } else if (grid[x - 1][y].getColorId() > 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * A method used to check if the space to the right is the end of our array or if element is filled
     *
     * @param x
     * @param y
     * @true if the element to the right is empty
     */
    public static boolean checkRight(int x, int y) {
        if (x + 1 == BOARD_WIDTH) {
            return false;
        } else if (grid[x + 1][y].getColorId() > 0) {
            return false;
        } else {
            return true;
        }
    }

    public void redrawBoard() {
        for (int x = 0; x < BOARD_WIDTH; x++) {
            for (int y = 0; y < BOARD_HEIGHT - 1; y++) {
                if (grid[x][y].getColorId() > 0) {
                    if (!checkDown(x, y)) {
                        grid[x][y + 1] = grid[x][y];
                        grid[x][y] = new Gem(0, 0, 0);
                    }
                }
            }
        }

        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[x].length; y++) {
                if (grid[x][y].getColorId() > 0) {
                    grid[x][y].imageView.relocate(x * GEM_SIZE, y * GEM_SIZE);
                }
            }
        }
    }

    public List<Gem> getMatches() {
        redrawBoard();
        List<Gem> matches = new ArrayList<>();
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[x].length; y++) {
                if (matches.contains(grid[x][y]))
                    //check if we already cell to the list
                    continue;

                //check if colorId != 0
                if (grid[x][y].getColorId() != 0) {
                    //check down for 3 matches
                    if (y + 1 < BOARD_HEIGHT && grid[x][y].getColorId() == grid[x][y + 1].getColorId()) {
                        if (y + 2 < BOARD_HEIGHT && grid[x][y].getColorId() == grid[x][y + 2].getColorId()) {
                            matches.add(grid[x][y]);
                            matches.add(grid[x][y + 1]);
                            matches.add(grid[x][y + 2]);
                            grid[x][y].setDestory();
                            grid[x][y + 1].setDestory();
                            grid[x][y + 2].setDestory();
                            if (y + 3 < BOARD_HEIGHT) {
                                if (grid[x][y].getColorId() == grid[x][y + 3].getColorId()) {
                                    matches.add(grid[x][y + 3]);
                                    grid[x][y + 3].setDestory();
                                    if (y + 4 < BOARD_HEIGHT) {
                                        if (grid[x][y].getColorId() == grid[x][y + 4].getColorId()) {
                                            matches.add(grid[x][y + 4]);
                                            grid[x][y + 4].setDestory();
                                        }
                                    }
                                }
                            }
                        }
                    }

                    //check right for 3 matches
                    if (x + 1 < BOARD_WIDTH && grid[x][y].getColorId() == grid[x + 1][y].getColorId()) {
                        if (x + 2 < BOARD_WIDTH && grid[x][y].getColorId() == grid[x + 2][y].getColorId()) {
                            matches.add(grid[x][y]);
                            matches.add(grid[x + 1][y]);
                            matches.add(grid[x + 2][y]);
                            grid[x][y].setDestory();
                            grid[x + 1][y].setDestory();
                            grid[x + 2][y].setDestory();
                            if (x + 3 < BOARD_WIDTH) {
                                if (grid[x][y].getColorId() == grid[x + 3][y].getColorId()) {
                                    matches.add(grid[x + 3][y]);
                                    grid[x + 3][y].setDestory();
                                    if (x + 4 < BOARD_WIDTH) {
                                        if (grid[x][y].getColorId() == grid[x + 4][y].getColorId()) {
                                            matches.add(grid[x + 4][y]);
                                            grid[x + 4][y].setDestory();
                                        }
                                    }
                                }
                            }
                        }
                    }

                    //check down right diagonal for 3 matches
                    if (y + 1 < BOARD_HEIGHT && x + 1 < BOARD_WIDTH && grid[x][y].getColorId() == grid[x + 1][y + 1].getColorId()) {
                        if (y + 2 < BOARD_HEIGHT && x + 2 < BOARD_WIDTH && grid[x][y].getColorId() == grid[x + 2][y + 2].getColorId()) {
                            matches.add(grid[x][y]);
                            matches.add(grid[x + 1][y + 1]);
                            matches.add(grid[x + 2][y + 2]);
                            grid[x][y].setDestory();
                            grid[x + 1][y + 1].setDestory();
                            grid[x + 2][y + 2].setDestory();
                            if (x + 3 < BOARD_WIDTH && y + 3 < BOARD_HEIGHT) {
                                if (grid[x][y].getColorId() == grid[x + 3][y + 3].getColorId()) {
                                    matches.add(grid[x + 3][y + 3]);
                                    grid[x + 3][y + 3].setDestory();
                                    if (x + 4 < BOARD_WIDTH && y + 4 < BOARD_HEIGHT) {
                                        if (grid[x][y].getColorId() == grid[x + 4][y + 4].getColorId()) {
                                            matches.add(grid[x + 4][y + 4]);
                                            grid[x + 4][y + 4].setDestory();
                                        }
                                    }
                                }
                            }
                        }
                    }

                    //check down left diagonal for 3 matches
                    if (y + 1 < BOARD_HEIGHT && x - 1 < BOARD_WIDTH && x - 1 >= 0 && grid[x][y].getColorId() == grid[x - 1][y + 1].getColorId()) {
                        if (y + 2 < BOARD_HEIGHT && x - 2 < BOARD_WIDTH && x - 2 >= 0 && grid[x][y].getColorId() == grid[x - 2][y + 2].getColorId()) {
                            matches.add(grid[x][y]);
                            matches.add(grid[x - 1][y + 1]);
                            matches.add(grid[x - 2][y + 2]);
                            grid[x][y].setDestory();
                            grid[x - 1][y + 1].setDestory();
                            grid[x - 2][y + 2].setDestory();
                            if (x - 3 >= 0 && y + 3 < BOARD_HEIGHT) {
                                if (grid[x][y].getColorId() == grid[x - 3][y + 3].getColorId()) {
                                    matches.add(grid[x - 3][y + 3]);
                                    grid[x - 3][y + 3].setDestory();
                                    if (x - 4 >= 0 && y + 4 < BOARD_HEIGHT) {
                                        if (grid[x][y].getColorId() == grid[x - 4][y + 4].getColorId()) {
                                            matches.add(grid[x - 4][y + 4]);
                                            grid[x - 4][y + 4].setDestory();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return matches;
    }
}
