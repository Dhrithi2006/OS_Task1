package os_task2;

public class CellMultiplicationTask implements Runnable{
	private final int row;
    private final int col;
    private final int colsA;
    private final double[][] matrixA;
    private final double[][] matrixB;
    private final double[][] resultMatrix;

    public CellMultiplicationTask(int row, int col, int colsA, 
                                  double[][] matrixA, double[][] matrixB, double[][] resultMatrix) {
        this.row = row;
        this.col = col;
        this.colsA = colsA;
        this.matrixA = matrixA;
        this.matrixB = matrixB;
        this.resultMatrix = resultMatrix;
    }

    @Override
    public void run() {
        double sum = 0.0;
        for (int k = 0; k < colsA; k++) {
            sum += matrixA[row][k] * matrixB[k][col];
        }
        resultMatrix[row][col] = sum;
    }

}
