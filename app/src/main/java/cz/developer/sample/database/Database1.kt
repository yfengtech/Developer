package com.cz.demo.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class Database1(context: Context) : SQLiteOpenHelper(context, name, null, version) {
    companion object {
        private val name = "test_db1" //数据库名称
        private val version = 1 //数据库版本
    }

    override fun onCreate(db: SQLiteDatabase) {
        //创建五张表
        for(i in 1..5){
            db.execSQL("CREATE TABLE IF NOT EXISTS person$i(personid integer primary key autoincrement, " +
                    "name1 varchar(20)," +
                    "name2 varchar(20)," +
                    "name3 varchar(20)," +
                    "name4 varchar(20)," +
                    "name5 varchar(20)," +
                    "name6 varchar(20)," +
                    "name7 varchar(20)," +
                    "name8 varchar(20)," +
                    "name9 varchar(20)," +
                    "name10 varchar(20)," +
                    "age INTEGER)")
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    }

}