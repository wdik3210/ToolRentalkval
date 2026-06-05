package com.example.toolrental.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.toolrental.models.Booking;
import com.example.toolrental.models.Tool;
import com.example.toolrental.models.User;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "toolrental.db";
    private static final int DATABASE_VERSION = 1;

    // Users table
    private static final String TABLE_USERS = "users";
    private static final String COL_ID = "id";
    private static final String COL_EMAIL = "email";
    private static final String COL_PASSWORD = "password";
    private static final String COL_ROLE = "role";
    private static final String COL_FULLNAME = "fullname";

    // Tools table
    private static final String TABLE_TOOLS = "tools";
    private static final String COL_NAME = "name";
    private static final String COL_DESCRIPTION = "description";
    private static final String COL_PRICE_PER_DAY = "price_per_day";
    private static final String COL_IMAGE_URI = "image_uri";
    private static final String COL_OWNER_ID = "owner_id";

    // Bookings table
    private static final String TABLE_BOOKINGS = "bookings";
    private static final String COL_TOOL_ID = "tool_id";
    private static final String COL_RENTER_ID = "renter_id";
    private static final String COL_START_DATE = "start_date";
    private static final String COL_END_DATE = "end_date";
    private static final String COL_STATUS = "status";
    private static final String COL_TOTAL_PRICE = "total_price";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUsers = "CREATE TABLE " + TABLE_USERS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_EMAIL + " TEXT UNIQUE, " +
                COL_PASSWORD + " TEXT, " +
                COL_ROLE + " TEXT, " +
                COL_FULLNAME + " TEXT)";
        db.execSQL(createUsers);

        String createTools = "CREATE TABLE " + TABLE_TOOLS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " TEXT, " +
                COL_DESCRIPTION + " TEXT, " +
                COL_PRICE_PER_DAY + " REAL, " +
                COL_IMAGE_URI + " TEXT, " +
                COL_OWNER_ID + " INTEGER)";
        db.execSQL(createTools);

        String createBookings = "CREATE TABLE " + TABLE_BOOKINGS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TOOL_ID + " INTEGER, " +
                COL_RENTER_ID + " INTEGER, " +
                COL_START_DATE + " TEXT, " +
                COL_END_DATE + " TEXT, " +
                COL_STATUS + " TEXT, " +
                COL_TOTAL_PRICE + " REAL)";
        db.execSQL(createBookings);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TOOLS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKINGS);
        onCreate(db);
    }

    // User methods
    public long addUser(String email, String password, String role, String fullName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_EMAIL, email);
        cv.put(COL_PASSWORD, password);
        cv.put(COL_ROLE, role);
        cv.put(COL_FULLNAME, fullName);
        return db.insert(TABLE_USERS, null, cv);
    }

    public User login(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null, COL_EMAIL + "=? AND " + COL_PASSWORD + "=?",
                new String[]{email, password}, null, null, null);
        if (cursor.moveToFirst()) {
            User user = new User(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4)
            );
            cursor.close();
            return user;
        }
        cursor.close();
        return null;
    }

    // Tool methods
    public long addTool(String name, String description, double pricePerDay, String imageUri, int ownerId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_NAME, name);
        cv.put(COL_DESCRIPTION, description);
        cv.put(COL_PRICE_PER_DAY, pricePerDay);
        cv.put(COL_IMAGE_URI, imageUri);
        cv.put(COL_OWNER_ID, ownerId);
        return db.insert(TABLE_TOOLS, null, cv);
    }

    public boolean updateTool(int id, String name, String description, double pricePerDay, String imageUri) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_NAME, name);
        cv.put(COL_DESCRIPTION, description);
        cv.put(COL_PRICE_PER_DAY, pricePerDay);
        cv.put(COL_IMAGE_URI, imageUri);
        return db.update(TABLE_TOOLS, cv, COL_ID + "=?", new String[]{String.valueOf(id)}) > 0;
    }

    public boolean deleteTool(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_TOOLS, COL_ID + "=?", new String[]{String.valueOf(id)}) > 0;
    }

    public List<Tool> getAllTools() {
        List<Tool> tools = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TOOLS, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            tools.add(new Tool(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getDouble(3),
                    cursor.getString(4),
                    cursor.getInt(5)
            ));
        }
        cursor.close();
        return tools;
    }

    public List<Tool> getToolsByOwner(int ownerId) {
        List<Tool> tools = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TOOLS, null, COL_OWNER_ID + "=?", new String[]{String.valueOf(ownerId)}, null, null, null);
        while (cursor.moveToNext()) {
            tools.add(new Tool(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getDouble(3),
                    cursor.getString(4),
                    cursor.getInt(5)
            ));
        }
        cursor.close();
        return tools;
    }

    public Tool getToolById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TOOLS, null, COL_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor.moveToFirst()) {
            Tool tool = new Tool(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getDouble(3),
                    cursor.getString(4),
                    cursor.getInt(5)
            );
            cursor.close();
            return tool;
        }
        cursor.close();
        return null;
    }

    // Booking methods
    public long addBooking(int toolId, int renterId, String startDate, String endDate, String status, double totalPrice) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_TOOL_ID, toolId);
        cv.put(COL_RENTER_ID, renterId);
        cv.put(COL_START_DATE, startDate);
        cv.put(COL_END_DATE, endDate);
        cv.put(COL_STATUS, status);
        cv.put(COL_TOTAL_PRICE, totalPrice);
        return db.insert(TABLE_BOOKINGS, null, cv);
    }

    public boolean updateBookingStatus(int bookingId, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_STATUS, status);
        return db.update(TABLE_BOOKINGS, cv, COL_ID + "=?", new String[]{String.valueOf(bookingId)}) > 0;
    }

    public boolean deleteBooking(int bookingId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_BOOKINGS, COL_ID + "=?", new String[]{String.valueOf(bookingId)}) > 0;
    }

    public List<Booking> getBookingsByRenter(int renterId) {
        List<Booking> bookings = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_BOOKINGS, null, COL_RENTER_ID + "=?", new String[]{String.valueOf(renterId)}, null, null, null);
        while (cursor.moveToNext()) {
            bookings.add(new Booking(
                    cursor.getInt(0),
                    cursor.getInt(1),
                    cursor.getInt(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getDouble(6)
            ));
        }
        cursor.close();
        return bookings;
    }

    public List<Booking> getBookingsByToolOwner(int ownerId) {
        List<Booking> bookings = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT b.* FROM " + TABLE_BOOKINGS + " b " +
                "JOIN " + TABLE_TOOLS + " t ON b." + COL_TOOL_ID + " = t." + COL_ID +
                " WHERE t." + COL_OWNER_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(ownerId)});
        while (cursor.moveToNext()) {
            bookings.add(new Booking(
                    cursor.getInt(0),
                    cursor.getInt(1),
                    cursor.getInt(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getDouble(6)
            ));
        }
        cursor.close();
        return bookings;
    }

    public Booking getBookingById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_BOOKINGS, null, COL_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor.moveToFirst()) {
            Booking booking = new Booking(
                    cursor.getInt(0),
                    cursor.getInt(1),
                    cursor.getInt(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getDouble(6)
            );
            cursor.close();
            return booking;
        }
        cursor.close();
        return null;
    }
}
