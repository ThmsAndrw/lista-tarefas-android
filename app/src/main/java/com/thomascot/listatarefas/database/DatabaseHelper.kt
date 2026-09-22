package com.thomascot.listatarefas.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE $TABLE_TAREFAS (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_TITULO TEXT NOT NULL,
                $COL_DESCRICAO TEXT NOT NULL,
                $COL_CONCLUSAO INTEGER NOT NULL
            )
            """.trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TAREFAS")
        onCreate(db)
    }

    companion object {
        const val DATABASE_NAME = "tarefas.db"
        const val DATABASE_VERSION = 1
        const val TABLE_TAREFAS = "tarefas"
        const val COL_ID = "id"
        const val COL_TITULO = "titulo"
        const val COL_DESCRICAO = "descricao"
        const val COL_CONCLUSAO = "conclusao"
    }
}