import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Complete {
    private static final int TOTAL_CARDS = 12;
    private static final int MAX_FLIPPED_CARDS = 2;
    private static CardButton[] buttons;
    private static CardInfo[] cards;
    private static int flippedCardsCount = 0;
    private static CardButton firstFlippedButton;
    private static CardButton secondFlippedButton;
    private static long startTime;
    private static Login login;

    public static void main(String[] args) {
        login = new Login();
    }

    // Method to start the game
    private static void startGame() {
        // Create a JPanel for the GUI
        JFrame frame = new JFrame();
        JPanel panel = new JPanel(new GridLayout(4, 4));

        // Containing image paths and the default image
        String[] imagePaths = {"E:\\Semester 06\\Web Engineering_WE\\FlipFlop\\crow.jpg", "E:\\Semester 06\\Web Engineering_WE\\FlipFlop\\duck.jpg", "E:\\Semester 06\\Web Engineering_WE\\FlipFlop\\frog.jpg", "E:\\Semester 06\\Web Engineering_WE\\FlipFlop\\pigeon.jpg", "E:\\Semester 06\\Web Engineering_WE\\FlipFlop\\crow.jpg", "E:\\Semester 06\\Web Engineering_WE\\FlipFlop\\duck.jpg", "E:\\Semester 06\\Web Engineering_WE\\FlipFlop\\frog.jpg", "E:\\Semester 06\\Web Engineering_WE\\FlipFlop\\pigeon.jpg", "E:\\Semester 06\\Web Engineering_WE\\FlipFlop\\sparrow.jpg", "E:\\Semester 06\\Web Engineering_WE\\FlipFlop\\turtle.jpg", "E:\\Semester 06\\Web Engineering_WE\\FlipFlop\\sparrow.jpg", "E:\\Semester 06\\Web Engineering_WE\\FlipFlop\\turtle.jpg"};
        String[] imageNames = { "crow", "duck", "frog", "pigeon", "crow", "duck", "frog", "pigeon", "sparrow", "turtle", "sparrow", "turtle" };

        // Create an array to hold the card objects
        cards = new CardInfo[TOTAL_CARDS];

        // Create CardInfo instances for each card using a loop
        for (int i = 0; i < TOTAL_CARDS; i++) {
            ImageIcon imageIcon = new ImageIcon(imagePaths[i]);
            String imageName = imageNames[i];
            cards[i] = new CardInfo(imageIcon, imageName);
        }

        // Shuffle the cards
        shuffleCards();

        // Create a JButton for each card
        buttons = new CardButton[TOTAL_CARDS];
        for (int i = 0; i < TOTAL_CARDS; i++) {
            buttons[i] = new CardButton();
            buttons[i].setCardInfo(cards[i]);
            buttons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    CardButton clickedButton = (CardButton) e.getSource();
                    if (!clickedButton.isFlipped() && flippedCardsCount < MAX_FLIPPED_CARDS) {
                        // Flip the clicked card
                        flipCard(clickedButton);
                        flippedCardsCount++;

                        if (flippedCardsCount == 1) {
                            // First card flipped
                            firstFlippedButton = clickedButton;
                        } else if (flippedCardsCount == 2) {
                            // Second card flipped
                            secondFlippedButton = clickedButton;

                            // Compare the two flipped cards
                            if (isMatch(firstFlippedButton, secondFlippedButton)) {
                                // Cards matched, disable them
                                firstFlippedButton.setEnabled(false);
                                secondFlippedButton.setEnabled(false);
                                resetFlippedCards();
                                checkGameCompletion();
                                // score = score + 10;
                            } else {
                                // Cards not matched, flip them back after a delay
                                javax.swing.Timer timer = new javax.swing.Timer(1000, new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        flipCard(firstFlippedButton);
                                        flipCard(secondFlippedButton);
                                        resetFlippedCards();
                                    }
                                });
                                timer.setRepeats(false);
                                timer.start();
                            }
                        }
                    }
                }
            });
            panel.add(buttons[i]);
        }

        // Configure the frame and panel properties
        frame.setTitle("FlipFlop");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Start the timer
        startTime = System.currentTimeMillis();
    }

    // Method to shuffle the cards
    private static void shuffleCards() {
        Random random = new Random();
        for (int i = TOTAL_CARDS - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            CardInfo temp = cards[i];
            cards[i] = cards[index];
            cards[index] = temp;
        }
    }

    // Method to flip the card
    private static void flipCard(CardButton cardButton) {
        cardButton.setFlipped(!cardButton.isFlipped());
        if (cardButton.isFlipped())
            cardButton.setIcon(cardButton.getCardInfo().getImageIcon());
        else
            cardButton.setIcon(null);
    }

    // Method to reset flipped cards
    private static void resetFlippedCards() {
        flippedCardsCount = 0;
        firstFlippedButton = null;
        secondFlippedButton = null;
    }

    // Method to check if two cards are a match
    private static boolean isMatch(CardButton card1, CardButton card2) {
        CardInfo info1 = card1.getCardInfo();
        CardInfo info2 = card2.getCardInfo();
        return info1.getImageName().equals(info2.getImageName());
    }

    // Method to check if the game is completed
    private static void checkGameCompletion() {
        boolean allCardsDisabled = true;
        for (CardButton button : buttons) {
            if (button.isEnabled()) {
                allCardsDisabled = false;
                break;
            }
        }

        if (allCardsDisabled) {
            // Game completed
            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;
            double seconds = elapsedTime / 1000.0;
            JOptionPane.showMessageDialog(null, "Congratulations! You completed the game in " + seconds + " seconds.");
        }
    }

    // Custom CardButton class
    static class CardButton extends JButton {
        private CardInfo cardInfo;
        private boolean flipped;

        public CardInfo getCardInfo() {
            return cardInfo;
        }

        public void setCardInfo(CardInfo cardInfo) {
            this.cardInfo = cardInfo;
        }

        public boolean isFlipped() {
            return flipped;
        }

        public void setFlipped(boolean flipped) {
            this.flipped = flipped;
        }
    }

    // Custom CardInfo class
    static class CardInfo {
        private ImageIcon imageIcon;
        private String imageName;

        public CardInfo(ImageIcon imageIcon, String imageName) {
            this.imageIcon = imageIcon;
            this.imageName = imageName;
        }

        public ImageIcon getImageIcon() {
            return imageIcon;
        }

        public String getImageName() {
            return imageName;
        }
    }

    // Login class with authentication
    static class Login extends JFrame {
        private JTextField usernameField;
        private JPasswordField passwordField;

        public Login() {
            super("Login");
            setLayout(new GridLayout(3, 2));

            JLabel usernameLabel = new JLabel("Username:");
            usernameField = new JTextField();
            JLabel passwordLabel = new JLabel("Password:");
            passwordField = new JPasswordField();
            JButton loginButton = new JButton("Login");

            loginButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String username = usernameField.getText();
                    String password = new String(passwordField.getPassword());

                    // Perform authentication here
                    if (authenticate(username, password)) {
                        dispose();  // Close the login window
                        startGame(); // Start the game
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid username or password");
                    }
                }
            });

            add(usernameLabel);
            add(usernameField);
            add(passwordLabel);
            add(passwordField);
            add(loginButton);

            setSize(300, 150);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);
            setVisible(true);
        }

        private boolean authenticate(String username, String password) {
		    String url = "jdbc:ucanaccess://Record.accdb";
		    String DBusername = "uName";
        	String DBpassword = "uPass";

		    try (Connection connection = DriverManager.getConnection(url, DBusername, DBpassword)) {
		        String query = "SELECT * FROM users WHERE uName = ? AND uPass = ?";
		        PreparedStatement statement = connection.prepareStatement(query);
		        statement.setString(1, username);
		        statement.setString(2, password);

		        ResultSet resultSet = statement.executeQuery();

		        if (resultSet.next()) {
		            // User exists in the database
		            return true;
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }

		    return false;
		}
    }
}
