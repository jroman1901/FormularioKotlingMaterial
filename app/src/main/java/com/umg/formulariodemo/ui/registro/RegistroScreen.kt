package com.umg.formulariodemo.ui.registro
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBarDefaults.colors
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.umg.formulariodemo.model.Persona

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroScreen() {

    // ── Estado del formulario ──────────────────────────────
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var edadText by remember { mutableStateOf("") }

    // ── Errores de validación ──────────────────────────────
    var errorNombre by remember { mutableStateOf<String?>(null) }
    var errorApellido by remember { mutableStateOf<String?>(null) }
    var errorEmail by remember { mutableStateOf<String?>(null) }
    var errorEdad by remember { mutableStateOf<String?>(null) }

    // ── Lista en memoria ──────────────────────────────────
    val personas = remember { mutableStateListOf<Persona>() }
    var nextId    by remember { mutableIntStateOf(1) }

    // ── Snackbar ──────────────────────────────────────────
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    fun validar(): Boolean {
        var valido = true

        errorNombre = if (nombre.isBlank()) {
            valido = false; "El nombre es obligatorio"
        } else null

        errorApellido = if (apellido.isBlank()) {
            valido = false; "El apellido es obligatorio"
        } else null

        errorEmail = when {
            email.isBlank() -> {
                valido = false; "El correo es obligatorio"
            }

            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                valido = false; "Correo no válido"
            }

            else -> null
        }

        errorEdad = when {
            edadText.isBlank() -> null  // edad es opcional
            edadText.toIntOrNull() == null -> {
                valido = false; "Solo números"
            }

            edadText.toInt() !in 1..120 -> {
                valido = false; "Edad entre 1 y 120"
            }

            else -> null
        }

        return valido
    }

    // ── Función para limpiar campos ───────────────────────
    fun limpiar() {
        nombre = ""; apellido = ""; email = ""; telefono = ""; edadText = ""
        errorNombre = null; errorApellido = null; errorEmail = null; errorEdad = null
    }

    fun guardar() {
        if (!validar()) return
        personas.add(
            Persona(
                id = nextId++,
                nombre = nombre,
                apellido = apellido,
                email = email,
                telefono = telefono,
                edad = edadText.toInt()
            )
        )
        limpiar()
    }

    // -- Scaffold principal ---

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Registro de Personas", fontWeight = FontWeight.SemiBold)
                        Text(
                            "Datos en memoria",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    )
    { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            // ── TARJETA DEL FORMULARIO ────────────────────
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {

                        Text(
                            "Nuevo Registro",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        // Nombre
                        OutlinedTextField(
                            value = nombre,
                            onValueChange = { nombre = it; errorNombre = null },
                            label = { Text("Nombre *") },
                            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                            isError = errorNombre != null,
                            supportingText = errorNombre?.let { { Text(it) } },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Spacer(Modifier.height(8.dp))

                        // Apellido
                        OutlinedTextField(
                            value = apellido,
                            onValueChange = { apellido = it; errorApellido = null },
                            label = { Text("Apellido *") },
                            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                            isError = errorApellido != null,
                            supportingText = errorApellido?.let { { Text(it) } },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Spacer(Modifier.height(8.dp))

                        // Email
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it; errorEmail = null },
                            label = { Text("Correo electrónico *") },
                            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                            isError = errorEmail != null,
                            supportingText = errorEmail?.let { { Text(it) } },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                        )

                        Spacer(Modifier.height(8.dp))

                        // Edad y Teléfono en fila
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = edadText,
                                onValueChange = { edadText = it; errorEdad = null },
                                label = { Text("Edad") },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.DateRange,
                                        contentDescription = null
                                    )
                                },
                                isError = errorEdad != null,
                                supportingText = errorEdad?.let { { Text(it) } },
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )
                            OutlinedTextField(
                                value = telefono,
                                onValueChange = { telefono = it },
                                label = { Text("Teléfono") },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Phone,
                                        contentDescription = null
                                    )
                                },
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                            )
                        }

                        Spacer(Modifier.height(16.dp))

                        // Botón Guardar
                        Button(
                            onClick = { guardar() },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(50)
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("Guardar Registro")
                        }

                        Spacer(Modifier.height(8.dp))

                        // Botón Limpiar
                        OutlinedButton(
                            onClick = { limpiar() },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(50)
                        ) {
                            Icon(Icons.Default.Clear, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("Limpiar campos")
                        }
                    }
                }
            }  // item

            // ── ENCABEZADO DE LISTA ───────────────────────
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Registros guardados", style = MaterialTheme.typography.titleMedium)
                    Badge {
                        Text("${personas.size}")
                    }
                }
            }

            // ── LISTA DE PERSONAS ─────────────────────────
            if (personas.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Sin registros aún",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                items(personas, key = { it.id }) { persona ->
                    PersonaCard(
                        persona = persona,
                        onEliminar = { personas.remove(persona) }
                    )
                }
            }

        }

    }
}

// ── Tarjeta individual de persona ─────────────────────────

@Composable
fun PersonaCard(persona: Persona, onEliminar: () -> Unit) {
    val iniciales = buildString {
        if (persona.nombre.isNotEmpty()) append(persona.nombre.first().uppercaseChar())
        if (persona.apellido.isNotEmpty()) append(persona.apellido.first().uppercaseChar())
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Avatar con iniciales
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    iniciales,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            // Info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "${persona.nombre} ${persona.apellido}",
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodyMedium
                )
                if (persona.email.isNotEmpty()) {
                    Text(
                        persona.email,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                if (persona.telefono.isNotEmpty()) {
                    Text(
                        persona.telefono,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Edad + botón eliminar
            Column(horizontalAlignment = Alignment.End) {
                if (persona.edad > 0) {
                    Text(
                        "${persona.edad} años",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                }
                IconButton(onClick = onEliminar, modifier = Modifier.size(32.dp)) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}


