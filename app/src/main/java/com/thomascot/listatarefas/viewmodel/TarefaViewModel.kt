package com.thomascot.listatarefas.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.thomascot.listatarefas.database.DatabaseHelper
import com.thomascot.listatarefas.model.Tarefa
import com.thomascot.listatarefas.repository.TarefaRepository
import kotlinx.coroutines.launch

sealed interface TarefaUiState {
    object Loading : TarefaUiState
    data class Success(val tarefas: List<Tarefa>) : TarefaUiState
    data class Error(val message: String) : TarefaUiState
}

class TarefaViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = TarefaRepository(DatabaseHelper(application))

    var uiState: TarefaUiState by mutableStateOf(TarefaUiState.Loading)
        private set

    init { carregarTarefas() }

    fun carregarTarefas() {
        viewModelScope.launch {
            uiState = TarefaUiState.Loading
            uiState = try {
                TarefaUiState.Success(repository.listarTodos())
            } catch (e: Exception) {
                TarefaUiState.Error(e.message ?: "Erro ao acessar o banco de dados.")
            }
        }
    }

    fun salvarTarefa(titulo: String, descricao: String, conclusao: Int) {
        viewModelScope.launch {
            repository.inserir(Tarefa(titulo = titulo, descricao = descricao, conclusao = conclusao))
            carregarTarefas()
        }
    }
}