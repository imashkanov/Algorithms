import java.io.*;

public class Main {
  static int[][] Matr = new int[7][7];
  static int[] labeled = new int[7];
  static int label = 0;

  private static void readDataFromFile() throws IOException {
    StreamTokenizer st = new StreamTokenizer(new BufferedReader(new FileReader("input.txt")));
    st.nextToken();
    int count = (int) st.nval;
    for (int i = 0; i < count; i++) {
      st.nextToken();
      int a = (int) st.nval;
      st.nextToken();
      int b = (int) st.nval;
      Matr[a][b]++;
      Matr[b][a]++;
    }
  }

  private static void writeDataTofile() throws IOException {
    FileWriter fw = new FileWriter("output.txt");
    fw.append(allEven() && isConnected() ? "Yes" : "No");
    fw.flush();
  }

  private static boolean allEven() {
    for (int i = 0; i < Matr.length; i++) {
      int sum = 0;
      for (int j = 0; j < Matr[i].length; j++)
        sum += Matr[i][j];
      if (sum % 2 == 1)
        return false;
    }
    return true;
  }

  private static boolean isConnected() {
    for (int i = 0; i < Matr.length; i++)
      for (int j = 0; j < Matr[i].length; j++)
        if (Matr[i][j] != 0) {
          dfs(i);
          return countVertex() == label;
        }
    return false;
  }

  private static int countVertex() {
    int count = 0;
    for (int i = 0; i < Matr.length; i++)
      for (int j = 0; j < Matr[i].length; j++)
        if (Matr[i][j] >= 1) {
          count++;
          break;
        }
    return count;
  }

  private static void dfs(int v) {
    for (int j = 0; j < Matr[v].length; j++)
      if (labeled[j] == 0 && Matr[v][j] >= 1) {
        labeled[j] = ++label;
        dfs(j);
      }
  }

  public static void main(String[] args) throws Exception {
    readDataFromFile();
    writeDataTofile();
  }
}