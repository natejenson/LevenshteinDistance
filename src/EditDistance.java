import java.util.Random;
import java.io.FileInputStream;

public class EditDistance {
	public static void main(String[] args){
		try{
			// Set the input file. The input file should have two lines, each
			// with a string for comparison.
			System.setIn(new FileInputStream(args[0]));
		}
		catch(Exception e){
			System.err.printf("Exception caught: %s", e.toString());
			System.exit(0);
		}
		
		String X = StdIn.readLine();
		String Y = StdIn.readLine();
		timeEditDistance(X,Y);
		System.out.println();
		printOptimalAlignment(X,Y);
	}

	private static int editDistance(String X, String Y){
		int[][] opt = alignmentTable(X,Y);
		return opt[0][0];
	}

	private static int[][] alignmentTable(String X, String Y){
		int penalty;
		int M = X.length();
		int N = Y.length();
		//create table
		int[][] opt = new int[M+1][N+1];
		//Fill base cases.
		for(int i = 0; i <= N; i++){
			opt[M][i] = (N * 2) - (i * 2);
		}
		for(int j = 0; j <= M; j++){
			opt[j][N] = (M * 2) - (j * 2);
		}
		//Fill in rest of table, starting at (M-1,N-1)
		for(int i = M-1; i >= 0; i--){
			for(int j = N-1; j >= 0; j--){
				if(X.charAt(i) == Y.charAt(j)){
					penalty = 0;
				}
				else{
					penalty = 1;
				}
				//find minimum of relevant surrounding values
				int temp = Math.min(opt[i+1][j+1] + penalty,opt[i][j+1] + 2);
				opt[i][j] = Math.min(opt[i+1][j] + 2, temp);
			}
		}
		return opt;
	}

	private static void printOptimalAlignment(String X, String Y){
		int penalty,i,j;
		StringBuilder newX = new StringBuilder("");
		StringBuilder newY = new StringBuilder("");
		StringBuilder penaltyString = new StringBuilder("");
		int M = X.length();
		int N = Y.length();
		int[][] opt = alignmentTable(X,Y);
		i = 0;
		j = 0;
		while((i <= M) && (j <= N)){
			//Check if at one edge, or both.
			if((i == M) || (j == N)){
				if((i == M) && (j == N)){
					break;
				}
				if(j == N){
					// right edge, only move down
					newX.append(X.charAt(i) + " ");
					newY.append("- ");
					penaltyString.append("2 ");
					i++;
				}
				else{
					// bottom edge, only move right
					newY.append(Y.charAt(j) + " ");
					newX.append("- ");
					penaltyString.append("2 ");
					j++;
				}
			}
			//else if not at edge:
			else{
				//right + 2
				if(opt[i][j] == opt[i][j+1] + 2){
					newY.append(Y.charAt(j) + " ");
					newX.append("- ");
					penaltyString.append("2 ");
					j++;
					continue;
				}
				//down + 2
				if(opt[i][j] == opt[i+1][j] + 2){
					newX.append(X.charAt(i) + " ");
					newY.append("- ");
					penaltyString.append("2 ");
					i++;
				}
				//else third case.
				else{
					if(X.charAt(i) == Y.charAt(j)){
						penalty = 0;
					}
					else{
						penalty = 1;
					}
					newX.append(X.charAt(i) + " ");
					newY.append(Y.charAt(j) + " ");
					penaltyString.append(penalty + " ");	
					i++;
					j++;
				}
			}
		}
		//Print optimal alignment
		System.out.print(newX + "\n" + newY + "\n" + penaltyString + "\n");
	}

	public static void timeEditDistance(String X, String Y){
		int myEditDistance;
		Stopwatch myWatch = new Stopwatch();
		myEditDistance = editDistance(X,Y);
		double timeElapsed = myWatch.elapsedTime();
		System.out.printf("Dynamic Edit Distance: %d \nTime Elapsed(seconds): %.3f\n", myEditDistance, timeElapsed );
	}
}

