
import java.lang.Thread;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
import javax.swing.JOptionPane;

public class BlackJack extends Game {

    private class Card {
        public void method() {
            System.out.print("starting");
        }
        private String value;
        private String type;

        Card(String value, String type) {
            this.value = value;
            this.type = type;
        }

        public String toString() {

            return value + "-" + type;
        }
        public String toString2() {
            return "";
        }

        public int getValue() {
            if ("AJQK".contains(value)) { //A J Q K
                if (value == "A") {
                    return 11;
                }
                return 10;
            }
            return Integer.parseInt(value); //2-10
        }

        public boolean isAce() {
            return value == "A";
        }

        public String getImagePath() {
            return "./cards/" + toString() + ".png";
        }
    }

    private ArrayList<Card> deck;
    Random random = new Random(); //shuffle deck

    //dealer
    private Card hiddenCard;
    private ArrayList<Card> dealerHand;
    private int dealerSum;
    private int dealerAceCount;
    private JLabel imageLabel;

    private int winCount = 0;

    //player
    private ArrayList<Card> playerHand;
    private int playerSum;
    private int playerAceCount;
    private boolean showEndGameScreen = false;

    private int decCardSize;

    //window
    private int boardWidth = 1440;
    private int boardHeight = 1000;


    private int cardWidth = 110; //ratio should 1/1.4
    private int cardHeight = 154;
    JFrame frame = new JFrame("Black Jack");
    JPanel gamePanel = new JPanel() {
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            gamePanel.setLayout(null);
            ImageIcon imageIcon = new ImageIcon(getClass().getResource("./cards/30gqe4wgamr61.png")); // Adjust the path accordingly
            JLabel imageLabel = new JLabel(imageIcon);


            imageLabel.setBounds(695, 10, imageIcon.getIconWidth(), imageIcon.getIconHeight()); // Adjust coordinates as needed


            gamePanel.add(imageLabel);
            imageLabel.setVisible(false);
            g.setFont(new Font("Arial", Font.PLAIN, 45));
            g.setColor(Color.white);
            g.drawString("Win Count: "+winCount, 1090, 700);
            if(!(showEndGameScreen)) {
                imageLabel.setVisible(true);
            }
            try {
                //draw hidden card
                Image hiddenCardImg = new ImageIcon(getClass().getResource("./cards/BACK.png")).getImage();
                if (!stayButton.isEnabled()) {
                    hiddenCardImg = new ImageIcon(getClass().getResource(hiddenCard.getImagePath())).getImage();
                }
                g.drawImage(hiddenCardImg, 20, 20, cardWidth, cardHeight, null);
                //draw dealer's hand
                for (int i = 0; i < dealerHand.size(); i++) {
                    Card card = dealerHand.get(i);
                    Image cardImg = new ImageIcon(getClass().getResource(card.getImagePath())).getImage();
                    g.drawImage(cardImg, cardWidth + 25 + (cardWidth + 5) * i, 20, cardWidth, cardHeight, null);
                }
                for (int i = 0; i < playerHand.size(); i++) {
                    Card card = playerHand.get(i);
                    Image cardImg = new ImageIcon(getClass().getResource(card.getImagePath())).getImage();
                    g.drawImage(cardImg, 20 + (cardWidth + 5) * i, 320, cardWidth, cardHeight, null);
                }
                g.setFont(new Font("Arial", Font.PLAIN, 20));
                g.setColor(Color.white);
                if (dealerHand.size() > 0) {
                    Card faceUpCard = dealerHand.get(0);
                    g.drawString("Dealer Total: " + faceUpCard.getValue(), 20, 260);
                } else {
                    g.drawString("Dealer Total: 0", 20, 260);
                }
                g.drawString("Player Total: " + playerSum, 20, 600);
                if (!stayButton.isEnabled()) {
                    g.setColor(new Color(69, 101, 77));
                    g.fillRect(20, 230, 200, 30);
                    g.fillRect(1090, 700, 400, 300);
                    g.setColor(Color.white);
                    g.drawString("Dealer Total: " + dealerSum, 20, 260);
                    dealerSum = reduceDealerAce();
                    playerSum = reducePlayerAce();
                    System.out.println("STAY: ");
                    System.out.println(dealerSum);
                    System.out.println(playerSum);
                    String message = "";
                    if (playerSum > 21) {
                        message = "You Lose!";

                    } else if (dealerSum > 21) {
                        message = "You Win!";

                    }
                    else if (playerSum == dealerSum) {
                        message = "Tie!";

                    } else if (playerSum > dealerSum) {
                        message = "You Win!";

                    } else if (playerSum < dealerSum) {
                        message = "You Lose!";

                    }
                    g.setFont(new Font("Arial", Font.PLAIN, 45));
                    g.setColor(Color.white);
                    g.drawString(message, 820, 700);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            if (showEndGameScreen) {
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, this.getWidth(), this.getHeight());
                g.setFont(new Font("Arial", Font.BOLD, 60));
                g.setColor(Color.WHITE);
                String message = "You are ready!";
                int messageWidth = g.getFontMetrics().stringWidth(message);
                g.drawString(message, (this.getWidth() - messageWidth) / 2, this.getHeight() / 2);
                String dislaimer = "If you lose more money don't sue me pls.";
                int dislaimerMessageWidth = g.getFontMetrics().stringWidth(dislaimer);
                g.drawString(dislaimer, (this.getWidth() - dislaimerMessageWidth) / 2, this.getHeight() / 2+200);
            }
        }
    };
    JPanel buttonPanel = new JPanel();
    JButton hitButton = new JButton("Hit");
    JButton stayButton = new JButton("Stay");
    JButton restartButton = new JButton("Restart");

