import java.io.*;
import java.util.Arrays;

public class Main {

  static int N; //размерность массива
  static int nMax = 0; //найденная максимальная длина набора
  static int[] inputArr; //исходный набор

  public static void readDataFromFile() throws IOException {
    BufferedReader in = new BufferedReader(new FileReader("input.txt"));
    N = Integer.parseInt(in.readLine());
    inputArr = new int[N];
    String s = in.readLine();
    String[] leksArr = s.split(" ");
    for (int i=0; i<N; i++) {
      inputArr[i] = Math.abs(Integer.parseInt(leksArr[i]));
    }
    in.close();
    inputArr = Arrays.stream(inputArr).sorted().toArray();
  }

  public static void calc() {
    if (N==1) {
      nMax = 1;
      return;
    }
    //сканирование всего массива слева направо с затиранием нулями того, что мы обработали
    //0 - зарезервированное число, на него нельзя делить
    for (int i=0; i<N; i++) {
      System.out.println();
      int val = inputArr[i];
      System.out.printf("%d", val);
      if (val==0)
        continue;
      int n = 1; //число найденных делимых в данной итерации
      int notnull = 0;
      for (int j=i+1; j<N; j++) {
        int d = inputArr[j];
        if (d!=0)
          notnull++;
        if (d==0 || d % val !=0)
          continue;
        n++;
        val = d;
        System.out.printf("-%d", val);
        inputArr[j] = 0;
      }
      if (n>nMax)
        nMax = n;
      if (notnull==0) //чисел не осталось, там все занулено
        break;
      //проверяем, если у нас текущее число единица. то повторяем цикл пока все не занулим. Она будет абсолютно во всех цепочках
      if (inputArr[i]==1) {
        i--; //чтобы вернуться вновь на тот же индекс средствами цикла
        continue;
      }
    }
    System.out.println("\n-------");
  }

  public static void writeDataToFile() throws IOException {
    BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
    writer.write(Integer.toString(nMax));
    writer.close();
  }

  public static void main(String[] args) {
    try {
      readDataFromFile();
      calc();
      writeDataToFile();
      System.out.println(nMax);
    } catch (IOException e) {
      e.printStackTrace();
    }

  }
}
