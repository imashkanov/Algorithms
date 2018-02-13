import jdk.nashorn.internal.runtime.regexp.joni.constants.NodeType;

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

  static class NodeTuple {
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

  static void log(String text) {
    //System.out.println(text);
  }

  static ArrayList<Integer> lst = new ArrayList<Integer>();

  static Node root = null;
  static int delValue = 0;


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
      delValue = sc.nextInt();
      System.out.println("node for delete: "+delValue);
      while (sc.hasNextInt()) {
        int i =sc.nextInt();
        System.out.println(i);
        addValueToBinaryTree(i, root);
      }
    } catch (Exception e) {
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

  private static NodeTuple findNodeByValue(Node node, Node parent, int value) {
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

  private static NodeTuple getButtomLeftNode(Node node, Node parent, int context) {
    if (node.left == null) {
      return new NodeTuple(node, parent);
    } else {
      if (context==0)
        return getButtomLeftNode(node.right, node, context++);
      else
        return getButtomLeftNode(node.left, node, context++);
    }
  }

  private static void deleteNode(NodeTuple nt) {
    if (nt.node.left == null && nt.node.right == null) {
      if (nt.parentNode.left == nt.node) {
        nt.parentNode.left = null;
      }
      if (nt.parentNode.right == nt.node) {
        nt.parentNode.right = null;
      }
      return;
    }
    if (nt.node.left != null && nt.node.right == null) {
      nt.parentNode.left = nt.node.left;
      nt.node = null;
      return;
    }
    if (nt.node.left == null && nt.node.right != null) {
      nt.parentNode.right = nt.node.right;
      nt.node = null;
      return;
     }
    NodeTuple brn = getButtomLeftNode(nt.node, nt.parentNode, 0); //самая низкая левая вершина
    deleteNode(brn);
    nt.node.value = brn.node.value;

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
    NodeTuple node = findNodeByValue(root, null, delValue);
    System.out.println(node);
    deleteNode(node);
    directLeftBypassTree(root);
    System.out.println(String.format("%d ms bypass", System.currentTimeMillis() - l));
    writeDataToFile();
    System.out.println(String.format("%d ms end", System.currentTimeMillis() - l));
    System.out.println(Runtime.getRuntime().totalMemory()/1024/1024);
  }
}