    BlackJack() {

        startGame();


        frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JButton restartButton = new JButton("Restart");
        restartButton.setFocusable(false);
        buttonPanel.add(restartButton);
        restartButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Reset the game
                startGame();
                // Enable the hit button
                hitButton.setEnabled(true);
                // Enable the stay button
                stayButton.setEnabled(true);
                // Repaint the game panel
                gamePanel.removeAll();
                gamePanel.repaint();
            }
        });


        gamePanel.setLayout(new BorderLayout());
        gamePanel.setBackground(new Color(69, 101, 77));
        frame.add(gamePanel);


        hitButton.setFocusable(false);
        buttonPanel.add(hitButton);
        stayButton.setFocusable(false);
        buttonPanel.add(stayButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        hitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Card card = deck.remove(deck.size() - 1);
                decCardSize = deck.size();
                playerSum += card.getValue();
                playerAceCount += card.isAce() ? 1 : 0;
                playerHand.add(card);
                if (reducePlayerAce() > 21) { //A + 2 + J --> 1 + 2 + J
                    hitButton.setEnabled(false);


                    JLabel bustLabel = new JLabel("BUST!!");
                    bustLabel.setFont(new Font("Arial", Font.BOLD, 170)); // Set font size and style
                    bustLabel.setForeground(new Color(255, 70, 70, 255)); // Set text color

                    bustLabel.setBounds(170, gamePanel.getHeight() - 170, 1000, 170);
                    gamePanel.add(bustLabel);
                    playLoss();
                }
                gamePanel.repaint();
            }
        });

        stayButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                hitButton.setEnabled(false);
                stayButton.setEnabled(false);


                while (dealerSum < 17) {

                    Card card = deck.remove(deck.size() - 1);
                    dealerSum += card.getValue();
                    dealerAceCount += card.isAce() ? 1 : 0;

                    dealerHand.add(card);
                }
                gamePanel.repaint();

                calculateWin();

            }

        });

        gamePanel.repaint();
    }
    public void calculateWin(){
        if (playerSum > 21) {
            playLoss();

        } else if (dealerSum > 21) {
            playWin();
            winCount++;

        }
        else if (playerSum == dealerSum) {

        } else if (playerSum > dealerSum) {
            playWin();
            winCount++;

        } else if (playerSum < dealerSum) {
            playLoss();

        }
        checkEndGame();
    }

    public void startGame() {
        checkEndGame();
        //deck
        buildDeck();
        shuffleDeck();

        //dealer
        dealerHand = new ArrayList<Card>();
        dealerSum = 0;
        dealerAceCount = 0;

        hiddenCard = deck.remove(deck.size() - 1); //remove card at last index
        dealerSum += hiddenCard.getValue();
        dealerAceCount += hiddenCard.isAce() ? 1 : 0;

        Card card = deck.remove(deck.size() - 1);
        dealerSum += card.getValue();
        dealerAceCount += card.isAce() ? 1 : 0;
        dealerHand.add(card);

        System.out.println("DEALER:");
        System.out.println(hiddenCard);
        System.out.println(dealerHand);
        System.out.println(dealerSum);
        System.out.println(dealerAceCount);


        //player
        playerHand = new ArrayList<Card>();
        playerSum = 0;
        playerAceCount = 0;


        for (int i = 0; i < 2; i++) {
            card = deck.remove(deck.size() - 1);
            playerSum += card.getValue();
            playerAceCount += card.isAce() ? 1 : 0;
            playerHand.add(card);
        }

        System.out.println("PLAYER: ");
        System.out.println(playerHand);
        System.out.println(playerSum);
        System.out.println(playerAceCount);
    }

    public void buildDeck() {
        deck = new ArrayList<Card>();
        String[] values = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
        String[] types = {"C", "D", "H", "S"};

        for (int i = 0; i < types.length; i++) {
            for (int j = 0; j < values.length; j++) {
                Card card = new Card(values[j], types[i]);
                deck.add(card);
            }
        }

        System.out.println("BUILD DECK:");
        System.out.println(deck);
    }

    public void shuffleDeck() {
        for (int i = 0; i < deck.size(); i++) {
            int j = random.nextInt(deck.size());
            Card currCard = deck.get(i);
            Card randomCard = deck.get(j);
            deck.set(i, randomCard);
            deck.set(j, currCard);
        }

        System.out.println("AFTER SHUFFLE");
        System.out.println(deck);
    }

    public int reducePlayerAce() {
        while (playerSum > 21 && playerAceCount > 0) {
            playerSum -= 10;
            playerAceCount -= 1;
        }
        return playerSum;
    }

    public int reduceDealerAce() {
        while (dealerSum > 21 && dealerAceCount > 0) {
            dealerSum -= 10;
            dealerAceCount -= 1;
        }
        return dealerSum;
    }

    public void playLoss() {
        Music player = new Music();

        String filePath = "src/unitThreeSounds/Wrong Buzzer - Sound Effect.wav";
        player.play(filePath);

    }

    public void playWin() {
        Music player = new Music();
        String filePath = "src/unitThreeSounds/HOORAY \uD83D\uDE01 (Tiktok Sound Effect).wav";
        player.play(filePath);

    }

    public void endGameMusic(){

        hitButton.setEnabled(false);
        stayButton.setEnabled(false);
        showEndGameScreen = true;
        frame.repaint();
        gamePanel.removeAll();
        gamePanel.repaint();
        Music player = new Music();
        String filePath = "src/unitThreeSounds/Mario's Victory Theme.wav";
        player.play(filePath);
    }
    public void checkEndGame() {
        if (winCount >= 5) {
            endGameMusic();
        }
    }


}




