package gemjam;


import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Board {

    final static int BOARD_WIDTH = 6;
    final static int BOARD_HEIGHT = 13;
    final static int GEM_SIZE = 80;

    Gem[][] grid;
    List<Gem> matches;

    public Board() {
        grid = new Gem[BOARD_WIDTH][BOARD_HEIGHT];
        fillArray();
        matches = new ArrayList<>();
    }

    /**
     * fill grid with empty Gems
     */
    private void fillArray() {
        for (int x = 0; x < grid.length; x++)
            for (int y = 0; y < grid[x].length; y++)
                grid[x][y] = new Gem(0, 0, 0);
    }

    /**
     * A method to set the current gems into the current grid positions
     *
     * @param gemList
     */
    public void setGridPositions(List<Gem> gemList, int y) {
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

        int x = (int) gem1.getImageView().getLayoutX() / GEM_SIZE;

        //gem1 is at the top
        if (gem1.getPosition() == 0) {
            grid[x][y] = gem2;
            grid[x][y - 1] = gem3;
            grid[x][y - 2] = gem1;
        }
        //gem1 is in the middle
        else if (gem1.getPosition() == 1) {
            grid[x][y] = gem3;
            grid[x][y - 1] = gem1;
            grid[x][y - 2] = gem2;
        } else
        //gem1 is at the bottom
        {
            grid[x][y] = gem1;
            grid[x][y - 1] = gem2;
            grid[x][y - 2] = gem3;;
        }
//        printArray();
    }

    public boolean checkTop(int x, int y) {
        if (y <= 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * A method used to check if the space below is the end of our array or if element below is filled
     *
     * @param x
     * @param y
     * @return true if the element below is empty
     */
    public boolean checkDown(int x, int y) {
        if (y + 1 >= BOARD_HEIGHT) {
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
     * @return boolean
     */
    public boolean checkLeft(int x, int y) {
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
    public boolean checkRight(int x, int y) {
        if (x + 1 >= BOARD_WIDTH) {
            return false;
        } else if (grid[x + 1][y].getColorId() > 0) {
            return false;
        } else {
            return true;
        }
    }

    public void redraw() {

        boolean noEmptySpaces = false;

        while (!noEmptySpaces) {
            int blankSpaces = 0;
            for (int x = 0; x < BOARD_WIDTH; x++) {
                for (int y = 0; y < BOARD_HEIGHT - 1; y++) {
                    if (grid[x][y].getColorId() > 0) {
                        if (grid[x][y + 1].getColorId() == 0) {
                            blankSpaces++;
                            grid[x][y + 1] = grid[x][y];
                            grid[x][y] = new Gem(0, 0, 0);
                        }
                    }
                }
            }
            if (blankSpaces > 0 ) {
                noEmptySpaces = false;
            } else {
                noEmptySpaces = true;
            }
        }



        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[x].length; y++) {
                if (grid[x][y].getColorId() > 0) {
                    grid[x][y].getImageView().relocate(x * GEM_SIZE, y * GEM_SIZE);
                }
            }
        }
    }

    public List<Gem> getMatches() {
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[x].length; y++) {

                //check if colorId != 0
                if (grid[x][y].getColorId() != 0) {
                    //check down for 3 matches
                    if (y + 1 < BOARD_HEIGHT && grid[x][y].getColorId() == grid[x][y + 1].getColorId()) {
                        if (y + 2 < BOARD_HEIGHT && grid[x][y].getColorId() == grid[x][y + 2].getColorId()) {
                            matches.add(grid[x][y]);
                            matches.add(grid[x][y + 1]);
                            matches.add(grid[x][y + 2]);
                            if (y + 3 < BOARD_HEIGHT) {
                                if (grid[x][y].getColorId() == grid[x][y + 3].getColorId()) {
                                    matches.add(grid[x][y + 3]);
                                    if (y + 4 < BOARD_HEIGHT) {
                                        if (grid[x][y].getColorId() == grid[x][y + 4].getColorId()) {
                                            matches.add(grid[x][y + 4]);
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
                            if (x + 3 < BOARD_WIDTH) {
                                if (grid[x][y].getColorId() == grid[x + 3][y].getColorId()) {
                                    matches.add(grid[x + 3][y]);
                                    if (x + 4 < BOARD_WIDTH) {
                                        if (grid[x][y].getColorId() == grid[x + 4][y].getColorId()) {
                                            matches.add(grid[x + 4][y]);
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
                            if (x + 3 < BOARD_WIDTH && y + 3 < BOARD_HEIGHT) {
                                if (grid[x][y].getColorId() == grid[x + 3][y + 3].getColorId()) {
                                    matches.add(grid[x + 3][y + 3]);
                                    if (x + 4 < BOARD_WIDTH && y + 4 < BOARD_HEIGHT) {
                                        if (grid[x][y].getColorId() == grid[x + 4][y + 4].getColorId()) {
                                            matches.add(grid[x + 4][y + 4]);
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
                            if (x - 3 >= 0 && y + 3 < BOARD_HEIGHT) {
                                if (grid[x][y].getColorId() == grid[x - 3][y + 3].getColorId()) {
                                    matches.add(grid[x - 3][y + 3]);;
                                    if (x - 4 >= 0 && y + 4 < BOARD_HEIGHT) {
                                        if (grid[x][y].getColorId() == grid[x - 4][y + 4].getColorId()) {
                                            matches.add(grid[x - 4][y + 4]);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        for (Gem gem : matches) {
            gem.setDestory();
        }

        return removeDupes(matches);
    }

    private void printArray() {
        for (int x = 0; x < grid.length; x++)
            for (int y = 0; y < grid[x].length; y++)
                System.out.println("x= " + x + " y=" + y + " Position: " + grid[x][y].getPosition() + " Color: " + grid[x][y].getColorId() + " Destroy:" + grid[x][y].getDestroy());
    }

    private static List<Gem> removeDupes(List<Gem> gemList) {
        //create a LinkedHashSet that won't allow duplicates.
        Set<Gem> set = new LinkedHashSet<>();

        //add the gem list to the set
        set.addAll(gemList);

        //clear our current list
        gemList.clear();

        //add back in the elements from the LinkedHashSet
        gemList.addAll(set);

        //return the new list with no duplicates.
        return gemList;

    }



}
