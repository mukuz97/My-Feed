package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBAdapter extends SQLiteOpenHelper{

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "SmallDB";

    private static final String TABLE_NAME = "news_articles";
    private static final String COLUMN_KEY = "_key";
    private static final String COLUMN_IMAGE = "image_src";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_DESCP = "description";
    private static final String COLUMN_SRC = "source";
    private static final String COLUMN_GUID = "guid";
    private static final String COLUMN_PUB_DATE = "pub_date";

    public DBAdapter(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_KEY + " INTEGER PRIMARY KEY," +
                COLUMN_IMAGE + " TEXT," +
                COLUMN_TITLE + " TEXT," +
                COLUMN_DESCP + " TEXT," +
                COLUMN_SRC + " TEXT," +
                COLUMN_GUID + " TEXT," +
                COLUMN_PUB_DATE + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void insert(Model model){
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TITLE, model.getTitle());
        contentValues.put(COLUMN_DESCP, model.getDescription());
        contentValues.put(COLUMN_IMAGE, model.getImage());
        contentValues.put(COLUMN_GUID, model.getGuid());
        contentValues.put(COLUMN_PUB_DATE, model.getPub_date());
        contentValues.put(COLUMN_SRC, model.getSource());
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
    }

    public Model getItem(int position){
        Model model = new Model();
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(TABLE_NAME, null, null,
                null, null, null, null);
        cursor.moveToPosition(position);
        model.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
        model.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_DESCP)));
        model.setImage(cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE)));
        model.setGuid(cursor.getString(cursor.getColumnIndex(COLUMN_GUID)));
        model.setPub_date(cursor.getString(cursor.getColumnIndex(COLUMN_PUB_DATE)));
        model.setSource(cursor.getString(cursor.getColumnIndex(COLUMN_SRC)));
        cursor.close();
        return model;
    }

    public boolean isPresent(Model model){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String []selectionArgs = { model.getGuid() };
        Cursor cursor = sqLiteDatabase.query(TABLE_NAME, null, COLUMN_GUID + " = ?",
                selectionArgs, null, null, null);
        return (cursor.getCount() != 0);
    }

    public int getSize(){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return(int)DatabaseUtils.queryNumEntries(sqLiteDatabase, TABLE_NAME);
    }
}
