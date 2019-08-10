import tester.*;
import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;
import java.util.*;

// constant class
class Constants {
  static final int CELL_SIDE = 50;
  static final Color CELL_COLOR = Color.black;
  static final Color TILE_COLOR = Color.gray;
  static final Color FLAG_COLOR = Color.red;
  static final int TEXT_SIZE = 25;
  static final Color TEXT_COLOR = Color.blue;
  static final int GRID_X = 10;
  static final int GRID_Y = 10;
  static final int MINE_RADIUS = 25;
  static final Color MINE_COLOR = Color.black;
  static final int WORLD_WIDTH = 500;
  static final int WORLD_HEIGHT = 500;
  static final double TICK_RATE = 0.0000001;
}

// represents a cell
class Cell {
  int side;
  Color c1;
  Color c2;
  Color c3;
  ArrayList<Cell> neighbors;
  int id;
  boolean isMine;
  boolean isHidden;
  boolean isFlagged;

  // default constructor
  Cell() {
    this.side = Constants.CELL_SIDE;
    this.c1 = Constants.CELL_COLOR;
    this.c2 = Constants.TILE_COLOR;
    this.c3 = Constants.FLAG_COLOR;
    this.neighbors = new ArrayList<Cell>(8);
    this.id = 0;
    this.isMine = false;
    this.isHidden = true;
    this.isFlagged = false;
  }

  // Draws this tile onto the background at the specified logical coordinates
  WorldScene drawAt(int col, int row, WorldScene background) {
    TextImage t = new TextImage(Integer.toString(this.id), Constants.TEXT_SIZE,
        Constants.TEXT_COLOR);

    if (this.isHidden && this.isFlagged) {
      background.placeImageXY(new RectangleImage(this.side, this.side, "solid", this.c2), col, row);
      background.placeImageXY(new RectangleImage(this.side, this.side, "outline", this.c1), col,
          row);
      background.placeImageXY(new TriangleImage(new Posn(col, row - (this.side / 3)),
          new Posn(col - (this.side / 3), row + (this.side / 3)),
          new Posn(col + (this.side / 3), row + (this.side / 3)), "solid", this.c3), col, row);
      return background;
    }
    else if (this.isHidden && !this.isFlagged) {
      background.placeImageXY(new RectangleImage(this.side, this.side, "solid", this.c2), col, row);
      background.placeImageXY(new RectangleImage(this.side, this.side, "outline", this.c1), col,
          row);
      return background;
    }
    else if (this.isMine) {
      background.placeImageXY(new RectangleImage(this.side, this.side, "outline", this.c1), col,
          row);
      background.placeImageXY(new CircleImage(Constants.MINE_RADIUS, "solid", Constants.MINE_COLOR),
          col, row);
      return background;
    }
    else if (this.id > 0) {
      background.placeImageXY(new RectangleImage(this.side, this.side, "outline", this.c1), col,
          row);
      background.placeImageXY(t, col, row);
      return background;
    }
    else {
      background.placeImageXY(new RectangleImage(this.side, this.side, "outline", this.c1), col,
          row);
      return background;
    }

  }

