import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.awt.event.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;


public class MyMole extends JFrame {
    private static final long serialVersionUID = 1L;
    Timer timer;
    int numberOfHits;
    int numberOfMisses;
    JButton[][] gameTabs;
    JPanel headerPanel;
    JPanel contentPanel;
    public MyMole() throws UnsupportedAudioFileException, IOException, LineUnavailableException  {
        super("doge");
        
        setSize(600, 500);
        setLayout(new BorderLayout(0, 0));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout(0, 0));
        JButton start = new JButton("Start");

        start.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                timer.start();
            }
        });
        
        headerPanel.add(start, BorderLayout.WEST);
       
        contentPanel = new JPanel();
        contentPanel.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(new ImageIcon("batLeft.png").getImage(),new Point(0,0),"custom cursor"));
        contentPanel.setLayout(new GridLayout(3, 3));
        gameTabs = new JButton[3][3];
        for (int x = 0; x < gameTabs.length; ++x) {
            for (int y = 0; y < gameTabs.length; ++y) {
                gameTabs[x][y] = new JButton("");
                gameTabs[x][y].setActionCommand("null");
                gameTabs[x][y].setBackground(new Color(255, 204, 0));
                gameTabs[x][y].setOpaque(true);
                contentPanel.add(gameTabs[x][y]);
                gameTabs[x][y].addActionListener(new TabsActionListener());
            }

        }
        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);

        setVisible(true);
        Random r = new Random();
        timer = new Timer(700, new ActionListener() {
            int xAxis;
            int yAxis;
            public void actionPerformed(ActionEvent evt) {
                gameTabs[xAxis][yAxis].setIcon(null);
                gameTabs[xAxis][yAxis].setActionCommand("null");
                xAxis = r.nextInt(3);
                yAxis = r.nextInt(3);
                gameTabs[xAxis][yAxis].setIcon(loadImage("doge2.png"));
                gameTabs[xAxis][yAxis].setActionCommand(gameTabs[xAxis][yAxis].getIcon().toString());
            }
        });
    }
    private ImageIcon loadImage(String path){
        Image image = new ImageIcon(this.getClass().getResource(path)).getImage();
        Image scaledImage = image.getScaledInstance(132, 132, java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }       
    public void playSound(String soundName){
        try{
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new
            File(soundName).getAbsoluteFile( ));
            Clip clip = AudioSystem.getClip( );
            clip.open(audioInputStream);
            clip.start( );
        }catch(Exception ex){
            System.out.println("Error with playing sound.");
            ex.printStackTrace();
        }
        
    }
    public static void main(String[] args){
        try {
            new MyMole();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
    private class TabsActionListener implements ActionListener{
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("null")){
                    numberOfMisses++;
                    playSound("laugh.wav");
                    for (int x = 0; x < gameTabs.length; ++x) {
                        for (int y = 0; y < gameTabs.length; ++y) {
                            if(!gameTabs[x][y].getActionCommand().equals("null"))
                                gameTabs[x][y].setIcon(loadImage("strongDoge.png"));
                        }
                    }
                }
                    
                else {
                    playSound("Bonk1.wav");
                    ((JButton)e.getSource()).setIcon(loadImage("512dogeBonk.png"));
                    numberOfHits++;
                }
                if(numberOfHits + numberOfMisses == 16){
                    timer.stop();
                    for (int x = 0; x < gameTabs.length; ++x) {
                        for (int y = 0; y < gameTabs.length; ++y) {
                            gameTabs[x][y].setIcon(null);
                            gameTabs[x][y].setActionCommand("null");
                        }
                    }
                    JOptionPane.showMessageDialog(null, "number of hits: " + numberOfHits + ", number of misses: " + numberOfMisses);
                    numberOfHits = 0;
                    numberOfMisses = 0;
                }
                    
                
            }
    } 

}