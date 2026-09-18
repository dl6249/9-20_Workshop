import java.util.Arrays;

public class MazeSearch {

    private static final String BREADCRUMB = "Breadcrumb";
    private static final String NOTHING = "NOTHING";

    /**
     * Searches through the given maze from (0, 0) to find the number of
     * bread crump positions in the maze. Breadcrumbs will always be within a
     * cardinal direction of each other.
     * Possible positions:
     * "BREADCRUMB" - breadcrumb to follow and take
     * "NOTHING" - no breadcrumb
     *
     * @param map 2D array indicating the value at each tile.
     * @param breadcrumbs int for the number of breadcrumbs in the maze
     * @return 2D array containing the positions moved to while collecting
     * all the breadcrumbs. (Stop once there are no more breadcrumbs on the path)
     * Each entry in the 2D array should be formatted as [row, col].
     */
    public static int[][] search(String[][] map, int breadcrumbs) {
        // Your code here
        
    }

    // -------------------------------------------------------------------
    // Tests
    // -------------------------------------------------------------------
    
    private static int testsRun = 0;
    private static int testsPassed = 0;

    public static void main(String[] args) {
        testStraightLine();
        testTurn();
        testSShape();
        testNoBreadcrumbs();
        testHookShape();

        System.out.println();
        System.out.println(testsPassed + " / " + testsRun + " tests passed.");
        if (testsPassed != testsRun) {
            System.exit(1);
        }
    }

    private static void testStraightLine() {
        String[][] map = {
                {BREADCRUMB, BREADCRUMB, BREADCRUMB, BREADCRUMB},
                {NOTHING, NOTHING, NOTHING, NOTHING},
        };
        int[][] expected = {{0, 0}, {0, 1}, {0, 2}, {0, 3}};
 
        check("Straight line of breadcrumbs is followed to the end",
                expected, search(map, expected.length));
    }
 
    private static void testTurn() {
        String[][] map = {
                {BREADCRUMB, BREADCRUMB, BREADCRUMB},
                {NOTHING, NOTHING, BREADCRUMB},
                {NOTHING, NOTHING, BREADCRUMB},
        };
        int[][] expected = {{0, 0}, {0, 1}, {0, 2}, {1, 2}, {2, 2}};
 
        check("Trail with a single turn (L-shape) is followed correctly",
                expected, search(map, expected.length));
    }
 
    private static void testSShape() {
        String[][] map = {
                {BREADCRUMB, NOTHING, NOTHING},
                {BREADCRUMB, NOTHING, NOTHING},
                {BREADCRUMB, BREADCRUMB, BREADCRUMB},
                {NOTHING, NOTHING, BREADCRUMB},
                {BREADCRUMB, BREADCRUMB, BREADCRUMB},
        };
        int[][] expected = {
                {0, 0}, {1, 0}, {2, 0}, {2, 1}, {2, 2}, {3, 2}, {4, 2}, {4, 1}, {4, 0}
        };
 
        check("S-shaped trail with 3 turns is followed correctly",
                expected, search(map, expected.length));
    }
 
    private static void testNoBreadcrumbs() {
        String[][] map = {
                {NOTHING, NOTHING, NOTHING},
                {NOTHING, NOTHING, NOTHING},
                {NOTHING, NOTHING, NOTHING},
        };
        int[][] expected = {};
 
        check("Maze with no breadcrumb at the start returns an empty path",
                expected, search(map, expected.length));
    }

    private static void testHookShape() {
        String[][] map = {
                {BREADCRUMB, BREADCRUMB, BREADCRUMB},
                {NOTHING, NOTHING, BREADCRUMB},
                {BREADCRUMB, NOTHING, BREADCRUMB},
                {BREADCRUMB, NOTHING, BREADCRUMB},
                {BREADCRUMB, BREADCRUMB, BREADCRUMB},
        };
        int[][] expected = {
                {0, 0}, {0, 1}, {0, 2}, {1, 2}, {2, 2}, {3, 2}, {4, 2}, {4, 1}, {4, 0}, {3, 0}, {2, 0}
        };
 
        check("Hook-shaped trail is followed correctly",
                expected, search(map, expected.length));
    }
 
    // -------------------------------------------------------------------
    // Test helper
    // -------------------------------------------------------------------
 
    private static void check(String testName, int[][] expected, int[][] actual) {
        testsRun++;
        if (Arrays.deepEquals(expected, actual)) {
            System.out.println("PASS: " + testName);
            testsPassed++;
        } else {
            System.out.println("FAIL: " + testName
                    + " | expected=" + Arrays.deepToString(expected)
                    + " actual=" + Arrays.deepToString(actual));
        }
    }
}