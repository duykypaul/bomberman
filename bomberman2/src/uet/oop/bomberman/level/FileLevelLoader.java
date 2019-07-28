package uet.oop.bomberman.level;

import java.io.*;
import java.util.ArrayList;
import uet.oop.bomberman.Board;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.entities.LayeredEntity;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.entities.character.enemy.Balloon;
import uet.oop.bomberman.entities.character.enemy.Doll;
import uet.oop.bomberman.entities.character.enemy.Kondoria;
import uet.oop.bomberman.entities.character.enemy.Oneal;
import uet.oop.bomberman.entities.tile.Grass;
import uet.oop.bomberman.entities.tile.Portal;
import uet.oop.bomberman.entities.tile.Wall;
import uet.oop.bomberman.entities.tile.destroyable.Brick;
import uet.oop.bomberman.entities.tile.item.BombItem;
import uet.oop.bomberman.entities.tile.item.FlameItem;
import uet.oop.bomberman.entities.tile.item.SpeedItem;
import uet.oop.bomberman.exceptions.LoadLevelException;
import uet.oop.bomberman.graphics.Screen;
import uet.oop.bomberman.graphics.Sprite;

public class FileLevelLoader extends LevelLoader {

    /**
     * Ma trận chứa thông tin bản đồ, mỗi phần tử lưu giá trị kí tự đ�?c được từ
     * ma trận bản đồ trong tệp cấu hình
     */
    public FileLevelLoader(Board board, int level) throws LoadLevelException {
        super(board, level);
    }

    @Override
    public void loadLevel(int level) {
        ArrayList<String> s = new ArrayList<>();
        FileReader fr = null;
        try {
            // TODO: ??c d? li?u t? t?p c?u h�nh /levels/Level{level}.txt
            fr = new FileReader("res\\levels\\Level" + level + ".txt");
            BufferedReader br = new BufferedReader(fr);
            String str = br.readLine();
            int line = 0;
            while (str != null) {
                line++;
                s.add(str);
                str = br.readLine();
            }
            br.close();
            fr.close();
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        }
        // TODO: c?p nh?t c�c gi� tr? ??c ???c v�o _width, _height, _level, _map
        String[] ar = s.get(0).trim().split(" ");
        _level = Integer.parseInt(ar[0]);
        _height = Integer.parseInt(ar[1]);
        _width = Integer.parseInt(ar[2]);
        _map = new char[_height][_width];
        for (int i = 0; i < _height; i++) {
            for (int j = 0; j < _width; j++) {
                _map[i][j] = s.get(i + 1).charAt(j);
            }
        }

    }

    @Override
    public void createEntities() {
        // TODO: tạo các Entity của màn chơi
        // TODO: sau khi tạo xong, g�?i _board.addEntity() để thêm Entity vào game

        // TODO: phần code mẫu ở dưới để hướng dẫn cách thêm các loại Entity vào game
        // TODO: hãy xóa nó khi hoàn thành chức năng load màn chơi từ tệp cấu hình
        // thêm Wall
        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                int pos = x + y * getWidth();
                char c = _map[y][x];
                switch (c) {
                    // Thêm grass
                    case ' ':
                        _board.addEntity(pos, new Grass(x, y, Sprite.grass));
                        break;
                    // Thêm Wall
                    case '#':
                        _board.addEntity(pos, new Wall(x, y, Sprite.wall));
                        break;
                    // Thêm Cửa
                    case 'x':
                        _board.addEntity(pos, new LayeredEntity(x, y,
                                new Grass(x, y, Sprite.grass),
                                new Portal(x, y, _board, Sprite.portal),
                                new Brick(x, y, Sprite.brick)));
                        break;
                    // Tường có thể phá
                    case '*':
                        _board.addEntity(x + y * _width,
                                new LayeredEntity(x, y,
                                        new Grass(x, y, Sprite.grass),
                                        new Brick(x, y, Sprite.brick)
                                )
                        );
                        break;
                    // Thêm Bomber
                    case 'p':
                        _board.addCharacter(new Bomber(Coordinates.tileToPixel(x), Coordinates.tileToPixel(y) + Game.TILES_SIZE, _board));
                        Screen.setOffset(0, 0);
                        _board.addEntity(x + y * _width, new Grass(x, y, Sprite.grass));
                        break;

                    // Thêm balloon
                    case '1':
                        _board.addCharacter(new Balloon(Coordinates.tileToPixel(x), Coordinates.tileToPixel(y) + Game.TILES_SIZE, _board));
                        _board.addEntity(x + y * _width, new Grass(x, y, Sprite.grass));
                        break;
                    // Thêm oneal
                    case '2':
                        _board.addCharacter(new Oneal(Coordinates.tileToPixel(x), Coordinates.tileToPixel(y) + Game.TILES_SIZE, _board));
                        _board.addEntity(pos, new Grass(x, y, Sprite.grass));
                        break;
                    // Thêm Doll
                    case '3':
                        _board.addCharacter(new Doll(Coordinates.tileToPixel(x), Coordinates.tileToPixel(y) + Game.TILES_SIZE, _board));
                        _board.addEntity(pos, new Grass(x, y, Sprite.grass));
                        break;
                    // Thêm Kondoria
                    case '4':
                        _board.addCharacter(new Kondoria(Coordinates.tileToPixel(x), Coordinates.tileToPixel(y) + Game.TILES_SIZE, _board));
                        _board.addEntity(pos, new Grass(x, y, Sprite.grass));
                        break;
                    case 'b':
                        LayeredEntity layer = new LayeredEntity(x, y,
                                new Grass(x, y, Sprite.grass),
                                new Brick(x, y, Sprite.brick));
                        layer.addBeforeTop(new BombItem(x, y, _level, Sprite.powerup_bombs));
                        _board.addEntity(pos, layer);
                        break;

                    case 's':
                        layer = new LayeredEntity(x, y,
                                new Grass(x, y, Sprite.grass),
                                new Brick(x, y, Sprite.brick));

                        layer.addBeforeTop(new SpeedItem(x, y, _level, Sprite.powerup_speed));
                        _board.addEntity(pos, layer);
                        break;

                    case 'f':
                        System.out.println("xvscv");
                        layer = new LayeredEntity(x, y,
                                new Grass(x, y, Sprite.grass),
                                new Brick(x, y, Sprite.brick));

                        layer.addBeforeTop(new FlameItem(x, y, _level, Sprite.powerup_flames));
                        _board.addEntity(pos, layer);
                        break;

                    default:
                        _board.addEntity(pos, new Grass(x, y, Sprite.grass));
                        break;

                }
            }
        }
    }
}
