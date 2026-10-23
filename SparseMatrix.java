import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.InputMismatchException;

/**
 * SparseMatrix.java
 * This is the main driver program for the Assignment 2 sparse matrix calculator.
 * It handles reading input files, parsing the data into custom linked lists,
 * queuing them in a FIFO structure, and performing the direct linked-list addition.
 *
 * TEST CASES DOCUMENTATION
 *
 * Test Case 1: Normal matrix addition
 * Purpose: Test the addition of matrices containing several non-zero elements.
 * Input files: matrix1.txt, matrix2.txt
 * Expected result: Result matrix with properly summed elements.
 * Actual result: Matrices sum correctly, matching manual calculations. (Passed)
 *
 * Test Case 2: Adding the maximum number of matrices
 * Purpose: Test the program with 6 input matrices.
 * Input files: mat1.txt, mat2.txt, mat3.txt, mat4.txt, mat5.txt, mat6.txt
 * Expected result: Program successfully queues and adds all 6 matrices.
 * Actual result: Program processed 6 files and output the correct sum. (Passed)
 *
 * Test Case 3: Matrix containing only zeros
 * Purpose: Test a matrix whose all elements are zero.
 * Input files: zero_matrix.txt
 * Expected result: Linked list remains empty; prints all 0s and no values in list view.
 * Actual result: Linked list was empty, 2D output was all zeros. (Passed)
 *
 * Test Case 4: Matrices containing negative values
 * Purpose: Test matrices containing both positive and negative integer values.
 * Input files: negative_matrix1.txt, negative_matrix2.txt
 * Expected result: Negative values are accurately represented and summed.
 * Actual result: Subtractions/negative additions processed correctly. (Passed)
 *
 * Test Case 5: Addition resulting in zero values
 * Purpose: Test cancellation (e.g., 5 + -5 = 0). The resulting zero must not be a node.
 * Input files: cancel_mat1.txt, cancel_mat2.txt
 * Expected result: Zero values from additions are dropped from the result linked list.
 * Actual result: Nodes that summed to 0 were completely excluded from the result list. (Passed)
 *
 * Test Case 6: Very sparse matrix
 * Purpose: Test a large matrix with only a few non-zero elements.
 * Input files: large_sparse1.txt, large_sparse2.txt
 * Expected result: Only non-zero nodes are stored; addition is efficient.
 * Actual result: Fast processing; empty spaces ignored effectively. (Passed)
 *
 * Test Case 7: Inconsistent matrix dimensions
 * Purpose: Test input matrices with different numbers of rows or columns.
 * Input files: matrix_4x5.txt, matrix_5x5.txt
 * Expected result: Program throws InconsistentDimensionException and stops.
 * Actual result: Exception thrown with clear message; execution halted. (Passed)
 *
 * Test Case 8: Invalid or missing input file
 * Purpose: Test the program with a non-existent file or corrupted text.
 * Input files: missing_file.txt, corrupt_format.txt
 * Expected result: MissingFileException or InvalidFormatException is thrown.
 * Actual result: Proper exception caught and user notified of the error. (Passed)
 */
public class SparseMatrix {

    // Main entry point: validates arguments, coordinates parsing, and triggers addition
    public static void main(String[] args) {
        // Ensure the user provided at least one file argument
        if (args.length == 0) {
            System.out.println("Please provide matrix file names as command line arguments.");
            System.out.println("Example: java SparseMatrix matrix1.txt matrix2.txt matrix3.txt matrix4.txt matrix5.txt matrix6.txt");
            return;
        }

        // Enforce the maximum limit of 6 matrices
        if (args.length > 6) {
            System.out.println("Maximum of 6 matrices allowed.");
            return;
        }

        MatrixQueue queue = new MatrixQueue(6);

        // Loop through all command line arguments to read files and queue them
        for (int i = 0; i < args.length; i++) {
            try {
                SparseMatrixList matrix = readMatrixFromFile(args[i]);
                queue.enqueue(matrix);

                // Display the matrix format immediately after reading
                System.out.print("Matrix " + (i + 1) + ": ");
                matrix.displayList();
                matrix.displayMatrix();
                System.out.println();

            } catch (MissingFileException | InvalidFormatException | InvalidValueException e) {
                System.err.println("Error processing file '" + args[i] + "': " + e.getMessage());
                return; // Stop execution if an input file is invalid
            }
        }

        // Dequeue the first matrix to act as the base for the running total
        try {
            SparseMatrixList result = queue.dequeue();

            // Continuously dequeue and add the remaining matrices
            while (!queue.isEmpty()) {
                SparseMatrixList nextMatrix = queue.dequeue();
                result = addMatrices(result, nextMatrix);
            }

            // Display the final resulting sum matrix
            System.out.print("Matrix Result: ");
            result.displayList();
            result.displayMatrix();

        } catch (InconsistentDimensionException e) {
            System.err.println("Addition Error: " + e.getMessage());
        }
    }

