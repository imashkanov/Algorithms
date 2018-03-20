import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {

  static class Chamber {
    int n;
    int a;
    int b;
    int num;

    @Override
    public String toString() {
      return String.format("[#%-5d | cap=%d | cntA=%d | cntB=%d]", num, n, a, b);
    }
  }

  static int p;
  static int needA;
  static int needB;
  static Chamber[] arr;

  public static void readDataFromFile() throws IOException {
    BufferedReader in = new BufferedReader(new FileReader("input.txt"));
    needA = Integer.parseInt(in.readLine());
    needB = Integer.parseInt(in.readLine());
    p = Integer.parseInt(in.readLine());
    arr = new Chamber[p];
    for (int i=0; i<p; i++) {
      arr[i] = new Chamber();
    }
    String s;
    int idx = 0;
    while (((s = in.readLine()) != null)) {
      String[] leksArr = s.split(" ");
      int cap = Integer.parseInt(leksArr[0]);
      int a = Integer.parseInt(leksArr[1]);
      int b = Integer.parseInt(leksArr[2]);
      arr[idx].a = a;
      arr[idx].b = b;
      arr[idx].n = cap;
      arr[idx].num = idx+1;
      idx++;
      if (idx== p+1)
        break;
    }
    in.close();
  }

  public static void calc1() {
    int allocatedA;
    int allocatedB;
    int leftA = needA;
    int leftB = needB;
    for (Chamber chamber: arr) {
      if (chamber.a + chamber.b == 0) {
        continue;
      }
      if (chamber.a != 0) {
        int aPut = chamber.n-chamber.a; //возможно разместить столько больных типа А в данной палате
        if (aPut>0 && aPut<=leftA) {
          chamber.a+=aPut;
          leftA-=aPut;
        }
        continue;
      }
      if (chamber.b != 0) {
        int bPut = chamber.n-chamber.b; //возможно разместить столько больных типа B в данной палате
        if (bPut>0 && bPut<=leftB) {
          chamber.b+=bPut;
          leftB-=bPut;
        }
        continue;
      }
    }
    System.out.printf("leftA=%d, leftB=%d\n", leftA, leftB);
    printTable();
    for (Chamber chamber: arr) {
      if (chamber.a + chamber.b != 0) {
        continue;
      }
      if (leftA >= chamber.n) {
        chamber.a = chamber.n;
        leftA -= chamber.n;
        continue;
      }
      if (leftB >= chamber.n) {
        chamber.b = chamber.n;
        leftB -= chamber.n;
        continue;
      }
    }
    System.out.printf("leftA=%d, leftB=%d\n", leftA, leftB);
    printTable();

  }

  public static void printTable() {
    for(Chamber c: arr) {
      System.out.printf("%s\n", c);
    }
  }

  public static void main(String[] args) {
    try {
      readDataFromFile();
      System.out.printf("needA = %d, needB = %d, cntCham = %d\n", needA, needB, p);
      printTable();
      calc1();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
