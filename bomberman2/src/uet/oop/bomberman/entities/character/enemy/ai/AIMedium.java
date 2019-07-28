package uet.oop.bomberman.entities.character.enemy.ai;

import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.entities.character.enemy.Enemy;

public class AIMedium extends AI {

    Bomber _player;
    Enemy _e;

    public AIMedium(Bomber player, Enemy e) {
        _player = player;
        _e = e;
    }

    @Override
    public int calculateDirection() {
        int vertical = random.nextInt(11);
        if (vertical < 5) {
            int v = calculateRowDirection();
            if (v != -1) {
                return v;
            } else {
                return calculateColDirection();
            }
        } else if(vertical < 10) {
            int h = calculateColDirection();

                if (h != -1) {
                    return h;
                } else {
                    return calculateRowDirection();
                }
        } else {
            return random.nextInt(4);
        }
    }

    protected int calculateColDirection() {
        if (_player.getXTile() < _e.getXTile()) {
            return 3;
        } else if (_player.getXTile() > _e.getXTile()) {
            return 1;
        }

        return -1;
    }

    protected int calculateRowDirection() {
        if (_player.getYTile() < _e.getYTile()) {
            return 0;
        } else if (_player.getYTile() > _e.getYTile()) {
            return 2;
        }
        return -1;
    }

}
