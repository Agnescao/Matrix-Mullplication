import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
public class naive{
	
	public static void main(String args[]){
		int m, n, p, q, sum=0;
		
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
		
		
		
		
		
		for(int i=0; i<m;i++){
			for(int j=0;j<q;j++){
				for(int k=0; k<p; k++){
					sum=sum+ first[i][k] * second[k][j];
				}
				
				multiply[i][j]=sum;
				sum=0;
			}
		}
		System.out.println("result matrices: -");
		for(int a=0; a<m; a++){
			for(int b=0;b<n; b++){
				System.out.print(multiply[a][b] + "\t");
				
			}
			System.out.print("\n");
		}
		
	}
		
	}

	
}
