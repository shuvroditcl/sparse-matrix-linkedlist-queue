# Sparse Matrix Addition Engine (Custom LinkedList & Queue)

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Data Structures](https://img.shields.io/badge/Data_Structures-LinkedList_%26_Queue-blue?style=for-the-badge)
![Tests](https://img.shields.io/badge/Tests-10_Passed-brightgreen?style=for-the-badge)
![License](https://img.shields.io/badge/License-MIT-green?style=for-the-badge)

A high-efficiency Java engine designed to parse, queue, and sum large sparse matrices directly in memory without converting them into 2D arrays[cite: 15, 16, 23]. Built from scratch using custom singly linked lists and a FIFO circular/array queue without relying on `java.util.LinkedList` or Java Collection frameworks[cite: 16, 23].

---

## 🚀 Key Features
- **Custom Singly Linked List (`SparseMatrixList` & `Node`):** Stores only non-zero matrix elements ordered by ascending row and column coordinates, complete with $O(1)$ tail-pointer appending[cite: 16, 19, 23].
- **FIFO Processing Queue (`MatrixQueue`):** Buffers up to 6 parsed sparse matrices and processes sequential matrix additions in First-In, First-Out order[cite: 15, 16, 23].
- **Direct Linked-List Addition (`SparseMatrix`):** Uses a two-pointer simultaneous traversal algorithm to sum matrices directly in compressed form, automatically dropping zero-cancelled nodes (e.g., `5 + (-5) = 0`)[cite: 16, 17, 23].
- **Custom Exception Hierarchy:** Enforces strict validation with four dedicated exceptions[cite: 16, 19, 23]:
  - `MissingFileException` — Triggered when an input `.txt` file does not exist[cite: 16, 23].
  - `InvalidFormatException` — Triggered on malformed dimensions, incomplete grids, or non-integer tokens[cite: 16, 23].
  - `InconsistentDimensionException` — Triggered when attempting to sum matrices of differing `R x C` sizes[cite: 16, 23].
  - `InvalidValueException` — Handles invalid matrix element values[cite: 16, 19, 23].
- **Automated Test Suite (`TestRunner`):** Self-contained test harness executing 10 normal, boundary, and error test cases with formatted console reporting[cite: 21, 24].

---

## 🏗️ System Architecture (Class Diagram)
```mermaid
classDiagram
    SparseMatrix ..> MatrixQueue : enqueues/dequeues
    SparseMatrix ..> SparseMatrixList : builds & sums
    TestRunner ..> SparseMatrix : tests
    MatrixQueue "1" o-- "0..6" SparseMatrixList : stores in FIFO order
    SparseMatrixList "1" *-- "0..*" Node : linked nodes

    class SparseMatrix {
        +main(String[] args)$
        +readMatrixFromFile(String filename)$ SparseMatrixList
        +addMatrices(SparseMatrixList m1, SparseMatrixList m2)$ SparseMatrixList
    }
    class SparseMatrixList {
        +int rows
        +int cols
        +Node head
        +insert(int r, int c, int val)
        +append(Node[] tailRef, int r, int c, int val)
        +displayList()
        +displayMatrix()
        +countNodes() int
    }
    class Node {
        +int row
        +int col
        +int value
        +Node next
    }
    class MatrixQueue {
        +enqueue(SparseMatrixList matrix)
        +dequeue() SparseMatrixList
        +isEmpty() boolean
    }
```

---

## ⚡ Algorithm Complexity Analysis

| Representation | Time Complexity (Adding $N$ Matrices) | Space Overhead ($1,000,000 \times 1,000,000$ Matrix) |
| :--- | :--- | :--- |
| **Custom Sparse Linked List** | $\mathcal{O}(N \times L)$ | Stores only $L$ non-zero nodes (a few KB) |
| **Traditional 2D Array (`int[][]`)** | $\mathcal{O}(N \times R \times C)$ | Requires $10^{12}$ cells (impossible in standard RAM) |

*Where $N$ = number of input matrices, $L$ = total non-zero elements, and $R, C$ = rows and columns[cite: 25].*

By using simultaneous two-pointer traversal over sorted linked nodes, the addition algorithm visits only meaningful non-zero coordinates, completely bypassing empty zeroes[cite: 23, 25]. See [`TimeComplexity.txt`](./TimeComplexity.txt) for the full breakdown.

---

## 💻 How to Run

### 1. Compile the Source Code
```bash
javac *.java
```

### 2. Mode A: Run the CLI Calculator (Up to 6 Matrices)
Pass between 1 and 6 matrix files via command-line arguments[cite: 17, 21, 23]:
```bash
java SparseMatrix matrix1.txt matrix2.txt matrix3.txt matrix4.txt matrix5.txt matrix6.txt
```

### 3. Mode B: Run the Automated Test Suite (10 Test Cases)
Execute the built-in test runner, which pulls test matrices from the `testcases/` directory[cite: 20, 21, 24]:
```bash
java TestRunner
```

---

## 🧪 Automated Test Suite Coverage (`TestRunner.java`)
1. **Test 1:** Normal Matrix Addition (`matrix1.txt`, `matrix2.txt`)[cite: 24]
2. **Test 2:** Maximum Capacity Addition — 6 Matrices (`matrix1.txt` -> `matrix6.txt`)[cite: 24]
3. **Test 3:** All-Zeros Matrix Handling (`matrix3.txt`)[cite: 24]
4. **Test 4:** Negative Values Handling (`matrix4.txt`)[cite: 24]
5. **Test 5:** Zero Cancellation Addition — `5 + (-5) = 0` (`matrix1.txt`, `matrix5.txt`)[cite: 17, 24]
6. **Test 6:** Very Sparse Structure Check (`matrix6.txt`)[cite: 24]
7. **Test 7:** Boundary — Inconsistent Dimensions (`InconsistentDimensionException`)[cite: 24]
8. **Test 8:** Boundary — Invalid File Format (`InvalidFormatException`)[cite: 24]
9. **Test 9:** Boundary — Missing File (`MissingFileException`)[cite: 24]
10. **Test 10:** Single Matrix Input (`N = 1`)[cite: 24]

---

## 👤 Author
**Subrota Debnath**  
Master of Information Sciences — Massey University[cite: 25]
