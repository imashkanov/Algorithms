import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {

  static class Point {
    int x;
    int y;
    int alt;

    Point() {}

    Point(int x, int y, int alt) {
      this.x = x;
      this.y = y;
      this.alt = alt;
    }

    @Override
    public String toString() {
      return String.format("%d:%d/%d", x, y , alt);
    }
  }

  static Point[][] Matr;
  static int N;
  static int M;
  static int K;
  static Point From;
  static Point To;

  public static void readDataFromFile() throws IOException {
    BufferedReader in = new BufferedReader(new FileReader("in.txt"));
    String s;
    s = in.readLine();
    String[] leksArr = s.split(" ");
    N = Integer.parseInt(leksArr[0]);
    M = Integer.parseInt(leksArr[1]);
    Matr = new Point[N][M];
    for (int i=0; i<N; i++) {
      for (int j=0; j<M; j++) {
        Matr[i][j] = new Point();
        Matr[i][j].x =i+1;
        Matr[i][j].y =j+1;
      }
    }
    int cntN = N;
    while (cntN-- != 0) {
      s = in.readLine();
      leksArr = s.split(" ");
      for (int i=0; i<leksArr.length; i++) {
        Matr[N-cntN-1][i].alt = Integer.parseInt(leksArr[i]);
      }
    }
    K = Integer.parseInt(in.readLine());
    s = in.readLine();
    leksArr = s.split(" ");
    From = new Point();
    From.x = Integer.parseInt(leksArr[0]);
    From.y = Integer.parseInt(leksArr[1]);
    To = new Point();
    To.x = Integer.parseInt(leksArr[0]);
    To.y = Integer.parseInt(leksArr[1]);
    in.close();
  }

  public static void printMatr(String comment) {
    System.out.printf("%s\n--------------\n", comment);
    for (int i=0; i<N; i++) {
      for (int j=0; j<M; j++) {
        System.out.printf("%s ", Matr[i][j]);
      }
      System.out.println();
    }
  }

  public static void main(String[] args) {
    try {
      readDataFromFile();
      printMatr("Source matr:");
      System.out.println(K);
      System.out.printf("From: %s To: %s", From, To);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
