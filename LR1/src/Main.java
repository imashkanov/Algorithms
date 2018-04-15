import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Main implements Runnable {


  class Node {
    Node left; //левый потомок
    Node right; //правый потомок
    int cntLeft; //количество элементов в левом поддереве
    int cntRight; //количество элементов в правом поддереве
    int leftHeight; //высота левого поддерева
    int rightHeight; //высота правого поддерева
    int value; //ключ

    Node (int value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return String.format("[V:%-3d  L:%5s R:%5s  hL:%-2d hR:%-2d   cL/cR:%d/%d]", value, left!=null, right!=null,
        leftHeight, rightHeight, cntLeft, cntRight);
    }
  }

  class NodeTuple { //класс Node, переделанный для рекурсии: вместо node передается пара из двух node
    // первый - мыб второй - наш отец
    Node node;
    Node parentNode;

    NodeTuple(Node node, Node parentNode) {
      this.node = node;
      this.parentNode = parentNode;
    }

    @Override
    public String toString() {
      return String.format("%s %s", node, parentNode);
    }
  }

  ArrayList<Node> lst = new ArrayList<Node>();  //список вершин
  Node root = null; //корень дерева

  private void fillListRandom(String arg) {
    int cnt = Integer.parseInt(arg);
    for (int i=0; i<cnt; i++) {
      int digit = (int) (Math.random() * 1000000);
      addValueToBinaryTree(digit);
    }
  }

  private void readDataFromFile() throws IOException {
    long ll = System.currentTimeMillis();
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
      addValueToBinaryTree(i);
      l = System.currentTimeMillis() - l;
      time_add += l;
    }
    in.close();
    System.out.printf("--read %d ms\n--parse %d ms\n--add %d ms\n", time_read, time_parse, time_add);
    System.out.println(String.format("%d ms readDataFromFile()\n----------", System.currentTimeMillis() - ll));
  }

  private int heightOfTree(Node node) { //рекурсивный метод, возвращающий для данной вершины её высоту
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

  private int calcHeight(Node node) {
    if (node != null) {
      return Math.max(calcHeight(node.left), calcHeight(node.right)) + 1;
    } else {
      return -1;
    }
  }

  private void addValueToBinaryTree(int x) {
    if (root == null) {
      root = new Node(x);
      return;
    }
    Node node = root;
    while (true) {
      if (x < node.value) {
        if (node.left == null) {
          node.left = new Node(x);
          return;
        } else {
          node = node.left;
        }
      } else if (x > node.value) {
        if (node.right == null) {
          node.right = new Node(x);
          return;
        } else {
          node = node.right;
        }
      } else {
        return;
      }
    }
  }

  /* Рекурсивный вариант
  private void addValueToBinaryTree(int value, Node node) { //метод добавления вершины в дерево
    if (node==null || root == null) {
      root = new Node(value);
      return;
    }
    if (value==node.value)
      return;
    if (value<node.value) {
      if (node.left != null)
        addValueToBinaryTree(value, node.left);
      else
        node.left = new Node(value);
    } else {
      if (node.right != null)
        addValueToBinaryTree(value, node.right);
      else
        node.right = new Node(value);
    }
  }*/

  //метод поиска вершины по заданному значению
  private NodeTuple findNodeByValue(Node node, Node parent, int value) {
    if (node == null)
      return null;
    if (node.value == value)
      return new NodeTuple(node, parent);
    NodeTuple res = null;
    if (node.left != null) {
      NodeTuple resLeft = findNodeByValue(node.left, node, value);
      if (resLeft != null)
        res = resLeft;
    }
    if (node.right != null) {
      NodeTuple resRight = findNodeByValue(node.right, node, value);
      if (resRight != null)
        res = resRight;
    }
    return res;
  }

  //метод возвращает наименьшую по значению вершину (самую нижнюю левую) для данной вершины
  private NodeTuple getButtomLeftNode(Node node, Node parent, int context) {
    if (node.left == null) {
      return new NodeTuple(node, parent);
    } else {
      if (context==0)
        return getButtomLeftNode(node.right, node, ++context);
      else
        return getButtomLeftNode(node.left, node, ++context);
    }
  }

  //метод удаляет вершину из дерева правым удалением
  private void deleteNode(NodeTuple nt) {
    if (nt.node.left == null && nt.node.right == null) {
      if (nt.parentNode!=null && nt.parentNode.left == nt.node) {
        nt.parentNode.left = null;
      }
      if (nt.parentNode!=null && nt.parentNode.right == nt.node) {
        nt.parentNode.right = null;
      }
      return;
    }
    //определяем где мы у родительского нода: слева или справа
    boolean we_on_left = false;
    boolean we_on_right = false;
    if (nt.parentNode!=null) {
      we_on_right = nt.parentNode.right == nt.node;
      we_on_left  = nt.parentNode.left == nt.node;
    }
    if (nt.node.left != null && nt.node.right == null) {
      if (we_on_right)
        nt.parentNode.right = nt.node.left;
      if (we_on_left)
        nt.parentNode.left = nt.node.left;
      if (nt.parentNode==null)
        root = nt.node.left;
      nt.node = null;
      return;
    }
    if (nt.node.left == null && nt.node.right != null) {
      if (we_on_right)
        nt.parentNode.right = nt.node.right;
      if (we_on_left)
        nt.parentNode.left = nt.node.right;
      if (nt.parentNode==null)
        root = nt.node.right;
      nt.node = null;
      return;
    }
    NodeTuple brn = getButtomLeftNode(nt.node, nt.parentNode, 0); //самая низкая левая вершина
    int old_value = brn.node.value;
    deleteNode(brn);
    nt.node.value = old_value;

  }
  //левый обход бинарного дерева
  private void directLeftBypassTree(Node node) {
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
  private int directLeftBypassTreeWithCalc(Node node) {
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
  private int valueOfNeededNode() {
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
  private void writeDataToFile(boolean simpleDigit, ArrayList<Node> lst) {
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
    Thread t1 = new Thread(null, new Main(), "",2  * 1024 * 1024);
    t1.setPriority(10);
    t1.start();
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
    lst = new ArrayList<Node>();
    directLeftBypassTreeWithCalc(root);
    long ll = System.currentTimeMillis() ;
    root.leftHeight = heightOfTree(root.left);
    root.rightHeight= heightOfTree(root.right);
    System.out.println(String.format("%d ms heightOfTree", System.currentTimeMillis() - ll));
    ll = System.currentTimeMillis() ;
    int val = valueOfNeededNode();
    //int val = 20;
    if (val != 0) {
      NodeTuple node = findNodeByValue(root, null, val);
      if (node != null) {
        deleteNode(node);
      }
    }
    System.out.println(String.format("%d ms deleteNode", System.currentTimeMillis() - ll));
    lst = new ArrayList<Node>();
    directLeftBypassTree(root);
    writeDataToFile(true, lst);
    System.out.println(String.format("%d ms TOTAL PROGRAM", System.currentTimeMillis() - l));
  }


}

