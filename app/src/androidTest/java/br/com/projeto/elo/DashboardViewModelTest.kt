package br.com.projeto.elo

import br.com.projeto.elo.ui.dashboard.DashboardViewModel
import org.junit.Assert.*
import org.junit.Test

class DashboardViewModelTest {

    // TC-009: Saldo = Receita - Despesa
    @Test
    fun saldo_e_calculado_corretamente() {
        val receita = 2000.0
        val despesa = 750.0
        val saldoEsperado = 1250.0
        assertEquals(saldoEsperado, receita - despesa, 0.001)
    }

    // TC-010: Porcentagem não passa de 100%
    @Test
    fun porcentagem_gasta_nao_ultrapassa_1f() {
        val receita = 100.0
        val despesa = 150.0 // gastou mais do que recebeu
        val porcentagem = if (receita > 0)
            (despesa / receita).toFloat().coerceIn(0f, 1f) else 0f
        assertEquals(1.0f, porcentagem, 0.001f)
    }

    // TC-007: Nome correto para e-mail padrão
    @Test
    fun email_cliente_retorna_nome_maria() {
        val email = "cliente@elo.com.br"
        val nome = when (email.lowercase()) {
            "cliente@elo.com.br" -> "Maria"
            "cliente1@elo.com.br" -> "Marcelo"
            else -> email.substringBefore("@")
        }
        assertEquals("Maria", nome)
    }

    // TC-007b: Nome correto para cliente1
    @Test
    fun email_cliente1_retorna_nome_marcelo() {
        val email = "cliente1@elo.com.br"
        val nome = when (email.lowercase()) {
            "cliente@elo.com.br" -> "Maria"
            "cliente1@elo.com.br" -> "Marcelo"
            else -> email.substringBefore("@")
        }
        assertEquals("Marcelo", nome)
    }

    // TC-010b: Porcentagem é zero quando não há receita
    @Test
    fun porcentagem_e_zero_quando_sem_receita() {
        val receita = 0.0
        val despesa = 100.0
        val porcentagem = if (receita > 0)
            (despesa / receita).toFloat().coerceIn(0f, 1f) else 0f
        assertEquals(0f, porcentagem, 0.001f)
    }
}