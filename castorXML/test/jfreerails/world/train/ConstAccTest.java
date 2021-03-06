/*
 * Created on 09-Jul-2005
 *
 */
package jfreerails.world.train;

import java.util.Random;

import junit.framework.TestCase;

public class ConstAccTest extends TestCase {

    public void testTandS() {
        SpeedAgainstTime acc1 = ConstAcc.uat(0, 10, 5);
        double s = acc1.getS();
        SpeedAgainstTime acc2 = ConstAcc.uas(0, 10, s);
        assertEquals(acc1, acc2);

        acc1 = ConstAcc.uat(10, 0, 5);
        assertEquals(50, acc1.getS(), 0.00001);
        acc2 = ConstAcc.uas(10, 0, acc1.getS());
        assertEquals(acc1, acc2);

    }

    public void testEquals() {
        SpeedAgainstTime acc1 = ConstAcc.uat(0, 10, 4);
        SpeedAgainstTime acc2 = ConstAcc.uat(0, 10, 4);
        assertEquals(acc1, acc2);
    }

    public void testContract() {
        Random r = new Random(88);
        for (int i = 0; i < 1000; i++) {
            ConstAcc acc1 = ConstAcc.uat(r.nextDouble(), r.nextDouble(), r
                    .nextDouble());
            checkContract(acc1);
            ConstAcc acc2 = ConstAcc.uas(r.nextDouble(), r.nextDouble(), r
                    .nextDouble());
            checkContract(acc2);
        }
    }

    /**
     * Checks the specified object satisfies the contract defined by the
     * interface SpeedAgainstTime.
     */
    public static void checkContract(SpeedAgainstTime sat) {
        double s = sat.getS();
        double ulps = Math.ulp(s);
        double t = sat.getT();
        double ulpt = Math.ulp(t);

        // Check calcS()
        checkCalcSCalcVandCalcA(sat, 0 - Double.MIN_VALUE);
        checkCalcSCalcVandCalcA(sat, t + ulpt);
        checkCalcSCalcVandCalcA(sat, 0 + Double.MIN_VALUE);
        checkCalcSCalcVandCalcA(sat, t - ulpt);
        for (double d = 0; d < 1d; d += 0.1d) {
            checkCalcSCalcVandCalcA(sat, t * d);
        }
        double actualS = sat.calcS(0);
        assertEquals(0d, actualS);
        actualS = sat.calcS(t);
        assertEquals(s, actualS);

        // Check calcT()
        checkCalcT(sat, 0 - Double.MIN_VALUE);
        checkCalcT(sat, s + ulps);
        checkCalcT(sat, 0 + Double.MIN_VALUE);
        checkCalcT(sat, s - ulps);
        for (double d = 0; d < 1d; d += 0.1d) {
            checkCalcT(sat, s * d);
        }
        double actualT = sat.calcT(0);
        assertEquals(0d, actualT);
        actualT = sat.calcT(s);
        assertEquals(t, actualT);

    }

    private static void checkCalcSCalcVandCalcA(SpeedAgainstTime sat, double t) {
        boolean exceptionExpected = (t < 0) || (t > sat.getT());
        try {
            double actualS = sat.calcS(t);
            assertTrue(actualS >= 0);
            assertTrue(actualS <= sat.getS());
            assertFalse(exceptionExpected);
        } catch (IllegalArgumentException e) {
            assertTrue(exceptionExpected);
        }
        // Also check getV and getA
        try {
            double v = sat.calcV(t);
            assertTrue(v >= 0);
            assertFalse(exceptionExpected);
        } catch (IllegalArgumentException e) {
            assertTrue(exceptionExpected);
        }
        try {
            sat.calcA(t);
            assertFalse(exceptionExpected);
        } catch (IllegalArgumentException e) {
            assertTrue(exceptionExpected);
        }

    }

    private static void checkCalcT(SpeedAgainstTime sat, double s) {
        boolean exceptionExpected = (s < 0) || (s > sat.getS());
        try {
            double actualT = sat.calcT(s);
            assertTrue(actualT >= 0);
            assertTrue(actualT <= sat.getT());
            assertFalse(exceptionExpected);
        } catch (IllegalArgumentException e) {
            assertTrue(exceptionExpected);
        }
    }

}
