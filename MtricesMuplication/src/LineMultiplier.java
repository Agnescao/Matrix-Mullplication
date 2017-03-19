import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class LineMultiplier implements Callable<int[][]> {
	int[][] a;
	int[][] b;
	int start;
	int rowsToDo;
//	public List< int[]> result;
	int sum = 0;
	int part;

	public LineMultiplier(int[][] a, int[][] b, int startLine, int rowsToDo) {
		// TODO Auto-generated constructor stub
		this.a = a;
		this.b = b;
		//result = new int[a.length][b[0].length];
		start = startLine;
		this.rowsToDo = rowsToDo;
	}

	@Override
	public int[][] call() throws Exception {
		int[][] result = new int[rowsToDo][a.length];
		int row = 0;
		for (int i = start; row <= rowsToDo; i++) {
			for (int k = 0; k < b.length; k++) {
				for (int j = 0; j < b[0].length; j++) {
					result[row][j] += a[i][k] * b[k][j];
				//	C[i][j]		   += a[i][k] * b[k][j];
				}
			}
			row++;
		}
		return result;
	}
}
