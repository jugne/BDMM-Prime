package bdmm.util;

import java.util.Arrays;

/**
 * @author dkuh004
 *         Date: May 28, 2012
 *         Time: 11:25:01 AM
 */
public class Utils {

    /**
     * Finds the index of the time interval t lies in.  Note that if t
     * lies on a boundary between intervals, the interval returned will be
     * the _earlier_ of these two intervals.
     *
     * @param t time for which to identify interval
     * @param times interval start times
     * @return index identifying interval.
     */
    public static int getIntervalIndex(double t, double[] times) {

        int index = Arrays.binarySearch(times, t);

        if (index < 0)
            index = -index - 2;
        else
            index -= 1;

        // return at most the index of the last interval (m-1)
        return Math.max(0, Math.min(index, times.length-1));
    }

    /**
     * In-place reversal of array of (little d) doubles.
     * @param array array to reverse
     */
    public static void reverseDoubleArray(double[] array) {
        double tmp;
        for (int i=0; i<array.length/2; i++) {
            tmp = array[i];
            array[i] = array[array.length - 1 - i];
            array[array.length - 1 - i] = tmp;
        }
    }

    /**
     * In-place reversal of an array.
     * @param array array to reverse.
     * @param <T> type of elements in array
     */
    public static <T> void reverseArray(T[] array) {
        T tmp;
        for (int i=0; i<array.length/2; i++) {
            tmp = array[i];
            array[i] = array[array.length - 1 - i];
            array[array.length - 1 - i] = tmp;
        }
    }
}
