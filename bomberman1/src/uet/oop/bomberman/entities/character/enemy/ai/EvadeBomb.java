package uet.oop.bomberman.entities.character.enemy.ai;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.entities.character.enemy.Enemy;

public class EvadeBomb extends AI {

    private Enemy _e;
    private Board _board;

    public EvadeBomb(Enemy e, Board b) {
        _e = e;
        _board = b;
    }

    @Override
    public int calculateDirection() {
        int x_e = _e.getXTile();
        int y_e = _e.getYTile();
        // x_e - 2; y_e const
        if (_board.getBombAt((double) x_e - 2, (double) y_e) != null) {
            return random.nextInt(3);
        }
        // x_e - 1; y_e - (1;-1)
        if (_board.getBombAt((double) x_e - 1, (double) y_e - 1) != null) {
            return random.nextInt(2) + 1;       // 1 or 2
        }

        if (_board.getBombAt((double) x_e - 1, (double) y_e) != null) {
            return random.nextInt(3);  // 0 or 1 or 2
        }

        if (_board.getBombAt((double) x_e - 1, (double) y_e + 1) != null) {
            return random.nextInt(2) % 3;        // 0 or 1
        }

        // x_e; y_e - (2;-2)
        if (_board.getBombAt((double) x_e, (double) y_e - 2) != null) {
            return random.nextInt(3) + 1;       // 3 or 1 or 2
        }

        if (_board.getBombAt((double) x_e, (double) y_e - 1) != null) {
            return random.nextInt(3) + 1;       // 3 or 1 or 2
        }

        if (_board.getBombAt((double) x_e, (double) y_e) != null) {
            return random.nextInt(4);       
        }

        if (_board.getBombAt((double) x_e, (double) y_e + 1) != null) {
            return (random.nextInt(3) + 3) % 4 ;       // 3 or 0 or 1
        }

        if (_board.getBombAt((double) x_e, (double) y_e + 2) != null) {
            return (random.nextInt(3) + 3) % 4 ;       // 3 or 0 or 1
        }

        // x_e + 1; y_e - (1;-1)
        if (_board.getBombAt((double) x_e + 1, (double) y_e - 1) != null) {
            return random.nextInt(2) + 2;       // 2 or 3
        }

        if (_board.getBombAt((double) x_e + 1, (double) y_e) != null) {
            return (random.nextInt(3) + 2) % 4;  // 2 or 3 or 0
        }

        if (_board.getBombAt((double) x_e + 1, (double) y_e + 1) != null) {
            return (random.nextInt(2) + 3) % 4;        // 3 or 0
        }
        
        // x_e + 2; y_e const
        if (_board.getBombAt((double) x_e - 2, (double) y_e) != null) {
            return (random.nextInt(3) + 2) % 4;  // 2 or 3 or 0
        }

        return random.nextInt(4);
    }

}
