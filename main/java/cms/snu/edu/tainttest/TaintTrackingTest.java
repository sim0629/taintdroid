package cms.snu.edu.tainttest;

import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import dalvik.system.Taint;

public class TaintTrackingTest {
    private static final String TAG = "ArrayElemTrackingTest";
    private static boolean testResult = true;
    private static int failCount = 0;
    private static String data0;
    private static String data1;

    private final Context mContext;

    public TaintTrackingTest(Context context) {
        this.mContext = context;
        testResult = true;
        failCount = 0;

        /* prepare tainted data */
        data0 = "1234";
        Taint.addTaintString(data0, Taint.TAINT_CONTACTS);
        Log.i(TAG, "String1: " + Taint.getTaintString(data0));

        data1 = "5678";
        Taint.addTaintString(data1, Taint.TAINT_HISTORY);
        Log.i(TAG, "String2: " + Taint.getTaintString(data1));
    }

    public static void localTest() {
        stringTest();
        stringToCharTest();
        stringCharGrainedTest();

        stringArrayTest();
        booleanArrayTest(data0, data1);
        charArrayTest();
        byteArrayTest();
        intArrayTest(data0, data1);
        shortArrayTest(data0, data1);
        longArrayTest(data0, data1);
        floatArrayTest(data0, data1);
        doubleArrayTest();

        if(testResult) {
            Log.i(TAG, "Test successes for all cases.");
        }
        else {
            Log.i(TAG, "Test fails for " + failCount + " cases.");
        }
    }

    public void tmpTest() {


        char[] a = TaintDataFactory.getTaintedCharArray();

        Log.w(TAG, "Taint of a: " + Taint.getTaintCharArray(a));
        Log.w(TAG, "Taint of a[0]: " + Taint.getTaintChar(a[0]));
        Log.w(TAG, "Taint of a[1]: " + Taint.getTaintChar(a[1]));
        Log.w(TAG, "a[0]: " + a[0]);
        Log.w(TAG, "a[1]: " + a[1]);

        Parcel p = Parcel.obtain();

        p.writeCharArray(a);

        p.setDataPosition(0);
        char[] b = p.createCharArray();

        Log.w(TAG, "Taint of b: " + Taint.getTaintCharArray(b));
        Log.w(TAG, "Taint of b[0]: " + Taint.getTaintChar(b[0]));
        Log.w(TAG, "Taint of b[1]: " + Taint.getTaintChar(b[1]));
        Log.w(TAG, "b[0]: " + b[0]);
        Log.w(TAG, "b[1]: " + b[1]);

        p.recycle();

        Log.i(TAG, "temptest finish");
    }

    private static void tempTest() {

        String a = "a";
        String b = "b";
        String c = "c";

        Taint.addTaintString(a, Taint.TAINT_CAMERA);
        Taint.addTaintString(b, Taint.TAINT_CONTACTS);

        Log.i(TAG, "String" + Taint.getTaintString(a));
        Log.i(TAG, "String" + Taint.getTaintString(b));
        Log.i(TAG, "String" + Taint.getTaintString(c));
        String d = a+b+c;
        Log.i(TAG, "String" + Taint.getTaintString(d));


        int test = 5;
        test = Taint.addTaintInt(test, Taint.TAINT_ICCID);
        Integer te = new Integer(test);

        Log.i(TAG, "test" + Taint.getTaintInt(test));
        Log.i(TAG, "getRef: " + Taint.getTaintRef(te));

        String strArr[] = new String[2];
        strArr[0] = data0;
        strArr[1] = data1;
        Taint.addTaintObjectArray(strArr, Taint.TAINT_CAMERA);

        Log.i(TAG, "strArr: " + Taint.getTaintRef(strArr));
        Log.i(TAG, "strArr: " + Taint.getTaintObjectArray(strArr));

        Log.i(TAG, "strArr[0]: " + Taint.getTaintRef(strArr[0]));
        Log.i(TAG, "strArr[0]: " + Taint.getTaintString(strArr[0]));
        Log.i(TAG, "strArr[1]: " + Taint.getTaintRef(strArr[1]));
        Log.i(TAG, "strArr[1]: " + Taint.getTaintString(strArr[1]));
    }

