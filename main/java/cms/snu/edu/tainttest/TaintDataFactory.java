package cms.snu.edu.tainttest;

import android.util.Log;

import dalvik.system.Taint;

public class TaintDataFactory {
    private final static String TAG = "TaintDataFactory";

    public static String getTaintedString() {

        String string1 = "test";

        int taint1 = Taint.TAINT_ACCELEROMETER;

        Taint.addTaintString(string1, taint1);

        return string1;
    }

    public static String[] getTaintedStringArray() {
        String string1 = "a";
        String string2 = "b";

        int taint1 = Taint.TAINT_ACCELEROMETER;
        int taint2 = Taint.TAINT_ACCOUNT;

        Taint.addTaintString(string1, taint1);
        Taint.addTaintString(string2, taint2);

        String[] stringArray = {string1, string2};

        return stringArray;
    }

    public static double[] getTaintedDoubleArray() {
        double double1 = (double) 0.5;
        double double2 = (double) 1.5;

        int taint1 = Taint.TAINT_ACCELEROMETER;
        int taint2 = Taint.TAINT_ACCOUNT;

        double1 = Taint.addTaintDouble(double1, taint1);
        double2 = Taint.addTaintDouble(double2, taint2);

        double[] doubleArray = {double1, double2};

        return doubleArray;
    }

    public static float[] getTaintedFloatArray() {
        float float1 = (float) 0.5;
        float float2 = (float) 1.5;

        int taint1 = Taint.TAINT_ACCELEROMETER;
        int taint2 = Taint.TAINT_ACCOUNT;

        float1 = Taint.addTaintFloat(float1, taint1);
        float2 = Taint.addTaintFloat(float2, taint2);

        float[] floatArray = {float1, float2};

        return floatArray;
    }

    public static int[] getTaintedIntArray() {
        int int1 = 0;
        int int2 = 1;

        int taint1 = Taint.TAINT_ACCELEROMETER;
        int taint2 = Taint.TAINT_ACCOUNT;

        int1 = Taint.addTaintInt(int1, taint1);
        int2 = Taint.addTaintInt(int2, taint2);

        int[] intArray = {int1, int2};

        return intArray;
    }

    public static long[] getTaintedLongArray() {
        long long1 = 0;
        long long2 = 1;

        int taint1 = Taint.TAINT_ACCELEROMETER;
        int taint2 = Taint.TAINT_ACCOUNT;

        long1 = Taint.addTaintLong(long1, taint1);
        long2 = Taint.addTaintLong(long2, taint2);

        long[] longArray = {long1, long2};

        return longArray;
    }

    public static char[] getTaintedCharArray() {
        char char1 = 'a';
        char char2 = 'b';

        int taint1 = Taint.TAINT_ACCELEROMETER;
        int taint2 = Taint.TAINT_ACCOUNT;

        char1 = Taint.addTaintChar(char1, taint1);
        char2 = Taint.addTaintChar(char2, taint2);

        char[] charArray = {char1, char2};

        return charArray;
    }

    public static boolean[] getTaintedBooleanArray() {
        boolean bool1 = true;
        boolean bool2 = false;

        int taint1 = Taint.TAINT_ACCELEROMETER;
        int taint2 = Taint.TAINT_ACCOUNT;

        bool1 = Taint.addTaintBoolean(bool1, taint1);
        bool2 = Taint.addTaintBoolean(bool2, taint2);

        boolean[] booleanArray = {bool1, bool2};

        return booleanArray;
    }

    public static byte[] getTaintedByteArray() {
        byte byte1 = 0;
        byte byte2 = 1;

        int taint1 = Taint.TAINT_ACCELEROMETER;
        int taint2 = Taint.TAINT_ACCOUNT;

        byte1 = Taint.addTaintByte(byte1, taint1);
        byte2 = Taint.addTaintByte(byte2, taint2);

        byte[] byteArray = {byte1, byte2};

        return byteArray;
    }

    private static void handleCaseResult(String testCase, int result, int answer) {
        if(result == answer) {
            Log.i(TAG, "[SUCCESS - " + testCase + "]" + "\tresult: " + result + ",\tanswer:" + answer);
        } else {
            Log.i(TAG, "[FAIL - " + testCase + "]" + "\tresult: " + result + ",\tanswer:" + answer);
        }
    }
}
