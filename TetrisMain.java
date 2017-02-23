package tetris;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class TetrisMain extends JFrame {

    Tetris tetris = new Tetris();

    public TetrisMain() {
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // TODO
        this.setSize(280, 350);
        this.setTitle("Tetris verion 1");
        this.setResizable(true);

        JMenuBar menu = new JMenuBar();
        this.setJMenuBar(menu);
        JMenu gameMenu = new JMenu("game");
        JMenuItem newGameItem = gameMenu.add("new game");
        newGameItem.addActionListener(this.NewGameAction);
        JMenuItem pauseItem = gameMenu.add("pause");
        pauseItem.addActionListener(this.PauseAction);
        JMenuItem continueItem = gameMenu.add("continue");
        continueItem.addActionListener(this.ContinueAction);
        JMenuItem exitItem = gameMenu.add("exit");
        exitItem.addActionListener(this.ExitAction);
        
        JMenu helpMenu = new JMenu("help");
        JMenuItem aboutItem = helpMenu.add("guide");
        aboutItem.addActionListener(this.GuideAction);
        menu.add(gameMenu);
        
        menu.add(helpMenu);

        this.add(this.tetris);
        this.tetris.setFocusable(true);
    }

    public static void main(String[] args) {
        System.out.println("Hello, Tetris!");
        try {
            ProgramArgs a = ProgramArgs.parseArgs(args);
            Tetris tetris = new Tetris(a.getFPS(), a.getSpeed(), a.getSequence());

        } catch (IllegalArgumentException e) {
            System.out.println(e);
        }
        TetrisMain tetrisMain = new TetrisMain();
        tetrisMain.setVisible(true);
    }


    ActionListener NewGameAction = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            TetrisMain.this.tetris.Initial();
        }
    };

    ActionListener PauseAction = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            TetrisMain.this.tetris.SetPause(true);
        }
    };

    ActionListener ContinueAction = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            TetrisMain.this.tetris.SetPause(false);
        }
    };

    ActionListener ExitAction = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    };

    ActionListener GuideAction = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(TetrisMain.this, "space to drop down, arrow to control direction, etc, as cs349f16 assignment1 said", "guide", JOptionPane.WARNING_MESSAGE);
        }
    };

}

