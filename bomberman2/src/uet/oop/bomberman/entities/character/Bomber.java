package uet.oop.bomberman.entities.character;

import java.awt.Color;
import java.util.ArrayList;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.LayeredEntity;
import uet.oop.bomberman.entities.bomb.Bomb;
import uet.oop.bomberman.entities.tile.Wall;
import uet.oop.bomberman.graphics.Screen;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.input.Keyboard;

import java.util.Iterator;
import java.util.List;

import uet.oop.bomberman.entities.bomb.Flame;
import uet.oop.bomberman.entities.character.enemy.Enemy;
import uet.oop.bomberman.entities.tile.item.Item;
import uet.oop.bomberman.level.Coordinates;
import uet.oop.bomberman.audio.AudioGame;
import uet.oop.bomberman.entities.Message;
import uet.oop.bomberman.entities.bomb.FlameSegment;
import uet.oop.bomberman.entities.tile.item.BombItem;
import uet.oop.bomberman.entities.tile.item.FlameItem;
import uet.oop.bomberman.entities.tile.item.SpeedItem;

public class Bomber extends Character {

    private List<Bomb> _bombs;
    protected Keyboard _input;
    public static List<Item> _items = new ArrayList<>();
    /**
     * nếu giá trị này < 0 thì cho phép đặt đối tượng Bomb tiếp theo, cứ mỗi lần
     * đặt 1 Bomb mới, giá trị này sẽ được reset về 0 và giảm dần trong mỗi lần
     * update()
     */
    protected int _timeBetweenPutBombs = 0;

    static int _timeUsedBombItem = 0;
    public static boolean _UsedBombItem = false;

    static int _timeUsedFlameItem = 0;
    static boolean _UsedFlameItem = false;

    static int _timeUsedSpeedItem = 0;
    static boolean _UsedSpeedItem = false;

    public Bomber(int x, int y, Board board) {
        super(x, y, board);
        _bombs = _board.getBombs();
        _input = _board.getInput();
        _sprite = Sprite.player_right;
    }

    @Override
    public void update() {
        clearBombs();
        if (!_alive) {
            afterKill();
            return;
        }

        if (_timeBetweenPutBombs < -7500) {
            _timeBetweenPutBombs = 0;
        } else {
            _timeBetweenPutBombs--;
        }

        timeLimitUsedItem();

        animate();

        calculateMove();

        detectPlaceBomb();

    }

    @Override
    public void render(Screen screen) {
        calculateXOffset();

        if (_alive) {
            chooseSprite();
        } else {
            _sprite = Sprite.player_dead1;
        }

        screen.renderEntity((int) _x, (int) _y - _sprite.SIZE, this);
    }

    public void calculateXOffset() {
        int xScroll = Screen.calculateXOffset(_board, this);
        Screen.setOffset(xScroll, 0);
    }

    /**
     * Kiểm tra xem có đặt được bom hay không? nếu có thì đặt bom tại vị trí
     * hiện tại của Bomber
     */
    private void detectPlaceBomb() {
        // @todo: kiểm tra xem phím điều khiển đặt bom có được gõ và giá trị _timeBetweenPutBombs, Game.getBombRate() có thỏa mãn hay không
        // @todo:  Game.getBombRate() sẽ trả về số lượng bom có thể đặt liên tiếp tại thời điểm hiện tại
        // @todo: _timeBetweenPutBombs dùng để ngăn chặn Bomber đặt 2 Bomb cùng tại 1 vị trí trong 1 khoảng thời gian quá ngắn
        // @todo: nếu 3 điều kiện trên thỏa mãn thì thực hiện đặt bom bằng placeBomb()
        // @todo: sau khi đặt, nhớ giảm số lượng Bomb Rate và reset _timeBetweenPutBombs về 0
        int bombRate = Game.getBombRate();

        if (_input.space && _timeBetweenPutBombs < 0 && bombRate >= 1) {
            double centerX = _x + _sprite.getRealWidth() / 2;
            double centerY = _y - _sprite.getRealHeight() / 2;
            placeBomb(Coordinates.pixelToTile(centerX), Coordinates.pixelToTile(centerY));
            AudioGame.playDropBomb();
            Game.addBombRate(-1);
            _timeBetweenPutBombs = 30;  // Tương ứng với 30 khung hình.
        }
    }