  // adds respective neighbor cells for corner, edge, and middle cases to their
  // own cells lists
  void addNeighbors(int i, int j, ArrayList<ArrayList<Cell>> cells, int xBound, int yBound) {
    if (i == 0 && j == 0) {
      this.neighbors.add(cells.get(i).get(j + 1));
      this.neighbors.add(cells.get(i + 1).get(j + 1));
      this.neighbors.add(cells.get(i + 1).get(j));
    }

    else if (i == yBound - 1 && j == 0) {
      this.neighbors.add(cells.get(i - 1).get(j));
      this.neighbors.add(cells.get(i - 1).get(j + 1));
      this.neighbors.add(cells.get(i).get(j + 1));
    }

    else if (i == 0 && j == xBound - 1) {
      this.neighbors.add(cells.get(i).get(j - 1));
      this.neighbors.add(cells.get(i + 1).get(j - 1));
      this.neighbors.add(cells.get(i + 1).get(j));
    }

    else if (i == yBound - 1 && j == xBound - 1) {
      this.neighbors.add(cells.get(i - 1).get(j - 1));
      this.neighbors.add(cells.get(i - 1).get(j));
      this.neighbors.add(cells.get(i).get(j - 1));
    }

    else if (i == 0 && j != 0 && j != xBound - 1) {
      this.neighbors.add(cells.get(i).get(j - 1));
      this.neighbors.add(cells.get(i).get(j + 1));
      this.neighbors.add(cells.get(i + 1).get(j - 1));
      this.neighbors.add(cells.get(i + 1).get(j));
      this.neighbors.add(cells.get(i + 1).get(j + 1));
    }

    else if (i != 0 && i != yBound - 1 && j == 0) {
      this.neighbors.add(cells.get(i - 1).get(j));
      this.neighbors.add(cells.get(i + 1).get(j));
      this.neighbors.add(cells.get(i - 1).get(j + 1));
      this.neighbors.add(cells.get(i).get(j + 1));
      this.neighbors.add(cells.get(i + 1).get(j + 1));
    }

    else if (i == yBound - 1 && j != 0 && j != xBound - 1) {
      this.neighbors.add(cells.get(i).get(j - 1));
      this.neighbors.add(cells.get(i).get(j + 1));
      this.neighbors.add(cells.get(i - 1).get(j - 1));
      this.neighbors.add(cells.get(i - 1).get(j));
      this.neighbors.add(cells.get(i - 1).get(j + 1));
    }

    else if (i != 0 && i != yBound - 1 && j == xBound - 1) {
      this.neighbors.add(cells.get(i - 1).get(j));
      this.neighbors.add(cells.get(i + 1).get(j));
      this.neighbors.add(cells.get(i - 1).get(j - 1));
      this.neighbors.add(cells.get(i).get(j - 1));
      this.neighbors.add(cells.get(i + 1).get(j - 1));
    }

    else {
      this.neighbors.add(cells.get(i - 1).get(j - 1));
      this.neighbors.add(cells.get(i - 1).get(j));
      this.neighbors.add(cells.get(i - 1).get(j + 1));
      this.neighbors.add(cells.get(i).get(j - 1));
      this.neighbors.add(cells.get(i).get(j + 1));
      this.neighbors.add(cells.get(i + 1).get(j - 1));
      this.neighbors.add(cells.get(i + 1).get(j));
      this.neighbors.add(cells.get(i + 1).get(j + 1));
    }
  }

  // void method to set the cell to a mine
  void setMine() {
    this.isMine = true;
  }

  // void method to flag a cell
  void setFlag() {
    this.isFlagged = true;
  }

  // sets the cell id field to the number of mines surrounding the cell
  void setId() {
    this.id = this.countMines();
  }

  // void method for testing
  void setId(int num) {
    this.id = num;
  }

  // accumulates the number of mines around the cell
  int countMines() {
    int acc = 0;

    for (Cell c : this.neighbors) {
      if (c.isMine) {
        acc = acc + 1;
      }
      else {
        acc = acc + 0;
      }
    }
    return acc;
  }

  // reveal the piece under the tile
  void setReveal() {
    if (this.id == 0 && !this.isMine) {
      for (Cell c : this.neighbors) {
        c.neighbors.remove(this);
      }
      this.isHidden = false;
      this.flood();
    }
    else {
      this.isHidden = false;
    }
  }

  // initiates flooding to reveal neighbor cells
  void flood() {
    for (Cell c : this.neighbors) {
      c.setReveal();
    }
  }
}

// represents the world
class Minesweeper extends World {
  int mines; // mines number
  ArrayList<ArrayList<Cell>> cells; // the cells
  ArrayList<Posn> pos;
  int totalMines;

  Random rand = new Random();

  // default constructor
  Minesweeper(int mines) {
    this.mines = mines;
    this.cells = this.initCells();
    this.pos = new ArrayList<Posn>();
    this.totalMines = mines;

    this.initMines();
    this.initNeighbors();
  }

  // add neighbors and detect mines in the neighbors
  void initNeighbors() {
    for (int i = 0; i < Constants.GRID_Y; i++) {
      for (int j = 0; j < Constants.GRID_X; j++) {
        this.cells.get(i).get(j).addNeighbors(i, j, this.cells, Constants.GRID_X, Constants.GRID_Y);
        this.cells.get(i).get(j).setId();
      }
    }
  }

  // generates mines
  void initMines() {
    while (this.mines != 0) {
      this.generateMine();
      this.mines = this.mines - 1;
    }
  }

  // generates a mine at a random position, sets the marker
  void generateMine() {
    int x = rand.nextInt(Constants.GRID_X);
    int y = rand.nextInt(Constants.GRID_Y);
    Posn c = new Posn(x, y);

    if (pos.contains(c)) {
      this.generateMine();
    }
    else {
      this.cells.get(y).get(x).setMine();
      pos.add(new Posn(x, y));
    }
  }

