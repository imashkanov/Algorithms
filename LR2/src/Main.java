import java.io.*;
import java.util.HashMap;
import java.util.TreeSet;

public class Main {

  static int N;
  //static int[] mArr;
  static int result;
  //static TreeSet<String> set = new TreeSet<String>();
  static HashMap<Integer, Integer> cache = new HashMap<Integer, Integer>();
  static boolean DEBUG = false;


  public static void readDataFromFile() throws IOException {
    BufferedReader in = new BufferedReader(new FileReader("input.txt"));
    N = Integer.parseInt(in.readLine());
   /* mArr = new int[N];
    for (int i=0; i<N; i++) {
      mArr[i] = i+1;
    }*/
    in.close();
  }
/*
  public static void reduce(int[] arr, int tr, int level) {
    int n = arr.length / 2;
    if (arr.length%2!=0)
      n+=tr;
    int[] resArr = new int[n];
    StringBuilder sb = new StringBuilder();
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
  }*/

  public static int calc(int n) throws Exception {
    if (n<3)
      throw new Exception("<3!!!!!!!!!!!!!");
    int res = 0;
    if (cache.containsKey(n)) {
      res = cache.get(n);
      return res;
    }
    res = calc(n/2 + n%2 ) + calc(n/2);
    cache.put(n, res);
    return res;
  }

  public static void writeDataToFile() throws IOException {
    System.out.println(result);
    BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
    writer.write(Integer.toString(result));
    writer.close();
  }

  public static void main(String[] args) throws Exception {
    long l = System.currentTimeMillis() ;
    try {
      readDataFromFile();
      //reduce(mArr, 0, 0);
      //reduce(mArr, 1, 0);
      cache.put(3, 1);
      cache.put(4, 2);
      cache.put(5, 2);
      cache.put(6, 2);
      result = calc(N);
      writeDataToFile();
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println(String.format("%d ms end", System.currentTimeMillis() - l));
  }
}
