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
    int value;
    ArrayList<Chamber> availChams = new ArrayList<Chamber>();//из каких палат состоит найденное разложение

    Flag() {}

    Flag(int value, ArrayList<Chamber> availChams) {
      this.value = value;
      this.availChams = availChams;
    }

    @Override
    public String toString() {
      return String.format("[#%d::%d] ", value, availChams.size());
    }

  }

  private static final boolean DEBUG = false;

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
    printTable("After sort data:");
  }


  public static void calcNotEmpty() {
    leftA = needA;
    leftB = needB;

    //заполнение непустых палат
    for (Chamber c : mArr) {
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

/*  public static void calcEmptyGreedy() {
    //заполнение пустых палат
    for (Chamber c : mArr) {
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
  }*/

  public static ArrayList<Flag> getFlagsPrecise(Chamber[] chambers) {
    if (DEBUG)
      System.out.printf("getFlagsPrecise inc len %d\n", chambers.length);
    //массив вместимостей оставшихся пустых палат
    int[] caps = Arrays.stream(chambers).mapToInt(x -> x.cntOfFree()).toArray();
    //суммарная вместимость всех пустых палат
    int totalCap = Arrays.stream(caps).sum();
    if (DEBUG)
      System.out.printf("getFlagsPrecise totalCap=%d\n", totalCap);
    //массив возможных вариантов размещений количеств от 1 до totalCap
    ArrayList<Flag> flags = new ArrayList<Flag>();
    if (caps.length == 0) {
      return flags;
    }
    flags.add(new Flag());//добавляем 0, который всегда можем получить
    for (int idxCap=0; idxCap<caps.length; idxCap++) {
      int cap = caps[idxCap];
      int[] values = flags.stream().mapToInt(x -> x.value).toArray();
      int[] values2 = new int[values.length];
      Arrays.fill(values2, cap);
      //всегда добавляем размещение одной палаты
      Chamber newChamber = chambers[idxCap];
      //flags.add(new Flag(cap,new ArrayList<Chamber>(Arrays.asList(newChamber))));
      for (int i=0; i<values.length; i++) {
        values2[i] += values[i];
      }
      //добавляем во флаги те значения, которых ещё не было
      for (int j=0; j<values2.length; j++) {
        int v = values2[j];
        if (Arrays.binarySearch(values, v)>=0)
          continue;
        Flag f = new Flag();
        f.value = v;
        f.availChams.addAll(flags.get(j).availChams);
        f.availChams.add(newChamber);
        flags.add(f);
      }
    }
    flags.sort(new Comparator<Flag>() {
      @Override
      public int compare(Flag f1, Flag f2) {
        return Integer.compare(f1.value, f2.value);
      }
    });
    return flags;
  }



  public static void calcEmptyA() {
    if (mArr.length == 0) return;
    Chamber[] arrEmpty = Arrays.stream(mArr).filter(x -> (x.getTypeOfFlu() == Chamber.typeOfFlu.ANY)).toArray(Chamber[]::new);
    ArrayList<Flag> flags = getFlagsPrecise(arrEmpty);
    //размещаем тип А
    for (int i=flags.size()-1; i>0; i--) {
      Flag f = flags.get(i);
      if (f.value>leftA && flags.get(i-1).value>0)
        continue;
      for (Chamber c: f.availChams) {
        int aPut = Math.min(leftA, c.cntOfFree());
        c.a += aPut;
        leftA -= aPut;
      }
      f.value = -1;
      f.availChams = new ArrayList<Chamber>();
      break;
    }

    /* Диагностический вывод полученного списка флагов
    for (int i=1; i< flags.size(); i++) {
      System.out.printf("%d:%s ",flags.get(i).value, flags.get(i).availChams);
    }
    System.out.println();*/
  }

  public static void calcEmptyB() {
    //размещаем тип B
    Chamber[] arrEmpty = Arrays.stream(mArr).filter(x -> (x.getTypeOfFlu() == Chamber.typeOfFlu.ANY)).toArray(Chamber[]::new);
    ArrayList<Flag> flags = getFlagsPrecise(arrEmpty);
    for (int i=flags.size()-1; i>0; i--) {
      Flag f = flags.get(i);
      if (f.value>leftB && flags.get(i-1).value>0)
        continue;
      for (Chamber c: f.availChams) {
        int bPut = Math.min(leftB, c.cntOfFree());
        c.b += bPut;
        leftB -= bPut;
      }
      f.value = -1;
      f.availChams = new ArrayList<Chamber>();
      break;
    }
  }

  public static void calcEmptyGreedy() {
    Chamber[] arrEmpty = Arrays.stream(mArr).filter(x -> (x.getTypeOfFlu() == Chamber.typeOfFlu.ANY)).toArray(Chamber[]::new);
    if (arrEmpty.length == 0) return;
    //заполнение пустых палат по методу в лоб
    for (int idx=0; idx<arrEmpty.length; idx++) {
      //условие прекращения цикла = когда остается менее 8 палат (для точного алгоритма)
      if (arrEmpty.length-idx<=100) {
        System.out.printf("calcEmptyGreedy break on idx=%d of %d\n", idx, arrEmpty.length);
        break;
      }
      Chamber c = arrEmpty[idx];
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

  public static void printTable(String caption) {
    if (!DEBUG)
      return;
    System.out.println();
    System.out.println(caption);
    System.out.println("----------");
    for(Chamber c: mArr) {
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
      printTable("Source data:");
      sort();
      calcNotEmpty();
      calcEmptyGreedy();
      if (leftA >= leftB) {
        calcEmptyA();
        calcEmptyB();
      } else {
        calcEmptyB();
        calcEmptyA();
      }

      allocated = needA+needB-leftB-leftA;
      writeDataToFile();
      printTable("After allocation:");
      if (DEBUG)
        System.out.printf("leftA=%d, leftB=%d\n", leftA, leftB);
      System.out.println(String.format("%d ms end", System.currentTimeMillis() - l));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
