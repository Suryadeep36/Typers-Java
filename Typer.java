import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

import javax.sound.sampled.*;
import SentanceGenerator.SentanceGenerator;
import java.io.*;

public class Typer extends Frame implements KeyListener, ActionListener, WindowListener {
    String text;
    Vector<Panel> p;
    Panel header;
    Panel body;
    int panelIndex = 0;
    long startTime = -1;
    int mistakes = 0;
    Label wpm;
    Label accuracy;
    Label intro;
    boolean doneTyping;
    Vector<TypingResult> playerStats;
    Clip soundClip;
    MenuBar menu;
    Menu m1;

    public void playSingleKey() {
        try {
            File singleKeyFile = new File("./Sounds/singleKeyType.wav");
            AudioInputStream singleKeyStream = AudioSystem.getAudioInputStream(singleKeyFile);
            soundClip = AudioSystem.getClip();
            soundClip.open(singleKeyStream);
        } catch (Exception e) {
            System.out.println(e);
        }
        if (soundClip.isRunning() == false) {
            try {
                soundClip.start();
            } catch (Exception e) {
                System.out.println(e);
            }
        } else {
            soundClip.setFramePosition(0);
            soundClip.start();
        }
    }

    public void playGameOver() {
        try {
            File gameOverFile = new File("./Sounds/gameOver.wav");
            AudioInputStream gameOverStream = AudioSystem.getAudioInputStream(gameOverFile);
            soundClip = AudioSystem.getClip();
            soundClip.open(gameOverStream);
        } catch (Exception e) {
            System.out.println(e);
        }
        if (soundClip.isRunning() == false) {
            try {
                soundClip.start();
            } catch (Exception e) {
                System.out.println(e);
            }
        } else {
            soundClip.setFramePosition(0);
            soundClip.start();
        }
    }

    public void playWrongKey() {
        try {
            File wrongKeyFile = new File("./Sounds/wrongKeyType.wav");
            AudioInputStream wrongKeyStream = AudioSystem.getAudioInputStream(wrongKeyFile);
            soundClip = AudioSystem.getClip();
            soundClip.open(wrongKeyStream);
        } catch (Exception e) {
            System.out.println(e);
        }
        if (soundClip.isRunning() == false) {
            try {
                soundClip.start();
            } catch (Exception e) {
                System.out.println(e);
            }
        } else {
            soundClip.setFramePosition(0);
            soundClip.start();
        }
    }

    public void generateAndRenderText() {
        body = new Panel();
        body.setBackground(Color.yellow);
        body.setLayout(new GridBagLayout());
        text = SentanceGenerator.generateParagraph(3);
        panelIndex = 0;
        GridBagConstraints gbc = new GridBagConstraints();
        int counter = 0;
        int j = 0;
        gbc.ipadx = -5;
        p = new Vector<>();
        for (int i = 0; i < text.length(); i++) {
            Panel newPanel = new Panel();
            p.addElement(newPanel);
            panelIndex++;
            counter++;
            if (counter % 70 == 0) {
                j++;
            }
            gbc.gridx = counter % 70;
            gbc.gridy = j;
            Label character = new Label(text.charAt(i) + "");
            character.setFont(new Font("SERIF", Font.PLAIN, 25));
            newPanel.add(character);
            body.add(newPanel, gbc);
        }
        add(body);
        revalidate();
    }

    public String generateHeaderText(int wpm) {
        if (wpm <= 20) {
            playGameOver();
            return "That is so bad!! Try again";
        } else if (wpm <= 40) {
            playGameOver();
            return "It's alright but you can do better.";
        } else if (wpm <= 80) {
            return "Wow! you type real fast!!";
        } else {
            return "Ok, that is cheating!!";
        }
    }

