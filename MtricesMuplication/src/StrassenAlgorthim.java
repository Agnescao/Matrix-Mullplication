import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class StrassenAlgorthim {
	/**
	 * Function to multiply matrices
	 * 
	 * @throws ExecutionException
	 * @throws InterruptedException
	 **/
	public int[][] multiply(int[][] A, int[][] B) throws InterruptedException, ExecutionException {

		int n = A.length;

		int[][] R = new int[n][n];
		/** base case **/
		if (n == 4) {
			for (int i = 0; i < 4; i++) {
				for (int k = 0; k < 4; k++) {
					for (int j = 0; j < 4; j++) {
						R[i][j] += A[i][k] * B[k][j];
					}
				}
			}
			return R;
		}
		
		int[][] A11 = new int[n / 2][n / 2];
		int[][] A12 = new int[n / 2][n / 2];
		int[][] A21 = new int[n / 2][n / 2];
		int[][] A22 = new int[n / 2][n / 2];
		int[][] B11 = new int[n / 2][n / 2];
		int[][] B12 = new int[n / 2][n / 2];
		int[][] B21 = new int[n / 2][n / 2];
		int[][] B22 = new int[n / 2][n / 2];

		/** Dividing matrix A into 4 halves **/
		split(A, A11, 0, 0);
		split(A, A12, 0, n / 2);
		split(A, A21, n / 2, 0);
		split(A, A22, n / 2, n / 2);
		/** Dividing matrix B into 4 halves **/
		split(B, B11, 0, 0);
		split(B, B12, 0, n / 2);
		split(B, B21, n / 2, 0);
		split(B, B22, n / 2, n / 2);

		/**
		 * M1 = (A11 + A22)(B11 + B22) M2 = (A21 + A22) B11 M3 = A11 (B12 - B22)
		 * M4 = A22 (B21 - B11) M5 = (A11 + A12) B22 M6 = (A21 - A11) (B11 +
		 * B12) M7 = (A12 - A22) (B21 + B22)
		 **/

		ExecutorService executor = Executors.newFixedThreadPool(4);
		List<FutureTask<int[][]>> taskList1 = new ArrayList<FutureTask<int[][]>>();
		// Start thread for the first half of the numbers
		FutureTask<int[][]> futureTask_2 = new FutureTask<int[][]>(new Callable<int[][]>() {
			@Override
			public int[][] call() throws InterruptedException, ExecutionException {
				return multiply(add(A11, A22), add(B11, B22));
			}
		});
		FutureTask<int[][]> futureTask_3 = new FutureTask<int[][]>(new Callable<int[][]>() {
			@Override
			public int[][] call() throws InterruptedException, ExecutionException {
				return multiply(add(A21, A22), B11);
			}
		});
		FutureTask<int[][]> futureTask_4 = new FutureTask<int[][]>(new Callable<int[][]>() {
			@Override
			public int[][] call() throws InterruptedException, ExecutionException {
				return multiply(A11, sub(B12, B22));
			}
		});
		FutureTask<int[][]> futureTask_5 = new FutureTask<int[][]>(new Callable<int[][]>() {
			@Override
			public int[][] call() throws InterruptedException, ExecutionException {
				return multiply(A22, sub(B21, B11));
			}
		});
		FutureTask<int[][]> futureTask_6 = new FutureTask<int[][]>(new Callable<int[][]>() {
			@Override
			public int[][] call() throws InterruptedException, ExecutionException {
				return multiply(add(A11, A12), B22);
			}
		});
		FutureTask<int[][]> futureTask_7 = new FutureTask<int[][]>(new Callable<int[][]>() {
			@Override
			public int[][] call() throws InterruptedException, ExecutionException {
				return multiply(sub(A21, A11), add(B11, B12));
			}
		});
		FutureTask<int[][]> futureTask_8 = new FutureTask<int[][]>(new Callable<int[][]>() {
			@Override
			public int[][] call() throws InterruptedException, ExecutionException {
				return multiply(sub(A12, A22), add(B21, B22));
			}
		});
		taskList1.add(futureTask_2);
		taskList1.add(futureTask_3);
		taskList1.add(futureTask_4);
		taskList1.add(futureTask_5);
		taskList1.add(futureTask_6);
		taskList1.add(futureTask_7);
		taskList1.add(futureTask_8);
		executor.execute(futureTask_2);
		executor.execute(futureTask_3);
		executor.execute(futureTask_4);
		executor.execute(futureTask_5);
		executor.execute(futureTask_6);
		executor.execute(futureTask_7);
		executor.execute(futureTask_8);

		FutureTask<int[][]> ftrTask = taskList1.get(0);
		final int[][] M1 = ftrTask.get();
		FutureTask<int[][]> ftrTask1 = taskList1.get(1);
		final int[][] M2 = ftrTask1.get();
		FutureTask<int[][]> ftrTask2 = taskList1.get(2);
		final int[][] M3 = ftrTask2.get();
		FutureTask<int[][]> ftrTask3 = taskList1.get(3);
		final int[][] M4 = ftrTask3.get();
		FutureTask<int[][]> ftrTask4 = taskList1.get(4);
		final int[][] M5 = ftrTask4.get();
		FutureTask<int[][]> ftrTask5 = taskList1.get(5);
		final int[][] M6 = ftrTask5.get();
		FutureTask<int[][]> ftrTask6 = taskList1.get(6);
		final int[][] M7 = ftrTask6.get();

		executor.shutdown();

		/**
		 * C11 = M1 + M4 - M5 + M7 C12 = M3 + M5 C21 = M2 + M4 C22 = M1 - M2 +
		 * M3 + M6
		 **/
		int[][] C11 = add(sub(add(M1, M4), M5), M7);
		int[][] C12 = add(M3, M5);
		int[][] C21 = add(M2, M4);
		int[][] C22 = add(sub(add(M1, M3), M2), M6);

		/** join 4 halves into one result matrix **/
		join(C11, R, 0, 0);
		join(C12, R, 0, n / 2);
		join(C21, R, n / 2, 0);
		join(C22, R, n / 2, n / 2);

		/** return result **/
		return R;
	}

	/** Funtion to sub two matrices **/
	public int[][] sub(int[][] A, int[][] B) {
		int n = A.length;
		int[][] C = new int[n][n];
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				C[i][j] = A[i][j] - B[i][j];
		return C;
	}

	/** Funtion to add two matrices **/
	public int[][] add(int[][] A, int[][] B) {
		int n = A.length;
		int[][] C = new int[n][n];
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				C[i][j] = A[i][j] + B[i][j];
		return C;
	}

	/** Funtion to split parent matrix into child matrices **/
	public void split(int[][] P, int[][] C, int iB, int jB) {
		for (int i1 = 0, i2 = iB; i1 < C.length; i1++, i2++)
			for (int j1 = 0, j2 = jB; j1 < C.length; j1++, j2++)
				C[i1][j1] = P[i2][j2];
	}

	/** Funtion to join child matrices intp parent matrix **/
	public void join(int[][] C, int[][] P, int iB, int jB) {
		for (int i1 = 0, i2 = iB; i1 < C.length; i1++, i2++)
			for (int j1 = 0, j2 = jB; j1 < C.length; j1++, j2++)
				P[i2][j2] = C[i1][j1];
	}

	/**
	 * Main function
	 * 
	 * @throws ExecutionException
	 * @throws InterruptedException
	 **/
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		Scanner scan = new Scanner(System.in);
		System.out.println("Strassen Multiplication Algorithm Test\n");
		/** Make an object of Strassen class **/
		StrassenAlgorthim s = new StrassenAlgorthim();

		System.out.println("Enter order n :");
		int N = scan.nextInt();
		/** Accept two 2d matrices **/

		int[][] A = new int[N][N];
		for (int i = 0; i < N; i++)
			for (int j = 0; j < N; j++)
				A[i][j] = 1;

		int[][] B = new int[N][N];
		for (int i = 0; i < N; i++)
			for (int j = 0; j < N; j++)
				B[i][j] = 1;
		long startTime = System.nanoTime();
		int[][] C = s.multiply(A, B);
		long finishTime = System.nanoTime();
		System.out.println("Multiplication   took " + (finishTime - startTime) / 1000000.0 + " milliseconds.");
		/*System.out.println("\nProduct of matrices A and  B : ");
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++)
				System.out.print(C[i][j] + " ");
			System.out.println();
		}
*/
	}
}