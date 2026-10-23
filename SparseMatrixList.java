/**
 * SparseMatrixList.java
 * This file defines a custom singly linked list used to represent a sparse matrix.
 * It strictly stores only non-zero elements to save memory.
 * Nodes are kept sorted in ascending order primarily by row, and secondarily by column.
 */
public class SparseMatrixList {
    Node head;
    int rows;
    int cols;

    // Constructor to initialize the list with the matrix dimensions
    public SparseMatrixList(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.head = null;
    }

    // Inserts a new node in the correct sorted position (ascending by row, then col).
    // Explicitly drops any values of 0.
    public void insert(int r, int c, int val) {
        if (val == 0) return; // Zeros are not nodes in the linked list

        Node newNode = new Node(r, c, val);

        // Insert at the head if list is empty or new node comes before the current head
        if (head == null || compareNodes(newNode, head) < 0) {
            newNode.next = head;
            head = newNode;
            return;
        }

        // Traverse the list to find the correct insertion point
        Node current = head;
        while (current.next != null && compareNodes(newNode, current.next) > 0) {
            current = current.next;
        }

        // If a node already exists at these exact coordinates, update its value
        if (current.row == r && current.col == c) {
            current.value = val;
            return;
        }

        // Splice the new node into the list
        newNode.next = current.next;
        current.next = newNode;
    }

    // Helper method for O(1) constant time insertion during matrix addition.
    // Uses a reference array to keep track of the tail node.
    public void append(Node[] tailRef, int r, int c, int val) {
        if (val == 0) return; // Drop resulting zeroes
        Node newNode = new Node(r, c, val);
        if (this.head == null) {
            this.head = newNode;
            tailRef[0] = newNode;
        } else {
            tailRef[0].next = newNode;
            tailRef[0] = newNode;
        }
    }

    // Helper method to compare coordinates of two nodes for sorting
    private int compareNodes(Node n1, Node n2) {
        if (n1.row != n2.row) {
            return Integer.compare(n1.row, n2.row);
        }
        return Integer.compare(n1.col, n2.col);
    }

    // Prints the non-zero elements of the linked list sequentially
    public void displayList() {
        Node current = head;
        while (current != null) {
            System.out.print(current.value + (current.next != null ? " " : ""));
            current = current.next;
        }
        System.out.println();
    }

    // Prints the full matrix in a 2D grid format, filling in zeroes for missing nodes
    public void displayMatrix() {
        Node current = head;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (current != null && current.row == i && current.col == j) {
                    System.out.print(current.value + " ");
                    current = current.next;
                } else {
                    System.out.print("0 ");
                }
            }
            System.out.println();
        }
    }

    public int countNodes() {
        int count = 0;
        Node current = head;
        while (current != null) {
            count++;
            current = current.next;
        }
        return count;
    }
}