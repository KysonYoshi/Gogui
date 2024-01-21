GoGui
=====

GoGui is a graphical interface for programs that play the game of Go.

GoGui was initially developed by [Markus Enzenberger](https://github.com/enz), and hosted on [sourceforge](http://gogui.sourceforge.net/). This is an unofficial branch developed with the aim of supporting other games besides Go. Other games can be supported by implementing some extensions to the Rule package in GoGui.

See [GoGUi Note](https://hackmd.io/7PkZfrOcQWS4jx7z3v0tHg?view#step-7-Add-field-background) or below for how to add new games.

# GoGui Note
## Installation
1. Download [NetBeans](https://netbeans.apache.org/) IDE
2. Clone the code from [github](https://github.com/iis-rlg-lab/gogui-general).
3. Add a new project.

![](https://i.imgur.com/FX7LEev.png)

![](https://i.imgur.com/DXIsnlw.png)

![](https://i.imgur.com/aq8kr7n.png)

4. Find the file that we just cloned from github.

![](https://i.imgur.com/XzSQDQw.png)

![](https://i.imgur.com/lHQETOB.png)

Finish!! Let's add a new game~
    
## Add Game
Four games have been pre-written, OOGomoku, Gomoku, Go, Hex. To learn how to add new games, we will use the addition of Hex game as an example. You can also check the [commit](https://github.com/iis-rlg-lab/gogui-general/commit/9efdd1103dca85fafbdfa6eb3a8bade39bd088b7) on [GitHub](https://github.com/iis-rlg-lab/gogui-general).
<a id="TODO-以Hex-game為例"></a>
### Using Hex game as an example:
1. [Add game rules](#新增遊戲規則) 
2. [Plug the game into menu](#step2)
3. [Add a new game board (optional)](#step3)
4. [Add an interface for drawing different shape of field background (optional)](#step4)
<a id="新增遊戲規則"></a>   
##   
### Add Hex game rule: [commit](https://github.com/iis-rlg-lab/gogui-general/commit/9efdd1103dca85fafbdfa6eb3a8bade39bd088b7#diff-b84726bc0e34416a1586d096d6344ed83a3ce4030fbd577d9f527dd78c1df7d7)
#### Step 1: create new class for new game rule in package net.sf.gogui.rule
File: `net\sf\gogui\rule`
![](https://i.imgur.com/WiXvYOb.png)
1. When adding new game rules, inherit the Rule.java abstract class's interface to implement. 
2. The main functions to implement are as follows:
```java=25
public abstract String gameName();

public abstract void reset(int boardsize);

public abstract boolean isEnd();

public abstract boolean isLegalMove(Point p);

public abstract List<Point> play(Point p);
```
- **getName**(): Return game name.
- **reset**(): initialize.
- **isEnd**(): To test whether one side wins.
- **isLegalMove**(): To test whether the chess piece is a legal move。
- **play**(): Implement the game rule. Play this chess piece; if there are any changes on the board, e.g., a piece is captured, the color of pieces change, etc., return a list of pieces that need to be changed. If there are no change, return `null`. (more detials of `Point` see: [Rule.java](https://github.com/iis-rlg-lab/gogui-general/blob/main/src/net/sf/gogui/rule/Rule.java))
3. The following message return function is actually optional, and the return message has been preset, and the developer can also define it by themselves.
```java=37
public String getEndMoveMainMessage() {
    return "Game finished";
}

public String getEndMoveoptionalMessage() {
    return "The game is finished.";
}

public String getIllegalMoveMainMessage() {
    return "Play illegal move";
}

public String getIllegalMoveoptionalMessage() {
    return "This move violates the rule.";
}

public String getIllegalMoveDestructiveOption() {
    return "Play illegal";
}
```
![](https://i.imgur.com/YMNtOht.png)
 ![](https://i.imgur.com/1UPWyiD.png)
- **getEndMoveMainMessage**(): Message title when the game ends.
- **getEndMoveoptionalMessage**(): Message displayed when the game ends.
- **getIllegalMoveMainMessage**(): Message title when an illegal move is made.
- **getIllegalMoveoptionalMessage**(): Message displayed when an illegal move is made.
- **getIllegalMoveDestructiveOption**(): Button name to choose when making an illegal move.

    
<a id="step2"></a>
##
### Plug the game into menu
#### Step 1: Add to game menu: [commit](https://github.com/iis-rlg-lab/gogui-general/commit/9efdd1103dca85fafbdfa6eb3a8bade39bd088b7#diff-e19ed4d71c99bf60a8b61c49e703e5485d5c6a1f6623ad5a7e70bf3c723493e8)
- GoGuiMenuBar
File: `net\sf\gogui\gogui\GoGuiMenuBar.java`
After writing the game rules and game board, add a new game menu for users to switch games. 
![](https://i.imgur.com/3hGSTnf.png)
```java=427
menu.addRadioItem(gamesgroup, actions.m_actionOOGomoku);
menu.addRadioItem(gamesgroup, actions.m_actionGomoku);
menu.addRadioItem(gamesgroup, actions.m_actionGo);
menu.addRadioItem(gamesgroup, actions.m_actionHex);
// <TODO>
        
```
Use **addRadioItem** and **gamesgroup** to add the new game to the button group and input the action to be performed when the option is selected.

##
#### Step 2: Construct the game: [commit](https://github.com/iis-rlg-lab/gogui-general/commit/9efdd1103dca85fafbdfa6eb3a8bade39bd088b7#diff-0b16348700e9a39f668620c1c4e7f0d4cabd9f99c52e04a4730285d090077c32)


- GoGuiActions
File: `net\sf\gogui\gogui\GoGuiActions.java`
```java=183
public final GuiAction m_actionOOGomoku =
    new GuiAction(i18n("ACT_OOGomoku")) {
        public void actionPerformed(ActionEvent e) {
            m_goGui.actionSetGame(new OOGomoku(m_goGui.getGame().getSize()), new DrawGoBoard(m_goGui.getGuiBoard().getBoardPainter().m_boardStatus, new double[]{1, 0}, new double[]{ 0, 1}), new GoFieldFactory(), 15); } };

public final GuiAction m_actionGomoku =
    new GuiAction(i18n("ACT_Gomoku")) {
        public void actionPerformed(ActionEvent e) {
            m_goGui.actionSetGame(new Gomoku(m_goGui.getGame().getSize()), new DrawGoBoard(m_goGui.getGuiBoard().getBoardPainter().m_boardStatus, new double[]{1, 0}, new double[]{ 0, 1}), new GoFieldFactory(), 15); } };

public final GuiAction m_actionGo =
    new GuiAction(i18n("ACT_Go")) {
        public void actionPerformed(ActionEvent e) {
            m_goGui.actionSetGame(new Go(m_goGui.getGame().getSize()), new DrawGoBoard(m_goGui.getGuiBoard().getBoardPainter().m_boardStatus, new double[]{1, 0}, new double[]{ 0, 1}), new GoFieldFactory(), 19); } };

public final GuiAction m_actionHex =
    new GuiAction(i18n("ACT_Hex")) {
        public void actionPerformed(ActionEvent e) {
            m_goGui.actionSetGame(new Hex(m_goGui.getGame().getSize()), new DrawHexBoard(m_goGui.getGuiBoard().getBoardPainter().m_boardStatus, new double[]{1, 0}, new double[]{ 0.5, Math.sqrt(3)/2}), new HexFieldFactory(), 11); } };
// <TODO>

```

When the game option is clicked, the first step is to define the action to be performed by **m_actionHex**.
1. Construct a GuiAction type action, with the input being the name on the option. For naming conventions, please refer to [i18n](https://openhome.cc/Gossip/Rails/i18n.html).
2. Implement the constructor for this **GuiAction** directly.
3. The main purpose of this constructor is to use **actionSetGame** `net\sf\gogui\gogui\GoGui.java` to set a new game, requiring the input of the game rules with **Hex**(), the game board with **DrawHexBoard**(), the field property with **HexFieldFactory**(), and the default number of grid spaces on the board.

##
#### Step 3 Define [i18n](https://openhome.cc/Gossip/Rails/i18n.html) naming format: [commit](https://github.com/iis-rlg-lab/gogui-general/commit/9efdd1103dca85fafbdfa6eb3a8bade39bd088b7#diff-2d926892881a2d55ded2c0fad98307712fa69f74b1095f6a5c91825e09c4001a)

- text.properties
File: `net\sf\gogui\gogui\text.properties`
```java=26
ACT_OOGomoku=&OOGomoku
ACT_Gomoku=&Gomoku
ACT_Go=&Go
ACT_Hex=&Hex
// <TODO>
 
```
    
 <a id="step3"></a>
## 
### Add a new game board (optional)
#### Step 1: Draw a new board : [commit](https://github.com/iis-rlg-lab/gogui-general/commit/9efdd1103dca85fafbdfa6eb3a8bade39bd088b7#diff-4d8b34c3cbe0f060119735e9e5299e797522db4ca817fd8c8b12ed403fe4333c)
#### If you want to draw a new chessboard, different from Go or Hex game boards, e.g., chess or Chinese chess boards, you need to implement the following:

#### create new class (Draw{***gameName***}Board e.g. DrawHexBoard) for drawing different board (optional)
File: `net\sf\gogui\boardpainter`
![](https://i.imgur.com/gQdlKNS.png)

#### To draw a new game board, there are two methods: mesh-style drawing and custom irregular drawing. For a more regular grid-like game board, inherit the pre-written **DrawMeshBoard**.java. For a more irregular game board, inherit **DrawBoard** only.
1. Custom drawing:

```java=12
public abstract class DrawBoard
{
    public DrawBoard(BoardStatus boardStatus)
    {
        m_boardStatus = boardStatus;
    }
    
    public BoardStatus m_boardStatus;
    
    public abstract void draw(Graphics graphics);
    
    public abstract GoPoint getPoint(Point point);
    
    public abstract Point getLocation(int x, int y);
    
    public int[] getScreenRatio()
    {
        return new int[]{1, 1};
    }
    
    public int getFieldSize(double borderSize)
    {
        return Math.round((float)Math.floor(m_boardStatus.getWidth() / (m_boardStatus.getSize() + 2 * borderSize)));
    }
    
    public int getFieldOffset()
    {
        return (m_boardStatus.getWidth() - m_boardStatus.getSize() * m_boardStatus.getFieldSize()) / 2;
    }
}
```
You need to implement the following functions:
- **draw**(): Draw the board.
- **getPoint**(): Convert window pixel coordinates to chessboard coordinates (used by external programs that read mouse coordinates).
- **getLocation**():  Convert chessboard coordinates to window pixel coordinates (used frequently in `net\sf\gogui\boardpainter\BoardPainter.java`).
- **getScreenRatio**(): Chessboard window ratio; If it's a square chessboard, you don't need to implement this function as it defaults to 1:1. If it's a rectangular board, you need to set the ratio, e.g., 16:9 or 70:45 or ...
- **getFieldSize**(): Set the size of a square on the chessboard. (window size / board size)
- **getFieldOffset**(): Set the size of the chessboard boundary.

2. Mesh-style drawing:
![](https://i.imgur.com/KscsEzg.png)

- In **DrawMeshBoard**, the **getPoint**() and **getLocation**() parts have already been implemented.
- If you want to draw a mesh-style chessboard, such as a Hex board, you only need to implement the **draw**(), **getScreenRatio**(), **getFieldSize**(), and **getFieldSize**() functions.
- When implementing the mesh-style drawing method, you need to provide two direction vectors {horizontal}, {vertical} during construction, such as in a Go board: {1, 0}, {0, 1}, and a Hex board: {1, 0}, {0.5, sqrt(3)/2}. **P.S.** Remember to convert v1 and v2 into unit vectors.
    
When drawing the game board, some board parameters can be obtained using BoardStatus:
```java=11
    public class BoardStatus 
{
    public BoardStatus(BoardPainter boardpainter)
    {
        m_painter = boardpainter;
    }
    
    public int getFieldSize()
    {
        return m_painter.m_fieldSize;
    }
    
    public int getFieldOffset()
    {
        return m_painter.m_fieldOffset;
    }
    
    public boolean getFlipVertical()
    {
        return m_painter.m_flipVertical;
    }
    
    public boolean getFlipHorizontal()
    {
        return m_painter.m_flipHorizontal;
    }
    
    public int getSize()
    {
        return m_painter.m_size;
    }
    
    public int getWidth()
    {
        return m_painter.m_width;
    }
    
    public Point getCenter(int x, int y)
    {
        return m_painter.getCenter(x, y);
    }
    
    public BoardConstants getConstants()
    {
        return m_painter.m_constants;
    }
    
    private BoardPainter m_painter;
}
```
- **getFlipVertical**(): It will return a boolean value, if true, it'll vertically flip the chessboard coordinates.
- **getFlipHorizontal**(): It will return a boolean value, if true, it'll horizontally flip the chessboard coordinates.
- **getSize**(): Number of chessboard squares.
- **getWidth**(): Width of the chessboard.
- **getCenter**(): Convert chessboard coordinates to window pixel coordinates.
- **getConstants**(): Other chessboard parameters.

##
#### Step 2 Add new grid shape (optional): [commit](https://github.com/iis-rlg-lab/gogui-general/commit/9efdd1103dca85fafbdfa6eb3a8bade39bd088b7#diff-7c6ffce60eefe91c3b2aa608a4b6542ad18957d59d286ac249ea78ac4b717e59)
- When drawing the grid for the chessboard, you can use pre-drawn shape objects, such as **DrawHexagonGrid**, **DrawCrossGrid**, **DrawSquareGrid**, etc.
- During construction, input the grid color and thickness. When drawing the grid, input the coordinates and fieldSize.
- If you need to define your own shape, you can create a new class that inherits from **DrawBasicGrid**.
File: `net\sf\gogui\boardpainter\DrawHexagonGrid.java`
```java=12
public class DrawHexagonGrid extends DrawBasicGrid
{
    public DrawHexagonGrid(Color color, int thicknese) 
    {
        super(color, thicknese);
    }

    public void draw(Graphics g, double x, double y, int fieldSize) 
    {
        int[] xPoints = new int[6];
        int[] yPoints = new int[6];
        
        int size = (int)(fieldSize/ Math.sqrt(3));

        for (int i = 0; i < 6; i++) 
        {
            double angle = 2 * Math.PI / 6 * (i + 0.5);
            xPoints[i] = (int) (x + size * Math.cos(angle));
            yPoints[i] = (int) (y + size * Math.sin(angle));
        }
        Graphics2D g2 = (Graphics2D)g;
        g2.setColor(getColor());
        g2.setStroke(new BasicStroke(getThicknese())); 
        g2.drawPolygon(xPoints, yPoints, 6);
    }
}
```


<a id="step4"></a>
## 
### Add an interface for drawing different shape of field background (optional)
#### Step 1: Add an interface for drawing different shape of field background: [commit](https://github.com/iis-rlg-lab/gogui-general/commit/31776b800a93534d32304d49e02aaa5523b1e2de#diff-c3efe99082e9d931508103867ef2eb647e7beca40f3bfae9ef6396d92828e081)
File: `net\sf\gogui\boardpainter\HexFieldFactory.java`
    
![](https://hackmd.io/_uploads/S1AvEbWLn.png)


![](https://hackmd.io/_uploads/HJ0sVlbL2.png)
#### In this part, we will implement the visualization functionality used in the analysis tool. Since different chessboards require displaying different background shapes, we are using the "Factory" object-oriented design pattern here.
```java=2
package net.sf.gogui.boardpainter;

/**
 *
 * 
 */
public interface FieldFactory
{
    public Field createField();
}
```
- In this step, we only need to implement the **createField**() function. You can refer to the following Hex examples.
```java=2
package net.sf.gogui.boardpainter;

/**
 *
 * @author tylerliu
 */
public class HexFieldFactory implements FieldFactory
{
    public Field createField()
    {
        return new Field(new DrawHexProperty());
    }
}
```
- Here we inherit the FieldFactory interface, which can create a field object with the desired field property.

##
#### Step 2: Implement the shape of field background [commit](https://github.com/iis-rlg-lab/gogui-general/commit/31776b800a93534d32304d49e02aaa5523b1e2de#diff-dfc7c1a8cd5432b9c2eaf24b3d098bb5433caa340db2200932a72fd0325ac6fe)
File: `net\sf\gogui\boardpainter\DrawHexProperty.java`
```java=11
public abstract class DrawProperty
{
    public abstract void fillBackGround(Graphics graphics, int width, int height, Color color);

    public double getHeightRatio()
    {
        return 1.0;
    }

    public double getWidthRatio()
    {
        return 1.0;
    }
}
```

#### We inherit the DrawProperty interface for the Property part, allowing developers to use this interface to implement customized Field properties.
- **fillBackGround**(): This function is similar to the "draw grid" part in the previous steps. You can refer to the following example.
```java=11
public class DrawHexProperty extends DrawProperty
{
    public void fillBackGround(Graphics graphics, int width, int height, Color color)
    {
        int x = width / 2;
        int y = height / 2;
        int[] xPoints = new int[6];
        int[] yPoints = new int[6];
        int xsize = (int)(width / Math.sqrt(3));
        int ysize = (int)(width / Math.sqrt(3));
        for (int i = 0; i < 6; i++) 
        {
            double angle = 2 * Math.PI / 6 * (i + 0.5);
            xPoints[i] = (int) (x + xsize * Math.cos(angle));
            yPoints[i] = (int) (y + ysize * Math.sin(angle));
        }
        graphics.setColor(color);
        graphics.fillPolygon(xPoints, yPoints, 6);
    }

    public double getHeightRatio()
    {
        return 2 * Math.sqrt(3) / 3;
    }
}
```
