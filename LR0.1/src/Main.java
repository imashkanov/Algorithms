import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

 static class Node {
    Node left;
    Node right;
    int value;

    Node (int value) {
      this.value = value;
      System.out.println(String.format("+ %d", value));
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


  private static void readDataFromFile() {
    ArrayList<Integer> list = new ArrayList<Integer>();
    try {
      Scanner sc = new Scanner(new File("input.txt"));
      while (sc.hasNextInt()) {
        list.add(sc.nextInt());
      }
    } catch (Exception e) {
      //log("error read from file");
      System.exit(2);
    }
    for (int value : list) {
      try {
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
      addValueToBinaryTree(i, root);
    }
  }

  private static void addValueToBinaryTree(int value, Node node) {
    if (node==null || root == null) {
      root = new Node(value);
      return;
    }
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
  }

  private static void directLeftBypassTree(Node node) {
    if (root == null)
      return;
    if (root == node)
      lst.add(node.value);
    if (node.left != null) {
      lst.add(node.left.value);
      directLeftBypassTree(node.left);
    }
    if (node.right != null) {
      lst.add(node.right.value);
      directLeftBypassTree(node.right);
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
    readDataFromFile();
    fillTree();
    lst.clear();
    directLeftBypassTree(root);
    writeDataToFile();
    l = System.currentTimeMillis()  - l;
    //log(String.format("Size mem alloicated: %d M", Runtime.getRuntime().totalMemory()/1024/1024));
    System.out.println(String.format("%d milliseconds", l));
  }

}

