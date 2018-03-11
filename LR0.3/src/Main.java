import com.sun.deploy.util.StringUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {

  static InputMatr[] A; //размерности исходных матриц
  static int N;  //число исходных матрий
  static int[][] M;  //матрица расчета

  static class InputMatr {
    int cols;
    int rows;

    @Override
    public String toString() {
      return String.format("[%d x %d]", cols, rows);
    }
  }

  public static void printMatrOfOperCnt(String comment) {
    System.out.println(comment);
    for (int iy = 0; iy < N; iy++) {
      for (int ix = 0; ix < iy ; ix++) {
        System.out.printf("     ");
      }
      for (int ix = iy; ix < N; ix++) {
        System.out.printf("%4d ", M[iy][ix]);
      }
      System.out.printf("\n");
    }

  }

  public static void readDataFromFIle() throws IOException {
    BufferedReader in = new BufferedReader(new FileReader("input.txt"));
    N = Integer.parseInt(in.readLine());
    A = new InputMatr[N];
    M = new int[N][N];
    String s;
    int idx = 0;
    while ((s = in.readLine()) != null) {
      String[] leksArr = StringUtils.splitString(s, " ");
      InputMatr matr = new InputMatr();
      matr.cols = Integer.parseInt(leksArr[0]);
      matr.rows = Integer.parseInt(leksArr[1]);
      A[idx] = matr;
      idx++;
    }
    in.close();
  }

  public static void calc() {
    for (int step = 1; step< N; step++) {
      for (int i = 0; i< N -step; i++) {
        int col = i+step;
        int row = i;
        M[row][col] = mul2matr(row, col);
      }
      printMatrOfOperCnt(Integer.toString(step));
    }
  }

  private static int mul2matr(int n1, int n2) {
    int res = A[n1].cols*A[n1].rows*A[n2].rows;
    return res;
  }

  public static void main(String[] args) {
    try {
      readDataFromFIle();
      System.out.println(N);
      for (InputMatr matr: A) {
        System.out.println(matr);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    calc();

  }
}