    // Parses a text file, verifies dimensions, and constructs a SparseMatrixList
    public static SparseMatrixList readMatrixFromFile(String filename)
            throws MissingFileException, InvalidFormatException, InvalidValueException {
        File file = new File(filename);
        if (!file.exists()) {
            throw new MissingFileException("File '" + filename + "' not found.");
        }

        try (Scanner scanner = new Scanner(file)) {
            // Read the first line for row and column dimensions
            if (!scanner.hasNextInt()) throw new InvalidFormatException("Missing dimensions in file.");
            int rows = scanner.nextInt();
            if (!scanner.hasNextInt()) throw new InvalidFormatException("Missing column dimension in file.");
            int cols = scanner.nextInt();

            SparseMatrixList matrix = new SparseMatrixList(rows, cols);

            // Read the rest of the file and insert integer values into the linked list
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    if (!scanner.hasNextInt()) {
                        throw new InvalidFormatException("Incomplete matrix data. Expected " + (rows * cols) + " numeric elements.");
                    }
                    int val = scanner.nextInt();
                    matrix.insert(r, c, val);
                }
            }
            return matrix;
        } catch (InputMismatchException e) {
            throw new InvalidFormatException("Matrix data contains non-integer values.");
        } catch (FileNotFoundException e) {
            throw new MissingFileException("File '" + filename + "' could not be opened.");
        }
    }

    // Performs direct linked-list addition by traversing two lists simultaneously
    public static SparseMatrixList addMatrices(SparseMatrixList m1, SparseMatrixList m2)
            throws InconsistentDimensionException {

        // Throw an exception if the two matrices do not have the same dimensions
        if (m1.rows != m2.rows || m1.cols != m2.cols) {
            throw new InconsistentDimensionException("Matrix dimensions do not match (" +
                    m1.rows + "x" + m1.cols + " vs " + m2.rows + "x" + m2.cols + ").");
        }

        SparseMatrixList result = new SparseMatrixList(m1.rows, m1.cols);
        Node p1 = m1.head;
        Node p2 = m2.head;

        // Array used to pass the tail node by reference for O(1) appending
        Node[] tailRef = new Node[1];

        // Walk both lists. Append the node that comes first. If coordinates match, sum them.
        while (p1 != null && p2 != null) {
            if (p1.row == p2.row && p1.col == p2.col) {
                int sum = p1.value + p2.value;
                result.append(tailRef, p1.row, p1.col, sum);
                p1 = p1.next;
                p2 = p2.next;
            } else if (p1.row < p2.row || (p1.row == p2.row && p1.col < p2.col)) {
                result.append(tailRef, p1.row, p1.col, p1.value);
                p1 = p1.next;
            } else {
                result.append(tailRef, p2.row, p2.col, p2.value);
                p2 = p2.next;
            }
        }

        // If p1 has remaining nodes, append them all
        while (p1 != null) {
            result.append(tailRef, p1.row, p1.col, p1.value);
            p1 = p1.next;
        }

        // If p2 has remaining nodes, append them all
        while (p2 != null) {
            result.append(tailRef, p2.row, p2.col, p2.value);
            p2 = p2.next;
        }

        return result;
    }
}