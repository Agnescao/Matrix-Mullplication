


package simpleMtrixMulplication;

import java.util.Scanner;
public class Utils {

	

	public static int inputN() {
		// TODO Auto-generated method stub
		Scanner scan = new Scanner(System.in);
		
		System.out.println("Please enter N size of matrices (NXN) :");
		int n = scan.nextInt();
		return n;
	}
 
	public static int[][] generator(int n) {
		int[][] A = new int[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				A[i][j] = i+j;
			}
		}

		return A;
	}

	public  static void printMatrix(int[][] c) {
		// TODO Auto-generated method stub
		System.out.println("result matrices: -");
		for (int a = 0; a < c.length && a< 100; a++) {
			for (int b = 0; b < c[0].length && a< 100; b++) {
				System.out.print(c[a][b] + "\t");

			}
			System.out.print("\n");
		}

	}

	
	
}

