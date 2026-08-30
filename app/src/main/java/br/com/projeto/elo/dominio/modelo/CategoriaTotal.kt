package br.com.projeto.elo.dominio.modelo

// Projeção usada pelo Room para agregar o total gasto por categoria (GROUP BY).
// Os nomes dos campos precisam bater com as colunas do SELECT: categoria, total.
data class CategoriaTotal(
    val categoria: String,
    val total: Double
)
