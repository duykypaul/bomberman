package uet.oop.bomberman.level;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.exceptions.LoadLevelException;

/**
 * Load và lưu trữ thông tin bản đồ các màn chơi
 */
public abstract class LevelLoader {

    protected static int _width, _height; // default values just for testing
    protected int _level;
    protected Board _board;
    protected static char[][] _map;

    public LevelLoader(Board board, int level) throws LoadLevelException {
        _board = board;
        loadLevel(level);
    }

    public abstract void loadLevel(int level) throws LoadLevelException;

    public abstract void createEntities();

    public static int getWidth() {
        return _width;
    }

    public static int getHeight() {
        return _height;
    }

    public int getLevel() {
        return _level;
    }

    public static char[][] getMap() {
        return _map;
    }

}
