package project.minesweeper.model;

import java.util.Random;

/**
 * Created by Crystal on 10/1/2016.
 */
public class MinesweeperModel {

    private MinesweeperModel(){
        placeMines();
    }

    private static MinesweeperModel instance = null;

    public static final short EMPTY = 0;
    public static final short MINE = 1;
    public static final short FLAG = 2;
    public static final short NUM = 3;

    private short[][] model = {
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0}
    };

    public void resetModel() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                model[i][j] = 0;
            }
        }
        placeMines();
    }

    public void placeMines(){
        Random randomGenerator = new Random();
        int x = 0;
        int y = 0;
        int count = 0;
        while (count < 3){
            x = randomGenerator.nextInt(4);
            y = randomGenerator.nextInt(4);
            if (getField(x, y) == EMPTY){
                setField(x, y, MINE);
                count += 1;
            }
        }
    }

    public static MinesweeperModel getInstance() {
        if (instance == null) {
            instance = new MinesweeperModel();
        }
        return instance;
    }

    public short getField(int x, int y) {
        return model[x][y];
    }

    public void setField(int x, int y, short player) {
        model[x][y]=player;
    }
}