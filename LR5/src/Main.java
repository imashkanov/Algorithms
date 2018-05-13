import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class Main {

  static class Officer {
    int id = -1;
    int price = 0;
    int cost = 0;
    Officer parent;
    int slavescnt = -1;
    ArrayList<Officer> slaves;


    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder();
      int parent_id = parent!=null ? parent.id : -1;
      sb.append(String.format("ID:%d Price:%d  Parent:%d Slaves:%d  [ ", id, price, parent_id, slavescnt ));
      for (int i=0; i<slavescnt; i++) {
        sb.append(String.format("%d ", slaves.get(i).id));
      }
      sb.append("]");
      return sb.toString();
    }

  }

  static int N;
  static Officer[] mOfficers;
  static Officer mBestO;
  static boolean DEBUG = false;


  private static void readDataFromFile() throws IOException {
    BufferedReader in = new BufferedReader(new FileReader("in.txt"));
    N = Integer.parseInt(in.readLine());
    mOfficers = new Officer[N+1];
    String s;
    for (int i=1; i<=N; i++) {
      s = in.readLine();
      int[] leksArr = Arrays.stream(s.split(" ")).mapToInt(x-> Integer.parseInt(x)).toArray();
      int idx = 0;
      int officerId = leksArr[idx++];
      Officer o = getOfficerById(officerId);
      o.slavescnt = leksArr[idx++];
      for (int isl=0; isl<o.slavescnt; isl++) {
        int osl_id = leksArr[idx++];
        int osl_price = leksArr[idx++];
        Officer osl = getOfficerById(osl_id);
        osl.price = osl_price;
        osl.parent = o;
        o.slaves.add(osl);
      }
    }
    in.close();
  }

  //создает либо достает чиновника из списка по ID
  private static Officer getOfficerById(int id) {
    Officer o = mOfficers[id];
    if (o==null) {
      o = new Officer();
      o.slaves = new ArrayList<Officer>();
      o.id = id;
      mOfficers[id] = o;
    }
    return o;
  }

  static private void printAllOfficers() {
    if (!DEBUG)
      return;
    System.out.printf("All officers:\n-------------\n");
    for (int i=1; i<=N; i++) {
      Officer o = mOfficers[i];
      System.out.printf("%s\n", o);
    }
  }

  private static void printAllCost() {
    if (!DEBUG)
      return;
    System.out.printf("All costs:\n-------------\n");
    for (int i=1; i<=N; i++) {
      Officer o = mOfficers[i];
      System.out.printf("ID:%d Cost:%d\n", o.id, o.cost);
    }
    System.out.printf("-------------\nBest officer:\n");
    System.out.printf("ID:%d Cost:%d\n", mBestO.id, mBestO.cost);
  }


  private static void calc() {
    if (N == 0) {
      return;
    }
    LinkedList<Officer> queue = new LinkedList<Officer>();
    queue.add(mOfficers[1]);
    while (!queue.isEmpty()) {
      Officer o = queue.pop();
      int parentCost = o.parent == null ? 0 : o.parent.cost;
      o.cost = o.price + parentCost;
      queue.addAll(o.slaves);
    }
    //
    int min_cost = Integer.MAX_VALUE;
    for (Officer o : mOfficers) {
      if (o==null)
        continue;;
      if (o.slavescnt>0)
        continue;
      if (o.cost<min_cost) {
        min_cost = o.cost;
        mBestO = o;
      }
    }
  }

  private static void writeDataToFile() throws IOException {
    BufferedWriter writer = new BufferedWriter(new FileWriter("out.txt"));
    writer.write(String.format("%d\n", mBestO.cost));
    ArrayList<Integer> arr = new ArrayList<Integer>();
    Officer o = mBestO;
    while (o!=null) {
      arr.add(0, o.id);
      o = o.parent;
    }
    for (int i=0; i<arr.size(); i++) {
      writer.write(Integer.toString(arr.get(i)));
      if (i!=arr.size()-1) {
        writer.write(" ");
      }
    }
    writer.close();
  }



  public static void main(String[] args) throws IOException {
    readDataFromFile();
    printAllOfficers();
    calc();
    printAllCost();
    writeDataToFile();
  }



}
