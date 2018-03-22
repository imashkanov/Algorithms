import java.io.*;
import java.util.ArrayList;

public class Main {

  static long inputDigit;
  static ArrayList<Long> lstOfDegs = new ArrayList<Long>();
  static boolean isZero = false;

  public static void readDataFromFile() throws IOException {
    BufferedReader in = new BufferedReader(new FileReader("input.txt"));
    inputDigit = Long.parseLong(in.readLine());
    in.close();
    if (inputDigit == 0) {
      isZero = true;
    }
  }

  public static void calc() {
    char[] inputDigitBinaryInCharArr = Long.toBinaryString(inputDigit).toCharArray();
    for (int i = inputDigitBinaryInCharArr.length-1; i>=0; i--) {
      if (inputDigitBinaryInCharArr[i] == '1') {
        lstOfDegs.add((long)inputDigitBinaryInCharArr.length-i-1);
      }
    }
  }

  public static void writeDataToFile() throws IOException {
    BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
    if (isZero) {
      writer.write(Integer.toString(-1));
      writer.close();
      return;
    }
    for (long i: lstOfDegs) {
      writer.write(String.format("%s\n",Long.toString(i)));
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
