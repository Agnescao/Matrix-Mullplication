package Executor;

public class WorkThread implements Runnable {
	
	int[][] a, b;
	int sum=0;
	

	public WorkThread(int[][] a, int[][] b) {
		// TODO Auto-generated constructor stub
		this.a=a;
		this.b=b;
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		int[][] C= new int[a.length][b[0].length];
		for(int i=0; i<a.length; i++){
			for(int j=0; j<b[0].length; j++){
				for(int k=0; k<b.length; k++){
					sum+=a[i][k]*b[k][j];
					
				}
				C[i][j]=sum;
				sum=0;
			}
		
		}
		for(int i= 0; i<a.length;i++){
			for(int j=0; j<b[0].length;j++){
				System.out.print(C[i][j] + "\t");
			}
			System.out.print("\n");
		}

	}

}
