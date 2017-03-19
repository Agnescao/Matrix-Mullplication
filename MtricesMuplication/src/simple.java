import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class simple {

	public static void main(String args[]) {
		
		int N = inputN();
		int[][] A = generator(N);
		int[][] B = generator(N);
		int threads = Runtime.getRuntime().availableProcessors();
		System.err.println("Number of cores:\t" + threads);
		int[][] C = parallelMult(A, B);
		printMatrix(C);
		

	}

	private static int inputN() {
		// TODO Auto-generated method stub
		Scanner scan = new Scanner(System.in);
		System.out.println("simple Algorithm Test\n");
		System.out.println("Enter order n :");
		int N = scan.nextInt();
		return N;
	}

	private static int[][] generator(int N) {
		int[][] A = new int[N][N];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
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

	private static int[][] parallelMult(int[][] a, int[][] b) {
		// TODO Auto-generated method stub
		int[][] C = new int[a.length][b[0].length];
		int threadsNumber = 4;
		ExecutorService executor = Executors.newFixedThreadPool(4);
		List<Future<int[][]>> list = new ArrayList<Future<int[][]>>();
		int mod = a.length % threadsNumber;
		
		int part = a.length / threadsNumber;
		
		if (part < 1) {
			part = 1;
		}
		long startTime = System.nanoTime();

		for (int i = 0; i < a.length; i += part) {
			int j=i+ part;
			if(mod == 1 && i ==a.length-1){
				j= i+1;
			}else if(mod ==2 && i ==a.length -2){
					j =i+2;
			}else if(mod ==3 && i == a.length-3){
					j=i+3;
			}else{
				j=j;
				}
			Callable<int[][]> worker = new LineMultiplier(a, b, i, j, mod);
			Future<int[][]> submit = executor.submit(worker);
			list.add(submit);
		}
		
		long finishTime = System.nanoTime();
		
		System.out.println("Multiplication  with " + threadsNumber + " threads took "
				+ (finishTime - startTime) / 1000000.0 + " milliseconds.");

		// now retrieve the result
		int start = 0;
		int CF[][];
		
		for (Future<int[][]> future : list) {
			try {
				CF = future.get();
				int l=start + part;
				if(mod == 1 && start ==a.length-1){
					l= l+1;
				}else if(mod ==2 && start ==a.length -2){
					l= l+2;
				}else if(mod ==3 && start == a.length-3){
					l= l+3;
				}else{
					
					}
				for (int i = start; i < l; i++) {
					C[i] = CF[i];
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
			start += part;
		}
		executor.shutdown();
		
		return C;

	}
	 
}
