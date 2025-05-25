package com.example.appaulafirebase

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.appaulafirebase.ui.theme.AppAulaFirebaseTheme
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppAulaFirebaseTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    AppAulaFirebase()
                }
            }
        }
    }
}

data class Usuario(
    val nome: String = "", val idade: String = "", val nacionalidade: String = "",
    val pais: String = "", val cidade: String = ""
)

@Composable
fun AppAulaFirebase() {
    val db = Firebase.firestore
    var nome by remember { mutableStateOf("") }
    var idade by remember { mutableStateOf("") }
    var nacionalidade by remember { mutableStateOf("") }
    var genero by remember { mutableStateOf("") }
    var cidade by remember { mutableStateOf("") }
    var usuarios by remember { mutableStateOf(listOf<Usuario>()) }
    var mostrarLista by remember { mutableStateOf(false) }

    fun buscarUsuarios() {
        db.collection("users").get()
            .addOnSuccessListener { res ->
                usuarios = res.map {
                    Usuario(
                        nome = it["nome"] as String? ?: "",
                        idade = it["idade"] as String? ?: "",
                        nacionalidade = it["nacionalidade"] as String? ?: "",
                        pais = it["pais"] as String? ?: "",
                        cidade = it["cidade"] as String? ?: ""
                    )
                }
                mostrarLista = true
            }
    }

    if (mostrarLista) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text("Usuários cadastrados", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(12.dp))
            usuarios.forEach {
                Text("- ${it.nome}, ${it.idade} anos, ${it.cidade}/${it.pais}")
                Spacer(Modifier.height(6.dp))
            }
            Spacer(Modifier.height(24.dp))
            Button(onClick = { mostrarLista = false }) {
                Text("Voltar")
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Cadastro", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(16.dp))
            TextField(nome, { nome = it }, label = { Text("Nome:") }, colors = textFieldColors(), modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(12.dp))
            TextField(idade, { idade = it }, label = { Text("Idade:") }, colors = textFieldColors(), modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(12.dp))
            TextField(nacionalidade, { nacionalidade = it }, label = { Text("Nacionalidade:") }, colors = textFieldColors(), modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(12.dp))
            TextField(genero, { genero = it }, label = { Text("Gênero:") }, colors = textFieldColors(), modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(12.dp))
            TextField(cidade, { cidade = it }, label = { Text("Cidade") }, colors = textFieldColors(), modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(24.dp))
            Button(
                onClick = {
                    val dados = hashMapOf(
                        "nome" to nome,
                        "idade" to idade,
                        "nacionalidade" to nacionalidade,
                        "pais" to genero,
                        "cidade" to cidade
                    )
                    db.collection("users").add(dados)
                        .addOnSuccessListener {
                            Log.d("FIREBASE", "Usuário salvo: ${it.id}")
                            buscarUsuarios()
                        }
                        .addOnFailureListener { e -> Log.w("FIREBASE", "Erro ao salvar", e) }
                    nome = ""; idade = ""; nacionalidade = ""; genero = ""; cidade = ""
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text("Cadastrar")
            }
        }
    }
}

@Composable
fun textFieldColors() = TextFieldDefaults.colors(
    focusedContainerColor = MaterialTheme.colorScheme.surface,
    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
    focusedLabelColor = MaterialTheme.colorScheme.primary,
    unfocusedLabelColor = MaterialTheme.colorScheme.primary,
    cursorColor = MaterialTheme.colorScheme.primary
)

@Preview(showBackground = true)
@Composable
fun AppAulaFirebasePreview() {
    AppAulaFirebaseTheme {
        AppAulaFirebase()
    }
}