    private static void tempTest2(String str1, String str2) {
        int taint1 = Taint.getTaintString(str1);
        int taint2 = Taint.getTaintString(str2);

        /* extract integers from tainted strings */
        Integer integer1 = Integer.parseInt(str1);
        Integer integer2 = Integer.parseInt(str2);
        Log.i(TAG, "integer1: " + Taint.getTaintInt(integer1));
        Log.i(TAG, "integer1: " + Taint.getTaintRef(integer1));
        Log.i(TAG, "integer2: " + Taint.getTaintInt(integer2));
        Log.i(TAG, "integer2: " + Taint.getTaintRef(integer2));

        /* make an array with the tainted data */
        Integer integerArr[] = new Integer[2];
        Taint.addTaintObjectArray(integerArr, Taint.TAINT_CAMERA);
        integerArr[0] = integer1;
        integerArr[1] = integer2;

        Log.i(TAG, "integerArr: " + Taint.getTaintRef(integerArr));
        Log.i(TAG, "integerArr: " + Taint.getTaintObjectArray(integerArr));

        Log.i(TAG, "integerArr[0]: " + Taint.getTaintRef(integerArr[0]));
        Log.i(TAG, "integerArr[0]: " + Taint.getTaintInt(integerArr[0]));
        Log.i(TAG, "integerArr[1]: " + Taint.getTaintRef(integerArr[1]));
        Log.i(TAG, "integerArr[1]: " + Taint.getTaintInt(integerArr[1]));
    }

    public static void stringTest() {
        String str1 = "abcd";

        int taint1 = Taint.TAINT_CAMERA;
        Taint.addTaintString(str1, taint1);

        handleCaseResult("substring", Taint.getTaintString(str1.substring(2)), taint1);

        char char1 = 'c';
        int taint2 = Taint.TAINT_ACCELEROMETER;
        char1 = Taint.addTaintChar(char1, taint2);

        String temp = str1 + char1;

        handleCaseResult("stringCharConcat", Taint.getTaintString(str1 + char1), (taint1 | taint2));

        String str2 = new String("" + char1);

        handleCaseResult("stringInstanceFromChar", Taint.getTaintString(str2), taint2);

        String str3 = "sample";
        int taint3 = Taint.TAINT_ACCOUNT;
        Taint.addTaintString(str3, taint3);
        temp = str1 + str3;
        handleCaseResult("stringConcat", Taint.getTaintString(temp), (taint1 | taint3));
    }

    public static void stringToCharTest() {
        String str = "abcdefg";

        int taint = Taint.TAINT_HISTORY;
        Taint.addTaintString(str, taint);

        char char1 = str.charAt(0);
        char char2 = str.charAt(3);

        handleCaseResult("String.charAt(0)", Taint.getTaintChar(char1), taint);
        handleCaseResult("String.charAt(3)", Taint.getTaintChar(char2), taint);
    }

    /**
     * Currently this test should fail
     */
    public static void stringCharGrainedTest() {
        String str = "abcdefg";

        int taint = Taint.TAINT_HISTORY;
        Taint.addTaintString(str, taint);

        String temp = str + "clean";

        char char3 = temp.charAt(10);

        handleCaseResult("String char-grained tracking", Taint.getTaintChar(char3), Taint.TAINT_CLEAR);
    }

    public void fileTest() {
        String str = "abcdefghijk";

        Taint.addTaintString(str, Taint.TAINT_CAMERA);

        //save string to File
        writeToFile(str);

        //load string from File.
        String loadStr = readFromFile();

        handleCaseResult("fileTest", Taint.getTaintString(loadStr), Taint.TAINT_CAMERA);
    }

    private void writeToFile(String data) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(mContext.openFileOutput("taintFILE", Context.MODE_PRIVATE) );
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String readFromFile() {
        String ret = "";

        InputStream inputStream = null;
        try {
            inputStream = mContext.openFileInput("taintFILE");

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String recvString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while( (recvString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(recvString);
                }

                inputStream.close();
                ret = stringBuilder.toString();

            }
        } catch (FileNotFoundException e) {
            Log.e(TAG, "file not found");
        } catch (IOException e) {
            Log.e(TAG, "exception while reading");
        }
        return ret;
    }

