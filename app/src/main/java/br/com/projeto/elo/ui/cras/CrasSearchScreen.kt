package br.com.projeto.elo.ui.cras

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrasSearchScreen(
    aoVoltar: () -> Unit,
    viewModel: CrasSearchViewModel = hiltViewModel()
) {
    val cepDigitado by viewModel.cepDigitado.collectAsState()
    val carregando by viewModel.carregando.collectAsState()
    val erro by viewModel.erro.collectAsState()
    val resultados by viewModel.resultados.collectAsState()
    val enderecoEncontrado by viewModel.enderecoEncontrado.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Buscar CRAS Próximo", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = aoVoltar) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0F4C81),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF9FAFB))
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "Digite seu CEP para encontrar as unidades do CRAS mais próximas da sua residência.",
                color = Color.Gray,
                fontSize = 14.sp
            )

            OutlinedTextField(
                value = cepDigitado,
                onValueChange = { viewModel.onCepChanged(it) },
                label = { Text("CEP (somente números)") },
                placeholder = { Text("Ex: 01001000") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = { viewModel.buscarCras() }
                ),
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF0F4C81)
                )
            )

            Button(
                onClick = { viewModel.buscarCras() },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F4C81)),
                shape = RoundedCornerShape(12.dp),
                enabled = !carregando && cepDigitado.length == 8
            ) {
                if (carregando) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Buscar CRAS", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }

            if (erro != null) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFEE2E2)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = erro ?: "",
                        color = Color(0xFFB91C1C),
                        modifier = Modifier.padding(16.dp),
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            if (enderecoEncontrado != null) {
                Text(
                    text = "📍 $enderecoEncontrado",
                    fontSize = 13.sp,
                    color = Color(0xFF047857),
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFD1FAE5))
                        .padding(12.dp)
                )
            }

            if (resultados.isNotEmpty()) {
                Text("Resultados Mapeados:", fontWeight = FontWeight.Bold, fontSize = 16.sp, modifier = Modifier.padding(top = 8.dp))
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(resultados) { cras ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0xFFEFF6FF)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color(0xFF2563EB))
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                Column {
                                    Text(cras.nome, fontWeight = FontWeight.Bold, color = Color(0xFF1F2937), fontSize = 15.sp)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(cras.endereco, color = Color(0xFF6B7280), fontSize = 13.sp)
                                    Text("Bairro: ${cras.bairro}", color = Color(0xFF6B7280), fontSize = 12.sp)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(cras.distancia, color = Color(0xFF0F4C81), fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
