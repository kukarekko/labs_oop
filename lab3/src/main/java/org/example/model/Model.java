package org.example.model;

import java.util.Random;

public class Model {
    private int field[][];
    private int n;
    private int score;
    private Random random = new Random();

    public Model(int size){
        this.n = size;
        field = new int[n][n];
        score = 0;
        spawnTile();
        spawnTile();
    }

    public int getN() {
        return n;
    }

    public int[][] getField() {
        return field;
    }

    public int getScore() {
        return score;
    }

    private int[][] copyField() {
        int[][] copy = new int[n][n];
        for (int i = 0; i < n; i++) {
            System.arraycopy(field[i], 0, copy[i], 0, n);
        }
        return copy;
    }

    private boolean fieldsEqual(int[][] field1, int[][] field2) { //изменилось ли поле после хода
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (field1[i][j] != field2[i][j]) return false;
            }
        }
        return true;
    }

    public void spawnTile() {
        if (!hasEmpty()) {
            return;
        }

        while (true) {
            int a = random.nextInt(n * n);
            int x = a / n;
            int y = a % n;
            if (field[x][y] == 0) {
                int value = random.nextInt(10) < 9 ? 2 : 4; // 90% двойка, 10% четверка
                field[x][y] = value;
                break;
            }
        }
    }


    public void moveLeft() {
        int[][] oldField = copyField();

        for (int i = 0; i < n; i++) {
            compressRow(i);
            mergeRow(i);
            compressRow(i);
        }

        if (!fieldsEqual(oldField, field)) {
            spawnTile();
        }
    }

    public void moveRight() {
        reverseRow();
        moveLeft();
        reverseRow();

    }

    public void moveUp() {
        transpose();
        moveLeft();
        transpose();

    }

    public void moveDown() {
        transpose();
        moveRight();
        transpose();

    }

    public void reverseRow() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n / 2; j++) {
                int temp = field[i][j];
                field[i][j] = field[i][n - j - 1];
                field[i][n - j - 1] = temp;
            }
        }
    }

    public void transpose() {
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int temp = field[i][j];
                field[i][j] = field[j][i];
                field[j][i] = temp;
            }
        }
    }

    public void compressRow(int i) {
        int a = 0;
        for (int j = 0; j < n; j++) {
            if (field[i][j] == 0) {
                a++;
            } else {
                int d = j - a;
                field[i][d] = field[i][j];
                if (d != j) {
                   field[i][j] = 0;
                }
            }
        }
    }


    private void mergeRow(int i) {
        for (int j = 0; j < n - 1; j++) {
            if (field[i][j] != 0 && field[i][j] == field[i][j + 1]) {
                field[i][j] *= 2;
                score += field[i][j];
                field[i][j + 1] = 0;
            }
        }
    }

    public boolean hasEmpty() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (field[i][j] == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isGameOver() {
        if (hasEmpty()) {
            return false;
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n - 1; j++) {
                if (field[i][j] == field[i][j + 1]) return false;
                if (field[j][i] == field[j + 1][i]) return false;
            }
        }
        return true;
    }

    public void reset() {
        field = new int[n][n];
        score = 0;
        spawnTile();
        spawnTile();
    }


}
