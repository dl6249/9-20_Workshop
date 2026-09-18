import java.util.Arrays;

public class SafeWindSensor {

    /**
     * A single wind speed reading captured by a sensor.
     */
    public static class WindReading {
        private double speedKnots;

        public WindReading(double speedKnots) {
            this.speedKnots = speedKnots;
        }

        public double getSpeedKnots() {
            return speedKnots;
        }

        @Override
        public String toString() {
            return "WindReading{" + speedKnots + " knots}";
        }
    }

    /**
     * Replaces any bad primary reading (speed strictly below
     * minimumWindSpeed) by swapping in the corresponding backup
     * WindReading object, mutating the primaryReadings array in place.
     * Readings that are already plausible keep the exact same
     * WindReading instance they started with.
     *
     * @param primaryReadings  primary sensor readings (must not be null,
     * and must not contain null elements); this
     * array is mutated and returned
     * @param backupReadings   backup sensor readings (must not be null,
     * must not contain null elements, and must
     * be the same length as primaryReadings)
     * @param minimumWindSpeed threshold "a" -- readings whose speed is
     * strictly below this are considered sensor failures
     * @return the same array instance passed in as primaryReadings, with
     * any implausible readings' object references replaced by
     * the corresponding backup WindReading object references
     */
    public static WindReading[] correctReadings(WindReading[] primaryReadings,
        WindReading[] backupReadings, double minimumWindSpeed) {
       // Your code here
       for (int i = 0; i < primaryReadings.length; i++) {
            if (primaryReadings[i].getSpeedKnots() < minimumWindSpeed) primaryReadings[i] = backupReadings[i];
       }

       return primaryReadings;
    }

    // -------------------------------------------------------------------
    // Tests
    // -------------------------------------------------------------------

    private static int testsRun = 0;
    private static int testsPassed = 0;

    public static void main(String[] args) {
        testRandomReadings();
        testAllReadingsAreCorrectKeepsOriginalInstances();
        testAllReadingsAreFailuresSwapsEveryInstance();
        testEmptyArrays();
        testSingleReadingBelowThresholdIsSwapped();
        testSingleReadingAboveThresholdKeepsInstance();
        testBoundaryValueEqualToThresholdIsCorrect();
        testNegativeReadings();
        testIntegerLikeThresholdWithWholeNumberReadings();

        System.out.println();
        System.out.println(testsPassed + " / " + testsRun + " tests passed.");
        if (testsPassed != testsRun) {
            System.exit(1);
        }
    }

    private static void testRandomReadings() {
        WindReading[] primary = readings(2.5, 6.0, 1.0, 8.2, 0.5);
        WindReading[] backup = readings(4.0, 6.1, 3.3, 8.0, 2.9);
        double minimumWindSpeed = 3.0;
        double[] expectedSpeeds = {4.0, 6.0, 3.3, 8.2, 2.9};
        boolean[] expectedSwapped = {true, false, true, false, true};

        checkSpeedsAndIdentity("Random mix: only failed readings are swapped",
                primary, backup, minimumWindSpeed, expectedSpeeds, expectedSwapped);
    }

    private static void testAllReadingsAreCorrectKeepsOriginalInstances() {
        WindReading[] primary = readings(5.0, 6.0, 7.5);
        WindReading[] backup = readings(0.0, 0.0, 0.0);
        double minimumWindSpeed = 3.0;
        double[] expectedSpeeds = {5.0, 6.0, 7.5};
        boolean[] expectedSwapped = {false, false, false};

        checkSpeedsAndIdentity("No readings below minimum -> original instances kept",
                primary, backup, minimumWindSpeed, expectedSpeeds, expectedSwapped);
    }

    private static void testAllReadingsAreFailuresSwapsEveryInstance() {
        WindReading[] primary = readings(1.0, 2.0, 0.5);
        WindReading[] backup = readings(9.0, 9.5, 9.9);
        double minimumWindSpeed = 3.0;
        double[] expectedSpeeds = {9.0, 9.5, 9.9};
        boolean[] expectedSwapped = {true, true, true};

        checkSpeedsAndIdentity("All readings below minimum -> every instance swapped",
                primary, backup, minimumWindSpeed, expectedSpeeds, expectedSwapped);
    }

    private static void testEmptyArrays() {
        WindReading[] primary = readings();
        WindReading[] backup = readings();
        double minimumWindSpeed = 3.0;

        checkSpeedsAndIdentity("Empty arrays produce empty result",
                primary, backup, minimumWindSpeed, new double[] {}, new boolean[] {});
    }

    private static void testSingleReadingBelowThresholdIsSwapped() {
        WindReading[] primary = readings(1.0);
        WindReading[] backup = readings(7.0);
        double minimumWindSpeed = 3.0;

        checkSpeedsAndIdentity("Single reading below minimum is swapped",
                primary, backup, minimumWindSpeed, new double[] {7.0}, new boolean[] {true});
    }

