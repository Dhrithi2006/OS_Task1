package os_task2;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MatrixMultiplicationThreads {

    private static final int ROWS_A = 100;
    private static final int COLS_A = 100;
    private static final int ROWS_B = 100;
    private static final int COLS_B = 100;

    public static void main(String[] args) {
        double[][] matrixA = new double[ROWS_A][COLS_A];
        double[][] matrixB = new double[ROWS_B][COLS_B];
        double[][] resultMatrix = new double[ROWS_A][COLS_B];

        // 1. Fill matrices with random values
        Random random = new Random(42);
        for (int i = 0; i < ROWS_A; i++) {
            for (int j = 0; j < COLS_A; j++) {
                matrixA[i][j] = random.nextInt(10) + 1;
            }
        }
        for (int i = 0; i < ROWS_B; i++) {
            for (int j = 0; j < COLS_B; j++) {
                matrixB[i][j] = random.nextInt(10) + 1;
            }
        }

        System.out.println("Starting matrix multiplication (100x100 = 10,000 cell operations)...");
        long startTime = System.currentTimeMillis();

        // 2. Thread pool to handle concurrent execution
        int cores = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(cores * 2);

        // 3. Dispatch each cell calculation
        for (int i = 0; i < ROWS_A; i++) {
            for (int j = 0; j < COLS_B; j++) {
                executor.execute(new CellMultiplicationTask(i, j, COLS_A, matrixA, matrixB, resultMatrix));
            }
        }

        // 4. Shutdown executor and await task completion
        executor.shutdown();
        try {
            boolean finished = executor.awaitTermination(1, TimeUnit.MINUTES);
            if (!finished) {
                System.err.println("Timeout: Tasks did not complete in time.");
            }
        } catch (InterruptedException e) {
            System.err.println("Execution interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();
        }

        long totalTime = System.currentTimeMillis() - startTime;
        System.out.println("All 10,000 thread operations completed in " + totalTime + " ms.");

        // 5. Verification
        boolean passed = verifyResult(matrixA, matrixB, resultMatrix);
        System.out.println("Verification check: " + (passed ? "PASSED" : "FAILED"));
        System.out.println("Sample cell C[0][0] value: " + resultMatrix[0][0]);
        System.out.println("\n--- Sample Values from Matrix A (Top-Left 3x3) ---");
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.printf("%6.1f ", matrixA[i][j]);
            }
            System.out.println();
        }

        System.out.println("\n--- Sample Values from Matrix B (Top-Left 3x3) ---");
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.printf("%6.1f ", matrixB[i][j]);
            }
            System.out.println();
        }

        System.out.println("\n--- Computed Result Matrix C (Top-Left 3x3) ---");
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.printf("%8.1f ", resultMatrix[i][j]);
            }
            System.out.println();
        }

        System.out.println("\n--- Four Corner Cells of Result Matrix C (100x100) ---");
        System.out.printf("Top-Left     C[0][0]     : %.1f\n", resultMatrix[0][0]);
        System.out.printf("Top-Right    C[0][99]    : %.1f\n", resultMatrix[0][99]);
        System.out.printf("Bottom-Left  C[99][0]    : %.1f\n", resultMatrix[99][0]);
        System.out.printf("Bottom-Right C[99][99]   : %.1f\n", resultMatrix[99][99]);
    }

    private static boolean verifyResult(double[][] a, double[][] b, double[][] res) {
        for (int i = 0; i < ROWS_A; i++) {
            for (int j = 0; j < COLS_B; j++) {
                double expected = 0.0;
                for (int k = 0; k < COLS_A; k++) {
                    expected += a[i][k] * b[k][j];
                }
                if (Math.abs(expected - res[i][j]) > 1e-6) {
                    return false;
                }
            }
        }
        return true;
    }
}