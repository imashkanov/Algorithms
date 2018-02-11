import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {

 static class Node {
    Node left;
    Node right;
    Node parent;
    int value;
    boolean marker;

    Node (Node parent, int value) {
      this.parent = parent;
      this.value = value;
    }

    @Override
    public String toString() {
      return String.format("[V:%d L:%s R:%s]", value, left!=null, right!=null);
    }

  }

  static void log(String text) {
    //System.out.println(text);
  }

  static ArrayList<Integer> lst = new ArrayList<Integer>();
  static Node root = null;

  private static void readDataFromFile(String[] args) {
    String fileNameInput = args.length > 0 ? args[0] : "input.txt";
    List<String> list = null;
    try {
      list = Files.readAllLines(Paths.get(fileNameInput));
      //log(list.toString());
    } catch (IOException e) {
      //log("error read from file");
      System.exit(2);
    }

    for (String s : list) {
      try {
        int value = Integer.parseInt(s);
        if (!lst.contains(value)) {
          lst.add(value);
        }
      } catch (Exception e) {
        log("error in parsing string to int");
        System.exit(3);
      }
    }
    log(lst.toString());
  }

  private static void fillTree() {
    for (Integer i: lst) {
      addValueToBinaryTree(i);
    }
  }

  private static void addValueToBinaryTree(int value) {
    Node node = root;
    Node parent = null;
    if (node == null) {
      root = new Node(null, value);
      return;
    }
    while (true) {
      parent = node;
      boolean left = value < parent.value;
      if (left) {
        if (parent.left == null) {
          parent.left = new Node(parent, value);
          return;
        }
        node = parent.left;
      } else { //right
        if (parent.right == null) {
          parent.right = new Node(parent, value);
          return;
        }
        node = parent.right;
      }
    }
  }

  private static void directLeftBypassTree() {
    lst.clear();
    Node node = root;
    int maxDeep = 0;
    while (node != null) {
      if (maxDeep++ > 100)
        break;
      if (node.marker && (
        ((node.right!= null) && node.right.marker) ||
        ((node.right == null) && (node.left != null) && node.left.marker) )
        ) {
        node = node.parent;
        //log(String.format("   ^^ %s", node));
        continue;
      }
      if (node.marker && (node.right != null) && !node.right.marker) {
        node = node.right;
        //log(String.format("   > %s", node));
        continue;
      }
      lst.add(node.value);
      node.marker = true;
      //log(String.format("+ %s", node));
      if (node.left != null) {
        node = node.left;
        //log(String.format("   < %s", node));
        continue;
      }
      if (node.right != null) {
        node = node.right;
        //log(String.format("   > %s", node));
        continue;
      }
      node = node.parent;
      //log(String.format("   ^ %s", node));
    }
  }

  private static void writeDataToFile() {
    try {
      BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
      for (Integer i: lst) {
        writer.write(Integer.toString(i)+"\n");
      }
      writer.close();
    } catch (IOException e) {
      log("error in output file");
    }
  }


  public static void main(String[] args) {
    long l = System.currentTimeMillis() ;
    readDataFromFile(args);
    fillTree();
    directLeftBypassTree();
    writeDataToFile();
    l = System.currentTimeMillis()  - l;
    //log(String.format("Size mem alloicated: %d M", Runtime.getRuntime().totalMemory()/1024/1024));
    System.out.println(String.format("%d milliseconds", l));
  }

}

