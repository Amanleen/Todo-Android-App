package lakshya.com.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;


public class TodoDbOpenHelper extends SQLiteOpenHelper {

    interface TODO{
        String TABLE_NAME="Todo";
        String ID="ID";
        String TARGET_DATE ="TARGET_DATE";
        String TITLE="TITLE";
        String CREATION_DATE="CREATION_DATE";
    }

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String INTEGER_TYPE = " INTEGER";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TODO.TABLE_NAME + " (" +
                    TODO.ID + INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT " + COMMA_SEP +
                    TODO.TITLE + TEXT_TYPE + COMMA_SEP +
                    TODO.TARGET_DATE + INTEGER_TYPE + COMMA_SEP+
                    TODO.CREATION_DATE+INTEGER_TYPE+
            " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TODO.TABLE_NAME;


    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "todos.db";

    public TodoDbOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Todo handle upgrade
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public synchronized boolean add(Todo todo) {
        SQLiteDatabase db = getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(TODO.TITLE, todo.getTitle());
        values.put(TODO.TARGET_DATE, todo.getTargetDate());
        values.put(TODO.CREATION_DATE, todo.getCreationDate());
        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(TODO.TABLE_NAME,"null",values);
        todo.setId(newRowId);
        return newRowId!=-1;
    }

    public synchronized ArrayList<Todo> readAll() {
        SQLiteDatabase db = getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                TODO.ID,
                TODO.TITLE,
                TODO.TARGET_DATE,
                TODO.CREATION_DATE
        };

        String sortOrder =
                TODO.TARGET_DATE + " ASC";

        Cursor c = db.query(
                TODO.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );
        ArrayList<Todo> todos = new ArrayList<Todo>();
        c.moveToPosition(-1);
        while(c.moveToNext()) {
            int id = c.getInt(c.getColumnIndex(TODO.ID));
            String title = c.getString(c.getColumnIndex(TODO.TITLE));
            long targetDate = c.getLong(c.getColumnIndex(TODO.TARGET_DATE));
            long creationDate = c.getLong(c.getColumnIndex(TODO.CREATION_DATE));

            Todo todo = new Todo(title, targetDate, creationDate);
            todo.setId(id);
            todos.add(todo);
        }
        c.close();
        return todos;
    }

    public synchronized boolean delete(Todo todo) {
        // Define 'where' part of query.
        String selection = TODO.ID + " = ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = { String.valueOf(todo.getId()) };
        // Issue SQL statement.
        SQLiteDatabase db = getWritableDatabase();
        int result = db.delete(TODO.TABLE_NAME, selection, selectionArgs);
        return result!=0;
    }

    public synchronized boolean update(Todo todo) {
        SQLiteDatabase db = getReadableDatabase();

        // New value for one column
        ContentValues values = new ContentValues();
        values.put(TODO.TITLE, todo.getTitle());
        values.put(TODO.TARGET_DATE, todo.getTargetDate());

        // Which row to update, based on the ID
        String selection = TODO.ID + " = ?";
        String[] selectionArgs = { String.valueOf(todo.getId()) };

        int count = db.update(
                TODO.TABLE_NAME,
                values,
                selection,
                selectionArgs);
        return count==1;
    }
}
