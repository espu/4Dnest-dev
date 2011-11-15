package org.fourdnest.androidclient;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;



public class EggManager {
	
	private static final String TAG = EggManager.class.getSimpleName();
	
	static final String DB_NAME = "4dnest.eggs.db";
	static final int DB_VERSION = 1;
	
	// Table columns
	static final String TABLE = "egg";
	static final String C_ID = BaseColumns._ID;
	static final String C_NESTID = "nest_id";
	static final String C_LOCALFILEURI = "local_file_uri";
	static final String C_REMOTEFILEURI = "remote_file_uri";
	static final String C_CAPTION = "caption";
	static final String C_LASTUPLOAD = "last_upload";
	// TODO: Add tag relation table when we have tag manager
		
	private final EggDatabase eggDb;
	
	/**
	 * Creates new NestManager with specified context
	 * @param context
	 */
	public EggManager(Context context) {
		this.eggDb = new EggDatabase(context);
		
		Log.d(TAG, "EggManager created");
	}
	
	/**
	 * 
	 * @return ArrayList<Nest> List of saved nests
	 */
	public ArrayList<Egg> listEggs() {
		
		SQLiteDatabase db = this.eggDb.getReadableDatabase();
		
		Cursor result = db.query(TABLE,
				new String[]{
				C_ID, C_NESTID, C_LOCALFILEURI, C_REMOTEFILEURI, C_CAPTION
				}, // Columns
				null, // No WHERE
				null, // No arguments in selection
				null, // No GROUP BY
				null, // No HAVING
				C_ID, // Order by name
				"100"); // Limit 100
		
		ArrayList<Egg> eggs = new ArrayList<Egg>();
		
		if(result.getCount() > 0) {
			result.moveToFirst();
			
			while(!result.isAfterLast()) {
				Egg egg = this.extractEggFromCursor(result);
				
				eggs.add(egg);
				
				result.moveToNext();
			} 
		}
		
		return eggs;
	}
	
	/**
	 * 
	 * @param id of nest
	 * @return Nest with specified id or null
	 */
	public Egg getEgg(int id) {

		SQLiteDatabase db = this.eggDb.getReadableDatabase();
		Cursor result = db.query(TABLE,
				new String[]{
				C_ID, C_NESTID, C_LOCALFILEURI, C_REMOTEFILEURI, C_CAPTION, C_LASTUPLOAD
				}, // Columns
				C_ID + "==" + id, // Where
				null, // No arguments in selection
				null, // No GROUP BY
				null, // No HAVING
				null, //No ORDER BY
				"1"); // Limit 1
		
		
		Egg egg = null;
		if(result.getCount() > 0) {
			result.moveToFirst();
			egg = this.extractEggFromCursor(result);			
		} else {
			Log.d(TAG, "Egg with id " + id + " not found");
		}
		
		return egg;
		
	}
	
	private Egg extractEggFromCursor(Cursor cursor) {					
		int id = cursor.getInt(0);
		int nestId = cursor.getInt(1);
		
		URI localURI = null;
		try {
		localURI = new URI(cursor.getString(2));
		} catch(URISyntaxException e) {
			Log.d(TAG, "Error parsing local URI: " + cursor.getString(2));
		}
		
		
		URI remoteURI = null;
		try {
			remoteURI = new URI(cursor.getString(3));
		} catch(URISyntaxException e) {
			Log.d(TAG, "Error parsing remote URI" + cursor.getString(3));
		}
		
		String caption = cursor.getString(4);
		
		ArrayList<Tag> tags = new ArrayList<Tag>();
		
		Date lastUpload = null;
		try {
			lastUpload = DateFormat.getDateInstance(DateFormat.LONG, Locale.ENGLISH).parse(cursor.getString(5));
		} catch(ParseException e) {
			
		}
		
		Egg egg = new Egg(id, nestId, localURI, remoteURI, caption, tags, lastUpload);
		
		
		return egg;
	}
	
	/**
	 * Saves Nest to database, updating existing Nest with same id
	 * and creating new one if necessary 
	 * @param nest object to save
	 * @return long row id or -1 on failure
	 */
	public long saveEgg(Egg egg) {
		
		
		SQLiteDatabase db = this.eggDb.getWritableDatabase();
		
		// API level 8 would have insertWithOnConflict, have to work around it
		// and check for conflict and then either insert or update

		// Check if nest with id exists
		Cursor result = db.query(TABLE,
				new String[] {C_ID},
				C_ID + "==" + egg.getId(),
				null, // No selection args
				null, // No GROUP BY
				null, // No HAVING
				null, // No ORDER BY
				"1"); // LIMIT 1
		
		// Create ContentValues object for Nest
		ContentValues values = new ContentValues();
		values.put(C_ID, egg.getId());
		values.put(C_NESTID, egg.getNestId());
		
		values.put(C_LOCALFILEURI, egg.getLocalFileURI() != null ? egg.getLocalFileURI().toString() : null);
		values.put(C_REMOTEFILEURI, egg.getRemoteFileURI() != null ? egg.getRemoteFileURI().toString() : null);
		
		values.put(C_CAPTION, egg.getCaption());
		if(egg.getLastUpload() != null) {
			values.put(C_LASTUPLOAD, DateFormat.getDateInstance(DateFormat.LONG, Locale.ENGLISH).format(egg.getLastUpload()));
		} else {
			values.put(C_LASTUPLOAD, "");
		}
		
		long rowid;
		if(result.getCount() > 0) {
			// Update existing
			rowid = db.replace(TABLE, null, values);
			
			if(rowid < 0) {
				throw new SQLiteException("Error replacing existing Egg with id + "
						+ egg.getId() + " in database");
			}
			
			Log.d(TAG, "Updated Egg in db");
			
		} else {
			// Insert new row			
			rowid = db.insert(TABLE, null, values);
			if(rowid < 0) {
				throw new SQLiteException("Error inserting new Egg to database");
			}
			
			Log.d(TAG, "Inserted new Egg to db");
		}
		
		return rowid;
	}
	
	/**
	 * Closes database
	 */
	public void close() {
		Log.d(TAG, "db closed");
		this.eggDb.close();
	}
	
	
	
	// Actual database handler inside NestManager
	class EggDatabase extends SQLiteOpenHelper {
		
		Context context;
		
		public EggDatabase(Context context) {
			super(context, DB_NAME, null, DB_VERSION);			
			this.context = context;
			
			Log.d(TAG, "EggDatabase created");
		}

		// Called when DB is created for the first time (does not exist)
		@Override
		public void onCreate(SQLiteDatabase db) {
			
			// Prepare SQL table creation query 
			String tableCreateQuery = String.format(
						"CREATE TABLE %s(" +
						"%s int PRIMARY KEY," +
						"%s int DEFAULT NULL, " + 
						"%s text DEFAULT NULL," +
						"%s text DEFAULT NULL," +
						"%s text DEFAULT NULL)",
						TABLE,
						C_ID,
						C_NESTID,
						C_LOCALFILEURI,
						C_REMOTEFILEURI,
						C_CAPTION
			);
			
			db.execSQL(tableCreateQuery);
			
			Log.d(TAG, "onCreated SQL: " + tableCreateQuery);
		}

		// Called when DB version number has changed
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Add proper alter scripts for production use when version changes
			
			// For now, just drop and recreate
			String tableDropQuery = String.format("DROP TABLE IF EXISTS %s", TABLE);
			db.execSQL(tableDropQuery);
			Log.d(TAG, "onUpgrade: Dropped existing table");
			
			onCreate(db);		
		}
		
	}
	
}
