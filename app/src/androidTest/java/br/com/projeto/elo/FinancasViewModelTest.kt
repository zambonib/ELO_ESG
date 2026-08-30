package br.com.projeto.elo

import br.com.projeto.elo.ui.financas.FinancasViewModel
import br.com.projeto.elo.ui.financas.OrcamentoComGasto
import org.junit.Assert.assertEquals
import org.junit.Test

class FinancasViewModelTest {

    // TC-F01: Taxa de poupança = (receita - despesa) / receita
    @Test
    fun taxa_de_poupanca_calculada_corretamente() {
        val taxa = FinancasViewModel.calcularTaxaPoupanca(receitas = 2000.0, despesas = 1500.0)
        assertEquals(0.25f, taxa, 0.001f)
    }

    // TC-F02: Sem receita, a taxa de poupança é zero (evita divisão por zero)
    @Test
    fun taxa_de_poupanca_e_zero_sem_receita() {
        val taxa = FinancasViewModel.calcularTaxaPoupanca(receitas = 0.0, despesas = 100.0)
        assertEquals(0f, taxa, 0.001f)
    }

    // TC-F03: Score respeita os limites 0..100 (poupança negativa não vira score negativo)
    @Test
    fun score_nao_fica_negativo_com_gasto_acima_da_receita() {
        val taxa = FinancasViewModel.calcularTaxaPoupanca(receitas = 100.0, despesas = 300.0)
        val score = FinancasViewModel.calcularScore(taxa, aderenciaOrcamento = 1f)
        assertEquals(30, score) // apenas os 30% da aderência ao orçamento
    }

    // TC-F04: Score máximo com poupança total e orçamentos respeitados
    @Test
    fun score_maximo_com_poupanca_total_e_orcamentos_ok() {
        val score = FinancasViewModel.calcularScore(taxaPoupanca = 1f, aderenciaOrcamento = 1f)
        assertEquals(100, score)
    }

    // TC-F05: Aderência ao orçamento é a fração de categorias dentro do limite
    @Test
    fun aderencia_orcamento_conta_categorias_respeitadas() {
        val orcamentos = listOf(
            OrcamentoComGasto("Alimentação", limite = 500.0, gasto = 400.0), // ok
            OrcamentoComGasto("Transporte", limite = 200.0, gasto = 250.0), // estourou
            OrcamentoComGasto("Lazer", limite = 100.0, gasto = 50.0) // ok
        )
        val aderencia = FinancasViewModel.aderenciaOrcamento(orcamentos)
        assertEquals(2f / 3f, aderencia, 0.001f)
    }

    // TC-F06: Sem orçamentos definidos, a aderência é neutra (1)
    @Test
    fun aderencia_orcamento_neutra_sem_metas() {
        val aderencia = FinancasViewModel.aderenciaOrcamento(emptyList())
        assertEquals(1f, aderencia, 0.001f)
    }
}
