package simpleMtrixMulplication;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
public class singleThreadNaiveAlgorithm{
	
	public static void main(String args[]){
	/*	int m, n, p, q, sum=0;
		
		Scanner in =new Scanner(System.in);
		System.out.println("Enter the number of rows and colmuns of first matrix");
		m = in.nextInt();
		n = in.nextInt();
		
		int[][] first = new int[m][n];
		
		System.out.println("Enter elements of first matrix");
		
		for(int i=0; i<m; i++)
			for(int j=0; j<n; j++)
				first[i][j]=in.nextInt();
	
		
		System.out.println("Enter the number of rows and colmuns of second matrix");
		p = in.nextInt();
		q = in.nextInt();
		if (p != n){
			System.out.println("The number of culmuns in first matrix is not equal to that of second culmuns, please ");	
		}else{
		int[][] second = new int[p][q];
		int multiply[][] = new int[m][q];
		
		System.out.println("Enter elements of second matrix");
		
		for(int i=0; i<p; i++){
			for(int j=0; j<q; j++){
				second[i][j]=in.nextInt();
			}
		}
		
		for(int a=0; a<m; a++){
			for(int b=0;b<n; b++){
				System.out.print(first[a][b] + "\t");
				
			}
			System.out.print("\n");
		}
		
		*/
		int N=0;
		long startTime = 0;
		long finishTime = 0;
		while (N != -1) {
			System.out.println("simple Algorithm Test\n");
			N = Utils.inputN();
			int[][] A = Utils.generator(N);
			int[][] B = Utils.generator(N);
			int sum=0;
			startTime = System.nanoTime();
			int [][] multiply= new int[A.length][B[0].length];
		for(int i=0; i<A.length;i++){
			for(int j=0;j<B.length;j++){
				for(int k=0; k<B[0].length; k++){
					sum=sum+ A[i][k] * B[k][j];
				}
				
				multiply[i][j]=sum;
				sum=0;
			}
		}
		finishTime = System.nanoTime();
		for(int a=0; a<A.length; a++){
			for(int b=0;b<B[0].length; b++){
				System.out.print(multiply[a][b] + "\t");
				
			}
			System.out.print("\n");
		}
		
		System.out.println("Multiplication  with 1 threads took "
				+ (finishTime - startTime) / 1000000.0 + " milliseconds.");
		System.out.println("result matrices: -");
		
	}
		
	}

	
}
