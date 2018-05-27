import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Main implements Runnable {

  class FastScanner {
    BufferedReader reader;
    StringTokenizer tokenizer;

    public FastScanner(String fileName) throws Exception {
      reader = new BufferedReader(new FileReader(fileName));
    }

    public String next() throws Exception {
      while (tokenizer == null || !tokenizer.hasMoreTokens()) {
        String line = reader.readLine();
        if (line == null) {
          throw new Exception();
        }
        tokenizer = new StringTokenizer(line);
      }
      return tokenizer.nextToken();
    }

    public int nextInt() throws Exception {
      return Integer.parseInt(next());
    }
  }

  int N; //размерность массива
  int nMax = 0; //найденная максимальная длина набора
  int[] inputArr; //исходный набор
  boolean hasZero = false; //если встречались нули, то нужно будет есделать результату +1
  int[] cache;
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

  public void readDataFromFile() throws Exception {
    FastScanner fs = new FastScanner("input.txt");
    N = fs.nextInt();
    inputArr = new int[N];
    cache = new int[N+1];
    for (int i=0; i< cache.length; i++) {
      cache[i]=-1;
    }
    if (N==0)
      return;
    for (int i=0; i<N; i++) {
      inputArr[i] = fs.nextInt();
    }
    Arrays.sort(inputArr);
//    inputArr = Arrays.stream(inputArr).sorted().toArray();
  }

  //рекурсивный метод поиска длинцы цепочки делителей
  public int calcLen(int idx) {
    if (cache[idx] != -1) {
      return cache[idx];
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
    cache[idx] = cl;
    return  cl;
  }

  public void calc() {
    //внешний цикл чтобы добраться до всех элементов
    for (int i=inputArr.length-1, chainLen; i>=0; i--) {
      int val = inputArr[i];
      if (val == 0) {
        hasZero = true; //хотя бы раз встретили 0
        continue;
      }
      if (cache[i] != -1)  //уэе есть рассчитанное значение в кеше
        continue;
      chainLen = calcLen(i);
      nMax = Math.max(nMax, chainLen); //высчитываем новую максимальную длину цепочки
      cache[i] = chainLen;
    }
    if (hasZero) {
      nMax++; //если были нули, то мы всегда можем ноль подель на эту цепочку 1 раз (в начале)
    }
  }


  public void writeDataToFile() throws Exception {
    FileWriter writer =  new FileWriter("output.txt");
    writer.write(Integer.toString(nMax));
    writer.flush();
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
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    new Thread(null, new Main(), "", 2 * 1024 * 1024).start();
  }
}
