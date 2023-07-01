package com.example.ddi

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ddi.data.Curso
import com.example.ddi.data.CursoRepositorio
import com.example.ddi.data.Usuario
import com.example.ddi.data.UsuarioRepositorio
import com.example.ddi.ui.theme.CodeatTheme

class CursoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = intent.extras
        val username: String? = bundle?.getString("username")
        val password: String? = bundle?.getString("password")
        val usuario: Usuario = UsuarioRepositorio.iniciar(username!!, password!!)
        val nombre: String? = bundle.getString("nombre")
        val curso: Curso = CursoRepositorio.cursoElegido(nombre!!)

        setContent {
            Content(usuario, curso)
        }
    }

    @Composable
    private fun Content(usuario: Usuario, curso: Curso) {
        CodeatTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                )
                {
                    TopBar(curso)
                    Contenido(usuario, curso)
                }
            }
        }
    }

    @Composable
    private fun TopBar(curso: Curso) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .wrapContentHeight()
                .border(BorderStroke(1.dp, Black))
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextCustom(text = curso.nombre, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = painterResource(id = R.drawable.baseline_settings_24),
                contentDescription = "",
                modifier = Modifier
                    .clickable(enabled = true, onClick = {
                        configuracion()
                    })
            )
        }
    }

    @Composable
    private fun Contenido(usuario: Usuario, curso: Curso) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(25.dp),

            verticalArrangement = Arrangement.Center,
        ) {
            Creador(usuario, curso)
            Spacer(modifier = Modifier.height(20.dp))
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
            ){
                ButtonCustom(text = "Agregar curso", onClick = {
                    usuario.agregarCurso(curso)
                    misCursos(usuario.nickname, usuario.password)
                    finish()
                })
            }
        }
    }

    @Composable
    private fun Creador(usuario: Usuario, curso: Curso) {
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
        ){
            Image(painterResource(id = R.drawable.baseline_person_100), contentDescription = "", Modifier.width(50.dp))
            Column {
                TextCustom(text = "${curso.creador.nickname} (${curso.puntaje})", modifier = Modifier
                    .clickable(enabled = true, onClick = {
                        if(usuario.nickname != curso.creador.nickname) {
                            usuario(usuario.nickname, usuario.password, curso.creador.nickname)
                        } else {
                            perfil(usuario.nickname, usuario.password)
                        }
                    }))
                if(usuario.nickname != curso.creador.nickname) {
                    Button(colors = ButtonDefaults.elevatedButtonColors(containerColor = White),
                        shape = RoundedCornerShape(0),
                        border = BorderStroke(1.dp, Black),
                        onClick = {
                            usuario.agregarSeguido(curso.creador)
                            UsuarioRepositorio.creador(curso.creador.nickname).agregarSeguidor(usuario)
                            curso(usuario.nickname, usuario.password, curso.nombre)
                        }
                    ) {
                        if (!usuario.existeSeguido(curso.creador.nickname)) {
                            Text("Seguir", color = Black)
                        } else {
                            Text("Siguiendo", color = Black)
                        }
                    }
                }
            }
        }
    }

    private fun misCursos(username: String, password: String) {
        val intent = Intent(this, MisCursos::class.java).apply {
            putExtra("username", username)
            putExtra("password", password)
        }
        startActivity(intent)
        onStop()
    }

    private fun perfil(username: String, password: String) {
        val intent = Intent(this, PerfilActivity::class.java).apply {
            putExtra("username", username)
            putExtra("password", password)
        }
        startActivity(intent)
        onStop()
    }

    private fun configuracion() {
        val intent = Intent(this, ConfiguracionActivity::class.java)
        startActivity(intent)
        onStop()
    }

    private fun curso(username: String, password: String, nombre: String) {
        val intent = Intent(this, CursoActivity::class.java).apply {
            putExtra("username", username)
            putExtra("password", password)
            putExtra("nombre", nombre)
        }
        startActivity(intent)
        finish()
    }

    private fun usuario(username: String, password: String, creador: String) {
        val intent = Intent(this, UsuarioActivity::class.java).apply {
            putExtra("username", username)
            putExtra("password", password)
            putExtra("creador", creador)
        }
        startActivity(intent)
        finish()
    }
}