import java.io.*;
import java.lang.reflect.Array;
import java.util.Arrays;

public class Main {

  static int N; //число последовательностей
  static int M;  //размерной исходной последовательности
  static int NM; //результирующая размерность
  static int[][] matrOfSec; //массивы исходный последовательностей
  static int[] resSec;  //результирующий массив
  static boolean DEBUG = true;

  public static void readDataFromFile() throws IOException {
    BufferedReader in = new BufferedReader(new FileReader("input.txt"));
    String s = in.readLine();
    String[] leksArr = s.split(" ");
    N = Integer.parseInt(leksArr[0]);
    M = Integer.parseInt(leksArr[1]);
    if (M>100)
      DEBUG = false;
    NM = N*M;
    matrOfSec = new int[N][M];
    resSec = new int[NM];
    for (int i=0; i<N; i++) {
      s = in.readLine();
      leksArr = s.split(" ");
      for (int j=0; j<M; j++) {
        matrOfSec[i][j] = Integer.parseInt(leksArr[j]);
      }
    }
    in.close();
  }

  private static void printSrc() {
    if (!DEBUG)
      return;
    System.out.println("Source data:");
    System.out.printf("N=%d M=%d\n", N, M);
    for (int n=0;n<N; n++) {
      for (int m=0; m<M; m++) {
        System.out.printf("%d ", matrOfSec[n][m]);
      }
      System.out.println();
    }
    System.out.println("--------------------");
  }


  private static void printRes() {
    if (!DEBUG)
      return;
    System.out.println("Result of merge:");
    System.out.printf("NM=%d\n", NM);
    for (int n=0;n<NM; n++) {
      System.out.printf("%d ", resSec[n]);
    }
    System.out.println();
    System.out.println("--------------------");
  }

  private static void WriteDataToFile() throws IOException {
    BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
    for (int n=0;n<NM; n++) {
      writer.write(Integer.toString(resSec[n]));
      if (n!=NM-1) {
        writer.write(" ");
      }
    }
    writer.close();
  }


  private static void merge() {
    int[] idx = new int[N]; //массив динамических индексов цикла для всех N послед., как будто N переменных цикла
    long l = System.currentTimeMillis() ;
    for (int i=0; i<NM; i++) { //большой полный цикл по результирующему массиву
      int val = Integer.MAX_VALUE; //будущее найденное из N массивов минимальное значение
      int foundidx = Integer.MIN_VALUE; //будущий найденный индекс этого минимального значения
      for (int n=0; n<N; n++) { //смотрим за раз циклом все N массивов
        int dynIdx = idx[n]; //Динамический индекс массива, взятый из массива текущих индексов
        if (dynIdx>=M)
          continue; //вся последовательность обработана
        int v = matrOfSec[n][dynIdx]; //Достаем элемент n-ной последовательности по дин.индексу
        if (v<val) {
          val = v;
          foundidx = n;
        }
      }
      resSec[i] = val;  //используем минимальное из того, что нашли
      idx[foundidx]++;
      //
      long ll = System.currentTimeMillis() - l;
      if (ll>1000) {
        System.out.println(String.format("idx 1 sec %d", i));
        l = System.currentTimeMillis();
      }

    }
  }

  private static void merge2() {
    int i = 0;
    for (int n = 0; n < N; n++) {
      System.arraycopy(matrOfSec[n], 0, resSec, i, M);
      i+=M;
    }
    resSec = Arrays.stream(resSec).sorted().toArray();
  }

    public static void main(String[] args) {
    long l = System.currentTimeMillis() ;
    try {
      readDataFromFile();
      printSrc();
      merge2();
      printRes();
      WriteDataToFile();
    } catch (Exception e) {
      e.printStackTrace();
    }
    System.out.println(String.format("%d ms end", System.currentTimeMillis() - l));
  }


}
