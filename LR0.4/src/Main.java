import java.io.*;

public class Main {

  static int N;
  static int[] inputArr;
  static boolean sets;

  public static void readDataFromFile() throws IOException {
    BufferedReader in = new BufferedReader(new FileReader("input.txt"));
    String s;
    N = Integer.parseInt(in.readLine());
    s = in.readLine();
    String[] leksArr = s.split(" ");
    inputArr = new int[N];
    for (int i=0; i<N; i++) {
      inputArr[i] = Integer.parseInt(leksArr[i]);
    }
    in.close();
  }

  public static void calc() {
    int center = (N/2+N%2)-1;
    if (N==1) {
      sets = true;
      return;
    }
    if (N==2) {
      if (inputArr[0] > inputArr[1]) {
        sets = false;
        return;
      } else {
        sets = true;
        return;
      }
    }
    for (int i=center; i>=0; i--) {
      try {
        if ((inputArr[i] <= inputArr[(2*i)+1]) && (inputArr[i] <= inputArr[(2*i)+2])) {
          sets = true;
          continue;
        } else  {
          sets = false;
          break;
        }
      } catch (Exception e) {
        continue;
      }
    }
  }

  public static void writeDataToFile() throws IOException {
    BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
    if (sets) {
      writer.write("Yes");
    } else {
      writer.write("No");
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
