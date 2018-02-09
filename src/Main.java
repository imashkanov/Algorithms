import java.io.*;
import java.util.Set;
import java.util.TreeSet;

public class Main {

  public static void main(String[] args) {
    Tree t = new Tree();
    try {
      t.readDataFromFile();
      t.calculate();
      t.writeDataToFile();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

class Tree {

  Set<Long> set = new TreeSet<Long>();
  private final String fileNameIn = "input.txt";
  private final String fileNameOut = "output.txt";
  long res = 0;

  public void readDataFromFile() throws IOException {
    BufferedReader in = new BufferedReader(new FileReader(fileNameIn));
    String s;
    while ((s = in.readLine()) != null) {
      set.add(Long.parseLong(s));
    }
    in.close();
  }

  public void writeDataToFile() throws IOException {
    BufferedWriter out = new BufferedWriter(new FileWriter(fileNameOut));
    out.write(String.format("%d\n", res));
    out.close();
  }

  public void calculate() {
    for (Long i: set) {
      res += i;
    }
  }
}

