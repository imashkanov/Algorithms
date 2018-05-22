import java.io.*;
import java.util.Scanner;

public class Main {

    static int[][] Matr;
    static int[] metki;
    static  int n;
    static int metka = 1;

    private static void readDataFromFile() throws FileNotFoundException {
        Scanner sc = new Scanner(new File("input.in"));
        n = Integer.parseInt(sc.nextLine());
        Matr = new int[n][n];
        metki = new int[n];
        for (int i = 0, k = 0; i < Matr.length; i++, k = 0) {
            for (String s : sc.nextLine().split(" "))
                Matr[i][k++] = Integer.parseInt(s);
        }

    }


    private static void writeDataToFile() throws IOException {
        FileWriter fw = new FileWriter("output.out");
        fw.append(metka == n + 1 ? "YES" : "NO");
        fw.flush();
    }

    private static void dfs(int v) {
        for (int j = 0; j < Matr[v].length; j++)
            if (Matr[v][j] == 1 && metki[j] == 0) {
                metki[j] = metka++;
                dfs(j);
            }
    }

    public static void main(String[] args) throws IOException {
        readDataFromFile();
        metki[0] = metka++;
        dfs(0);
        writeDataToFile();
    }

}