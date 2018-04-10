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

  private static final boolean DEBUG = false;

  static int p;
  static int needA;
  static int needB;
  static Chamber[] arr;
  static int leftA;
  static int leftB;
  static int allocated;

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
    while ((idx<p) && (s = in.readLine()) != null) {
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

  public  static  void  sort() {
    Arrays.sort(arr, new Comparator<Chamber>() {
      @Override
      public int compare(Chamber c1, Chamber c2) {
        int cnt1 = c1.cntOfFree();
        int cnt2 = c2.cntOfFree();
        return Integer.compare(cnt2, cnt1);
      }
    });
    printTable("After sort data:");
  }


  public static void calcNotEmpty() {
    leftA = needA;
    leftB = needB;

    //заполнение непустых палат
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
  }

  public static void calcEmptyGreedy() {
    //заполнение пустых палат
    for (Chamber c : arr) {
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
    allocated = needA+needB-leftB-leftA;

    printTable("After allocation:");
    if (DEBUG)
      System.out.printf("leftA=%d, leftB=%d\n", leftA, leftB);
  }

  public static Flag[] getFlagsPrecise(Chamber[] arr) {
    System.out.printf("getFlagsPrecise inc len %d\n", arr.length);
    //массив вместимостей оставшихся пустых палат
    int[] caps = Arrays.stream(arr).mapToInt(x -> x.cntOfFree()).toArray();
    //суммарная вместимость всех пустых палат
    int totalCap = Arrays.stream(caps).sum();
    System.out.printf("getFlagsPrecise totalCap=%d\n", totalCap);
    //массив возможных вариантов размещений количеств от 1 до totalCap
    Flag[] flags = new Flag[totalCap+1];
    for (int i=0; i<flags.length; i++) {
      flags[i] = new Flag();
    }
    //максимальное число для перебора комбинаций - 2^(число палат)
    int maxLimit = (int)Math.pow(2, caps.length);
    ArrayList<Chamber> lst = new ArrayList<Chamber>();
    for (int iCombination=1; iCombination<maxLimit; iCombination++) {
      //накопление суммы
      int intermediateSum = 0;
      lst.clear();
      for (int bit=0; bit<caps.length; bit++) {
        int D = 1 << bit;
        if ((iCombination & D) != 0) {
          intermediateSum += caps[bit];
          lst.add(arr[bit]);
        }
      }
      if (intermediateSum>totalCap || intermediateSum==0)
        continue;
      flags[intermediateSum].avail = true;
      ArrayList<Chamber> lst4add = new ArrayList<Chamber>(lst);
      flags[intermediateSum].availChams = lst4add;
    }
    return flags;
  }



  public static void calcPrecise() {
    if (arr.length == 0) return;
    Flag[] flags = getFlagsPrecise(arr);
    /*for (int i=1; i< flags.length; i++) {
      System.out.printf("%d:%s ",i, flags[i].avail ? "Y" : "N");
    }
    System.out.println();*/
    //сперва находим оптимальное распределение для больных А
    //Первый случай, когда число оставшихся больных А заведомо больше, чем число размещений
    //в таком случае сканируем флаги с наибольшего вниз, как только найдем хотя бы какой-то вариант (наибольший) - используем
    if (leftA>=flags.length) {
      for (int i=flags.length; i>=1; i--) {
        if (i>leftA || !flags[i].avail) {
          continue;
        }
        for (Chamber c: flags[i].availChams) {
          int aPut = Math.min(leftA, c.cntOfFree());
          c.a += aPut;
          leftA -= aPut;
        }
        break;
      }
    } //первый случай
    else // leftA<flags.length
    //второй случай, когда число оставшихся больных А заведомо меньше, чем число размещений
    //в таком случае сканируем флаги c этого значения вверх, как только найдем хотя бы какой-то вариант (наименьший) - используем
    {
      boolean found = false;
      for (int i=leftA; i<=flags.length; i++) {
        if (!flags[i].avail) {
          continue;
        }
        for (Chamber c: flags[i].availChams) {
          int aPut = Math.min(leftA, c.cntOfFree());
          c.a += aPut;
          leftA -= aPut;
        }
        found = true;
        break;
      }
      //продолжается логика для случая leftA<flags.length, но если мы не нашли сканированием вверх ничего подходящего
      for (int i=leftA; i>=1; i--) {
        if (i>leftA || !flags[i].avail) {
          continue;
        }
        for (Chamber c: flags[i].availChams) {
          int aPut = Math.min(leftA, c.cntOfFree());
          c.a += aPut;
          leftA -= aPut;
        }
        break;
      }
    }
  }

  public static boolean areAllChamsEmpty() {
    int totalCntOfFree = Arrays.stream(arr).mapToInt(x -> x.cntOfFree()).sum();
    int totalCap = Arrays.stream(arr).mapToInt(x -> x.n).sum();
    return totalCap == totalCntOfFree;
  }


  public static void printTable(String caption) {
    if (!DEBUG)
      return;
    System.out.println();
    System.out.println(caption);
    System.out.println("----------");
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
    Chamber[] res = Arrays.stream(arr).filter(x -> (x.a !=0)).toArray(Chamber[]::new);
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
      printTable("Source data:");
      sort();
      calcNotEmpty();
      if (areAllChamsEmpty() && arr.length<=10) {
        calcPrecise();
        calcEmptyGreedy();
      } else {
        calcEmptyGreedy();
      }
      writeDataToFile();
      allocated = needA+needB-leftB-leftA;
      printTable("After allocation:");
      if (DEBUG)
        System.out.printf("leftA=%d, leftB=%d\n", leftA, leftB);
      System.out.println(String.format("%d ms end", System.currentTimeMillis() - l));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
