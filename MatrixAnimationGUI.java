package os_task2;
import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class MatrixAnimationGUI extends JFrame {

    private static final int ROWS = 100;
    private static final int COLS = 100;
    private static final int CELL_SIZE = 5; 

    private final double[][] matrixA = new double[ROWS][COLS];
    private final double[][] matrixB = new double[ROWS][COLS];
    private final double[][] resultMatrix = new double[ROWS][COLS];
    private final boolean[][] computed = new boolean[ROWS][COLS];

    private final AtomicInteger completedCells = new AtomicInteger(0);
    private final JLabel statusLabel = new JLabel("Status: Ready to start");
    private final JProgressBar progressBar = new JProgressBar(0, ROWS * COLS);
    private final JButton startButton = new JButton("Start Thread Multiplication");
    private final MatrixCanvas canvas = new MatrixCanvas();

    public MatrixAnimationGUI() {
        super("Multi-Threaded 100x100 Matrix Multiplication Demo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        Random rand = new Random(42);
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                matrixA[i][j] = rand.nextInt(10) + 1;
                matrixB[i][j] = rand.nextInt(10) + 1;
            }
        }

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        startButton.setFont(new Font("SansSerif", Font.BOLD, 13));
        startButton.addActionListener(e -> startComputation());
        topPanel.add(startButton);

        progressBar.setPreferredSize(new Dimension(200, 22));
        progressBar.setStringPainted(true);
        topPanel.add(progressBar);

        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        topPanel.add(statusLabel);

        add(topPanel, BorderLayout.NORTH);
        add(canvas, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void startComputation() {
        startButton.setEnabled(false);
        completedCells.set(0);
        progressBar.setValue(0);
        statusLabel.setText("Status: Threads computing...");

        Timer refreshTimer = new Timer(33, e -> {
            int count = completedCells.get();
            progressBar.setValue(count);
            canvas.repaint();

            if (count >= ROWS * COLS) {
                ((Timer) e.getSource()).stop();
                statusLabel.setText("Status: Finished (10,000 / 10,000 cells complete!)");
            }
        });
        refreshTimer.start();

        new Thread(() -> {
            int cores = Runtime.getRuntime().availableProcessors();
            ExecutorService executor = Executors.newFixedThreadPool(cores * 2);

            for (int i = 0; i < ROWS; i++) {
                for (int j = 0; j < COLS; j++) {
                    final int r = i;
                    final int c = j;

                    executor.execute(() -> {
                        double sum = 0.0;
                        for (int k = 0; k < COLS; k++) {
                            sum += matrixA[r][k] * matrixB[k][c];
                        }
                        resultMatrix[r][c] = sum;
                        computed[r][c] = true;
                        completedCells.incrementAndGet();

                        try {
                            Thread.sleep(0, 200000); 
                        } catch (InterruptedException ignored) {
                        }
                    });
                }
            }

            executor.shutdown();
        }).start();
    }

    private class MatrixCanvas extends JPanel {
        public MatrixCanvas() {
            setPreferredSize(new Dimension(COLS * CELL_SIZE + 20, ROWS * CELL_SIZE + 20));
            setBackground(new Color(25, 25, 30));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int offsetX = 10;
            int offsetY = 10;

            for (int r = 0; r < ROWS; r++) {
                for (int c = 0; c < COLS; c++) {
                    if (computed[r][c]) {
                        g.setColor(new Color(46, 204, 113));
                    } else {
                        g.setColor(new Color(50, 50, 60));
                    }
                    g.fillRect(offsetX + (c * CELL_SIZE), offsetY + (r * CELL_SIZE), 
                               CELL_SIZE - 1, CELL_SIZE - 1);
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MatrixAnimationGUI gui = new MatrixAnimationGUI();
            gui.setVisible(true);
        });
    }
}