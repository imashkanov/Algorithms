import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Main implements Runnable {


  static class Node {
    Node left; //левый потомок
    Node right; //правый потомок
    Node parent;
    int cntLeft; //количество элементов в левом поддереве
    int cntRight; //количество элементов в правом поддереве
    int leftHeight; //высота левого поддерева
    int rightHeight; //высота правого поддерева
    int value; //ключ

    Node (int value, Node parent) {
      this.value = value;
      this.parent = parent;
    }

    @Override
    public String toString() {
      return String.format("[V:%-3d  L:%5s R:%5s  hL:%-2d hR:%-2d   cL/cR:%d/%d]", value, left!=null, right!=null,
        leftHeight, rightHeight, cntLeft, cntRight);
    }
  }

  static ArrayList<Node> lst = new ArrayList<Node>();  //список вершин
  static Node root = null; //корень дерева

  private static void fillListRandom(String arg) {
    int cnt = Integer.parseInt(arg);
    for (int i=0; i<cnt; i++) {
      int digit = (int) (Math.random() * 1000000);
      addValueToBinaryTree(digit, root);
    }
  }

  private static void readDataFromFile() throws IOException {
    long time_read=0;
    long time_parse=0;
    long time_add=0;
    BufferedReader in = new BufferedReader(new FileReader("in.txt"));
    String s;
    while (true) {
      long l = System.currentTimeMillis();
      s = in.readLine();
      l = System.currentTimeMillis() - l;
      time_read += l;
      if (s== null)
        break;
      //
      l = System.currentTimeMillis();
      int i = Integer.parseInt(s);
      l = System.currentTimeMillis() - l;
      time_parse += l;
      //
      l = System.currentTimeMillis();
      addValueToBinaryTree(i, root);
      l = System.currentTimeMillis() - l;
      time_add += l;
    }
    in.close();
    System.out.printf("read %d ms\nparse %d ms\nadd %d ms\n", time_read, time_parse, time_add);
  }

  private static int heightOfTree(Node node) { //рекурсивный метод, возвращающий для данной вершины её высоту
    if (node == null)
      return 0;
    int leftHeight, rightHeight;
    //
    if (node.left != null) {
      leftHeight = heightOfTree(node.left);
      node.leftHeight = leftHeight;
    } else
       leftHeight = -1;
    //
    if (node.right != null) {
      rightHeight = heightOfTree(node.right);
      node.rightHeight = rightHeight;
    } else
       rightHeight = -1;
    int res = leftHeight > rightHeight ? leftHeight : rightHeight;
    return res+1;

  }

  private static void addValueToBinaryTree(int value, Node node) { //метод добавления вершины в дерево
    if (node==null || root == null) {
      root = new Node(value, null);
      return;
    }
    if (value==node.value)
      return;
    if (value<node.value) {
      if (node.left != null)
        addValueToBinaryTree(value, node.left);
      else
        node.left = new Node(value, node);
    } else {
      if (node.right != null)
        addValueToBinaryTree(value, node.right);
      else
        node.right = new Node(value, node);
    }
  }

  //метод поиска вершины по заданному значению
  private static Node findNodeByValue(Node node,Node parent, int value) {
    if (node == null)
      return null;
    if (node.value == value)
      return new Node(node.value, parent);
    Node res = null;
    if (node.left != null) {
      Node resLeft = findNodeByValue(node.left, node, value);
      if (resLeft != null)
        res = resLeft;
    }
    if (node.right != null) {
      Node resRight = findNodeByValue(node.right, node, value);
      if (resRight != null)
        res = resRight;
    }
    return res;
  }

  //метод возвращает наименьшую по значению вершину (самую нижнюю левую) для данной вершины
  private static Node getButtomLeftNode(Node node, Node parent, int context) {
    if (node.left == null) {
      return new Node(node.value, parent);
    } else {
      if (context==0)
        return getButtomLeftNode(node.right, node, ++context);
      else
        return getButtomLeftNode(node.left, node, ++context);
    }
  }

  //метод удаляет вершину из дерева правым удалением
  private static void deleteNode(Node node) {
    if (node.left == null && node.right == null) {
      if (node.parent!=null && node.parent.left == node) {
        node.parent.left = null;
      }
      if (node.parent!=null && node.parent.right == node) {
        node.parent.right = null;
      }
      return;
    }
    //определяем где мы у родительского нода: слева или справа
    boolean we_on_left = false;
    boolean we_on_right = false;
    if (node.parent!=null) {
      we_on_right = node.parent.right == node;
      we_on_left  = node.parent.left == node;
    }
    if (node.left != null && node.right == null) {
      if (we_on_right)
        node.parent.right = node.left;
      if (we_on_left)
        node.parent.left = node.left;
      if (node.parent==null)
        root = node.left;
      node = null;
      return;
    }
    if (node.left == null && node.right != null) {
      if (we_on_right)
        node.parent.right = node.right;
      if (we_on_left)
        node.parent.left = node.right;
      if (node.parent==null)
        root = node.right;
      node = null;
      return;
    }
    Node brn = getButtomLeftNode(node, node.parent, 0); //самая низкая левая вершина
    int old_value = brn.value;
    deleteNode(brn);
    node.value = old_value;

  }
  //левый обход бинарного дерева
  private static void directLeftBypassTree(Node node) {
    if (root == null)
      return;
    if (root == node) {
      lst.add(node);
    }
    if (node.left != null) {
      lst.add(node.left);
      directLeftBypassTree(node.left);
    }
    if (node.right != null) {
      lst.add(node.right);
      directLeftBypassTree(node.right);
    }
  }


  //левый обход бинарного дерева + подсчет высот и количеств потомков правого и левого поддеревьев
  private static int directLeftBypassTreeWithCalc(Node node) {
    if (root == null)
      return 0;
    if (root == node) {
      lst.add(node);
    }
    node.leftHeight = -1;
    node.rightHeight = -1;
    if (node.left != null) {
      //node.leftHeight = heightOfTree(node.left);
      lst.add(node.left);
      node.cntLeft = directLeftBypassTreeWithCalc(node.left) + 1;
    }
    if (node.right != null) {
      //node.rightHeight = heightOfTree(node.right);
      lst.add(node.right);
      node.cntRight = directLeftBypassTreeWithCalc(node.right) +1;
    }
    return node.cntLeft + node.cntRight;
  }


  //метод возвращает значение вершины, которая удовлетворяет всем требованиям задания
  private static int valueOfNeededNode() {
    ArrayList<Node> lstOfNeededNodes = new ArrayList<Node>();
    for (Node node: lst) {
      if ((node.leftHeight == node.rightHeight) && (node.cntLeft != node.cntRight)) {
        lstOfNeededNodes.add(node);
      }
    }
   Collections.sort(lstOfNeededNodes, new Comparator<Node>() {
      @Override
      public int compare(Node n1, Node n2) {
        return n1.value > n2.value ? 1:-1;
      }
    });
    //writeDataToFile(false, lstOfNeededNodes);
    int idx = 0;
    if (lstOfNeededNodes.size() % 2 != 0) {
      idx = (lstOfNeededNodes.size() -1) / 2;
      return lstOfNeededNodes.get(idx).value;
    } else {
      return -1;
    }
  }

  //метод записи списка в файл
  private static void writeDataToFile(boolean simpleDigit, ArrayList<Node> lst) {
    try {
      FileWriter writer = new FileWriter("out.txt");
      for (Node node : lst) {
        if (simpleDigit) {
          writer.write(Integer.toString(node.value) + "\n");
        } else {
          writer.write(node.toString() + "\n");
        }
      }
      writer.close();
    } catch (IOException e) {
      System.out.println("error in output file");
    }
  }


  public static void main(String[] args)  {
    new Thread(null, new Main(), "", 5 * 1024 * 1024).start();
  }

  @Override
  public void run() {
    long l = System.currentTimeMillis() ;
    try {
      readDataFromFile();
    } catch (Exception e) {
      System.out.println("sout error");
      System.exit(2);
    }
    System.out.println(String.format("%d ms load", System.currentTimeMillis() - l));
    lst = new ArrayList<Node>();
    directLeftBypassTreeWithCalc(root);
    root.leftHeight = heightOfTree(root.left);
    root.rightHeight= heightOfTree(root.right);
    int val = valueOfNeededNode();
    //int val = 20;
    if (val != 0) {
      Node node = findNodeByValue(root, null, val);
      if (node != null) {
        deleteNode(node);
      }
    }
    lst = new ArrayList<Node>();
    directLeftBypassTree(root);
    System.out.println(String.format("%d ms bypass", System.currentTimeMillis() - l));
    writeDataToFile(true, lst);
    System.out.println(String.format("%d ms end", System.currentTimeMillis() - l));
  }


}

