package simpleMtrixMulplication;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class simple {
	static int threadsNumber = Runtime.getRuntime().availableProcessors();
	static int N = 0;;

	public static void main(String args[]) {
		
		long startTime = 0;
		long finishTime = 0;
		while (N != -1) {
			System.out.println("simple Algorithm Test\n");
			N = Utils.inputN();
			int[][] A = Utils.generator(N);
			int[][] B = Utils.generator(N);
			System.err.println("Number of cores:\t" + threadsNumber);

			startTime = System.nanoTime();
			int[][] C = parallelMult(A, B);
			finishTime = System.nanoTime();

			System.out.println("Multiplication  with " + threadsNumber + " threads took "
					+ (finishTime - startTime) / 1000000.0 + " milliseconds.");
		//	Utils.printMatrix(C);
		}
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
			if (extraRowsNo > 0) {
				extraStep = 1;
				extraRowsNo--;
			} else {
				extraStep = 0;
			}
			System.out.println("a, b " + startLine + "; " + step + " + " + extraStep);
			Callable<int[][]> worker = new LineMultiplier(a, b, startLine, step + extraStep);
			Future<int[][]> submit = executor.submit(worker);
			list.add(submit);
			startLine += step + extraStep;
		}
		return list;
	}

	public static int[][] retriveResults(List<Future<int[][]>> list, ExecutorService executor) {
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
			for (int[] row : CF) {
				result[actualtRow] = row;
				actualtRow++;
			}
		}
		executor.shutdown();

		return result;

	}

}
