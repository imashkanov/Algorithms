import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Main {

  static int[] arrRes;

  public static void readDataAndCalc() throws IOException {
    BufferedReader in = new BufferedReader(new FileReader("input.txt"));
    String s = in.readLine();
    arrRes = new int[Integer.parseInt(s)];
    for (int i=0; i<arrRes.length-1; i++) {
      s = in.readLine();
      String[] arrLeks = s.split(" ");
      arrRes[Integer.parseInt(arrLeks[1]) - 1] = Integer.parseInt(arrLeks[0]);
    }
    in.close();
  }

  public static void writeDataToFile() throws IOException {
    BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
    for (int i: arrRes) {
      writer.write(Integer.toString(i)+" ");
    }
    writer.close();
  }

  public static void main(String[] args)  {
    try {
      readDataAndCalc();
      writeDataToFile();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}