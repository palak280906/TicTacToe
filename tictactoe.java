import java.awt.*;
import java.io.*;
import javax.swing.*;

public class tictactoe extends JFrame {
    private JButton[][] buttons = new JButton[3][3];
    private char currentPlayer = 'X';
    private JLabel statusLabel;
    private int scoreX = 0, scoreO = 0;
    private boolean vsAI = true;

    public tictactoe() {
        setTitle("Advanced Tic Tac Toe");
        setSize(400, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel boardPanel = new JPanel(new GridLayout(3, 3));
        Font font = new Font("Arial", Font.BOLD, 40);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton("");
                buttons[i][j].setFont(font);
                int row = i, col = j;
                buttons[i][j].addActionListener(e -> handleMove(row, col));
                boardPanel.add(buttons[i][j]);
            }
        }

        statusLabel = new JLabel("Player X's Turn", SwingConstants.CENTER);
        JButton resetBtn = new JButton("Reset");
        JButton modeBtn = new JButton("Switch Mode (2P/AI)");
        JButton saveBtn = new JButton("Save History");

        resetBtn.addActionListener(e -> resetGame());

        modeBtn.addActionListener(e -> {
            vsAI = !vsAI;
            resetGame();
            statusLabel.setText(vsAI ? "AI Mode: Player X's Turn" : "2 Player Mode: Player X's Turn");
        });

        saveBtn.addActionListener(e -> saveGame());

        JPanel bottomPanel = new JPanel(new GridLayout(2, 1));
        JPanel btnPanel = new JPanel();

        btnPanel.add(resetBtn);
        btnPanel.add(modeBtn);
        btnPanel.add(saveBtn);

        bottomPanel.add(statusLabel);
        bottomPanel.add(btnPanel);

        add(boardPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void handleMove(int row, int col) {
        if (!buttons[row][col].getText().equals("")) return;

        buttons[row][col].setText(String.valueOf(currentPlayer));

        if (checkWinner(currentPlayer)) {
            updateScore(currentPlayer);
            disableBoard();
            return;
        }

        // ✅ FIX: Disable board on draw
        if (isBoardFull()) {
            statusLabel.setText("Draw!");
            disableBoard();
            return;
        }

        currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
        statusLabel.setText("Player " + currentPlayer + "'s Turn");

        if (vsAI && currentPlayer == 'O') {
            aiMove();
        }
    }

    // (No change in AI logic as per your request)

    private void aiMove() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j].getText().equals("")) {
                    handleMove(i, j);
                    return;
                }
            }
        }
    }

    private boolean checkWinner(char player) {
        String p = String.valueOf(player);

        for (int i = 0; i < 3; i++) {
            if (buttons[i][0].getText().equals(p) &&
                buttons[i][1].getText().equals(p) &&
                buttons[i][2].getText().equals(p)) {
                highlightWinningCombo(new int[][]{{i,0},{i,1},{i,2}});
                return true;
            }
        }

        for (int j = 0; j < 3; j++) {
            if (buttons[0][j].getText().equals(p) &&
                buttons[1][j].getText().equals(p) &&
                buttons[2][j].getText().equals(p)) {
                highlightWinningCombo(new int[][]{{0,j},{1,j},{2,j}});
                return true;
            }
        }

        if (buttons[0][0].getText().equals(p) &&
            buttons[1][1].getText().equals(p) &&
            buttons[2][2].getText().equals(p)) {
            highlightWinningCombo(new int[][]{{0,0},{1,1},{2,2}});
            return true;
        }

        if (buttons[0][2].getText().equals(p) &&
            buttons[1][1].getText().equals(p) &&
            buttons[2][0].getText().equals(p)) {
            highlightWinningCombo(new int[][]{{0,2},{1,1},{2,0}});
            return true;
        }

        return false;
    }

    private void highlightWinningCombo(int[][] positions) {
        for (int[] pos : positions) {
            buttons[pos[0]][pos[1]].setBackground(Color.GREEN);
        }
    }

    private void updateScore(char player) {
        if (player == 'X') {
            scoreX++;
        } else {
            scoreO++;
        }
        statusLabel.setText("Player " + player + " Wins! | Score X: " + scoreX + " O: " + scoreO);
    }

    private void disableBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setEnabled(false);
            }
        }
    }

    private boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j].getText().equals("")) {
                    return false;
                }
            }
        }
        return true;
    }

    private void resetGame() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
                buttons[i][j].setEnabled(true);
                buttons[i][j].setBackground(null);
            }
        }
        currentPlayer = 'X';
        statusLabel.setText("Player X's Turn");
    }

    private void saveGame() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("game_history.txt", true))) {
            writer.println("Score X: " + scoreX + " | Score O: " + scoreO);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new tictactoe();
    }
}
