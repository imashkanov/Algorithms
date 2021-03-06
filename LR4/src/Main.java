import java.io.*;

public class Main {

  static int N; //число последовательностей
  static int M;  //размерной исходной последовательности
  static int NM; //результирующая размерность
  static int[][] matrOfSec; //массивы исходный последовательностей
  static int[] resSec;  //результирующий массив
  static boolean DEBUG = false;

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
    System.out.println("Result of combine2arrays:");
    System.out.printf("NM=%d\n", NM);
    for (int n=0;n<resSec.length; n++) {
      System.out.printf("%d ", resSec[n]);
    }
    System.out.println();
    System.out.println("--------------------");
  }

  private static void WriteDataToFile() throws IOException {
    BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
    for (int n=0;n<resSec.length; n++) {
      writer.write(Integer.toString(resSec[n]));
      if (n!=NM-1) {
        writer.write(" ");
      }
    }
    writer.close();
  }


  /*private static void combine2arrays() {
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
  }*/

  /*private static void merge2() {
    int i = 0;
    for (int n = 0; n < N; n++) {
      System.arraycopy(matrOfSec[n], 0, resSec, i, M);
      i += M;
    }
    resSec = Arrays.stream(resSec).sorted().toArray();
  }*/

/*
  private static void merge3() {
    int n = N;
    int m = M;
    int[][] newMatr;
    if (N==1) {
      System.arraycopy(matrOfSec[0], 0, resSec, 0, NM);
      return;
    }
    int[] oddAuxArr = N%2==0 ? new int[0] : matrOfSec[N-1];
    while (n>1) {
      n = n/2;
      m = m*2;
      newMatr = new int[n][m];
      for (int i=0; i<n; i++) {
        newMatr[i] = combine2arrays(matrOfSec[i*2], matrOfSec[i*2+1] );
      }
      matrOfSec = newMatr;
    }
    resSec = new int[m];
    System.arraycopy(matrOfSec[0], 0, resSec, 0, m);
    resSec = combine2arrays(resSec, oddAuxArr);
  }*/
/*
  private static void merge4() {
    resSec = new int[0];
    for (int n = 0; n < N; n++) {
      resSec = combine2arrays(resSec, matrOfSec[n]);
    }
  }*/

  private static void merge5() {
    if (N==1) {
      System.arraycopy(matrOfSec[0], 0, resSec, 0, NM);
      return;
    }
    int n = N;
    int m = M;
    int[][] newMatr;
    while (n>1) {
      int[] oddAuxArr = n % 2 == 0 ? new int[0] : matrOfSec[n-1]; //учтем если нужно нечетную запись (последнюю)
      n = n/2;
      m = m*2;
      newMatr = new int[n][m];
      for (int i=0; i<n; i++) {
        newMatr[i] = combine2arrays(matrOfSec[i*2], matrOfSec[i*2+1] );
      }
      if (oddAuxArr.length!=0) {
        newMatr[n-1] = combine2arrays(newMatr[n-1], oddAuxArr );
      }
      matrOfSec = newMatr;
    }
    resSec = matrOfSec[0];
  }

  private static int[] combine2arrays(int[] src1, int[] src2) {
    int len1 = src1.length;
    int len2 = src2.length;
    int idx1 = 0;
    int idx2 = 0;
    int lenNew = len1 + len2;
    int[] res = new int[lenNew];
    for (int i = 0; i < lenNew; i++) {
      if (idx2 < len2 && idx1 < len1) { //еще оба массива не достигли конца
        if (src1[idx1] > src2[idx2])
          res[i] = src2[idx2++];
        else
          res[i] = src1[idx1++];
      } else { //один из массивов уже полностью выбран
        if (idx2 < len2)  //еще остались элементы в src2
          res[i] = src2[idx2++]; //однозначно берем только из src2
        else
          res[i] = src1[idx1++]; //иначе из src1
      }
    }
    return res;
  }



  public static void main(String[] args) {
    long l = System.currentTimeMillis() ;
    try {
      readDataFromFile();
      printSrc();
      merge5();
      printRes();
      WriteDataToFile();
    } catch (Exception e) {
      e.printStackTrace();
    }
    System.out.println(String.format("%d ms end", System.currentTimeMillis() - l));
  }


}
