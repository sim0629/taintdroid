package cms.snu.edu.tainttest.source;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.Browser;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

public class DataSource {

    /**
     * https://gist.github.com/evandrix/7058235
     * http://www.higherpass.com/android/tutorials/working-with-android-contacts/
     * @return
     */
    public static String getContact(Context ctx) {
        final String TAG = "CONTACT";
        ContentResolver cr = ctx.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        String result = null;
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String contactID = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                // Using the contact ID now we will get contact phone number
                Cursor cursorPhone = ctx.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},

                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                                ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                                ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,

                        new String[]{contactID},
                        null);

                String contactNumber = null;

                if (cursorPhone.moveToFirst()) {
                    contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                }

                cursorPhone.close();

                result = contactNumber;
                Log.i(TAG, "contactID: " + contactID + ", name: " + name + ", phoneNumber: " + contactNumber);
                Toast.makeText(ctx, "Get Contact Data!", Toast.LENGTH_LONG).show();
            }
        }
        cur.close();
        return result;
    }


    public static String getBrowserBookmark(Activity activity) {
        final String TAG = "getBrowserBookmark";
        String result = null;
        String title = null;
        String url = null;

        String[] projection = new String[] {
                Browser.BookmarkColumns.TITLE
                , Browser.BookmarkColumns.URL
        };
        Cursor mCur = activity.managedQuery(android.provider.Browser.BOOKMARKS_URI,
                projection, null, null, null
        );
        mCur.moveToFirst();

        int titleIdx = mCur.getColumnIndex(Browser.BookmarkColumns.TITLE);
        int urlIdx = mCur.getColumnIndex(Browser.BookmarkColumns.URL);
        while (mCur.isAfterLast() == false) {
            title = mCur.getString(titleIdx);
            url = mCur.getString(urlIdx);
            result = url;
            mCur.moveToNext();
        }

        Log.i(TAG, "title: " + title + ", url: " + url);
        return result;
    }
}
