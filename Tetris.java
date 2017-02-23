package tetris;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Tetris extends JPanel {

    private static int ItemSize = 15;
    private static int ItemWidth = 10;
    private static int ItemHeigth = 24;
    private static double speed = 7.0;
    private int fps = 30;
    private boolean[][] ItemMap = new boolean[ItemHeigth][ItemWidth];
    private int Score = 0;    
    private boolean IsPause = false;
    static boolean[][][] Shape = Items.Shape;
    private Point NowItemPos;
    private boolean[][] NowItemMap;
    private boolean[][] NextItemMap;
    private int NextItemState;
    private int NowItemState;
    private Timer timer;

    public Tetris() {
        this.Initial();
        Double delay = speed*100;
        timer = new Timer(delay.intValue(), this.TimerListener);
        timer.start();
        this.addKeyListener(this.KeyListener);
    }
    
    public Tetris(int fps, double speed, String sequence) {
    	this.fps = fps;
    	this.speed = speed;
    }

    private void getNextItem() {
        this.NowItemState = this.NextItemState;
        this.NowItemMap = this.NextItemMap;
        this.NextItemState = this.CreateNewItemState();
        this.NextItemMap = this.getItemMap(NextItemState);
        this.NowItemPos = this.getNewItemPos();
    }
    
    private boolean checkHit(boolean[][] SrcNextItemMap,Point SrcNextItemPos) {
        for (int i = 0; i < SrcNextItemMap.length;i ++){
            for (int j = 0;j < SrcNextItemMap[i].length;j ++){
                if (SrcNextItemMap[i][j]){
                    if (SrcNextItemPos.y + i >= Tetris.ItemHeigth || SrcNextItemPos.x + j < 0 || SrcNextItemPos.x + j >= Tetris.ItemWidth){
                        return true;
                    }
                    else{
                        if (SrcNextItemPos.y + i < 0){
                            continue;
                        }
                        else{
                            if (this.ItemMap[SrcNextItemPos.y + i][SrcNextItemPos.x + j]){
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean onHit(){
        for (int i = 0;i < this.NowItemMap.length;i ++){
            for (int j = 0;j < this.NowItemMap[i].length;j ++){
                if (this.NowItemMap[i][j])
                    if (this.NowItemPos.y + i < 0)
                        return false;
                    else
                        this.ItemMap[this.NowItemPos.y + i][this.NowItemPos.x + j] = this.NowItemMap[i][j];
            }
        }
        return true;
    }
    
    private Point getNewItemPos(){
        return new Point(Tetris.ItemWidth / 2 - this.NowItemMap[0].length / 2, - this.NowItemMap.length);
    }

    
    public void Initial() {
        for (int i = 0;i < this.ItemMap.length;i ++){
            for (int j = 0;j < this.ItemMap[i].length;j ++){
                this.ItemMap[i][j] = false;
            }
        }
        this.Score = 0;
        this.NowItemState = this.CreateNewItemState();
        this.NowItemMap = this.getItemMap(this.NowItemState);
        this.NextItemState = this.CreateNewItemState();
        this.NextItemMap = this.getItemMap(this.NextItemState);
        this.NowItemPos = this.getNewItemPos();
        this.repaint();
    }

    public void SetPause(boolean value){
        this.IsPause = value;
        if (this.IsPause){
            this.timer.stop();
        }
        else{
            this.timer.restart();
        }
        this.repaint();
    }

    private int CreateNewItemState() {
        int Sum = Tetris.Shape.length * 4;
        return (int) (Math.random() * 1000) % Sum;
    }

    private boolean[][] getItemMap(int ItemState) {
        int Shape = ItemState / 4;
        int Arc = ItemState % 4;
        return this.RotateItem(Tetris.Shape[Shape], Math.PI / 2 * Arc);
    }

    
    private boolean[][] RotateItem(boolean[][] ItemMap, double angel) {
        
        int Heigth = ItemMap.length;
        int Width = ItemMap[0].length;
        
        boolean[][] ResultItemMap = new boolean[Heigth][Width];
        
        float CenterX = (Width - 1) / 2f;
        float CenterY = (Heigth - 1) / 2f;
        
        for (int i = 0; i < ItemMap.length; i++) {
            for (int j = 0; j < ItemMap[i].length; j++) {
                
                float RelativeX = j - CenterX;
                float RelativeY = i - CenterY;
                float ResultX = (float) (Math.cos(angel) * RelativeX - Math.sin(angel) * RelativeY);
                float ResultY = (float) (Math.cos(angel) * RelativeY + Math.sin(angel) * RelativeX);
				
                Point OrginPoint = new Point(Math.round(CenterX + ResultX), Math.round(CenterY + ResultY));
                ResultItemMap[OrginPoint.y][OrginPoint.x] = ItemMap[i][j];
            }
        }
        return ResultItemMap;
    }
   
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        g.setColor(Color.white);
        for (int i = 0; i < Tetris.ItemHeigth; i++) {
        	for (int k = 1; k < Tetris.ItemWidth+1; k++) {
        		g.drawRect(k * Tetris.ItemSize, i * Tetris.ItemSize, Tetris.ItemSize, Tetris.ItemSize);       
        	}
        }
        g.setColor(Color.black);
        for (int i = 0; i < this.NowItemMap.length; i++) {
            for (int j = 0; j < this.NowItemMap[i].length; j++) {
                if (this.NowItemMap[i][j])
                    g.fillRect((1 + this.NowItemPos.x + j) * Tetris.ItemSize, (this.NowItemPos.y + i) * Tetris.ItemSize,
                            Tetris.ItemSize, Tetris.ItemSize);
            }
        }
        g.setColor(Color.red);
        for (int i = 0; i < Tetris.ItemHeigth; i++) {
            for (int j = 0; j < Tetris.ItemWidth; j++) {
                if (this.ItemMap[i][j])
                    g.fillRect(Tetris.ItemSize + j * Tetris.ItemSize, i * Tetris.ItemSize, Tetris.ItemSize,
                            Tetris.ItemSize);
            }
        }
        g.setColor(Color.blue);
        for (int i = 0;i < this.NextItemMap.length;i ++){
            for (int j = 0;j < this.NextItemMap[i].length;j ++){
                if (this.NextItemMap[i][j])
                    g.fillRect(190 + j * Tetris.ItemSize, 30 + i * Tetris.ItemSize, Tetris.ItemSize, Tetris.ItemSize);
            }
        }
        g.setColor(Color.black);
        g.drawString("score:" + this.Score, 190, 10);
        if (this.IsPause){
            g.setColor(Color.white);
            g.fillRect(90, 120, 60, 30);
            g.setColor(Color.black);
            g.drawRect(90, 120, 60, 30);
            g.drawString("PAUSE", 100, 140);
        }
    }
    private int earnScore(){
        int lines = 0;
        for (int i = 0;i < this.ItemMap.length;i ++){
            boolean IsLine = true;
            for (int j = 0;j < this.ItemMap[i].length;j ++){
                if (!this.ItemMap[i][j]){
                    IsLine = false;
                    break;
                }
            }
            if (IsLine){
                for (int k = i;k > 0;k --){
                    this.ItemMap[k] = this.ItemMap[k - 1];
                }
                this.ItemMap[0] = new boolean[Tetris.ItemWidth];
                lines ++;
            }
        }
        return lines;
    }

    ActionListener TimerListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent arg0) {
            if (Tetris.this.checkHit(Tetris.this.NowItemMap, new Point(Tetris.this.NowItemPos.x, Tetris.this.NowItemPos.y + 1))){
                if (Tetris.this.onHit()){
                    Tetris.this.Score += Tetris.this.earnScore();
                    if (Tetris.this.Score > 20)  Tetris.this.speed *= 2;
                    Tetris.this.getNextItem();
                }
                else{
                    JOptionPane.showMessageDialog(Tetris.this.getParent(), "GAME OVER");
                    Tetris.this.Initial();
                }
            }
            else{
                Tetris.this.NowItemPos.y ++;
            }
            Tetris.this.repaint();
        }
    };

    
    java.awt.event.KeyListener KeyListener = new java.awt.event.KeyListener(){

        @Override
        public void keyPressed(KeyEvent e) {
            
            int code = e.getKeyCode();
            if (code == KeyEvent.VK_P) {
            	IsPause = !IsPause;
            	SetPause(IsPause);
            }
            else if (!IsPause){
                Point DesPoint;
                if(code == KeyEvent.VK_SPACE || code == KeyEvent.VK_NUMPAD8) {
                    DesPoint = new Point(Tetris.this.NowItemPos.x, Tetris.this.NowItemPos.y + 1);
                    if (!Tetris.this.checkHit(Tetris.this.NowItemMap, DesPoint)){
                        Tetris.this.NowItemPos = DesPoint;
                    }
                }    
                else if(code == KeyEvent.VK_UP || code == KeyEvent.VK_NUMPAD1 || code == KeyEvent.VK_NUMPAD5 
                	|| code == KeyEvent.VK_NUMPAD9 || code == KeyEvent.VK_X) {
                    boolean[][] TurnItem = Tetris.this.RotateItem(Tetris.this.NowItemMap,Math.PI / 2);
                    if (!Tetris.this.checkHit(TurnItem, Tetris.this.NowItemPos)){
                        Tetris.this.NowItemMap = TurnItem;
                    }
                }
                else if(code == KeyEvent.VK_CONTROL || code == KeyEvent.VK_NUMPAD3 
                	|| code == KeyEvent.VK_NUMPAD7 || code == KeyEvent.VK_Z) {
                    boolean[][] TurnItem = Tetris.this.RotateItem(Tetris.this.NowItemMap, - Math.PI / 2);
                    if (!Tetris.this.checkHit(TurnItem, Tetris.this.NowItemPos)){
                        Tetris.this.NowItemMap = TurnItem;
                    }
                }
                else if(code == KeyEvent.VK_RIGHT || code == KeyEvent.VK_NUMPAD6) {
                    DesPoint = new Point(Tetris.this.NowItemPos.x + 1, Tetris.this.NowItemPos.y);
                    if (!Tetris.this.checkHit(Tetris.this.NowItemMap, DesPoint)){
                        Tetris.this.NowItemPos = DesPoint;
                    }
                }
                else if(code == KeyEvent.VK_LEFT || code == KeyEvent.VK_NUMPAD4) {
                     DesPoint = new Point(Tetris.this.NowItemPos.x - 1, Tetris.this.NowItemPos.y);
                     if (!Tetris.this.checkHit(Tetris.this.NowItemMap, DesPoint)){
                         Tetris.this.NowItemPos = DesPoint;
                    }
                }
            }
            //System.out.println(Tetris.this.NowItemPos);
            repaint();
        }

        @Override
        public void keyReleased(KeyEvent e) { 
        }
        @Override
        public void keyTyped(KeyEvent e) {
        }
    };
        java.awt.event.MouseListener MouseListener = new java.awt.event.MouseListener(){

			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				boolean[][] TurnItem = Tetris.this.RotateItem(Tetris.this.NowItemMap,Math.PI / 2);
                if (!Tetris.this.checkHit(TurnItem, Tetris.this.NowItemPos)){
                    Tetris.this.NowItemMap = TurnItem;
                }
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				Point DesPoint;
				DesPoint = new Point(Tetris.this.NowItemPos.x, Tetris.this.NowItemPos.y + 1);
                if (!Tetris.this.checkHit(Tetris.this.NowItemMap, DesPoint)){
                    Tetris.this.NowItemPos = DesPoint;
                }
			}
    };
}