  // initializes grid with cells represented in rows and columns
  ArrayList<ArrayList<Cell>> initCells() {

    ArrayList<Cell> col = new ArrayList<Cell>(Constants.GRID_X);
    ArrayList<ArrayList<Cell>> rows = new ArrayList<ArrayList<Cell>>(Constants.GRID_Y);

    for (int i = 0; i < Constants.GRID_Y; i++) {
      for (int j = 0; j < Constants.GRID_X; j++) {
        col.add(new Cell());
      }
      rows.add(col);
      col = new ArrayList<Cell>(Constants.GRID_X);
    }
    return rows;
  }

  // count unopened tiles in the grid
  int countUnopened(int acc) {
    for (int i = 0; i < Constants.GRID_Y; i++) {
      for (int j = 0; j < Constants.GRID_X; j++) {
        if (!this.cells.get(i).get(j).isHidden) {
          acc--;
        }
      }
    }
    return acc;
  }

  // check if any bombs detonated
  boolean checkDetonate() {
    boolean acc = false;
    for (int i = 0; i < Constants.GRID_Y; i++) {
      for (int j = 0; j < Constants.GRID_X; j++) {
        if (!this.cells.get(i).get(j).isHidden && this.cells.get(i).get(j).isMine) {
          acc = true;
        }
      }
    }
    return acc;
  }

  // method to count the number of mines in a grid
  int totalMines() {
    int acc = 0;
    for (int i = 0; i < Constants.GRID_Y; i++) {
      for (int j = 0; j < Constants.GRID_X; j++) {
        if (this.cells.get(i).get(j).isMine) {
          acc++;
        }
      }
    }
    return acc;
  }

  // mouse click state
  public void onMouseClicked(Posn pos, String buttonName) {
    int row = (int) (Math.floor(pos.y / Constants.CELL_SIDE));
    int column = (int) (Math.floor(pos.x / Constants.CELL_SIDE));

    if (buttonName.equals("LeftButton") && this.cells.get(row).get(column).isMine) {
      this.cells.get(row).get(column).setReveal();
    }

    else if (buttonName.equals("LeftButton")) {
      this.cells.get(row).get(column).setReveal();
    }
    else if (buttonName.equals("RightButton") && this.cells.get(row).get(column).isFlagged) {
      this.cells.get(row).get(column).isFlagged = false;
    }
    else if (buttonName.equals("RightButton") && !this.cells.get(row).get(column).isFlagged) {
      this.cells.get(row).get(column).isFlagged = true;
    }
  }

  // draws a winning or losing game scene when prompted
  public WorldScene lastScene(String msg) {
    WorldScene scene = this.makeScene();
    TextImage a = new TextImage(msg, 70, Color.red);
    TextImage b = new TextImage(msg, 70, Color.green);

    if (msg.contains("Win")) {
      scene.placeImageXY(b, Constants.WORLD_WIDTH / 2, Constants.WORLD_HEIGHT / 2);
    }
    else if (msg.contains("Lose")) {
      scene.placeImageXY(a, Constants.WORLD_WIDTH / 2, Constants.WORLD_HEIGHT / 2);
    }
    return scene;
  }

  // end the game when the condition is fulfilled
  public WorldEnd worldEnds() {
    if (this.checkDetonate()) {
      return new WorldEnd(true, this.lastScene("You Lose"));
    }
    if (this.countUnopened(Constants.GRID_X * Constants.GRID_Y) == this.totalMines) {
      return new WorldEnd(true, this.lastScene("You Win!!"));
    }
    else {
      return new WorldEnd(false, this.makeScene());
    }
  }

  // draws the game in entirety
  public WorldScene makeScene() {
    WorldScene scene = this.getEmptyScene();
    for (int i = 0; i < Constants.GRID_Y; i++) {
      for (int j = 0; j < Constants.GRID_X; j++) {
        this.cells.get(i).get(j).drawAt(((j + 1) * 25) + (j * 25), ((i + 1) * 25) + (i * 25),
            scene);
      }
    }
    return scene;
  }
}

// an examples class
class ExampleMineSweeper {
  Cell c1;
  Cell c2;
  Cell c3;
  Cell c4;
  Cell c5;
  Cell c6;
  Cell c7;
  Cell c8;
  Cell c9;

  ArrayList<Cell> a1;
  ArrayList<Cell> a2;
  ArrayList<Cell> a3;

  ArrayList<ArrayList<Cell>> cells;

  WorldScene background1;
  WorldScene background2;
  WorldScene background3;
  WorldScene background4;
  WorldScene background5;
  WorldScene background6;
  WorldScene background7;
  RectangleImage rec1;
  RectangleImage rec2;
  CircleImage circ1;
  TriangleImage tri1;
  TextImage txt1;

  Minesweeper state;
  Minesweeper state2;
  WorldScene scene1;
  WorldScene scene2;

