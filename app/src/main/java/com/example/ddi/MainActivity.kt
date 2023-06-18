package com.example.ddi

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ddi.data.Usuario
import com.example.ddi.data.UsuarioRepositorio
import com.example.ddi.ui.theme.DDITheme

class MainActivity : ComponentActivity() {

    private lateinit var usuario: Usuario
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Content()
        }
    }

    @Composable
    private fun Content() {
        DDITheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                Column(
                    Modifier.fillMaxSize()
                        .padding(25.dp),
                verticalArrangement = Arrangement.Center,
                ) {
                    TextCustom(text = "Iniciar sesión")
                    Spacer(modifier = Modifier.height(20.dp))
                    val nombre = textFieldCustom(label = "Usuario", placeholder = "Usuario...")
                    Spacer(modifier = Modifier.height(10.dp))
                    val contrasenia = textFieldCustom(label = "Contraseña", placeholder = "Contraseña...")
                    Spacer(modifier = Modifier.height(10.dp))
                    ButtonCustom(text = "ok", onClick = {
                        if (UsuarioRepositorio.existe(nombre, contrasenia)) {
                            usuario = UsuarioRepositorio.iniciar(nombre, contrasenia)
                            misCursos(nombre, contrasenia)
                            finish()
                        }
                    }, width = 80.dp)
                    Spacer(modifier = Modifier.height(20.dp))
                    TextCustom(text = "¿No tiene una cuenta?")
                    Spacer(modifier = Modifier.height(10.dp))
                    ButtonCustom(text = "Registrarse", onClick = { registrarse() })
                }
            }
        }
    }

    private fun registrarse() {
        val intent = Intent(this, RegistrarActivity::class.java)
        startActivity(intent)
    }

    private fun misCursos(username: String, password: String) {
        val intent = Intent(this, MisCursos::class.java).apply {
            putExtra("username", username)
            putExtra("password", password)
        }
        startActivity(intent)
    }
}
