import java.io.*;
import java.util.ArrayList;

public class Main {

  static int N;
  static int[][][][][][][] Matr;

  private static void readDataFromFile() throws IOException {
    BufferedReader in = new BufferedReader(new FileReader("input.txt"));
    N = Integer.parseInt(in.readLine());
    in.close();
  }

  private static void initData() {
    Matr = new int[10][10][10][10][10][10][10];
    Matr[9][9][9][9][9][9][9] = 0;
  }

  /*private static void printMatr(String comment) {
    System.out.println(comment);
    //for (int i2 = 0; i2 < 10; i2++) {
    int i2=9, i3=9, i4=9, i5=9, i6=9;
      System.out.printf("i2=%d\n", i2);
      for (int i1 = 0; i1 < 10; i1++) {
        for (int i0 = 0; i0 < 10; i0++) {
          System.out.printf("%2d ", Matr[i6][i5][i4][i3][i2][i1][i0]);
        }
        System.out.println();
      }
    //}
    System.out.println();
  }*/

  private static void fillMatr() {
    for (int i6 = 9; i6 >= 0; i6--)
      for (int i5 = 9; i5 >= 0; i5--)
        for (int i4 = 9; i4 >= 0; i4--)
          for (int i3 = 9; i3 >= 0; i3--)
            for (int i2 = 9; i2 >= 0; i2--)
              for (int i1 = 9; i1 >= 0; i1--)
                for (int i0 = 9; i0 >= 0; i0--) {
                  if (i0 == 9 && i1 == 9 && i2 == 9 && i3 == 9 && i4 == 9 && i5 == 9 && i6 == 9)
                    continue;

                  int digit = 10000000 - (i0 + i1*10 + i2*100 + i3*1000 + i4*10000 + i5*100000 + i6*1000000);
                  int max = 0;
                  for (int j = i6; j<=i6+3 && j<10 && digit>1000000; j++) {
                    int val = Matr[j][i5][i4][i3][i2][i1][i0];
                    if (val > max)
                      max = val;
                  }
                  for (int j = i5; j<=i5+3 && j<10 && digit>100000; j++) {
                    int val = Matr[i6][j][i4][i3][i2][i1][i0];
                    if (val > max)
                      max = val;
                  }
                  for (int j = i4; j<=i4+3 && j<10 && digit>10000; j++) {
                    int val = Matr[i6][i5][j][i3][i2][i1][i0];
                    if (val > max)
                      max = val;
                  }
                  for (int j = i3; j<=i3+3 && j<10 && digit>1000; j++) {
                    int val = Matr[i6][i5][i4][j][i2][i1][i0];
                    if (val > max)
                      max = val;
                  }
                  for (int j = i2; j<=i2+3 && j<10 && digit>100; j++) {
                    int val = Matr[i6][i5][i4][i3][j][i1][i0];
                    if (val > max)
                      max = val;
                  }
                  for (int j = i1; j<=i1+3 && j<10 && digit>10; j++) {
                    int val = Matr[i6][i5][i4][i3][i2][j][i0];
                    if (val > max)
                      max = val;
                  }
                  for (int j = i0; j<=i0+3 && j<10; j++) {
                    int val = Matr[i6][i5][i4][i3][i2][i1][j];
                    if (val > max)
                      max = val;
                  }
                  Matr[i6][i5][i4][i3][i2][i1][i0] = 1 - max;
                }
  }

  /*private static int getMax(int i6, int i5, int i4, int i3, int i2, int i1, int i0) {
    int max = 0;
    max = updateMax(max, i6, i5, i4, i3, i2, i1, i0+1);
    max = updateMax(max, i6, i5, i4, i3, i2, i1, i0+2);
    max = updateMax(max, i6, i5, i4, i3, i2, i1, i0+3);
    if (i1==9 && i2==9 && i3==9 && i4==9 && i5==9 && i6==9)
      return max;
    //
    max = updateMax(max, i6, i5, i4, i3, i2, i1+1, i0);
    max = updateMax(max, i6, i5, i4, i3, i2, i1+2, i0);
    max = updateMax(max, i6, i5, i4, i3, i2, i1+3, i0);
    if (i2==9 && i3==9 && i4==9 && i5==9 && i6==9)
      return max;
    //
    max = updateMax(max, i6, i5, i4, i3, i2+1, i1, i0);
    max = updateMax(max, i6, i5, i4, i3, i2+2, i1, i0);
    max = updateMax(max, i6, i5, i4, i3, i2+3, i1, i0);
    if (i3==9 && i4==9 && i5==9 && i6==9)
      return max;
    //
    max = updateMax(max, i6, i5, i4, i3+1, i2, i1, i0);
    max = updateMax(max, i6, i5, i4, i3+2, i2, i1, i0);
    max = updateMax(max, i6, i5, i4, i3+3, i2, i1, i0);
    if (i4==9 && i5==9 && i6==9)
      return max;
    //
    max = updateMax(max, i6, i5, i4+1, i3, i2, i1, i0);
    max = updateMax(max, i6, i5, i4+2, i3, i2, i1, i0);
    max = updateMax(max, i6, i5, i4+3, i3, i2, i1, i0);
    if (i5==9 && i6==9)
      return max;
    //
    max = updateMax(max, i6, i5+1, i4, i3, i2, i1, i0);
    max = updateMax(max, i6, i5+2, i4, i3, i2, i1, i0);
    max = updateMax(max, i6, i5+3, i4, i3, i2, i1, i0);
    if (i6==9)
      return max;
    //
    max = updateMax(max, i6+1, i5, i4, i3, i2, i1, i0);
    max = updateMax(max, i6+2, i5, i4, i3, i2, i1, i0);
    max = updateMax(max, i6+3, i5, i4, i3, i2, i1, i0);
    //
    return max;
  }*/

