
import java.io.*;

public class Main {

  static int[] p; //размерности исходных матриц
  static int n;  //число исходных матриw

  public static void readDataFromFIle() throws Exception {
    BufferedReader in = new BufferedReader(new FileReader("input.txt"));
    n = Integer.parseInt(in.readLine());
    p = new int[n+1];
    String s;
    int idx = 0;
    while (((s = in.readLine()) != null)) {
      String[] leksArr = s.split(" ");
      int dimX = Integer.parseInt(leksArr[0]);
      int dimY = Integer.parseInt(leksArr[1]);
      if (idx == 0) {
        p[idx++] = dimX;
      }
      p[idx++] = dimY;
      if (idx== n+1)
        break;
    }
    in.close();
    if (idx< n)
       throw new Exception("Input file error format");
  }

  public static int calc() {
    int n = p.length - 1;
    int[][] dp = new int[n + 1][n + 1];

    for (int i = 1; i <= n; i++) {
      dp[i][i] = 0;
    }

    for (int l = 2; l <= n; l++) {
      for (int i = 1; i <= n - l + 1; i++) {
        int j = i + l - 1;
        dp[i][j] = Integer.MAX_VALUE;
        for (int k = i; k <= j - 1; k++) {
          dp[i][j] = Math.min(dp[i][j],
            dp[i][k] + dp[k + 1][j] + p[i - 1] * p[k] * p[j]);
        }
      }
    }
    return dp[1][n];
  }

  private static void writeDataToFile() {
    try {
      BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
      int res = calc();
      writer.write(Integer.toString(res));
      writer.close();
    } catch (IOException e) {
      System.out.println("error in output file");
    }
  }

  public static void main(String[] args) {
    try {
      long l = System.currentTimeMillis() ;
      readDataFromFIle();
      for (int i:p) {
        System.out.printf("%d ", i);
      }
      System.out.println();
      System.out.println(String.format("%d ms loaded", System.currentTimeMillis() - l));
      System.out.println(String.format("%d ms calc", System.currentTimeMillis() - l));
      writeDataToFile();
      System.out.println(String.format("%d ms write", System.currentTimeMillis() - l));
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

}
