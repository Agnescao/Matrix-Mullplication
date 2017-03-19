import java.util.concurrent.Callable;

public class LineMultiplier implements Callable<int[][]> {
	
	int[][] a;
	int[][] b;
	int start;
	int end;
	public int[][] C;
	int mod;
	int sum=0;
	int part;
	
	public LineMultiplier(int[][] a, int[][] b, int i, int j, int mod) {
		// TODO Auto-generated constructor stub
		this.a=a;
		this.b=b;
		C= new int[a.length][b[0].length];
		start=i;
		end=j;
		this.mod=mod;
		
		
	}

	@Override
	public int[][] call() throws Exception {
		// TODO Auto-generated method stub
		
		for(int i = start; i<end; i++)
			for(int k=0; k< b.length;k++)
				for(int j=0; j<b[0].length;j++)
					C[i][j]+= a[i][k]*b[k][j];
		return C;
		
	}

}