    public void ipcTest() {
        Intent launchIntent = mContext.getPackageManager().getLaunchIntentForPackage("edu.snu.cms.taintreceiver");
        launchIntent.putExtra("byte", Taint.addTaintByte((byte)'c', Taint.TAINT_CAMERA));
        launchIntent.putExtra("char", Taint.addTaintChar('c', Taint.TAINT_CAMERA));
        launchIntent.putExtra("string", TaintDataFactory.getTaintedString());

        launchIntent.putExtra("byteArray", TaintDataFactory.getTaintedByteArray());
        launchIntent.putExtra("booleanArray", TaintDataFactory.getTaintedBooleanArray());
        launchIntent.putExtra("charArray", TaintDataFactory.getTaintedCharArray());
        launchIntent.putExtra("longArray", TaintDataFactory.getTaintedLongArray());
        launchIntent.putExtra("intArray", TaintDataFactory.getTaintedIntArray());
        launchIntent.putExtra("floatArray", TaintDataFactory.getTaintedFloatArray());
        launchIntent.putExtra("doubleArray", TaintDataFactory.getTaintedDoubleArray());
        launchIntent.putExtra("stringArray", TaintDataFactory.getTaintedStringArray());
        Log.i(TAG, "sending intent");
        mContext.startActivity(launchIntent);
    }

    public static void stringArrayTest() {
        String str1 = "a";
        String str2 = "b";

        int taint1 = Taint.TAINT_CAMERA;
        int taint2 = Taint.TAINT_ACCOUNT;
        Taint.addTaintString(str1, taint1);
        Taint.addTaintString(str2, taint2);

        /* make an array with the tainted data */
        String stringArr[] = {str1, str2};

        handleCaseResult("stringArray", Taint.getTaintObjectArray(stringArr), (Taint.TAINT_CLEAR));
        handleCaseResult("stringArray[0]", Taint.getTaintString(stringArr[0]), (taint1));
        handleCaseResult("stringArray[1]", Taint.getTaintString(stringArr[1]), (taint2));
    }

    public static void booleanArrayTest(String str1, String str2) {
        int taint1 = Taint.getTaintString(str1);
        int taint2 = Taint.getTaintString(str2);

        /* extract booleans from tainted strings */
        boolean boolean1 = Boolean.parseBoolean(str1);
        boolean boolean2 = Boolean.parseBoolean(str2);
        Log.i(TAG, "boolean1: " + Taint.getTaintBoolean(boolean1));
        Log.i(TAG, "boolean2: " + Taint.getTaintBoolean(boolean2));

        /* make an array with the tainted data */
        boolean booleanArr[] = {boolean1, boolean2};

        handleCaseResult("booleanArray", Taint.getTaintBooleanArray(booleanArr), (taint1 | taint2));
        handleCaseResult("booleanArray[0]", Taint.getTaintBoolean(booleanArr[0]), (taint1));
        handleCaseResult("booleanArray[1]", Taint.getTaintBoolean(booleanArr[1]), (taint2));
    }

    public static void charArrayTest() {
        char char1 = 'a';
        char char2 = 'b';

        char1 = Taint.addTaintChar(char1, Taint.TAINT_CONTACTS);
        char2 = Taint.addTaintChar(char2, Taint.TAINT_HISTORY);

        int taint1 = Taint.getTaintChar(char1);
        int taint2 = Taint.getTaintChar(char2);

        Log.i(TAG, "char1: " + taint1);
        Log.i(TAG, "char2: " + taint2);

        char charArr[] = {char1, char2};

        /* make an array with the tainted data */
        handleCaseResult("charArray", Taint.getTaintCharArray(charArr), (taint1 | taint2));
        handleCaseResult("charArray[0]", Taint.getTaintChar(charArr[0]), (taint1));
        handleCaseResult("charArray[1]", Taint.getTaintChar(charArr[1]), (taint2));
    }

    public static void byteArrayTest() {
        byte byte1 = 'b';
        byte byte2 = 'b';

        byte1 = Taint.addTaintByte(byte1, Taint.TAINT_CONTACTS);
        byte2 = Taint.addTaintByte(byte2, Taint.TAINT_HISTORY);

        int taint1 = Taint.getTaintByte(byte1);
        int taint2 = Taint.getTaintByte(byte2);

        Log.i(TAG, "byte1: " + taint1);
        Log.i(TAG, "byte2: " + taint2);

        /* make an array with the tainted data */
        byte byteArr[] = {byte1, byte2};

        handleCaseResult("byteArray", Taint.getTaintByteArray(byteArr), (taint1 | taint2));
        handleCaseResult("byteArray[0]", Taint.getTaintByte(byteArr[0]), (taint1));
        handleCaseResult("byteArray[1]", Taint.getTaintByte(byteArr[1]), (taint2));
    }

