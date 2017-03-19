import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class simple {
	static int threadsNumber = Runtime.getRuntime().availableProcessors();
	static int N = 0;;
	public static void main(String args[]) {
		
		N = inputN();
		int[][] A = generator(N);
		int[][] B = generator(N);
		System.err.println("Number of cores:\t" + threadsNumber);
		
		long startTime = System.nanoTime();
		int[][] C = parallelMult(A, B);
		long finishTime = System.nanoTime();

		System.out.println("Multiplication  with " + threadsNumber + " threads took " + (finishTime - startTime) / 1000000.0 + " milliseconds.");
		printMatrix(C);
	}


	private static int[][] parallelMult(int[][] a, int[][] b) {
		
		ExecutorService executor = Executors.newFixedThreadPool(threadsNumber);
		
		List<Future<int[][]>> list = initThrets(executor, a, b);
		
		int[][] result = retriveResults(list, executor);
		
		return result;
	}

	public static List<Future<int[][]>> initThrets(ExecutorService executor, int[][] a, int[][] b) {
		List<Future<int[][]>> list = new ArrayList();
		int startLine = 0;
		int extraRowsNo = a.length % threadsNumber;
		int step = a.length / threadsNumber;
		int extraStep = 0;
		
		while (startLine < N) {
			if(extraRowsNo > 0){
				extraStep = 1;
				extraRowsNo --;
			} else {
				extraStep = 0;
			}
			System.out.println("a, b " + startLine + "; " + step  + " + " + extraStep);
			Callable<int[][]> worker = new LineMultiplier(a, b, startLine, step + extraStep);
			Future<int[][]> submit = executor.submit(worker);
			list.add(submit);
			startLine += step + extraStep;
		}
		return list;
	}
	
	public static int[][] retriveResults (List<Future<int[][]>> list, ExecutorService executor) {
		int[][] result = new int[N][N];
		int actualtRow = 0;
		int CF[][] = null;
		for (Future<int[][]> future : list) {
			try {
				CF = future.get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}	
			for(int[] row : CF){
				result[actualtRow] = row;
				actualtRow++;
			}
		}
		executor.shutdown();


		return result;

	}
	
	
	private static int inputN() {
		// TODO Auto-generated method stub
		Scanner scan = new Scanner(System.in);
		System.out.println("simple Algorithm Test\n");
		System.out.println("Enter order n :");
		int n = scan.nextInt();
		return n;
	}

	private static int[][] generator(int n) {
		int[][] A = new int[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				A[i][j] = 1;
			}
		}
			
		return A;
	}

	private static void printMatrix(int[][] c) {
		// TODO Auto-generated method stub
		System.out.println("result matrices: -");
		for (int a = 0; a < c.length; a++) {
			for (int b = 0; b < c[0].length; b++) {
				System.out.print(c[a][b] + "\t");

			}
			System.out.print("\n");
		}

	}

	
	
}
