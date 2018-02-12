import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

 static class Node {
    Node left;
    Node right;
    int value;

    Node (int value) {
      this.value = value;
      //System.out.println(String.format("+ %d", value));
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


  private static void fillListRandom(String arg) {
    int cnt = Integer.parseInt(arg);
    for (int i=0; i<cnt; i++) {
      int digit = (int) (Math.random() * 1000000);
      addValueToBinaryTree(digit, root);
    }
  }

  private static void readDataFromFile() {
    try {
      Scanner sc = new Scanner(new File("input.txt"));
      while (sc.hasNextInt()) {
        addValueToBinaryTree(sc.nextInt(), root);
      }
    } catch (Exception e) {
      //log("error read from file");
      System.exit(2);
    }
    }

  private static void addValueToBinaryTree(int value, Node node) {
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
    if (args.length == 1) {
      fillListRandom(args[0]);
    } else {
      readDataFromFile();
    }
    System.out.println(String.format("%d ms load", System.currentTimeMillis() - l));
    lst.clear();
    directLeftBypassTree(root);
    System.out.println(String.format("%d ms bypass", System.currentTimeMillis() - l));
    writeDataToFile();
    System.out.println(String.format("%d ms end", System.currentTimeMillis() - l));
    System.out.println(Runtime.getRuntime().totalMemory()/1024/1024);
  }
}