    private static void testSingleReadingAboveThresholdKeepsInstance() {
        WindReading[] primary = readings(5.0);
        WindReading[] backup = readings(7.0);
        double minimumWindSpeed = 3.0;

        checkSpeedsAndIdentity("Single reading above minimum keeps original instance",
                primary, backup, minimumWindSpeed, new double[] {5.0}, new boolean[] {false});
    }

    private static void testBoundaryValueEqualToThresholdIsCorrect() {
        WindReading[] primary = readings(3.0, 2.999, 3.001);
        WindReading[] backup = readings(99.0, 99.0, 99.0);
        double minimumWindSpeed = 3.0;
        // A reading exactly equal to the minimum is still considered correct.
        double[] expectedSpeeds = {3.0, 99.0, 3.001};
        boolean[] expectedSwapped = {false, true, false};

        checkSpeedsAndIdentity("Value exactly equal to minimum is NOT swapped (only strictly-less-than fails)",
                primary, backup, minimumWindSpeed, expectedSpeeds, expectedSwapped);
    }

    private static void testNegativeReadings() {
        WindReading[] primary = readings(-5.0, -1.0, -10.0);
        WindReading[] backup = readings(4.0, 1.0, 2.0);
        double minimumWindSpeed = 0.0;
        double[] expectedSpeeds = {4.0, 1.0, 2.0};
        boolean[] expectedSwapped = {true, true, true};

        checkSpeedsAndIdentity("Negative readings and negative minimum handled correctly",
                primary, backup, minimumWindSpeed, expectedSpeeds, expectedSwapped);
    }

    private static void testIntegerLikeThresholdWithWholeNumberReadings() {
        // Demonstrates the method works fine with "int"-style whole-number
        // values too, since an int minimum widens to double.
        WindReading[] primary = readings(1.0, 4.0, 2.0, 5.0);
        WindReading[] backup = readings(10.0, 10.0, 10.0, 10.0);
        int minimumWindSpeed = 3;
        double[] expectedSpeeds = {10.0, 4.0, 10.0, 5.0};
        boolean[] expectedSwapped = {true, false, true, false};

        checkSpeedsAndIdentity("Works correctly with an int-valued minimum wind speed",
                primary, backup, minimumWindSpeed, expectedSpeeds, expectedSwapped);
    }

    // -------------------------------------------------------------------
    // Test helpers
    // -------------------------------------------------------------------

    private static WindReading[] readings(double... speeds) {
        WindReading[] result = new WindReading[speeds.length];
        for (int i = 0; i < speeds.length; i++) {
            result[i] = new WindReading(speeds[i]);
        }
        return result;
    }

    /**
     * Runs correctReadings, then checks both:
     *   1. the resulting speeds match expectedSpeeds, and
     *   2. each slot holds the correct OBJECT IDENTITY: the original
     *      primary instance if expectedSwapped[i] is false, or the
     *      original backup instance if expectedSwapped[i] is true.
     */
    private static void checkSpeedsAndIdentity(String testName,
                                                WindReading[] primary,
                                                WindReading[] backup,
                                                double minimumWindSpeed,
                                                double[] expectedSpeeds,
                                                boolean[] expectedSwapped) {
        testsRun++;

        WindReading[] originalPrimaryRefs = Arrays.copyOf(primary, primary.length);
        WindReading[] originalBackupRefs = Arrays.copyOf(backup, backup.length);

        WindReading[] result = correctReadings(primary, backup, minimumWindSpeed);

        StringBuilder failures = new StringBuilder();

        if (result.length != expectedSpeeds.length) {
            failures.append("length mismatch (expected ").append(expectedSpeeds.length)
                    .append(", got ").append(result.length).append("); ");
        } else {
            for (int i = 0; i < result.length; i++) {
                if (result[i].getSpeedKnots() != expectedSpeeds[i]) {
                    failures.append("index ").append(i).append(" speed expected ")
                            .append(expectedSpeeds[i]).append(" but was ")
                            .append(result[i].getSpeedKnots()).append("; ");
                }
                WindReading expectedInstance = expectedSwapped[i] ? originalBackupRefs[i] : originalPrimaryRefs[i];
                if (result[i] != expectedInstance) {
                    failures.append("index ").append(i)
                            .append(expectedSwapped[i]
                                    ? " expected the backup object instance to be swapped in but it wasn't; "
                                    : " expected the original primary object instance to be kept but it was replaced; ");
                }
            }
        }

        if (failures.length() == 0) {
            System.out.println("PASS: " + testName);
            testsPassed++;
        } else {
            System.out.println("FAIL: " + testName + " | " + failures);
        }
    }
}