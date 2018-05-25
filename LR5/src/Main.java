import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;

public class Main implements Runnable {

  static class Officer {
    long cost; //стоимость
    int[] children; //массив индексов сыновей
  }

  static Officer[] arr;

  private static LinkedList<Long> checkCosts(int idx) { //метод на вход принимает индекс чиновника
    LinkedList<Long> res = new LinkedList<>();//заготовка под ответ
    res.addFirst(1000000000000000L);//чтобы любой другой максимум, полученный неискуственно, был меньше
    LinkedList<Long> temp; //будем получать из каждой подзадачи
    for (int i : arr[idx].children) {
      temp = checkCosts(i);
      if (temp.getFirst() < res.getFirst()) {
        res = temp;
      }
    }
    if (res.getFirst() == 1000000000000000L) { //если больше нет детей
      res.removeFirst();
      res.addFirst(arr[idx].cost);
      res.addLast(Long.valueOf((idx + 1)));//а это мы записываем путь
    } else {
      long tempCost = res.removeFirst();//стоимость убираем
      res.addFirst(Long.valueOf(idx + 1)); //добавляем номер чиновника
      res.addFirst(tempCost + arr[idx].cost); //высчитываем стоимость полную
    }
    return res;
  }

  @Override
  public void run() {
    Scanner sc = null;
    try {
      sc = new Scanner(new File("in.txt"));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    arr = new Officer[Integer.parseInt(sc.nextLine())];
    String[] s;
    for (int i = 0; i < arr.length; i++)
      arr[i] = new Officer();
    int current;

    for (Officer anArr : arr) {
      current = sc.nextInt() - 1;
      arr[current].children = new int[sc.nextInt()];
      for (int j = 0; j < arr[current].children.length; j++) {
        arr[current].children[j] = sc.nextInt() - 1;
        arr[arr[current].children[j]].cost = sc.nextInt();
      }
    }
    FileWriter fw = null;
    try {
      fw = new FileWriter("out.txt");
    } catch (IOException e) {
      e.printStackTrace();
    }
    LinkedList<Long> res = checkCosts(0); // результирующий список,[0]-стоимость, остальные - путь от корня до минимальной вершины
    int i = 0;
    for (Long l : res) {
      if (i == 0) {
        try {
          fw.append(l + "\n");
        } catch (IOException e) {
          e.printStackTrace();
        }
      } else {
        try {
          fw.append(l + " ");
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      i++;
    }
    try {
      fw.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    new Thread(null, new Main(), "", 20 * 1024 * 1024).start();
  }
}