  void initExamples() {
    this.c1 = new Cell();
    this.c2 = new Cell();
    this.c3 = new Cell();
    this.c4 = new Cell();
    this.c5 = new Cell();
    this.c6 = new Cell();
    this.c7 = new Cell();
    this.c8 = new Cell();
    this.c9 = new Cell();

    this.a1 = new ArrayList<Cell>(Arrays.asList(this.c1, this.c2, this.c3));
    this.a2 = new ArrayList<Cell>(Arrays.asList(this.c4, this.c5, this.c6));
    this.a3 = new ArrayList<Cell>(Arrays.asList(this.c7, this.c8, this.c9));

    this.cells = new ArrayList<ArrayList<Cell>>(Arrays.asList(this.a1, this.a2, this.a3));

    this.background1 = new WorldScene(50, 30);
    this.background2 = new WorldScene(50, 30);
    this.background3 = new WorldScene(50, 30);
    this.background4 = new WorldScene(50, 30);
    this.background5 = new WorldScene(50, 30);
    this.background6 = new WorldScene(50, 30);
    this.background7 = new WorldScene(50, 30);
    this.rec1 = new RectangleImage(50, 50, "outline", Color.black);
    this.rec2 = new RectangleImage(50, 50, "solid", Color.gray);
    this.circ1 = new CircleImage(Constants.MINE_RADIUS, "solid", Constants.MINE_COLOR);
    this.tri1 = new TriangleImage(new Posn(1, 3 - (50 / 3)), new Posn(1 - (50 / 3), 3 + (50 / 3)),
        new Posn(1 + (50 / 3), 3 + (50 / 3)), "solid", Color.red);
    this.txt1 = new TextImage(Integer.toString(1), Constants.TEXT_SIZE, Constants.TEXT_COLOR);

    this.background3.placeImageXY(this.rec1, 1, 3);
    this.background3.placeImageXY(this.circ1, 1, 3);

    this.background4.placeImageXY(this.rec2, 1, 3);
    this.background4.placeImageXY(this.rec1, 1, 3);
    this.background4.placeImageXY(this.tri1, 1, 3);

    this.background5.placeImageXY(this.rec2, 1, 3);
    this.background5.placeImageXY(this.rec1, 1, 3);

    this.background6.placeImageXY(this.rec1, 1, 3);
    this.background6.placeImageXY(this.txt1, 1, 3);

    this.background7.placeImageXY(this.rec1, 1, 3);

    this.state = new Minesweeper(10);
    this.state2 = new Minesweeper(10);
    this.scene1 = this.state.makeScene();
    this.scene2 = this.state.makeScene();

    this.scene1.placeImageXY(new TextImage("You Win!!", 70, Color.green), Constants.WORLD_WIDTH / 2,
        Constants.WORLD_HEIGHT / 2);
    this.scene2.placeImageXY(new TextImage("You Lose", 70, Color.red), Constants.WORLD_WIDTH / 2,
        Constants.WORLD_HEIGHT / 2);
  }

  void testInitMines(Tester t) {
    this.initExamples();

    t.checkExpect(this.state.totalMines(), 10);
    t.checkExpect(this.state2.totalMines(), 10);
  }

  void testRandom(Tester t) {
    this.initExamples();

    t.checkExpect(this.state.totalMines(), this.state2.totalMines());
  }

  void testAddNeighbors(Tester t) {
    this.initExamples();
    // first case
    t.checkExpect(this.c1.neighbors.size(), 0);
    this.c1.addNeighbors(0, 0, this.cells, 3, 3);
    t.checkExpect(this.c1.neighbors.size(), 3);

    this.initExamples();
    // second case
    t.checkExpect(this.c2.neighbors.size(), 0);
    this.c2.addNeighbors(0, 1, this.cells, 3, 3);
    t.checkExpect(this.c2.neighbors.size(), 5);

    this.initExamples();
    // third case
    t.checkExpect(this.c3.neighbors.size(), 0);
    this.c3.addNeighbors(0, 2, this.cells, 3, 3);
    t.checkExpect(this.c3.neighbors.size(), 3);

    this.initExamples();
    // fourth case
    t.checkExpect(this.c4.neighbors.size(), 0);
    this.c4.addNeighbors(1, 0, this.cells, 3, 3);
    t.checkExpect(this.c4.neighbors.size(), 5);

    this.initExamples();
    // fifth case
    t.checkExpect(this.c5.neighbors.size(), 0);
    this.c5.addNeighbors(1, 1, this.cells, 3, 3);
    t.checkExpect(this.c5.neighbors.size(), 8);

    this.initExamples();
    // sixth case
    t.checkExpect(this.c6.neighbors.size(), 0);
    this.c6.addNeighbors(1, 2, this.cells, 3, 3);
    t.checkExpect(this.c6.neighbors.size(), 5);

    this.initExamples();
    // seventh case
    t.checkExpect(this.c7.neighbors.size(), 0);
    this.c7.addNeighbors(2, 0, this.cells, 3, 3);
    t.checkExpect(this.c7.neighbors.size(), 3);

    this.initExamples();
    // eighth case
    t.checkExpect(this.c8.neighbors.size(), 0);
    this.c8.addNeighbors(2, 1, this.cells, 3, 3);
    t.checkExpect(this.c8.neighbors.size(), 5);

    this.initExamples();
    // ninth case
    t.checkExpect(this.c9.neighbors.size(), 0);
    this.c9.addNeighbors(2, 2, this.cells, 3, 3);
    t.checkExpect(this.c9.neighbors.size(), 3);
  }

