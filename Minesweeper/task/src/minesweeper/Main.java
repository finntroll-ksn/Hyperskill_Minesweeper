package minesweeper;

import java.util.*;

public class Main {
    static final int width = 9;
    static final int height = 9;
    static char[][] field = new char[width][height];
    static char[][] userField = new char[width][height];
    private static Scanner scanner = new Scanner(System.in);
    static int minesNumber;

    public static void main(String[] args) {
        System.out.println("How many mines do you want on the field? ");

        minesNumber = Integer.parseInt(scanner.nextLine());

        fillBoard();
        placeMines();
        fillCountMines();
        playGame();
    }

    static void fillBoard() {
        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {
                field[i][j] = '.';
            }
        }
    }

    static void printBoard() {
        System.out.println("\n |123456789|");
        System.out.println("—│—————————│");

        for(int i = 0; i < width; i++) {
            System.out.printf("%d|%s|\n", i+1, new String(userField[i]));
        }

        System.out.println("—│—————————│");
    }

    static void placeMines() {
        int count = 0;
        Random r = new Random();

        while (count < minesNumber) {
            int x = r.nextInt(width);
            int y = r.nextInt(height);

            if(!isMine(field[x][y])) {
                field[x][y] = '*';
                count++;
            }
        }
    }

    static boolean isMine(char cell) {
        return cell == '*';
    }

    static void fillCountMines() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (!isMine(field[i][j])) {
                    char mineNum = Character.forDigit(getNumberOfMines(i, j), 10);
                    field[i][j] = mineNum == '0' ? '.' : mineNum;
                }
            }
        }
    }

    static int getNumberOfMines(int row, int col) {
        int totalMines = 0;

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                try {
                    if (isMine(field[row + i][col + j])) {
                        totalMines++;
                    }
                } catch (ArrayIndexOutOfBoundsException ignored) {
                }
            }
        }

        return totalMines;
    }

    static void createUserField() {
        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {
                userField[i][j] = '.';
            }
        }
    }

    static boolean checkMines() {
        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {
                if (isMine(userField[i][j]) && userField[i][j] != field[i][j]) {

                }
            }
        }

        return true;
    }

    static int freeCell(int y, int x) {
        if (isMine(field[y][x])) {
            return -1;
        }

        if (userField[y][x] != '.' && userField[y][x] != '*') {
            return 0;
        }

        int freeCellsNumber = 1;

        if (field[y][x] != '.') {
            userField[y][x] = field[y][x];
        } else {
            userField[y][x] = '/';

            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (y + i >= 0 && y + i < width && x + j >= 0 && x + j < height && (i != 0 || j != 0)) {
                        freeCellsNumber += freeCell(y + i, x + j);
                    }
                }
            }
        }

        return freeCellsNumber;
    }

    static void playGame() {
        createUserField();
        printBoard();

        boolean gameEnd = false;
        int mines = 0;
        int freeCells = width * height;

        do {
            System.out.println("Set/unset mines marks or claim a cell as free:");
            String[] command = scanner.nextLine().split("\\s+");
            int x = Integer.parseInt(command[0]) - 1;
            int y = Integer.parseInt(command[1]) - 1;

            if (command[2].equals("free")) {
                int fc = freeCell(y, x);

                if (fc != -1) {
                    freeCells -= fc;

                    printBoard();

                    if (mines + freeCells == minesNumber) {
                        gameEnd = true;
                    }
                } else {
                    System.out.println("You stepped on a mine and failed!");
                    return;
                }
            } else {
                switch (userField[y][x]) {
                    case '.':
                        userField[y][x] = '*';
                        mines++;
                        printBoard();

                        break;
                    case '*':
                        userField[y][x] = '.';
                        mines--;
                        printBoard();

                        break;
                    default:
                        System.out.println("There is a number here!");
                        break;
                }

                if (mines == minesNumber && checkMines()) {
                    gameEnd = true;
                }
            }
        } while(!gameEnd);

        System.out.println("Congratulations! You found all mines!");
    }
}
