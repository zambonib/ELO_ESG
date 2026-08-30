package br.com.projeto.elo.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.projeto.elo.ui.components.BarraNavegacaoElo
import br.com.projeto.elo.ui.theme.*

/**
 * Modelo de dados para os Módulos de Educação.
 */
data class EducationModule(
    val id: Int,
    val title: String,
    val subtitle: String,
    val emoji: String,
    val duration: String,
    val xp: Int,
    val completed: Boolean,
    val locked: Boolean,
    val color: Color,
    val bg: Color
)

/**
 * Passo explicativo de uma aula.
 */
data class LessonStep(
    val emoji: String,
    val title: String,
    val text: String
)

/**
 * Pergunta do Quiz ao final da aula.
 */
data class QuizQuestion(
    val emoji: String,
    val title: String,
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String
)

/**
 * Conteúdo completo de uma aula interativa por módulo.
 */
data class ModuleLesson(
    val moduleId: Int,
    val step1: LessonStep,
    val step2: LessonStep,
    val quiz: QuizQuestion
)

/**
 * Modelo de dados para os Flashcards.
 */
data class FlashcardItem(
    val question: String,
    val answer: String
)

private enum class EducationView {
    LIST, LESSON, FLASHCARD
}

/**
 * Tela de Educação Financeira (EducationScreen).
 */
