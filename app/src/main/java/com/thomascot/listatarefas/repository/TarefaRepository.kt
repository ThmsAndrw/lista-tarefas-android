package com.thomascot.listatarefas.repository

import android.content.ContentValues
import com.thomascot.listatarefas.database.DatabaseHelper
import com.thomascot.listatarefas.model.Tarefa
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TarefaRepository(private val dbHelper: DatabaseHelper) {

    suspend fun inserir(tarefa: Tarefa) = withContext(Dispatchers.IO) {
        val db = dbHelper.writableDatabase
        val valores = ContentValues().apply {
            put(DatabaseHelper.COL_TITULO, tarefa.titulo)
            put(DatabaseHelper.COL_DESCRICAO, tarefa.descricao)
            put(DatabaseHelper.COL_CONCLUSAO, tarefa.conclusao)
        }
        db.insert(DatabaseHelper.TABLE_TAREFAS, null, valores)
    }

    suspend fun listarTodos(): List<Tarefa> = withContext(Dispatchers.IO) {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DatabaseHelper.TABLE_TAREFAS,
            null, null, null, null, null,
            "${DatabaseHelper.COL_CONCLUSAO} DESC"
        )
        val tarefas = mutableListOf<Tarefa>()
        cursor.use {
            while (it.moveToNext()) {
                tarefas.add(
                    Tarefa(
                        id = it.getLong(it.getColumnIndexOrThrow(DatabaseHelper.COL_ID)),
                        titulo = it.getString(it.getColumnIndexOrThrow(DatabaseHelper.COL_TITULO)),
                        descricao = it.getString(it.getColumnIndexOrThrow(DatabaseHelper.COL_DESCRICAO)),
                        conclusao = it.getInt(it.getColumnIndexOrThrow(DatabaseHelper.COL_CONCLUSAO))
                    )
                )
            }
        }
        tarefas
    }
}