    public void clearPanelBackground() {
        for (int i = 0; i < panelIndex; i++) {
            p.elementAt(i).setBackground(null);
        }
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == 10) {
            if (doneTyping == true) {
                remove(body);
                intro.setText("Start typing when ready!!");
                generateAndRenderText();
                panelIndex = 0;
                wpm.setText("WPM : ");
                accuracy.setText("Accuracy: ");
            }
        }
    }

    public void keyReleased(KeyEvent e) {

    }

    public void actionPerformed(ActionEvent ev) {
        try {
            File newFile = new File("./typingStats.txt");
            if (newFile.exists() == false) {
                newFile.createNewFile();
            }
            FileWriter fw = new FileWriter(newFile);
            for (int i = 0; i < playerStats.size(); i++) {
                fw.append("For test - " + (int) (i + 1) + "\n");
                fw.append("WPM : " + playerStats.get(i).wpm + "\n");
                fw.append("Accuracy : " + playerStats.get(i).accuracy + "\n");
                fw.append("Remark : " + generateHeaderText(playerStats.get(i).wpm) + "\n\n");
            }
            fw.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void generateResult(double timePassed) {
        int paraLength = text.length();
        float minutesPassed = (float) timePassed / 60;
        float wordTyped = paraLength / 5;
        int accuracy = (int) (100 - (mistakes * 100 / paraLength));
        int wpm = (int) (wordTyped / minutesPassed);
        this.intro.setText(generateHeaderText(wpm));
        this.wpm.setText("WPM : " + wpm);
        this.accuracy.setText("Accuracy : " + accuracy + "%");
        TypingResult newResult = new TypingResult(wpm, accuracy);
        playerStats.add(newResult);
        revalidate();
    }

    public void keyTyped(KeyEvent e) {
        Label temp = (Label) (p.elementAt(panelIndex).getComponent(0));
        String correctString = temp.getText();
        char userInput = e.getKeyChar();
        if (e.getExtendedKeyCode() == 10) {
            return;
        }
        if (startTime == -1) {
            startTime = System.nanoTime();
        }
        if ((userInput + "").equals(correctString)) {
            p.elementAt(panelIndex).setBackground(Color.green);
            playSingleKey();
            panelIndex++;
        } else {
            mistakes++;
            playWrongKey();
            p.elementAt(panelIndex).setBackground(Color.red);
        }
        if (panelIndex >= text.length()) {
            clearPanelBackground();
            long endTime = System.nanoTime();
            double timePassed = (endTime - startTime) / 10E8;
            generateResult(timePassed);
            startTime = -1;
            mistakes = 0;
            panelIndex = 0;
            doneTyping = true;
        }
    }

    public void windowOpened(WindowEvent e) {

    }

    public void windowClosed(WindowEvent e) {

    }

    public void windowIconified(WindowEvent e) {

    }

    public void windowDeiconified(WindowEvent e) {

    }

    public void windowActivated(WindowEvent e) {

    }

    public void windowDeactivated(WindowEvent e) {

    }

    public void windowClosing(WindowEvent e) {
        System.exit(0);
    }

    Typer() {
        setSize(500, 500);
        setTitle("Typers");
        setVisible(true);
        addWindowListener(this);
        setLayout(new GridLayout(2, 1));
        header = new Panel(new GridLayout(3, 1));
        Panel p1 = new Panel();
        Panel p2 = new Panel();
        Panel p3 = new Panel();
        playerStats = new Vector<TypingResult>();
        menu = new MenuBar();
        m1 = new Menu("Generate Stats");
        MenuItem menuItem = new MenuItem("Generate Stats");
        menuItem.addActionListener(this);
        m1.add(menuItem);
        menu.add(m1);
        setMenuBar(menu);
        intro = new Label("Start typing when ready!!");
        intro.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 26));
        wpm = new Label("WPM: ");
        wpm.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 26));
        accuracy = new Label("Accuracy: ");
        accuracy.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 26));
        p1.add(intro);
        p2.add(wpm);
        p3.add(accuracy);
        header.add(p1);
        header.add(p2);
        header.add(p3);
        header.setBackground(Color.cyan);
        add(header);
        body = new Panel();
        body.setBackground(Color.yellow);
        body.setLayout(new GridBagLayout());
        text = SentanceGenerator.generateParagraph(3);
        panelIndex = 0;
        GridBagConstraints gbc = new GridBagConstraints();
        int counter = 0;
        int j = 0;
        p = new Vector<>();
        for (int i = 0; i < text.length(); i++) {
            Panel newPanel = new Panel();
            p.addElement(newPanel);
            gbc.gridx = counter % 50;
            gbc.gridy = j;
            panelIndex++;
            counter++;
            if (counter % 50 == 0) {
                j++;
            }
            Label character = new Label(text.charAt(i) + "");
            character.setFont(new Font("SERIF", Font.PLAIN, 26));
            newPanel.add(character);
            body.add(newPanel, gbc);
        }
        add(body);
        addKeyListener(this);
        panelIndex = 0;
        doneTyping = false;
    }

    public static void main(String[] args) {
        Typer t = new Typer();
    }
}

class TypingResult {
    int wpm;
    int accuracy;

    TypingResult() {
        wpm = 0;
        accuracy = 0;
    }

    TypingResult(int wpm, int accuracy) {
        this.wpm = wpm;
        this.accuracy = accuracy;
    }
}