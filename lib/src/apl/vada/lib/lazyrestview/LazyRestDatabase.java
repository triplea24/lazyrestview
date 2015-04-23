package apl.vada.lib.lazyrestview;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import android.content.Context;

public class LazyRestDatabase {
	private final static String password = "12345";

	private SQLiteDatabase database;
	private final int threshold = 50;
	private int length;

	public LazyRestDatabase(Context context) {
		LazyRestView.log("Create Databasse ");
		SQLiteDatabase.loadLibs(context);
		database = SQLiteDatabase.openOrCreateDatabase(
				context.getDatabasePath("lrv.db").toString(), password, null);
		String createSQLCommand = "CREATE TABLE IF NOT EXISTS response " + "("
				+ "response_id INTEGER PRIMARY KEY,"
				+ "response_url TEXT UNIQUE," + "response_json TEXT" + ")";
		database.execSQL(createSQLCommand);
		String countSQLCommand = "SELECT count(*) FROM response";
		Cursor cursor = database.rawQuery(countSQLCommand, null);
		length = 0;
		if (cursor.moveToFirst())
			length = cursor.getInt(0);
		cursor.close();
	}

	public void save(final String url, final String response) {
		synchronized (database) {
			if (length < threshold) {
				LazyRestView.log("save new : " + url);
				String insertSQLCommand = "INSERT INTO response (response_id,response_url,response_json) VALUES (?,?,?)";
				database.execSQL(insertSQLCommand, new Object[] { ++length,
						url, response });
			} else {
				LazyRestView.log("save update : " + url);
				String updateSQLCommand = "UPDATE response SET response_url = ? , response_json=? WHERE response_id = ?";
				database.execSQL(updateSQLCommand, new Object[] { url,
						response, (++length % threshold) });
			}
		}

	}

	public String get(final String url) {
		synchronized (database) {
			String response = "";
			Cursor cursor = null;
			try {
				String sql = "SELECT * FROM response WHERE response_url = '"
						+ url + "'";
				cursor = database.rawQuery(sql, null);
				if (cursor.moveToNext()) {
					response = cursor.getString(cursor
							.getColumnIndex("response_json"));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (cursor != null)
				cursor.close();
			return response;
		}
	}

	public void close() {
		database.close();
	}
}
