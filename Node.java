public class Node {
    int row;
    int col;
    int value;
    Node next;

    public Node(int row, int col, int value) {
        this.row = row;
        this.col = col;
        this.value = value;
        this.next = null;
    }
}