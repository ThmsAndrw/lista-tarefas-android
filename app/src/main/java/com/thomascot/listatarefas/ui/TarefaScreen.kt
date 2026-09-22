package com.thomascot.listatarefas.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.thomascot.listatarefas.viewmodel.TarefaUiState
import com.thomascot.listatarefas.viewmodel.TarefaViewModel

@Composable
fun TarefaScreen(viewModel: TarefaViewModel = viewModel()) {
    var titulo by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }
    var conclusao by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(value = titulo, onValueChange = { titulo = it }, label = { Text("Título") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(value = descricao, onValueChange = { descricao = it }, label = { Text("Descricao") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(value = conclusao, onValueChange = { conclusao = it }, label = { Text("Conclusao") }, modifier = Modifier.fillMaxWidth())

        Button(
            onClick = {
                val conclusaoint = conclusao.toIntOrNull() ?: return@Button
                viewModel.salvarTarefa(titulo, descricao, conclusaoint)
                titulo = ""; descricao = ""; conclusao = ""
            },
            modifier = Modifier.padding(top = 12.dp)
        ) { Text("Salvar Tarefa") }

        Spacer(modifier = Modifier.height(16.dp))

        when (val state = viewModel.uiState) {
            is TarefaUiState.Loading -> CircularProgressIndicator()
            is TarefaUiState.Error -> Text("Erro: ${state.message}")
            is TarefaUiState.Success -> LazyColumn {
                items(state.tarefas, key = { it.id }) { tarefa ->
                    ListItem(
                        headlineContent = { Text(tarefa.titulo) },
                        supportingContent = { Text("${tarefa.descricao} · ${tarefa.conclusao}") }
                    )
                }
            }
        }
    }
}