@Composable
fun EducationScreen(
    aoNavegar: (String) -> Unit = {}
) {
    var view by remember { mutableStateOf(EducationView.LIST) }
    var cardIndex by remember { mutableStateOf(0) }
    var isFlipped by remember { mutableStateOf(false) }
    var lessonStep by remember { mutableStateOf(0) }
    var quizAnswer by remember { mutableStateOf<Int?>(null) }
    var selectedModuleId by remember { mutableStateOf(1) }

    // Módulos com todos desbloqueados.
    val modules = remember {
        listOf(
            EducationModule(1, "O que são Juros?", "Como eles afetam seu bolso no dia a dia", "💡", "4 min", 50, completed = true, locked = false, PrimaryGreen, SecondaryGreenBg),
            EducationModule(2, "Gasto Necessário vs. Supérfluo", "Aprenda a separar o que precisa do que quer", "🛒", "5 min", 60, completed = false, locked = false, ModuloAzul, ModuloAzulBg),
            EducationModule(3, "Como montar uma Reserva", "Sua proteção contra imprevistos", "🏦", "6 min", 70, completed = false, locked = false, AccentAmber, AccentAmberLight),
            EducationModule(4, "Juros Compostos — A Mágica", "Por que guardar um pouco todo mês muda tudo", "🚀", "5 min", 80, completed = false, locked = false, ModuloRoxo, ModuloRoxoBg),
            EducationModule(5, "Direitos e Benefícios Sociais", "Descontos na luz, água e auxílios do governo", "🤝", "7 min", 90, completed = false, locked = false, ModuloRosa, ModuloRosaBg)
        )
    }

    // Aulas personalizadas e educativas para cada módulo
    val lessonsMap = remember {
        mapOf(
            1 to ModuleLesson(
                moduleId = 1,
                step1 = LessonStep(
                    emoji = "💰",
                    title = "O que são Juros?",
                    text = "Juro é o \"aluguel\" do dinheiro. Quando você pega dinheiro emprestado (empréstimo, cartão de crédito), você paga um valor a mais por usá-lo. Quando você guarda ou investe, você recebe esse valor a mais."
                ),
                step2 = LessonStep(
                    emoji = "⚠️",
                    title = "O perigo do Cartão de Crédito",
                    text = "No cartão, os juros podem passar de 15% ao mês! Uma fatura de R$ 100 que você não pagar vira mais de R$ 500 em um ano. Por isso, nunca pague apenas o valor mínimo da fatura!"
                ),
                quiz = QuizQuestion(
                    emoji = "🤔",
                    title = "Teste seu conhecimento",
                    question = "Se você tem R$ 500 e os juros são de 10% ao mês, quanto você recebe de juros no 1º mês?",
                    options = listOf("R$ 5,00", "R$ 50,00", "R$ 500,00", "R$ 5.000,00"),
                    correctIndex = 1,
                    explanation = "10% de R$ 500 = R$ 50"
                )
            ),
            2 to ModuleLesson(
                moduleId = 2,
                step1 = LessonStep(
                    emoji = "🛒",
                    title = "Gasto Necessário vs. Supérfluo",
                    text = "Gasto Necessário é o essencial para você e sua família viverem: comida básica, conta de água, luz, aluguel, remédio e transporte. Sem eles, sua vida para."
                ),
                step2 = LessonStep(
                    emoji = "🎯",
                    title = "O que é Gasto Supérfluo?",
                    text = "Supérfluo é aquilo que queremos, mas podemos viver sem ou adiar: pedir delivery todo fim de semana, compras por impulso ou assinaturas que não usamos. Identificar os supérfluos é o segredo para sobrar dinheiro!"
                ),
                quiz = QuizQuestion(
                    emoji = "🛒",
                    title = "Identifique o Essencial",
                    question = "Qual das opções abaixo é um exemplo de GASTO NECESSÁRIO?",
                    options = listOf("Assinatura de 3 streamings de filmes", "Conta de água e luz da casa", "Comprar sapato novo todo mês", "Lanches por aplicativo no trabalho"),
                    correctIndex = 1,
                    explanation = "Água e luz são contas essenciais de sobrevivência e moradia."
                )
            ),
            3 to ModuleLesson(
                moduleId = 3,
                step1 = LessonStep(
                    emoji = "🛡️",
                    title = "O que é a Reserva de Emergência?",
                    text = "É um dinheiro guardado exclusivamente para imprevistos: remédio urgente, conserto de fogão ou geladeira, ou perda temporária de renda. Ela evita que você tenha que pegar empréstimo com juros altos."
                ),
                step2 = LessonStep(
                    emoji = "🏦",
                    title = "Onde guardar a Reserva?",
                    text = "A reserva deve ficar em um lugar seguro e que você possa sacar a qualquer momento (liquidez diária), como Tesouro Selic ou contas digitais seguras que rendem 100% do CDI. Nunca deixe debaixo do colchão!"
                ),
                quiz = QuizQuestion(
                    emoji = "🏦",
                    title = "Proteção Financeira",
                    question = "Para que serve a Reserva de Emergência?",
                    options = listOf("Comprar roupas de marca na promoção", "Cobrir imprevistos de saúde ou urgências sem se endividar", "Fazer apostas na internet", "Gastar em viagens de férias"),
                    correctIndex = 1,
                    explanation = "A reserva serve para te proteger de imprevistos sem entrar no vermelho!"
                )
            ),
            4 to ModuleLesson(
                moduleId = 4,
                step1 = LessonStep(
                    emoji = "🚀",
                    title = "A Mágica dos Juros Compostos",
                    text = "Juros Compostos são juros que rendem em cima de juros! No primeiro mês, rende sobre o que você guardou. No segundo mês, rende sobre o que você guardou MAIS o lucro do mês anterior. É uma bola de neve positiva!"
                ),
                step2 = LessonStep(
                    emoji = "⏳",
                    title = "O Poder de Guardar R$ 50 Todo Mês",
                    text = "Guardar R$ 50 todo mês com disciplina rende muito mais do que guardar uma quantia grande uma única vez. O tempo faz o dinheiro trabalhar por você enquanto você dorme!"
                ),
                quiz = QuizQuestion(
                    emoji = "🚀",
                    title = "Efeito Bola de Neve",
                    question = "Por que começar a guardar dinheiro cedo faz tanta diferença?",
                    options = listOf("Porque o banco dá brindes semanais", "Porque o tempo multiplica o rendimento dos juros sobre juros", "Porque os juros diminuem com os anos", "Não faz diferença quando começar"),
                    correctIndex = 1,
                    explanation = "Quanto mais tempo o dinheiro rende, maior é a bola de neve dos juros compostos!"
                )
            ),
            5 to ModuleLesson(
                moduleId = 5,
                step1 = LessonStep(
                    emoji = "💡",
                    title = "Tarifa Social de Energia e Água",
                    text = "Famílias de baixa renda inscritas no CadÚnico têm direito a até 65% de desconto na conta de luz (Tarifa Social de Energia) e descontos na conta de água. É um direito seu garantido por lei!"
                ),
                step2 = LessonStep(
                    emoji = "🏛️",
                    title = "Como acessar os Benefícios Sociais?",
                    text = "O caminho para todos os benefícios é manter seu Cadastro Único (CadÚnico) atualizado no CRAS da sua região. Ele dá acesso a Bolsa Família, BPC, Tarifa Social, ID Jovem e Minha Casa Minha Vida."
                ),
                quiz = QuizQuestion(
                    emoji = "🤝",
                    title = "Cidadania e Direitos",
                    question = "Onde a família deve ir para cadastrar ou atualizar o CadÚnico?",
                    options = listOf("Na agência de banco privado", "No CRAS (Centro de Referência de Assistência Social)", "No shopping center", "Na concessionária de veículos"),
                    correctIndex = 1,
                    explanation = "O CRAS é a unidade pública responsável pela assistência social e CadÚnico."
                )
            )
        )
    }

    val flashcards = remember {
        listOf(
            FlashcardItem("O que é juro simples?", "É quando você paga juros só sobre o valor inicial. Ex: R$100 a 10% = R$10 por mês, sempre."),
            FlashcardItem("O que é juro composto?", "É quando os juros crescem sobre os juros. Ex: R$100 a 10% = R$10 no 1º mês, R$11 no 2º mês (juros sobre R$110)."),
            FlashcardItem("Por que dívida no cartão é perigosa?", "O cartão cobra juros compostos de até 15% ao mês. R$100 não pago vira mais de R$500 em 1 ano!"),
            FlashcardItem("O que é Tarifa Social de Energia?", "É um desconto de até 65% na conta de luz para famílias inscritas no CadÚnico com renda de até meio salário mínimo por pessoa."),
            FlashcardItem("Qual a diferença entre guardar e investir?", "Guardar debaixo do colchão perde valor com a inflação. Investir faz o dinheiro render juros e crescer com o tempo.")
        )
    }

    Scaffold(
        bottomBar = {
            if (view == EducationView.LIST) {
                BarraNavegacaoElo(rotaAtual = "educacao", aoNavegar = aoNavegar)
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (view) {
                EducationView.FLASHCARD -> FlashcardView(
                    cards = flashcards,
                    currentIndex = cardIndex,
                    isFlipped = isFlipped,
                    onFlipToggle = { isFlipped = !isFlipped },
                    onNext = {
                        if (cardIndex < flashcards.size - 1) {
                            cardIndex++
                            isFlipped = false
                        } else {
                            view = EducationView.LIST
                            cardIndex = 0
                            isFlipped = false
                        }
                    },
                    onPrev = {
                        if (cardIndex > 0) {
                            cardIndex--
                            isFlipped = false
                        }
                    },
                    onBack = {
                        view = EducationView.LIST
                        cardIndex = 0
                        isFlipped = false
                    }
                )

                EducationView.LESSON -> {
                    val currentLesson = lessonsMap[selectedModuleId] ?: lessonsMap[1]!!
                    InteractiveLessonView(
                        lesson = currentLesson,
                        stepIndex = lessonStep,
                        quizAnswer = quizAnswer,
                        onSelectAnswer = { quizAnswer = it },
                        onContinue = {
                            if (lessonStep < 2) {
                                lessonStep++
                                quizAnswer = null
                            } else {
                                view = EducationView.LIST
                                lessonStep = 0
                                quizAnswer = null
                            }
                        },
                        onExit = {
                            view = EducationView.LIST
                            lessonStep = 0
                            quizAnswer = null
                        }
                    )
                }

                EducationView.LIST -> ModuleListView(
                    modules = modules,
                    onOpenFlashcards = { view = EducationView.FLASHCARD },
                    onOpenLesson = { module ->
                        selectedModuleId = module.id
                        lessonStep = 0
                        quizAnswer = null
                        view = EducationView.LESSON
                    }
                )
            }
        }
    }
}

/**
 * Sub-tela: Lista de Módulos da Trilha de Educação
 */
@Composable
private fun ModuleListView(
    modules: List<EducationModule>,
    onOpenFlashcards: () -> Unit,
    onOpenLesson: (EducationModule) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .verticalScroll(rememberScrollState())
            .padding(bottom = 90.dp)
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.linearGradient(listOf(PrimaryGreen, PrimaryGreenLight)))
                .statusBarsPadding()
                .padding(20.dp)
        ) {
            Column {
                Text("Educação Financeira 📚", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Black)
                Text("Microaulas práticas de 4–7 minutos para o seu dia a dia.", color = Color.White.copy(alpha = 0.85f), fontSize = 14.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(10.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(0.2f)
                                .clip(CircleShape)
                                .background(AccentAmber)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("1/5 concluídas", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            // Card CTA Flashcards
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Brush.linearGradient(listOf(GradienteVioleta, GradienteIndigo)))
                    .clickable { onOpenFlashcards() }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("🃏", fontSize = 28.sp)
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text("Flashcards interativos", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Black)
                    Text("5 cards com perguntas rápidas — Pratique agora!", color = Color.White.copy(alpha = 0.85f), fontSize = 12.sp)
                }
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.ArrowForward, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                }
            }

            Text("Módulos Educativos", fontSize = 16.sp, fontWeight = FontWeight.Black, color = TextDark, modifier = Modifier.padding(top = 8.dp))

            // Módulos com setas ativas para todos
            modules.forEach { mod ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                        .border(
                            1.dp,
                            if (mod.completed) mod.color.copy(alpha = 0.4f) else BorderLight,
                            RoundedCornerShape(16.dp)
                        )
                        .clickable { onOpenLesson(mod) }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(mod.bg),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(mod.emoji, fontSize = 24.sp)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        if (mod.completed) {
                            Text("✓ Concluído", color = PrimaryGreen, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                        Text(mod.title, fontSize = 14.sp, fontWeight = FontWeight.Black, color = TextDark)
                        Text(mod.subtitle, fontSize = 12.sp, color = TextMuted)
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Text("⏱ ${mod.duration}", fontSize = 10.sp, color = TextMuted)
                            Text("+${mod.xp} XP", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = mod.color)
                        }
                    }

                    // Seta de Ação para acessar o módulo
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(if (mod.completed) SecondaryGreenBg else mod.color)
                            .clickable { onOpenLesson(mod) },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "Iniciar",
                            tint = if (mod.completed) PrimaryGreen else Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Sub-tela: Modo Flashcards com Animação de Revelação
 */
@Composable
private fun FlashcardView(
    cards: List<FlashcardItem>,
    currentIndex: Int,
    isFlipped: Boolean,
    onFlipToggle: () -> Unit,
    onNext: () -> Unit,
    onPrev: () -> Unit,
    onBack: () -> Unit
) {
    val card = cards[currentIndex]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.linearGradient(listOf(PrimaryGreen, PrimaryGreenLight)))
                .statusBarsPadding()
                .padding(20.dp)
        ) {
            Column {
                Text(
                    text = "← Voltar",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 14.sp,
                    modifier = Modifier.clickable { onBack() }
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Flashcards 🃏", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Black)
                Text("Toque no card para revelar a resposta", color = Color.White.copy(alpha = 0.85f), fontSize = 14.sp)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Progresso
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                cards.forEachIndexed { idx, _ ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(8.dp)
                            .clip(CircleShape)
                            .background(if (idx <= currentIndex) PrimaryGreen else BorderLight)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Card Principal
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(24.dp))
                    .background(if (isFlipped) PrimaryGreen else Color.White)
                    .border(1.dp, BorderLight, RoundedCornerShape(24.dp))
                    .clickable { onFlipToggle() }
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    if (!isFlipped) {
                        Text("❓", fontSize = 44.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(card.question, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextDark, textAlign = TextAlign.Center)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Toque para ver a resposta", fontSize = 14.sp, color = TextMuted)
                    } else {
                        Text("✅", fontSize = 44.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(card.answer, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.White, textAlign = TextAlign.Center)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (isFlipped) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(
                        onClick = onPrev,
                        enabled = currentIndex > 0,
                        modifier = Modifier.weight(1f).height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = FlashcardErroBg)
                    ) {
                        Text("← Revisar", color = FlashcardErroText, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = onNext,
                        modifier = Modifier.weight(1f).height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
                    ) {
                        Text(if (currentIndex < cards.size - 1) "Próximo →" else "Concluir 🎉", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text("${currentIndex + 1} de ${cards.size} cards", fontSize = 14.sp, color = TextMuted)
        }
    }
}

/**
 * Sub-tela: Aula Interativa com Quiz baseada no módulo selecionado
 */
@Composable
private fun InteractiveLessonView(
    lesson: ModuleLesson,
    stepIndex: Int,
    quizAnswer: Int?,
    onSelectAnswer: (Int) -> Unit,
    onContinue: () -> Unit,
    onExit: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.linearGradient(listOf(PrimaryGreen, PrimaryGreenLight)))
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onExit() }
                ) {
                    Text(
                        text = "← Sair da aula",
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Spacer(modifier = Modifier.height(14.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    repeat(3) { idx ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(6.dp)
                                .clip(CircleShape)
                                .background(if (idx <= stepIndex) AccentAmber else Color.White.copy(alpha = 0.3f))
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${stepIndex + 1} de 3",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.White)
                    .border(1.dp, BorderLight, RoundedCornerShape(24.dp))
                    .padding(18.dp)
            ) {
                if (stepIndex == 0) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(lesson.step1.emoji, fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(lesson.step1.title, fontSize = 20.sp, fontWeight = FontWeight.Black, color = TextDark)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(lesson.step1.text, fontSize = 15.sp, color = TextDark, textAlign = TextAlign.Center, lineHeight = 22.sp)
                    }
                } else if (stepIndex == 1) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(lesson.step2.emoji, fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(lesson.step2.title, fontSize = 20.sp, fontWeight = FontWeight.Black, color = TextDark)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(lesson.step2.text, fontSize = 15.sp, color = TextDark, textAlign = TextAlign.Center, lineHeight = 22.sp)
                    }
                } else {
                    // Quiz Dinâmico da Aula Atual
                    val quiz = lesson.quiz
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(quiz.emoji, fontSize = 40.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(quiz.title, fontSize = 18.sp, fontWeight = FontWeight.Black, color = TextDark)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(quiz.question, fontSize = 14.sp, color = TextDark, textAlign = TextAlign.Center, lineHeight = 20.sp)
                        Spacer(modifier = Modifier.height(14.dp))

                        val letters = listOf("A", "B", "C", "D")
                        val correct = quiz.correctIndex

                        quiz.options.forEachIndexed { i, opt ->
                            val isSelected = quizAnswer == i
                            val isCorrectOpt = i == correct

                            val cardBg = when {
                                quizAnswer != null && isCorrectOpt -> QuizSucessoCardBg
                                quizAnswer != null && isSelected && !isCorrectOpt -> QuizErroCardBg
                                else -> QuizDefaultCardBg
                            }

                            val borderColor = when {
                                quizAnswer != null && isCorrectOpt -> QuizSucessoBorda
                                quizAnswer != null && isSelected && !isCorrectOpt -> QuizErroBorda
                                else -> QuizDefaultBorda
                            }

                            val badgeBg = when {
                                quizAnswer != null && isCorrectOpt -> QuizSucessoBadge
                                quizAnswer != null && isSelected && !isCorrectOpt -> QuizErroBadge
                                else -> QuizDefaultBadge
                            }

                            val badgeTextColor = when {
                                quizAnswer != null && isCorrectOpt -> QuizSucessoTexto
                                quizAnswer != null && isSelected && !isCorrectOpt -> QuizErroTexto
                                else -> QuizDefaultTexto
                            }

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(cardBg)
                                    .border(1.5.dp, borderColor, RoundedCornerShape(14.dp))
                                    .clickable(enabled = quizAnswer == null) { onSelectAnswer(i) }
                                    .padding(horizontal = 14.dp, vertical = 11.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Badge com Letra (A, B, C, D)
                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .clip(CircleShape)
                                            .background(badgeBg),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            letters.getOrElse(i) { "•" },
                                            color = badgeTextColor,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Text(
                                        text = opt,
                                        color = TextDark,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        lineHeight = 18.sp,
                                        modifier = Modifier.weight(1f)
                                    )

                                    // Ícone de status no lado direito
                                    if (quizAnswer != null) {
                                        if (isCorrectOpt) {
                                            Box(
                                                modifier = Modifier
                                                    .size(22.dp)
                                                    .clip(RoundedCornerShape(4.dp))
                                                    .background(QuizSucessoBorda),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text("✓", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                            }
                                        } else if (isSelected) {
                                            Text("✕", color = QuizErroBorda, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }

                        // Banner de Feedback Explicativo (100% visível)
                        if (quizAnswer != null) {
                            Spacer(modifier = Modifier.height(12.dp))
                            val isCorrect = quizAnswer == correct
                            val feedbackBg = if (isCorrect) QuizSucessoCardBg else QuizErroCardBg
                            val feedbackTextColor = if (isCorrect) QuizSucessoTexto else VermelhoSeta
                            val feedbackText = if (isCorrect)
                                "🎉 Parabéns! ${quiz.explanation}"
                            else
                                "✕ Quase! ${quiz.explanation}"

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(feedbackBg)
                                    .padding(horizontal = 14.dp, vertical = 12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = feedbackText,
                                    color = feedbackTextColor,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    lineHeight = 18.sp,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (stepIndex < 2 || quizAnswer != null) {
                Button(
                    onClick = onContinue,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
                ) {
                    Text(
                        if (stepIndex < 2) "Continuar →" else "Concluir aula 🎉 +${lesson.moduleId * 10 + 40} XP",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}
