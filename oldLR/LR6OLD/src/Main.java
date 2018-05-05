import com.sun.corba.se.impl.oa.poa.POAImpl;

import java.io.*;
import java.util.ArrayList;

public class Main implements Runnable{

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

  public Point getLeft(Point p) {
    if (p.x == 0) {
      return null;
    }
    else {
      return Matr[p.y][p.x-1];
    }
  }

  public Point getUp(Point p) {
    if (p.y == 0) {
      return null;
    }
    else {
      return Matr[p.y-1][p.x];
    }
  }

  public Point getRight(Point p) {
    if (p.x == N-1) {
      return null;
    }
    else {
      return Matr[p.y][p.x+1];
    }
  }

  public Point getDown(Point p) {
    if (p.y == M-1) {
      return null;
    }
    else {
      return Matr[p.y+1][p.x];
    }
  }

  public void setStep(Point p, int step, Point from) {
    if (p != null && p.step==0) {
      p.step = step + Math.abs(p.alt-from.alt);
      p.from = from;
    }
  }

  public void calc() {
    int step=1;
    boolean done = false;
    pFrom.step=step++;
    while (!done) {
      for (int y=0; y<M && !done; y++) {
        for (int x=0; x<N && !done; x++) {
          Point p = Matr[y][x];
          if (p.step != step-1)
            continue;
          Point pl = getLeft(p);
          Point pr = getRight(p);
          Point pu = getUp(p);
          Point pd = getDown(p);
          setStep(pl, step, p);
          setStep(pd, step, p);
          setStep(pu, step, p);
          setStep(pr, step, p);
          if (pl == pTo || pr == pTo || pu == pTo || pd == pTo) {
            done = true;
          }
        }
      }
      step++;
    }//while
    if (done) {
      Point p = pTo;
      while (p != null) {
        path.add(0, p);
        p.inPath = true;
        p = p.from;
      }
    }
  }

  public void printPath() {
    for (int i=0; i<path.size(); i++) {
      System.out.printf("%d)%s %s", path.get(i).step, path.get(i), i<path.size()-1 ? "-> " : "\n");
    }
  }

  public void writeDataToFile() throws IOException {
    BufferedWriter out = new BufferedWriter(new FileWriter("out.txt"));
    out.write(Integer.toString(pTo.step));
    out.close();
  }

  @Override
  public void run() {
    try {
      readDataFromFile();
      printMatrCoord("Source matr:");
      calc();
      printMatrStep("Steps:");
      printPath();
      System.out.printf("pFrom: %s pTo: %s", pFrom, pTo);
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
