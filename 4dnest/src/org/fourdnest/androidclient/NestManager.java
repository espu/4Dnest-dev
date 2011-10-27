package org.fourdnest.androidclient;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;



public class NestManager {
	
	private static final String TAG = NestDatabase.class.getSimpleName();
	
	static final String DB_NAME = "4dnest.nests.db";
	static final int DB_VERSION = 1;
	
	// Table columns
	static final String TABLE = "nest";
	static final String C_ID = BaseColumns._ID;
	static final String C_NAME = "name";
	static final String C_DESCRIPTION = "description";
	static final String C_ADDRESS = "address";
	static final String C_PROTOCOL = "protocol";
	
	private final NestDatabase nestDb;
	
	/**
	 * Creates new NestManager with specified context
	 * @param context
	 */
	public NestManager(Context context) {
		this.nestDb = new NestDatabase(context);
		
		Log.d(TAG, "NestManager created");
	}
	
	/**
	 * 
	 * @return ArrayList<Nest> List of saved nests
	 */
	public ArrayList<Nest> listNests() {
		
		SQLiteDatabase db = this.nestDb.getReadableDatabase();
		
		Cursor result = db.query(TABLE,
				new String[]{
				C_ID, C_NAME, C_DESCRIPTION, C_ADDRESS, C_PROTOCOL
				}, // Columns
				null, // No WHERE
				null, // No arguments in selection
				null, // No GROUP BY
				null, // No HAVING
				C_NAME, // Order by name
				"1"); // Limit 1
		
		ArrayList<Nest> nests = new ArrayList<Nest>();
		
		result.moveToFirst();
		while(!result.isLast()) {
			Nest nest = new Nest(
					result.getInt(0),
					result.getString(1),
					result.getString(2),
					result.getString(3),
					result.getString(4)
					);
			
			nests.add(nest);
		}
		
		return nests;
	}
	
	/**
	 * 
	 * @param id of nest
	 * @return Nest with specified id or null
	 */
	public Nest getNest(int id) {

		SQLiteDatabase db = this.nestDb.getReadableDatabase();
		Cursor result = db.query(TABLE,
				new String[]{
				C_ID, C_NAME, C_DESCRIPTION, C_ADDRESS, C_PROTOCOL
				}, // Columns
				C_ID + "==" + id, // Where
				null, // No arguments in selection
				null, // No GROUP BY
				null, // No HAVING
				null, //No ORDER BY
				"1"); // Limit 1
		
		
		Nest nest = null;
		if(result.getCount() > 0) {
			result.moveToFirst();
			nest = new Nest(
					result.getInt(0),
					result.getString(1),
					result.getString(2),
					result.getString(3),
					result.getString(4)
					);
			
		} else {
			Log.d(TAG, "Nest with id " + id + " not found");
		}
		
		
		return nest;
		
	}
	
	/**
	 * Saves Nest to database, updating existing Nest with same id
	 * and creating new one if necessary 
	 * @param nest object to save
	 * @return long row id or -1 on failure
	 */
	public long saveNest(Nest nest) {
		
		
		SQLiteDatabase db = this.nestDb.getWritableDatabase();
		
		// API level 8 would have insertWithOnConflict, have to work around it
		// and check for conflict and then either insert or update

		// Check if nest with id exists
		Cursor result = db.query(TABLE,
				new String[] {C_ID},
				C_ID + "==" + nest.id,
				null, // No selection args
				null, // No GROUP BY
				null, // No HAVING
				null, // No ORDER BY
				"1"); // LIMIT 1
		
		// Create ContentValues object for Nest
		ContentValues values = new ContentValues();
		values.put(C_ID, nest.id);
		values.put(C_NAME, nest.name);
		values.put(C_DESCRIPTION, nest.description);
		values.put(C_ADDRESS, nest.address);
		values.put(C_PROTOCOL, nest.protocolName);
		
		long rowid;
		if(result.getCount() > 0) {
			// Update existing
			rowid = db.replace(TABLE, null, values);
			
			if(rowid < 0) {
				throw new SQLiteException("Error replacing existing nest with id + "
						+ nest.id + " in database");
			}
			
			Log.d(TAG, "Updated Nest in db");
			
		} else {
			// Insert new row			
			rowid = db.insert(TABLE, null, values);
			if(rowid < 0) {
				throw new SQLiteException("Error inserting new nest to database");
			}
			
			Log.d(TAG, "Inserted new Nest to db");
		}
		
		return rowid;
	}
	
	/**
	 * Closes database
	 */
	public void close() {
		Log.d(TAG, "db closed");
		this.nestDb.close();
	}
	
	
	
	// Actual database handler inside NestManager
	class NestDatabase extends SQLiteOpenHelper {
		
		Context context;
		
		public NestDatabase(Context context) {
			super(context, DB_NAME, null, DB_VERSION);			
			this.context = context;
			
			Log.d(TAG, "NestDatabase created");
		}

		// Called when DB is created for the first time (does not exist)
		@Override
		public void onCreate(SQLiteDatabase db) {
			
			// Prepare SQL table creation query 
			String tableCreateQuery = String.format(
						"CREATE TABLE %s(" +
						"%s int PRIMARY KEY," +
						"%s text," +
						"%s text," +
						"%s text," +
						"%s text)",
						TABLE,
						C_ID,
						C_NAME,
						C_DESCRIPTION,
						C_ADDRESS,
						C_PROTOCOL				
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
