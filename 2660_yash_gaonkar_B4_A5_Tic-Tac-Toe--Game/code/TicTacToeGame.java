import javax.swing.*;
import java.awt.*;

interface Resettable { void reset(); }

abstract class GamePlayer {
    private final String name;
    private final char symbol;
    public GamePlayer(String name, char symbol) {
        this.name = name; this.symbol = symbol;
    }
    public String getName() { return name; }
    public char getSymbol() { return symbol; }
    public abstract String getPlayerType();
}

class HumanPlayer extends GamePlayer {
    public HumanPlayer(String name, char symbol) { super(name, symbol); }
    @Override public String getPlayerType() { return "Human"; }
}

public class TicTacToeGame extends JFrame implements Resettable {
    private final JButton[][] buttons = new JButton[3][3];
    private final char[][] board = new char[3][3];
    private final GamePlayer[] players = {
        new HumanPlayer("Player 1", 'X'), new HumanPlayer("Player 2", 'O')
    };
    private final JLabel status = new JLabel("", SwingConstants.CENTER);
    private final JLabel score = new JLabel("", SwingConstants.CENTER);
    private int turn = 0, xWins = 0, oWins = 0, draws = 0;
    private boolean gameOver = false;

    public TicTacToeGame() {
        super("Tic-Tac-Toe | Java OOP Assignment 5");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(440, 540); setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JLabel title = new JLabel("TIC-TAC-TOE", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        add(title, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(8, 8));
        status.setFont(new Font("SansSerif", Font.BOLD, 16));
        center.add(status, BorderLayout.NORTH);
        JPanel grid = new JPanel(new GridLayout(3, 3, 6, 6));
        for (int r = 0; r < 3; r++) for (int c = 0; c < 3; c++) {
            final int row = r, col = c;
            buttons[r][c] = new JButton("");
            buttons[r][c].setFont(new Font("SansSerif", Font.BOLD, 48));
            buttons[r][c].setFocusPainted(false);
            buttons[r][c].addActionListener(e -> makeMove(row, col));
            grid.add(buttons[r][c]);
        }
        center.add(grid, BorderLayout.CENTER);
        center.setBorder(BorderFactory.createEmptyBorder(12, 18, 8, 18));
        add(center, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new GridLayout(3, 1, 5, 5));
        score.setFont(new Font("SansSerif", Font.PLAIN, 14));
        JButton restart = new JButton("New Game");
        restart.addActionListener(e -> reset());
        bottom.add(score); bottom.add(restart);
        bottom.add(new JLabel("Take turns placing X and O.", SwingConstants.CENTER));
        bottom.setBorder(BorderFactory.createEmptyBorder(5, 18, 18, 18));
        add(bottom, BorderLayout.SOUTH);
        updateStatus(); updateScore();
    }

    private void makeMove(int r, int c) {
        if (gameOver) return;
        if (board[r][c] != '\0') {
            JOptionPane.showMessageDialog(this, "That square is already occupied.");
            return;
        }
        char symbol = players[turn].getSymbol();
        board[r][c] = symbol;
        buttons[r][c].setText(String.valueOf(symbol));
        buttons[r][c].setEnabled(false);

        if (won(symbol)) {
            gameOver = true;
            if (symbol == 'X') xWins++; else oWins++;
            status.setText(players[turn].getName() + " (" + symbol + ") wins!");
            JOptionPane.showMessageDialog(this, status.getText());
            disableBoard(); updateScore(); return;
        }
        if (full()) {
            gameOver = true; draws++;
            status.setText("It's a draw!");
            JOptionPane.showMessageDialog(this, "It's a draw!");
            updateScore(); return;
        }
        turn = 1 - turn; updateStatus();
    }

    private boolean won(char s) {
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == s && board[i][1] == s && board[i][2] == s) return true;
            if (board[0][i] == s && board[1][i] == s && board[2][i] == s) return true;
        }
        return (board[0][0] == s && board[1][1] == s && board[2][2] == s)
            || (board[0][2] == s && board[1][1] == s && board[2][0] == s);
    }

    private boolean full() {
        for (int r = 0; r < 3; r++) for (int c = 0; c < 3; c++)
            if (board[r][c] == '\0') return false;
        return true;
    }

    private void disableBoard() {
        for (JButton[] row : buttons) for (JButton b : row) b.setEnabled(false);
    }
    private void updateStatus() {
        status.setText("Turn: " + players[turn].getName() + " (" + players[turn].getSymbol() + ")");
    }
    private void updateScore() {
        score.setText("Score   X: " + xWins + "     O: " + oWins + "     Draws: " + draws);
    }

    @Override public void reset() {
        for (int r = 0; r < 3; r++) for (int c = 0; c < 3; c++) {
            board[r][c] = '\0'; buttons[r][c].setText(""); buttons[r][c].setEnabled(true);
        }
        turn = 0; gameOver = false; updateStatus(); updateScore();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
            catch (Exception ex) { System.err.println("Using default look and feel."); }
            new TicTacToeGame().setVisible(true);
        });
    }
}