    public static void intArrayTest(String str1, String str2) {
        int taint1 = Taint.getTaintString(str1);
        int taint2 = Taint.getTaintString(str2);

        /* extract integers from tainted strings */
        int int1 = Integer.parseInt(str1);
        int int2 = Integer.parseInt(str2);
        Log.i(TAG, "int1: " + Taint.getTaintInt(int1));
        Log.i(TAG, "int2: " + Taint.getTaintInt(int2));

        /* make an array with the tainted data */
        int intArr[] = {int1, int2};

        handleCaseResult("intArray", Taint.getTaintIntArray(intArr), (taint1 | taint2));
        handleCaseResult("intArray[0]", Taint.getTaintInt(intArr[0]), (taint1));
        handleCaseResult("intArray[1]", Taint.getTaintInt(intArr[1]), (taint2));
    }

    public static void shortArrayTest(String str1, String str2) {
        int taint1 = Taint.getTaintString(str1);
        int taint2 = Taint.getTaintString(str2);

        /* extract shorts from tainted strings */
        short short1 = Short.parseShort(str1);
        short short2 = Short.parseShort(str2);
        Log.i(TAG, "short1: " + Taint.getTaintShort(short1));
        Log.i(TAG, "short2: " + Taint.getTaintShort(short2));

        /* make an array with the tainted data */
        short shortArr[] = {short1, short2};

        handleCaseResult("shortArray", Taint.getTaintShortArray(shortArr), (taint1 | taint2));
        handleCaseResult("shortArray[0]", Taint.getTaintShort(shortArr[0]), (taint1));
        handleCaseResult("shortArray[1]", Taint.getTaintShort(shortArr[1]), (taint2));
    }

    public static void longArrayTest(String str1, String str2) {
        int taint1 = Taint.getTaintString(str1);
        int taint2 = Taint.getTaintString(str2);

        /* extract longs from tainted strings */
        long long1 = Long.parseLong(str1);
        long long2 = Long.parseLong(str2);
        Log.i(TAG, "long1: " + Taint.getTaintLong(long1));
        Log.i(TAG, "long2: " + Taint.getTaintLong(long2));

        /* make an array with the tainted data */
        long longArr[] = {long1, long2};

        handleCaseResult("longArray", Taint.getTaintLongArray(longArr), (taint1 | taint2));
        handleCaseResult("longArray[0]", Taint.getTaintLong(longArr[0]), (taint1));
        handleCaseResult("longArray[1]", Taint.getTaintLong(longArr[1]), (taint2));
    }

    public static void floatArrayTest(String str1, String str2) {
        int taint1 = Taint.getTaintString(str1);
        int taint2 = Taint.getTaintString(str2);

        /* extract longs from tainted strings */
        long long1 = Long.parseLong(str1);
        long long2 = Long.parseLong(str2);

        float float1 = (float)long1 * (float)0.43;
        float float2 = (float)long2 * (float)0.43;

        Log.i(TAG, "float1: " + Taint.getTaintFloat(float1));
        Log.i(TAG, "float2: " + Taint.getTaintFloat(float2));

        /* make an array with the tainted data */
        float floatArr[] = {float1, float2};

        handleCaseResult("floatArray", Taint.getTaintFloatArray(floatArr), (taint1 | taint2));
        handleCaseResult("floatArray[0]", Taint.getTaintFloat(floatArr[0]), (taint1));
        handleCaseResult("floatArray[1]", Taint.getTaintFloat(floatArr[1]), (taint2));
    }

    public static void doubleArrayTest() {
        double double1 = 0.24;
        double double2 = 0.35;

        double1 = Taint.addTaintDouble(double1, Taint.TAINT_CONTACTS);
        double2 = Taint.addTaintDouble(double2, Taint.TAINT_HISTORY);

        int taint1 = Taint.getTaintDouble(double1);
        int taint2 = Taint.getTaintDouble(double2);

        Log.i(TAG, "double1: " + taint1);
        Log.i(TAG, "double2: " + taint2);

        /* make an array with the tainted data */
        double doubleArr[] = {double1, double2};

        handleCaseResult("doubleArray", Taint.getTaintDoubleArray(doubleArr), (taint1 | taint2));
        handleCaseResult("doubleArray[0]", Taint.getTaintDouble(doubleArr[0]), (taint1));
        handleCaseResult("doubleArray[1]", Taint.getTaintDouble(doubleArr[1]), (taint2));
    }

    private static void handleCaseResult(String testCase, int result, int answer) {
        if(result == answer) {
            Log.i(TAG, "[SUCCESS - " + testCase + "]" + "\tresult: " + result + ",\tanswer:" + answer);
        } else {
            Log.i(TAG, "[FAIL - " + testCase + "]" + "\tresult: " + result + ",\tanswer:" + answer);
            testResult = false;
            failCount++;
        }
    }
}
