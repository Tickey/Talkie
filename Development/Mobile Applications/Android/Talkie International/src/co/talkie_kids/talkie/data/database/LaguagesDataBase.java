package co.talkie_kids.talkie.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import co.talkie_kids.talkie.data.model.Language;

public class LaguagesDataBase extends SQLiteOpenHelper {

	public static final String TABLE_LANGUAGES = "languages";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_FLAG_IMAGE_URL = "flag_image_url";

	private static final String DATABASE_NAME = "languages.db";
	private static final int DATABASE_VERSION = 1;

	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_LANGUAGES + "(" + COLUMN_ID + " integer primary key, "
			+ COLUMN_FLAG_IMAGE_URL + " text not null, " + COLUMN_NAME
			+ " text not null);";

	public LaguagesDataBase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LANGUAGES);
		onCreate(db);
	}

	public void addLanguage(Language language) {
		ContentValues values = new ContentValues();
		values.put(COLUMN_ID, language.id);
		values.put(COLUMN_FLAG_IMAGE_URL, language.languageFlagImageName);
		values.put(COLUMN_NAME, language.name);

		SQLiteDatabase writableDatabase = getWritableDatabase();
		writableDatabase.insert(TABLE_LANGUAGES, null, values);

		writableDatabase.close();
	}
}
