import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class Main implements Runnable{

  boolean DEBUG = false;

  class Point {
    int x;
    int y;
    int alt;
    Point from;
    int step;
    boolean inPath;

    Point() {}

    Point(int x, int y, int alt) {
      this.x = x;
      this.y = y;
      this.alt = alt;
    }

    @Override
    public String toString() {
      return String.format("%d/%d:%d", x, y , alt);
    }
  }

  Point[][] Matr;
  int N;//количество х(столбцов)
  int M;//количество y(строк)
  int K;
  Point pFrom;
  Point pTo;
  ArrayList<Point> path = new ArrayList<Point>();

  public void readDataFromFile() throws IOException {
    BufferedReader in = new BufferedReader(new FileReader("in.txt"));
    String s;
    s = in.readLine();
    String[] leksArr = s.split(" ");
    N = Integer.parseInt(leksArr[0]);
    M = Integer.parseInt(leksArr[1]);
    Matr = new Point[M][N];
    for (int y=0; y<M; y++) {
      for (int x=0; x<N; x++) {
        Matr[y][x] = new Point();
        Matr[y][x].x = x;
        Matr[y][x].y = y;
      }
    }
    int cntN = N;
    while (cntN-- != 0) {
      s = in.readLine();
      leksArr = s.split(" ");
      for (int i=0; i<leksArr.length; i++) {
        Matr[N-cntN-1][i].alt = Integer.parseInt(leksArr[i]);
      }
    }
    K = Integer.parseInt(in.readLine());
    s = in.readLine();
    leksArr = s.split(" ");
    pFrom = Matr[Integer.parseInt(leksArr[1])-1][Integer.parseInt(leksArr[0])-1];
    pTo = Matr[Integer.parseInt(leksArr[3])-1][Integer.parseInt(leksArr[2])-1];
    in.close();
  }

  public void printMatrCoord(String comment) {
    if (!DEBUG)
      return;
    System.out.printf("%s\n--------------\n", comment);
    for (int y=0; y<M; y++) {
      for (int x=0; x<N; x++) {
        System.out.printf("%s ", Matr[y][x]);
      }
      System.out.println();
    }
    System.out.printf("From %s\n", pFrom);
    System.out.printf("To %s\n", pTo);
    System.out.println();
  }

  public void printMatrStep(String comment) {
    if (!DEBUG)
      return;
    System.out.printf("%s\n--------------\n", comment);
    for (int y=0; y<M; y++) {
      for (int x=0; x<N; x++) {
        Point p = Matr[y][x];
        String s = String.format(p.inPath ? "%-2d● " : "%-3d ", p.step);
        System.out.print(s);
      }
      System.out.println();
    }
  }

  public ArrayList<Point> getNeibourhoods(Point p) {
    ArrayList<Point> res = new ArrayList<Point>();
    if (p.x > 0)
      res.add(Matr[p.y][p.x - 1]);
    if (p.y > 0)
      res.add(Matr[p.y - 1][p.x]);
    if (p.x < N - 1)
      res.add(Matr[p.y][p.x + 1]);
    if (p.y < M - 1)
      res.add(Matr[p.y + 1][p.x]);
    return res;
  }

  //метод прореживает весь имеющийся списко соседей, отбрасывая тех, кто уже ближе и того, откуда мы пришли
  public ArrayList<Point> calcNeib(ArrayList<Point> lst, Point pOrigin) {
    ArrayList<Point> res = new ArrayList<Point>();
    for (Point p: lst) {
      if (pOrigin.from == p) //наткнулись на точку, из которой мы приходили
        continue;
      int newStep = pOrigin.step + K + Math.abs(pOrigin.alt-p.alt);
      if (p.step==0 || p.step>newStep) {
        p.from = pOrigin;
        p.step = newStep;
        res.add(p);
      }
    }
    return res;
  }

  public void printPath() {
    if (!DEBUG)
      return;
    for (int i=0; i<path.size(); i++) {
      System.out.printf("%d)%s %s", path.get(i).step, path.get(i), i<path.size()-1 ? "-> " : "\n");
    }
  }

  private void calc() {
    LinkedList<Point> queue = new LinkedList<Point>();
    queue.addLast(pFrom);
    pFrom.from= pFrom;
    while (!queue.isEmpty()) {
      Point p = queue.pop();
      if (DEBUG)
        System.out.println(p);
      ArrayList<Point> neib = getNeibourhoods(p);
      neib = calcNeib(neib, p);
      queue.addAll(neib);
    }
    //
    Point p = pTo;
    while (true) {
      path.add(0, p);
      p.inPath = true;
      if (p.from==p)
        break;
      p = p.from;
    }
  }

  public void writeDataToFile() throws IOException {
    System.out.printf("Path length = %d\n", pTo.step);
    BufferedWriter out = new BufferedWriter(new FileWriter("out.txt"));
    out.write(Integer.toString(pTo.step));
    out.close();
  }

  @Override
  public void run() {
    try {
      readDataFromFile();
      if (DEBUG)
        System.out.printf("pFrom: %s pTo: %s\n", pFrom, pTo);
      printMatrCoord("Source matr:");
      calc();
      printMatrStep("Steps:");
      printPath();
      writeDataToFile();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args)  {
    Thread t = new Thread(null, new Main(), "",2  * 1024 * 1024);
    t.setPriority(10);
    t.start();
  }
}
