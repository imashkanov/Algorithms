import java.io.*;
import java.util.TreeSet;

public class Main {

  static int N;
  static int[] mArr;
  static TreeSet<String> set = new TreeSet<String>();
  static boolean DEBUG = false;


  public static void readDataFromFile() throws IOException {
    BufferedReader in = new BufferedReader(new FileReader("input.txt"));
    N = Integer.parseInt(in.readLine());
    mArr = new int[N];
    for (int i=0; i<N; i++) {
      mArr[i] = i+1;
    }
    in.close();
  }

  public static void reduce(int[] arr, int tr, int level) {
    int n = arr.length / 2;
    if (arr.length%2!=0)
      n+=tr;
    int[] resArr = new int[n];
    StringBuilder sb = new StringBuilder();
    /*sb.append(tr);
    for (int i=0; i<level; i++) {
      sb.append(".");
    }*/
    for (int i=0;i<resArr.length; i++) {
      resArr[i] = tr==1 ? arr[2*i] : arr[2*i+1];
      sb.append(String.format("%d ", resArr[i]));
    }
    String s = sb.toString().trim();
    if (DEBUG)
      System.out.println(s);
    if (resArr.length>3) {
      reduce(resArr, 0, level+1);
      reduce(resArr, 1, level+1);
      return;
    }
    set.add(s);
  }

  public static void writeDataToFile() throws IOException {
    BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
    writer.write(Integer.toString(set.size()));
    writer.close();
  }

  public static void main(String[] args) {
    long l = System.currentTimeMillis() ;
    try {
      readDataFromFile();
      reduce(mArr, 0, 0);
      reduce(mArr, 1, 0);
      if (DEBUG) {
        System.out.println("------------");
        System.out.println(set);
      }
      System.out.println(set.size());
      writeDataToFile();
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println(String.format("%d ms end", System.currentTimeMillis() - l));
  }
}
