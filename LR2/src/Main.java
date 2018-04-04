import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class Main {

  static class Chamber {
    int n;
    int a;
    int b;
    int num;

    public int cntOfFree() {
      return n-a-b;
    }

    enum typeOfFlu { ANY, A, B }

    public typeOfFlu getTypeOfFlu() {
      if (a>0) {
        return typeOfFlu.A;
      }
      if (b>0) {
        return typeOfFlu.B;
      }
      return typeOfFlu.ANY;
    }

    @Override
    public String toString() {
      return String.format("[#%-5d | cap:%d  A/B: %d/%d  free:%d   %s", num, n, a, b, cntOfFree(), getTypeOfFlu());
    }
  }

  //класс - флаг доступности разложения по данному количеству свободных мест
  static class Flag {
    boolean avail = false;
    ArrayList<Chamber> availChams = new ArrayList<Chamber>();//из каких палат состоит найденное разложение
  }
  private static final boolean DEBUG = true;

  static int p;
  static int needA;
  static int needB;
  static Chamber[] mArr;
  static int leftA;
  static int leftB;
  static int allocated;

  public static void readDataFromFile() throws IOException {
    BufferedReader in = new BufferedReader(new FileReader("input.txt"));
    needA = Integer.parseInt(in.readLine());
    needB = Integer.parseInt(in.readLine());
    p = Integer.parseInt(in.readLine());
    mArr = new Chamber[p];
    for (int i=0; i<p; i++) {
      mArr[i] = new Chamber();
    }
    String s;
    int idx = 0;
    while ((idx<p) && (s = in.readLine()) != null) {
      String[] leksArr = s.split(" ");
      int cap = Integer.parseInt(leksArr[0]);
      int a = Integer.parseInt(leksArr[1]);
      int b = Integer.parseInt(leksArr[2]);
      mArr[idx].a = a;
      mArr[idx].b = b;
      mArr[idx].n = cap;
      mArr[idx].num = idx+1;
      idx++;
      if (idx== p+1)
        break;
    }
    in.close();
  }

  public  static  void  sort() {
    Arrays.sort(mArr, new Comparator<Chamber>() {
      @Override
      public int compare(Chamber c1, Chamber c2) {
        int cnt1 = c1.cntOfFree();
        int cnt2 = c2.cntOfFree();
        return Integer.compare(cnt2, cnt1);
      }
    });
    printTable("After sort data:", mArr);
  }

  //заполнение непустых палат
  public static void calcNotEmpty(Chamber[] arr) {
    leftA = needA;
    leftB = needB;

    for (Chamber c : arr) {
      if (c.getTypeOfFlu() == Chamber.typeOfFlu.ANY) {
        continue;
      }
      //размещаем в непустую палату тип А
      if (c.getTypeOfFlu() == Chamber.typeOfFlu.A) {
        if (c.cntOfFree() > 0) {
          int aPut = Math.min(leftA, c.cntOfFree());
          c.a += aPut;
          leftA -= aPut;
          if (DEBUG)
           System.out.printf("*%d* A->%d (%d)\n", c.num, aPut, leftA);
          continue;
        }
      }
      //размещаем в непустую палату тип В
      if (c.getTypeOfFlu() == Chamber.typeOfFlu.B) {
        if (c.cntOfFree() > 0) {
          int bPut = Math.min(leftB, c.cntOfFree());
          c.b += bPut;
          leftB -= bPut;
          if (DEBUG)
           System.out.printf("*%d* B->%d (%d)\n", c.num, bPut, leftB);
          continue;
        }
      }
    }
    printTable("After allocation:", arr);
    if (DEBUG)
     System.out.printf("leftA=%d, leftB=%d\n", leftA, leftB);
  }

  public static void calcEmptyGreedy(Chamber[] arr) {
    //заполнение пустых палат по методу в лоб
    for (int idx=0; idx<arr.length; idx++) {
      //условие прекращения цикла = когда остается менее 16 палат (для точного алгоритма)
      if (arr.length-idx<=16) {
        System.out.printf("calcEmptyGreedy break on idx=%d of %d\n", idx, arr.length);
        break;
      }
      Chamber c = arr[idx];
      if (c.getTypeOfFlu() != Chamber.typeOfFlu.ANY) {
        continue;
      }
      //размещаем в пустую палату тип А
      if (leftA > leftB) {
        if (c.cntOfFree() > 0) {
          int aPut = Math.min(leftA, c.cntOfFree());
          c.a += aPut;
          leftA -= aPut;
          if (DEBUG)
            System.out.printf("*%d* A=>%d (%d)\n", c.num, aPut, leftA);
          continue;
        }
      } else {
        //иначе размещаем в пустую палату тип В
        if (c.cntOfFree() > 0) {
          int bPut = Math.min(leftB, c.cntOfFree());
          c.b += bPut;
          leftB -= bPut;
          if (DEBUG)
            System.out.printf("*%d* B=>%d (%d)\n", c.num, bPut, leftB);
          continue;
        }
      }
    } //for

  }

  public static ArrayList<Chamber> getCombination(int cnt) {
    ArrayList<Chamber> res = new ArrayList<Chamber>();

    return res;
  }

  //точное распределение пустых палат по методу динамического программирования
  public static void calcEmptyPrecise(Chamber[] arr) {
    System.out.printf("calcEmptyPrecise inc len %d\n", arr.length);
    int[] caps = Arrays.stream(arr).mapToInt(x -> x.cntOfFree()).toArray();
    int totalCap = Arrays.stream(caps).sum();
    System.out.printf("calcEmptyPrecise totalCap=%d\n", totalCap);
    for (Chamber c: arr) {
    }
  }



  public static void printTable(String caption, Chamber[] arr) {
    if (!DEBUG)
      return;
    System.out.println();
    System.out.println(caption);
    System.out.println("----------");
    System.out.printf("Len=%d\n", arr.length);
    for(Chamber c: arr) {
      System.out.printf("%s\n", c);
    }
  }

  public static void writeDataToFile() throws IOException {
    Chamber[] arrOfA;
    BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
    writer.write(Integer.toString(allocated));
    if (allocated == needA + needB) {
      writer.write("\n");
      arrOfA = sortFilteredArrByCap();
      StringBuilder sb = new StringBuilder();
      for (Chamber c: arrOfA) {
        sb.append(c.num);
        sb.append(" ");
      }
      String s = sb.toString().trim();
      writer.write(s);
      if (DEBUG)
        System.out.println(s);
    }
    writer.close();
  }

  //сортировка по номеру палаты
  public static Chamber[] sortFilteredArrByCap() {
    Chamber[] res = Arrays.stream(mArr).filter(x -> (x.a !=0)).toArray(Chamber[]::new);
    Arrays.sort(res, new Comparator<Chamber>() {
      @Override
      public int compare(Chamber o1, Chamber o2) {
        return Integer.compare(o1.num, o2.num);
      }
    });
    return res;
  }

  public static void main(String[] args) {
    try {
      long l = System.currentTimeMillis() ;
      readDataFromFile();
      System.out.printf("need alloc: %d needA:%d, needB:%d, cntCham:%d\n", needA+needB, needA, needB, p);
      printTable("Source data:", mArr);
      sort();
      Chamber[] arrNotEmpty = Arrays.stream(mArr).filter(x -> (x.getTypeOfFlu() != Chamber.typeOfFlu.ANY)).toArray(Chamber[]::new);
      printTable("Arr of not empty:", arrNotEmpty);
      calcNotEmpty(arrNotEmpty);
      Chamber[] arrEmpty = Arrays.stream(mArr).filter(x -> (x.getTypeOfFlu() == Chamber.typeOfFlu.ANY)).toArray(Chamber[]::new);
      printTable("Arr of empty1:", arrEmpty);
      calcEmptyGreedy(arrEmpty);
      arrEmpty = Arrays.stream(mArr).filter(x -> (x.getTypeOfFlu() == Chamber.typeOfFlu.ANY)).toArray(Chamber[]::new);
      printTable("Arr of empty2:", arrEmpty);
      calcEmptyPrecise(arrEmpty);
      allocated = needA+needB-leftB-leftA;
      writeDataToFile();
      System.out.printf("allocated = %d\n", allocated);
      System.out.println(String.format("%d ms end", System.currentTimeMillis() - l));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