    protected void placeBomb(int x, int y) {
        // @todo thực hiện tạo đối tượng bom, đặt vào vị trí (x, y)
        Bomb bomb = new Bomb(x, y, _board);
        _bombs.add(bomb);
    }

    private void clearBombs() {
        Iterator<Bomb> bs = _bombs.iterator();

        Bomb b;
        while (bs.hasNext()) {
            b = bs.next();
            if (b.isRemoved()) {
                bs.remove();
                Game.addBombRate(1);
            }
        }

    }

    // giới hạn thời gian sử dụng các item 
    private void timeLimitUsedItem() {
        // flameItem
        if (_UsedFlameItem == true && _timeUsedFlameItem <= 0) {
            _UsedFlameItem = false;
            if (Game.getBombRadius() > 1) {
                Game.addBombRadius(-1);
            }
        }
        if (_UsedFlameItem) {
            _timeUsedFlameItem--;
        }

        // SpeedItem
        if (_UsedSpeedItem == true && _timeUsedSpeedItem <= 0) {
            _UsedSpeedItem = false;
            if (Game.getBomberSpeed() > 1) {
                Game.addBomberSpeed(-1);
            }
        }
        if (_UsedSpeedItem) {
            _timeUsedSpeedItem--;
        }

        // BombItem
        if (_UsedBombItem == true && _timeUsedBombItem <= 0) {
            _UsedBombItem = false;
            Game._BombBeforeNextLevel = 1;
            Game.addBombRate(-1);
        }
        if (_UsedBombItem) {
            _timeUsedBombItem--;
        }
    }

    @Override
    public void kill() {
        if (!_alive) {
            return;
        }
        _alive = false;
        _board.addLefts(-1);
        Message msg = new Message("-1 LEFT", getXMessage(), getYMessage(), 2, Color.white, 14);
        _board.addMessage(msg);
        AudioGame.playDeath();
    }

    @Override
    protected void afterKill() {
        if (_timeAfter > 0) {
            --_timeAfter;
        } else {
            if (_board.getLefts() == 0) {
                _board.endGame();
            } else {
                _board.restartLevel();
            }
        }
    }

    @Override
    protected void calculateMove() {
        // @todo: xử lý nhận tín hiệu điều khiển hướng đi từ _input và gọi move() để thực hiện di chuyển
        // @todo: nhớ cập nhật lại giá trị cờ _moving khi thay đổi trạng thái di chuyển
        _moving = true;

        if (_input.up) {
            move(0, -Game.getBomberSpeed());
        } else if (_input.down) {
            move(0, Game.getBomberSpeed());
        } else if (_input.left) {
            move(-Game.getBomberSpeed(), 0);
        } else if (_input.right) {
            move(Game.getBomberSpeed(), 0);
        } else {
            _moving = false;
        }
    }

    @Override
    public boolean canMove(double x, double y) {
        // @todo: kiểm tra có đối tượng tại vị trí chuẩn bị di chuyển đến và có thể di chuyển tới đó hay không
        int tileX = Coordinates.pixelToTile(x);
        int tileY = Coordinates.pixelToTile(y);
//        System.out.println(tileX + " " + tileY);
        Entity nextEntity = _board.getEntity(tileX, tileY, this);
        return collide(nextEntity);
    }

    public void moveCenterX() {
        int pixelOfEntity = Coordinates.tileToPixel(1);
        double centerX = _x + _sprite.getRealWidth() / 2;
        int tileCenterX = Coordinates.pixelToTile(centerX);
        _x = Coordinates.tileToPixel(tileCenterX) + pixelOfEntity / 2 - _sprite.getRealWidth() / 2;
    }

    public void moveCenterY() {
        int pixelOfEntity = Coordinates.tileToPixel(1);
        double centerY = _y - _sprite.getRealHeight() / 2;
        int tileCenterY = Coordinates.pixelToTile(centerY);
        _y = Coordinates.tileToPixel(tileCenterY) + pixelOfEntity / 2 + _sprite.getRealHeight() / 2;
    }

