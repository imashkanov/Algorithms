import java.util.HashSet;
import java.util.Set;

public class Main {

  public static void main(String[] args) {

  }
}

class Node {
  Node left;
  Node right;
  Node parent;
  int value;

  Node() {};

  Node (Node parent, int value) {
    this.parent = parent;
    this.value = value;
  }
}

class Processor {
  Set<Node> set = new HashSet<Node>();


}

