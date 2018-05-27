import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Main {

    static int[][] Matr;
    static int N;
    static int[] labeled;
    static int label = 1;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("input.txt"));
        N = Integer.parseInt(br.readLine());
        Matr = new int[N][N];
        labeled = new int[N];
        for (int i = 0; i < Matr.length; i++) {
            int k = 0;
            for (String s : br.readLine().split(" "))
                Matr[i][k++] = Integer.parseInt(s);
        }

        for (int i = 0; i < labeled.length; i++)
            if (labeled[i] == 0) {
                labeled[i] = label++;
                dfs(i);
            }

        FileWriter fw = new FileWriter("output.txt");
        for (int i : labeled)
            fw.append(String.valueOf(i)).append(" ");
        br.close();
        fw.close();
    }

    static void dfs(int v) {
        for (int j = 0; j < Matr[v].length; j++)
            if (Matr[v][j] == 1 && labeled[j] == 0) {
                labeled[j] = label++;
                dfs(j);
            }
    }
}