    public void autoMoveCenter() {
        int pixelOfEntity = Coordinates.tileToPixel(1);
        double centerX = _x + _sprite.getRealWidth() / 2;
        double centerY = _y - _sprite.getRealHeight() / 2;

        boolean contactTop = !canMove(centerX, centerY - pixelOfEntity / 2);
        boolean contactDown = !canMove(centerX, centerY + pixelOfEntity / 2);
        boolean contactLeft = !canMove(centerX - pixelOfEntity / 2, centerY);
        boolean contactRight = !canMove(centerX + pixelOfEntity / 2, centerY);

        // Các trường hợp đi một nửa người vào tường cũng tự động căn giữa.
        if (_direction != 0 && contactDown) {
            moveCenterY();
        }
        if (_direction != 1 && contactLeft) {
            moveCenterX();
        }
        if (_direction != 2 && contactTop) {
            moveCenterY();
        }
        if (_direction != 3 && contactRight) {
            moveCenterX();
        }
    }

    @Override
    public void move(double xa, double ya) {
        // @todo: sử dụng canMove() để kiểm tra xem có thể di chuyển tới điểm đã tính toán hay không và thực hiện thay đổi tọa độ _x, _y
        // @todo: nhớ cập nhật giá trị _direction sau khi di chuyển : up, right, down, left -> 0, 1, 2, 3
        // @todo: Di chuyển nhân vật ra giữa.

        // Tính tọa độ tâm người
        double centerX = _x + _sprite.getRealWidth() / 2;
        double centerY = _y - _sprite.getRealHeight() / 2;

        if (xa > 0) {
            _direction = 1;
        }
        if (xa < 0) {
            _direction = 3;
        }
        if (ya > 0) {
            _direction = 2;
        }
        if (ya < 0) {
            _direction = 0;
        }
        if (canMove(centerX + xa, centerY + ya)) {
            _x += xa;
            _y += ya;
        }

        autoMoveCenter();
    }

    public boolean handleCollidePortal() {
        if (_board.detectNoEnemies()) {
            _board.nextLevel();
            return true;
        }

        return false;
    }

    @Override
    public boolean collide(Entity e) {
        // @todo: xử lý va chạm với Flame
        // @todo: xử lý va chạm với Enemy

        if (e instanceof Flame) {
            this.kill();
            return true;
        }

        if (e instanceof Enemy) {
            this.kill();
            return true;
        }

        if (e instanceof Wall) {
            return false;
        }

        if (e instanceof Bomb) {
            return e.collide(this);
        }

        if (e instanceof LayeredEntity) {
            return e.collide(this);
        }

        return true;
    }

    private void chooseSprite() {
        switch (_direction) {
            case 0:
                _sprite = Sprite.player_up;
                if (_moving) {
                    _sprite = Sprite.movingSprite(Sprite.player_up_1, Sprite.player_up_2, _animate, 20);
                }
                break;
            case 1:
                _sprite = Sprite.player_right;
                if (_moving) {
                    _sprite = Sprite.movingSprite(Sprite.player_right_1, Sprite.player_right_2, _animate, 20);
                }
                break;
            case 2:
                _sprite = Sprite.player_down;
                if (_moving) {
                    _sprite = Sprite.movingSprite(Sprite.player_down_1, Sprite.player_down_2, _animate, 20);
                }
                break;
            case 3:
                _sprite = Sprite.player_left;
                if (_moving) {
                    _sprite = Sprite.movingSprite(Sprite.player_left_1, Sprite.player_left_2, _animate, 20);
                }
                break;
            default:
                _sprite = Sprite.player_right;
                if (_moving) {
                    _sprite = Sprite.movingSprite(Sprite.player_right_1, Sprite.player_right_2, _animate, 20);
                }
                break;
        }

    }

    // TODO1
    public void addItem(Item aThis) {
        int _time = 1000;
        if (aThis.isRemoved()) {
            return;
        }
        _items.add(aThis);
        aThis.setValues();
        if (aThis instanceof BombItem) {
            Game._BombBeforeNextLevel++;
            _timeUsedBombItem = _time; // ~ 120 seconds
            _UsedBombItem = true;
            System.out.println(Game.getBombRate());
        } else if (aThis instanceof SpeedItem) {
            _timeUsedSpeedItem = _time;
            _UsedSpeedItem = true;
        } else if (aThis instanceof FlameItem) {
            _timeUsedFlameItem = _time;
            _UsedFlameItem = true;
        }

    }

    public void clearUsedPowerups() {
        for (int i = 0; i < _items.size(); i++) {
            if (!_items.get(i).isActive()) {
                _items.remove(i);
                i--;
            }
        }
    }

    public void removePowerups() {
        for (int i = 0; i < _items.size(); i++) {
            _items.remove(i);
            i--;
        }
    }

    public List<Bomb> get_bombs() {
        return _bombs;
    }

}
