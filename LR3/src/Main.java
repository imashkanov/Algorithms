import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.StringTokenizer;

public class Main implements Runnable {

  class FastScanner {
    BufferedReader reader;
    StringTokenizer tokenizer;

    public FastScanner(String fileName) throws IOException {
      reader = new BufferedReader(new FileReader(fileName));
    }

    public String next() throws IOException {
      while (tokenizer == null || !tokenizer.hasMoreTokens()) {
        String line = reader.readLine();
        if (line == null) {
          throw new EOFException();
        }
        tokenizer = new StringTokenizer(line);
      }
      return tokenizer.nextToken();
    }

    public int nextInt() throws IOException {
      return Integer.parseInt(next());
    }
  }

  int N; //размерность массива
  int nMax = 0; //найденная максимальная длина набора
  int[] inputArr; //исходный набор
  boolean hasZero = false; //если встречались нули, то нужно будет есделать результату +1
  HashMap<Integer, Integer> cache = new HashMap<Integer, Integer>();
/*
  public void readDataFromFile() throws IOException {
    BufferedReader in = new BufferedReader(new FileReader("input.txt"));
    N = Integer.parseInt(in.readLine());
    inputArr = new int[N];
    if (N==0)
      return;
    String s = in.readLine();
    String[] leksArr = s.split(" ");
    for (int i=0; i<N; i++) {
      inputArr[i] = Math.abs(Integer.parseInt(leksArr[i]));
    }
    in.close();
    inputArr = Arrays.stream(inputArr).sorted().toArray();
  }*/

  public void readDataFromFile() throws IOException {
    FastScanner fs = new FastScanner("input.txt");
    N = fs.nextInt();
    inputArr = new int[N];
    if (N==0)
      return;
    for (int i=0; i<N; i++) {
      inputArr[i] = fs.nextInt();
    }
    inputArr = Arrays.stream(inputArr).sorted().toArray();
  }

  //рекурсивный метод поиска длинцы цепочки делителей
  public int calcLen(int idx) {
    if (cache.containsKey(idx)) {
      return cache.get(idx);
    }
    int val = inputArr[idx]; //делимое, с которым мы работаем
    int cl = 1; //начальная длина цепочки
    for (int i=idx-1; i>=0; i--) {
      int d = inputArr[i];
      if (d==0)
        continue;
      if (val % d != 0)
        continue;
      int c2 = calcLen(i);
      if ((c2+1) > cl)
        cl = c2+1;
      //break;
    }
    cache.put(idx, cl);
    return  cl;
  }

  public void calc() {
    //внешний цикл чтобы добраться до всех элементов
    for (int i=inputArr.length-1; i>=0; i--) {
      int val = inputArr[i];
      if (val == 0) {
        hasZero = true; //хотя бы раз встретили 0
        continue;
      }
      if (cache.containsKey(i))  //уэе есть рассчитанное значение в кеше
        continue;
      int chainLen = calcLen(i);
      nMax = Math.max(nMax, chainLen); //высчитываем новую максимальную длину цепочки
      cache.put(i, chainLen);
    }
    if (hasZero) {
      nMax++; //если были нули, то мы всегда можем ноль подель на эту цепочку 1 раз (в начале)
    }
  }


  public void writeDataToFile() throws IOException {
    BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
    writer.write(Integer.toString(nMax));
    writer.close();
  }

  @Override
  public void run() {
    long l = System.currentTimeMillis();
    try {
      readDataFromFile();
      calc();
      writeDataToFile();
      System.out.println(nMax);
      System.out.printf("%d ms\n", System.currentTimeMillis()-l);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    new Thread(null, new Main(), "", 2 * 1024 * 1024).start();
  }
}
