package uet.oop.bomberman.entities.character.enemy.ai;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.entities.character.enemy.Enemy;

public class AIHigh extends AI {
    private AIMedium aiMedium;
    private EvadeBomb aiEvade;
    
    Board _board;
    Enemy _e;

    public AIHigh(Bomber bomber, Enemy e, Board b) {
        aiMedium = new AIMedium(bomber, e);
        aiEvade = new EvadeBomb(e, b);
        _board = b;
        _e = e;
    }

    @Override
    public int calculateDirection() {
        if(!_board.getBomber().get_bombs().isEmpty()) {
            return aiEvade.calculateDirection();
        }
        return aiMedium.calculateDirection();
    }
}