  /*private static int updateMax(int oldMax, int i6, int i5, int i4, int i3, int i2, int i1, int i0) {
    if (i6>9 || i5>9 || i4>9 || i3>9 || i2>9 || i1>9 || i0>9 )
      return oldMax;
    int val = Matr[i6][i5][i4][i3][i2][i1][i0];
    return val>oldMax ? val : oldMax;
  }*/

  private static int[] getWin() {
    int i0 = 0, i1 = 0, i2=0, i3=0, i4=0, i5=0, i6=0;
    ArrayList<Integer> res = new ArrayList<Integer>();
    i0 = N % 10;
    i1 = N/10 % 10;
    i2 = N/100 % 10;
    i3 = N/1000 % 10;
    i4 = N/10000 % 10;
    i5 = N/100000 % 10;
    i6 = N/1000000 % 10;
    if (Matr[i6][i5][i4][i3][i2][i1][i0]==1)
      return new int[0];
    int val;
    check(res, i6+1, i5, i4, i3, i2, i1, i0);
    check(res, i6+2, i5, i4, i3, i2, i1, i0);
    check(res, i6+3, i5, i4, i3, i2, i1, i0);
    //
    check(res, i6, i5+1, i4, i3, i2, i1, i0);
    check(res, i6, i5+2, i4, i3, i2, i1, i0);
    check(res, i6, i5+3, i4, i3, i2, i1, i0);
    //
    check(res, i6, i5, i4+1, i3, i2, i1, i0);
    check(res, i6, i5, i4+2, i3, i2, i1, i0);
    check(res, i6, i5, i4+3, i3, i2, i1, i0);
    //
    check(res, i6, i5, i4, i3+1, i2, i1, i0);
    check(res, i6, i5, i4, i3+2, i2, i1, i0);
    check(res, i6, i5, i4, i3+3, i2, i1, i0);
    //
    check(res, i6, i5, i4, i3, i2+1, i1, i0);
    check(res, i6, i5, i4, i3, i2+2, i1, i0);
    check(res, i6, i5, i4, i3, i2+3, i1, i0);
    //
    check(res, i6, i5, i4, i3, i2, i1+1, i0);
    check(res, i6, i5, i4, i3, i2, i1+2, i0);
    check(res, i6, i5, i4, i3, i2, i1+3, i0);
    //
    check(res, i6, i5, i4, i3, i2, i1, i0+1);
    check(res, i6, i5, i4, i3, i2, i1, i0+2);
    check(res, i6, i5, i4, i3, i2, i1, i0+3);
    return res.stream().sorted().mapToInt(Integer::valueOf).toArray();
  }

  private static void check(ArrayList<Integer> res, int i6, int i5, int i4, int i3, int i2, int i1, int i0) {
    if (i6>9 || i5>9 || i4>9 || i3>9 || i2>9 || i1>9 || i0>9 )
      return;
    int val = Matr[i6][i5][i4][i3][i2][i1][i0];
    if (val==0)
      return;
    int digit = i0 + i1*10 + i2*100 + i3*1000 + i4*10000 + i5*100000 + i6*1000000;
    res.add(digit);
  }

  private static void writeDataToFile(int[] res) throws IOException {
    BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
    if (res.length==0) {
      writer.write("The second wins");
    } else {
      writer.write("The first wins\n");
      for (int idx=0; idx<res.length; idx++) {
        int val = res[idx];
        writer.write(Integer.toString(val));
        if (idx<res.length-1)
          writer.write(" ");
      }
    }
    writer.close();
  }

  public static void main(String[] args) {
    long l = System.currentTimeMillis() ;
    try {
      readDataFromFile();
      System.out.println(String.format("%d ms readDataFromFile", System.currentTimeMillis() - l));
      l = System.currentTimeMillis() ;
      initData();
      System.out.println(String.format("%d ms initdata", System.currentTimeMillis() - l));
      l = System.currentTimeMillis() ;
      //printMatr("Source matr:");
      fillMatr();
      System.out.println(String.format("%d ms fillmatr", System.currentTimeMillis() - l));
      l = System.currentTimeMillis() ;
      //printMatr("Filled matr:");
      int[] res = getWin();
      System.out.println(String.format("%d ms getWin", System.currentTimeMillis() - l));
      l = System.currentTimeMillis() ;
      writeDataToFile(res);
      System.out.println(String.format("%d ms write to file", System.currentTimeMillis() - l));
      l = System.currentTimeMillis() ;
      for (int val:res)
        System.out.printf("%d ", val);
      System.out.println();
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println(String.format("%d ms end", System.currentTimeMillis() - l));
  }


}
