package Strassen;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import simpleMtrixMulplication.Utils;
import simpleMtrixMulplication.simpleAlgorithmMain;

public class parallelStrassenAlgorthim {
	/**
	 * Function to multiply matrices
	 * 
	 * @throws ExecutionException
	 * @throws InterruptedException
	 **/
	int thold = 2;
	boolean firstRun = true;
	public int[][] multiply(int[][] A, int[][] B) throws InterruptedException, ExecutionException {

		int N = A.length;
		int[][] R = new int[N][N];
		/** base case **/
		if (N <= thold || N == 3|| N == 5|| N == 7|| N == 9|| N == 11) {
			//return simple.parallelMult(A, B, 2);
			for (int i = 0; i < N; i++) {
				for (int k = 0; k < N; k++) {
					for (int j = 0; j < N; j++) {
						R[i][j] += A[i][k] * B[k][j];
					}
				}
			}
			return R;
		}
		
		/**
		 * M1 = (A11 + A22)(B11 + B22) M2 = (A21 + A22) B11 M3 = A11 (B12 - B22)
		 * M4 = A22 (B21 - B11) M5 = (A11 + A12) B22 M6 = (A21 - A11) (B11 +
		 * B12) M7 = (A12 - A22) (B21 + B22)
		 **/

		final int[][] M1;
		final int[][] M2;
		final int[][] M3;
		final int[][] M4;
		final int[][] M5;
		final int[][] M6;
		final int[][] M7;

		
		if(firstRun){
			firstRun = false;		
			List<FutureTask<int[][]>> taskList = runThrets(A, B, N);
			M1 = taskList.get(0).get();
			M2 = taskList.get(1).get();
			M3 = taskList.get(2).get();
			M4 = taskList.get(3).get();
			M5 = taskList.get(4).get();
			M6 = taskList.get(5).get();
			M7 = taskList.get(6).get();
		} else {
			List<int[][]> taskList = runCalculations(A, B, N);
			M1 = taskList.get(0);
			M2 = taskList.get(1);
			M3 = taskList.get(2);
			M4 = taskList.get(3);
			M5 = taskList.get(4);
			M6 = taskList.get(5);
			M7 = taskList.get(6);
		}

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
		join(C12, R, 0, N / 2);
		join(C21, R, N / 2, 0);
		join(C22, R, N / 2, N / 2);

		/** return result **/
		return R;
	}

	private List<FutureTask<int[][]>> runThrets (int[][] A, int[][] B, int N) {
		ExecutorService executor = Executors.newFixedThreadPool(7);
		List<FutureTask<int[][]>> taskList = new ArrayList<FutureTask<int[][]>>();
		// Start thread for the first half of the numbers
		
		int[][] A11 = new int[N / 2][N / 2];
		int[][] A12 = new int[N / 2][N / 2];
		int[][] A21 = new int[N / 2][N / 2];
		int[][] A22 = new int[N / 2][N / 2];
		int[][] B11 = new int[N / 2][N / 2];
		int[][] B12 = new int[N / 2][N / 2];
		int[][] B21 = new int[N / 2][N / 2];
		int[][] B22 = new int[N / 2][N / 2];

		/** Dividing matrix A into 4 halves **/
		split(A, A11, 0, 0);
		split(A, A12, 0, N / 2);
		split(A, A21, N / 2, 0);
		split(A, A22, N / 2, N / 2);
		/** Dividing matrix B into 4 halves **/
		split(B, B11, 0, 0);
		split(B, B12, 0, N / 2);
		split(B, B21, N / 2, 0);
		split(B, B22, N / 2, N / 2);

		
		
		taskList.add(runThret(add(A11, A22), add(B11, B22)));
		taskList.add(runThret(add(A21, A22), B11));
		taskList.add(runThret(A11, sub(B12, B22)));
		taskList.add(runThret(A22, sub(B21, B11)));
		taskList.add(runThret(add(A11, A12), B22));
		taskList.add(runThret(sub(A21, A11), add(B11, B12)));
		taskList.add(runThret(sub(A12, A22), add(B21, B22)));
		
		for(FutureTask<int[][]> task : taskList){
			executor.execute(task);
		}
		executor.shutdown();
		return taskList;
	}

	private List<int[][]> runCalculations(int[][] A, int[][] B, int N) {
		List<int[][]> taskList = new ArrayList();
		int[][] A11 = new int[N / 2][N / 2];
		int[][] A12 = new int[N / 2][N / 2];
		int[][] A21 = new int[N / 2][N / 2];
		int[][] A22 = new int[N / 2][N / 2];
		int[][] B11 = new int[N / 2][N / 2];
		int[][] B12 = new int[N / 2][N / 2];
		int[][] B21 = new int[N / 2][N / 2];
		int[][] B22 = new int[N / 2][N / 2];

		/** Dividing matrix A into 4 halves **/
		split(A, A11, 0, 0);
		split(A, A12, 0, N / 2);
		split(A, A21, N / 2, 0);
		split(A, A22, N / 2, N / 2);
		/** Dividing matrix B into 4 halves **/
		split(B, B11, 0, 0);
		split(B, B12, 0, N / 2);
		split(B, B21, N / 2, 0);
		split(B, B22, N / 2, N / 2);
 
		try {
			taskList.add(multiply(add(A11, A22), add(B11, B22)));
			taskList.add(multiply(add(A21, A22), B11));
			taskList.add(multiply(A11, sub(B12, B22)));
			taskList.add(multiply(A22, sub(B21, B11)));
			taskList.add(multiply(add(A11, A12), B22));
			taskList.add(multiply(sub(A21, A11), add(B11, B12)));
			taskList.add(multiply(sub(A12, A22), add(B21, B22)));
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		return taskList;
	}

	
	private FutureTask<int[][]> runThret(int[][] a, int[][] b) {
		
		FutureTask<int[][]> futureTask = new FutureTask<int[][]>(new Callable<int[][]>() {
			@Override
			public int[][] call() throws InterruptedException, ExecutionException {
				return multiply(a, b);
			}
		});
		return futureTask;
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
		int N = 0;
		while(N != 1){
			System.out.println("Strassen Multiplication Algorithm Test\n");
			parallelStrassenAlgorthim s = new parallelStrassenAlgorthim();
			N = Utils.inputN();
			int [][] A = Utils.generator(N);
			int [][] B = Utils.generator(N);
			
			long startTime = System.nanoTime();
			int[][] C = s.multiply(A, B);
			long finishTime = System.nanoTime();
			Utils.printMatrix(C);
			System.out.println("Multiplication   took " + (finishTime - startTime) / 1000000.0 + " milliseconds.");
			
		}
	}
}