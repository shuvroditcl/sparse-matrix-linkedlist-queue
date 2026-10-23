/**
 * MatrixQueue.java
 * This file implements a custom First-In-First-Out (FIFO) circular queue data structure.
 * It is specifically designed to hold a maximum of 6 SparseMatrixList objects,
 * ensuring they are processed in the exact order they were read from the input files.
 */
public class MatrixQueue {
    private SparseMatrixList[] queue;
    private int front, rear, size, capacity;

    // Initializes the queue with a maximum capacity (e.g., 6 matrices)
    public MatrixQueue(int capacity) {
        this.capacity = capacity;
        this.queue = new SparseMatrixList[capacity];
        this.front = 0;
        this.rear = 0;
        this.size = 0;
    }

    // Adds a new matrix to the rear of the queue
    public void enqueue(SparseMatrixList matrix) {
        if (size == capacity) {
            System.out.println("Queue is full. Cannot enqueue more matrices.");
            return;
        }
        queue[rear] = matrix;
        rear = (rear + 1) % capacity;
        size++;
    }

    // Removes and returns the matrix at the front of the queue
    public SparseMatrixList dequeue() {
        if (isEmpty()) {
            return null;
        }
        SparseMatrixList matrix = queue[front];
        front = (front + 1) % capacity;
        size--;
        return matrix;
    }

    // Checks if the queue is currently empty
    public boolean isEmpty() {
        return size == 0;
    }
}