  void testSetFlag(Tester t) {
    this.initExamples();

    t.checkExpect(this.c1.isFlagged, false);
    this.c1.setFlag();
    t.checkExpect(this.c1.isFlagged, true);
  }

  void testSetId(Tester t) {
    this.initExamples();

    t.checkExpect(this.c1.id, 0);
    this.c1.setId(3);
    t.checkExpect(this.c1.id, 3);
  }

  void testSetReveal(Tester t) {
    this.initExamples();

    t.checkExpect(this.c1.isHidden, true);
    this.c1.setReveal();
    t.checkExpect(this.c1.isHidden, false);
  }

  void testSetMine(Tester t) {
    this.initExamples();

    t.checkExpect(this.c1.isMine, false);
    this.c1.setMine();
    t.checkExpect(this.c1.isMine, true);
  }

  void testCountMines(Tester t) {
    this.initExamples();

    this.c1.addNeighbors(0, 0, this.cells, 3, 3);
    t.checkExpect(this.c1.countMines(), 0);

    c1.setId();

    t.checkExpect(this.c1.id, 0);

    t.checkExpect(this.c2.isMine, false);
    this.c2.setMine();
    t.checkExpect(this.c2.isMine, true);

    t.checkExpect(this.c1.countMines(), 1);

    c1.setId();

    t.checkExpect(this.c1.id, 1);

    t.checkExpect(this.c4.isMine, false);
    this.c4.setMine();
    t.checkExpect(this.c4.isMine, true);

    t.checkExpect(this.c1.countMines(), 2);

    c1.setId();

    t.checkExpect(this.c1.id, 2);
  }

  void testLastScene(Tester t) {
    this.initExamples();

    t.checkExpect(this.state.lastScene("You Win!!"), this.scene1);

    this.initExamples();

    t.checkExpect(this.state.lastScene("You Lose"), this.scene2);
  }

  void testCountUnopened(Tester t) {
    this.initExamples();

    t.checkExpect(this.state.countUnopened(100), 100);
  }

  void testCheckDetonate(Tester t) {
    this.initExamples();

    t.checkExpect(this.state.checkDetonate(), false);
  }

  void testTotalMines(Tester t) {
    this.initExamples();

    t.checkExpect(this.state.totalMines(), 10);
  }

  void testDrawAt(Tester t) {
    // first condition
    this.initExamples();

    this.c1.setFlag();

    t.checkExpect(this.background1, this.background2);

    this.c1.drawAt(1, 3, this.background1);

    t.checkExpect(this.background1, this.background4);

    // second condition
    this.initExamples();

    t.checkExpect(this.background1, this.background2);

    this.c1.drawAt(1, 3, this.background1);

    t.checkExpect(this.background1, this.background5);

    // third condition
    this.initExamples();

    this.c1.setMine();
    this.c1.setReveal();

    t.checkExpect(this.background1, this.background2);

    this.c1.drawAt(1, 3, this.background1);

    t.checkExpect(this.background1, this.background3);

    // fourth condition
    this.initExamples();

    t.checkExpect(this.background1, this.background2);

    this.c1.setId(1);
    this.c1.setReveal();
    this.c1.drawAt(1, 3, this.background1);

    t.checkExpect(this.background1, this.background6);

    // fifth condition
    this.initExamples();

    t.checkExpect(this.background1, this.background2);

    this.c1.setReveal();
    this.c1.drawAt(1, 3, this.background1);

    t.checkExpect(this.background1, this.background7);
  }

  // big bang initializer
  void testGame(Tester t) {
    Minesweeper world = new Minesweeper(10); // initialize with amount of mines

    world.bigBang(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, Constants.TICK_RATE);
  }
}
