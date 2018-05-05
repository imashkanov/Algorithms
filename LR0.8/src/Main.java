import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileWriter;
import java.util.LinkedList;

public class Main {

  static int N;
  static int[][] Matr;
  static int[] metArr;
  static int met = 1;

  public static void readDataFromFile() throws IOException {
    BufferedReader in = new BufferedReader(new FileReader("input.txt"));
    N = Integer.parseInt(in.readLine());
    Matr = new int[N][N];
    metArr = new int[N];
    for (int i = 0; i < N; i++) {
      int j = 0;
      String[] arrLeks = in.readLine().split(" ");
      for (String s : arrLeks) {
        Matr[i][j++] = Integer.parseInt(s);
      }
    }
    in.close();
  }

  public static void calc() {
    LinkedList<Integer> queue = new LinkedList<>();
    for (int i=0; i<N; i++) {
      if (metArr[i] == 0) {
        metArr[i] = met++;
        queue.addLast(i);
      }
      while (!queue.isEmpty()) {
        int tmp = queue.pop();
        for (int j = 0; j < N; j++) {
          if (Matr[tmp][j] == 1 && metArr[j] == 0) {
            queue.addLast(j);
            metArr[j] = met++;
          }
        }
      }
    }
  }

  public static void writeDataToFile() throws IOException {
    BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
    for (int i : metArr) {
      writer.write(Integer.toString(i) + " ");
    }
    writer.close();
  }

  public static void main(String[] args) {
    try {
      readDataFromFile();
      calc();
      writeDataToFile();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}