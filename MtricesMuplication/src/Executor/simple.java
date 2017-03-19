package Executor;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class simple {
	
	public static void main(String args[]){
		
		Scanner scan = new Scanner(System.in);
        System.out.println("simple Algorithm Test\n");
      
        System.out.println("Enter rows and columns n :");
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
	        
	        
	       for(int i= 0; i< A.length; i+=2){
	    	  int [] A1= A[i];
	       }
	     
	        ExecutorService executor = Executors.newFixedThreadPool(10);
	        for(int i=0; i<A.length; i++){
	        	Runnable worker = new WorkThread(A, B);
	        	executor.execute(worker);
	        }
	        executor.shutdown();
		
	}

}
