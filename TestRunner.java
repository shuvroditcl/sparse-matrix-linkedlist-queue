public class TestRunner {
    private static int passedCount = 0;
    private static int failedCount = 0;

    public static void main(String[] args) {
        printHeader("SPARSE MATRIX AUTOMATED TEST SUITE");

        runTest1();
        runTest2();
        runTest3();
        runTest4();
        runTest5();
        runTest6();
        runTest7();
        runTest8();
        runTest9();
        runTest10();

        printSummary();
    }

    // --- UI Formatting Helpers ---
    private static void printHeader(String title) {
        System.out.println("\n=================================================================");
        System.out.println("| " + centerText(title, 63) + " |");
        System.out.println("=================================================================\n");
    }

    private static String centerText(String text, int width) {
        int padding = (width - text.length()) / 2;
        return String.format("%" + (padding + text.length()) + "s", text).concat(String.format("%" + (width - (padding + text.length())) + "s", ""));
    }

    private static void printTestHeader(int testNum, String description, String inputs) {
        System.out.println(">> TEST " + testNum + ": " + description);
        System.out.println("  |-- Inputs  : " + inputs);
    }

    private static void pass() {
        System.out.println("  \\-- Status  : [ PASS ]\n");
        System.out.println("-------------------------------------------------------------------");
        passedCount++;
    }

    private static void fail(String reason) {
        System.out.println("  |-- Error   : " + reason);
        System.out.println("  \\-- Status  : [ FAIL ]\n");
        System.out.println("-------------------------------------------------------------------");
        failedCount++;
    }

    private static void printSummary() {
        System.out.println("\n=================================================================");
        System.out.println("| TEST RUN COMPLETE                                             |");
        System.out.println("| " + passedCount + " Passed | " + failedCount + " Failed                                          |");
        System.out.println("=================================================================\n");
    }

    // --- Test Cases ---
    private static void runTest1() {
        printTestHeader(1, "Normal Matrix Addition", "matrix1.txt, matrix2.txt");
        try {
            SparseMatrixList m1 = SparseMatrix.readMatrixFromFile("testcases/matrix1.txt");
            SparseMatrixList m2 = SparseMatrix.readMatrixFromFile("testcases/matrix2.txt");

            System.out.print("  |-- Matrix 1 List  : ");
            m1.displayList();
            System.out.println("  |-- Matrix 1 Grid  :");
            m1.displayMatrix();

            System.out.print("\n  |-- Matrix 2 List  : ");
            m2.displayList();
            System.out.println("  |-- Matrix 2 Grid  :");
            m2.displayMatrix();

            SparseMatrixList result = SparseMatrix.addMatrices(m1, m2);
            System.out.print("\n  |-- Result List    : ");
            result.displayList();
            System.out.println("  |-- Result Grid    :");
            result.displayMatrix();
            pass();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    private static void runTest2() {
        printTestHeader(2, "Maximum Capacity (6 Matrices)", "matrix1.txt -> matrix6.txt");
        try {
            String[] files = {"testcases/matrix1.txt", "testcases/matrix2.txt", "testcases/matrix3.txt",
                    "testcases/matrix4.txt", "testcases/matrix5.txt", "testcases/matrix6.txt"};
            SparseMatrixList result = SparseMatrix.readMatrixFromFile(files[0]);
            for (int i = 1; i < files.length; i++) {
                result = SparseMatrix.addMatrices(result, SparseMatrix.readMatrixFromFile(files[i]));
            }
            System.out.print("  |-- Final Res List : ");
            result.displayList();
            System.out.println("  |-- Final Res Grid :");
            result.displayMatrix();
            pass();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    private static void runTest3() {
        printTestHeader(3, "All-Zeros Matrix", "matrix3.txt");
        try {
            SparseMatrixList m1 = SparseMatrix.readMatrixFromFile("testcases/matrix3.txt");
            System.out.print("  |-- Input List     : ");
            m1.displayList();
            System.out.println("  |-- Input Grid     :");
            m1.displayMatrix();
            if (m1.countNodes() == 0) pass();
            else fail("List was not empty.");
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    private static void runTest4() {
        printTestHeader(4, "Negative Values Handling", "matrix4.txt");
        try {
            SparseMatrixList m1 = SparseMatrix.readMatrixFromFile("testcases/matrix4.txt");
            System.out.print("  |-- Input List     : ");
            m1.displayList();
            System.out.println("  |-- Input Grid     :");
            m1.displayMatrix();
            pass();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    private static void runTest5() {
        printTestHeader(5, "Zero Cancellation Addition", "matrix1.txt, matrix5.txt");
        try {
            SparseMatrixList m1 = SparseMatrix.readMatrixFromFile("testcases/matrix1.txt");
            SparseMatrixList m2 = SparseMatrix.readMatrixFromFile("testcases/matrix5.txt");

            System.out.print("  |-- Matrix 1 List  : ");
            m1.displayList();
            System.out.println("  |-- Matrix 1 Grid  :");
            m1.displayMatrix();

            System.out.print("\n  |-- Matrix 5 List  : ");
            m2.displayList();
            System.out.println("  |-- Matrix 5 Grid  :");
            m2.displayMatrix();

            SparseMatrixList result = SparseMatrix.addMatrices(m1, m2);
            System.out.print("\n  |-- Result List    : ");
            result.displayList();
            System.out.println("  |-- Result Grid    :");
            result.displayMatrix();

            if (result.countNodes() == 0) pass(); else fail("Cancellation failed, nodes remain.");
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    private static void runTest6() {
        printTestHeader(6, "Very Sparse Structure Check", "matrix6.txt");
        try {
            SparseMatrixList m1 = SparseMatrix.readMatrixFromFile("testcases/matrix6.txt");
            System.out.print("  |-- Input List     : ");
            m1.displayList();
            System.out.println("  |-- Input Grid     :");
            m1.displayMatrix();
            System.out.println("  |-- Nodes Stored   : " + m1.countNodes());
            if (m1.countNodes() <= 2) pass(); else fail("Too many nodes stored.");
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    private static void runTest7() {
        printTestHeader(7, "Boundary: Inconsistent Dimensions", "matrix1.txt, matrix7.txt");
        try {
            SparseMatrixList m1 = SparseMatrix.readMatrixFromFile("testcases/matrix1.txt");
            SparseMatrixList m2 = SparseMatrix.readMatrixFromFile("testcases/matrix7.txt");

            System.out.print("  |-- Matrix 1 List  : ");
            m1.displayList();
            System.out.println("  |-- Matrix 1 Grid  :");
            m1.displayMatrix();

            System.out.print("\n  |-- Matrix 7 List  : ");
            m2.displayList();
            System.out.println("  |-- Matrix 7 Grid  :");
            m2.displayMatrix();

            SparseMatrix.addMatrices(m1, m2);
            fail("Exception was not thrown.");
        } catch (InconsistentDimensionException e) {
            System.out.println("\n  |-- Caught  : " + e.getMessage());
            pass();
        } catch (Exception e) {
            fail("Wrong exception thrown.");
        }
    }

    private static void runTest8() {
        printTestHeader(8, "Boundary: Invalid File Format", "matrix_badformat.txt");
        try {
            SparseMatrix.readMatrixFromFile("testcases/matrix_badformat.txt");
            fail("Exception was not thrown.");
        } catch (InvalidFormatException e) {
            System.out.println("  |-- Caught  : " + e.getMessage());
            pass();
        } catch (Exception e) {
            fail("Wrong exception thrown.");
        }
    }

    private static void runTest9() {
        printTestHeader(9, "Boundary: Missing File", "missing_file.txt");
        try {
            SparseMatrix.readMatrixFromFile("missing_file.txt");
            fail("Exception was not thrown.");
        } catch (MissingFileException e) {
            System.out.println("  |-- Caught  : " + e.getMessage());
            pass();
        } catch (Exception e) {
            fail("Wrong exception thrown.");
        }
    }

    private static void runTest10() {
        printTestHeader(10, "Single Matrix Input (N=1)", "matrix1.txt");
        try {
            SparseMatrixList m1 = SparseMatrix.readMatrixFromFile("testcases/matrix1.txt");
            System.out.print("  |-- Input List     : ");
            m1.displayList();
            System.out.println("  |-- Input Grid     :");
            m1.displayMatrix();
            pass();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}