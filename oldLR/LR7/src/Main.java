import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Main implements Runnable {

  class Point {
    int x;
    int y;
    int value;

    Point() {}

    Point(int x, int y, int value) {
      this.x = x;
      this.y = y;
      this.value = value;
    }

    @Override
    public String toString() {
      return String.format("%d/%d:%d\n", x, y , value);
    }
  }

  int N;//количество х(столбцов)
  int M;//количество y(строк)
  Point[][] Matr;

  private void readDataFromFile() throws IOException {
    BufferedReader in = new BufferedReader(new FileReader("in.txt"));
    String s;
    s = in.readLine();
    String[] leksArr = s.split(" ");
    N = Integer.parseInt(leksArr[0]);
    M = Integer.parseInt(leksArr[1]);
    Matr = new Point[M][N];
    for (int y=0; y<M; y++) {
      for (int x=0; x<N; x++) {
        Matr[y][x] = new Point();
        Matr[y][x].x = x;
        Matr[y][x].y = y;
      }
    }
    int cntN = N;
    while (cntN-- != 0) {
      s = in.readLine();
      char[] chars = s.toCharArray();
      for (int i=0; i<chars.length; i++) {
        Matr[N-cntN-1][i].value = Character.getNumericValue(chars[i]);
      }
    }
    in.close();
  }

  public void printMatrCoord(String comment) {
    System.out.printf("%s\n--------------\n", comment);
    for (int y=0; y<M; y++) {
      for (int x=0; x<N; x++) {
        System.out.printf("%s ", Matr[y][x]);
      }
      System.out.println();
    }
    System.out.println();
  }

  @Override
  public void run() {
    try {
      readDataFromFile();
      printMatrCoord("Source:");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    Thread t = new Thread(null, new Main(), "",2  * 1024 * 1024);
    t.setPriority(10);
    t.start();
  }

}
