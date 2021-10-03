package com.alif.clothingretail.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.List;

public class Database extends SQLiteAssetHelper {
    private static final String DB_NAME= "clothingretail.db";
    private static final int DB_VER = 1;

    public Database(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }
}
