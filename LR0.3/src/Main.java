import com.sun.deploy.util.StringUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {

  static InputMatr[] A; //размерности исходных матриц
  static int N;  //число исходных матрий
  static Cell[][] M;  //матрица расчета

  static class InputMatr {
    int cols;
    int rows;

    @Override
    public String toString() {
      return String.format("[%d x %d]", cols, rows);
    }
  }

  static class Cell {
    int cols;
    int rows;
    int count;

    @Override
    public String toString() {
      String s = String.format("[%dx%d]", cols, rows);
      return String.format("%5d %-7s", count, s);
    }
  }

  public static void printMatrOfOperCnt(String comment) {
    System.out.println(comment);
    for (int iy = 0; iy < N; iy++) {
      for (int ix = 0; ix < iy ; ix++) {
        System.out.printf("    X         ");
      }
      for (int ix = iy; ix < N; ix++) {
        System.out.printf("%s ", M[iy][ix]);
      }
      System.out.printf("\n");
    }

  }

  public static void readDataFromFIle() throws IOException {
    BufferedReader in = new BufferedReader(new FileReader("input.txt"));
    N = Integer.parseInt(in.readLine());
    A = new InputMatr[N];
    M = new Cell[N][N];
    for (int row=0; row<N; row++) {
      for (int col=0; col<N; col++) {
        M[col][row] = new Cell();
      }
    }
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

  private static Cell mul2matr(int r, int c) {
    Cell res = new Cell();
    res.cols = A[r].cols;
    res.rows = A[c].rows;
    if (M[r][c-1].cols>0) {
      int leftCnt = M[r][c - 1].count;
      int left = M[r][c - 1].cols * M[r][c - 1].rows * res.rows;
      int resL = leftCnt + left;
      int botmCnt = M[r + 1][c].count;
      int botm = M[r + 1][c].cols * res.cols * res.rows;
      int resB = botmCnt + botm;
      res.count = Math.min(resL, resB);
    } else {
      res.count = A[c].cols*A[c].rows*A[r].cols;
    }
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
