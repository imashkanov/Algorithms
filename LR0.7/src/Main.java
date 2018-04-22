import java.io.*;

public class Main {

  static int N;
  static int[] resArr;
  static int[][] Matr;

  public static void readDataFromFile() throws IOException {
    BufferedReader in = new BufferedReader(new FileReader("input.txt"));
    N = Integer.parseInt(in.readLine());
    Matr = new int[N][N];
    resArr = new int[N];
    for (int i=0; i<N; i++) {
      int j=0;
      String[] arrLeks = in.readLine().split(" ");
      for (String s: arrLeks) {
        Matr[i][j++] = Integer.parseInt(s);
      }
    }
    in.close();
  }

  public static void calc() {
    for (int i=0; i<N; i++) {
      for (int j=0; j<N; j++) {
        if (Matr[i][j] == 1) {
          resArr[j] = i+1;
        }
      }
    }
  }

  public static void writeDataToFile() throws IOException {
    FileWriter writer = new FileWriter("output.txt");
    for (int i : resArr) {
      writer.write(Integer.toString(i)+" ");
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