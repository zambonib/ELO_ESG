const PptxGenJS = require('pptxgenjs');
let pptx = new PptxGenJS();

pptx.layout = 'LAYOUT_16x9';

// Define a master slide design
pptx.defineSlideMaster({
  title: 'MASTER_SLIDE',
  background: { color: 'FFFFFF' },
  objects: [
    { rect: { x: 0, y: 0, w: '100%', h: 0.8, fill: { color: '0F4C81' } } },
    { text: { text: 'ELO ESG - Apresentação de Projeto', options: { x: 0.5, y: 0.1, w: 9.0, h: 0.6, fontSize: 18, color: 'FFFFFF', bold: true } } }
  ]
});

// Slide 1: Capa
let slide1 = pptx.addSlide({ masterName: 'MASTER_SLIDE' });
slide1.addText('App ELO ESG', { x: 1.0, y: 2.0, w: 8.0, h: 1.5, fontSize: 44, bold: true, color: '0F4C81', align: 'center' });
slide1.addText('Apresentação Técnica e Funcional do Projeto', { x: 1.0, y: 3.5, w: 8.0, h: 1.0, fontSize: 24, color: '363636', align: 'center' });

// Slide 2: Objetivo do Aplicativo
let slide2 = pptx.addSlide({ masterName: 'MASTER_SLIDE' });
slide2.addText('Objetivo do Aplicativo', { x: 0.5, y: 1.0, w: 9.0, fontSize: 32, bold: true, color: '0F4C81' });
slide2.addText(
    'O ELO ESG tem como principal objetivo integrar tecnologia e responsabilidade socioambiental, ajudando o usuário a:\n' +
    '• Ter acesso rápido a serviços sociais públicos (como o CRAS).\n' +
    '• Gerenciar finanças pessoais de forma sustentável (Governança).\n' +
    '• Aprender sobre sustentabilidade e economia (Ambiental e Educação).\n' +
    '• Criar hábitos saudáveis e rastrear conquistas.', 
    { x: 0.5, y: 2.0, w: 9.0, h: 3.0, fontSize: 20, color: '363636', bullet: true }
);

// Slide 3: Tecnologia Escolhida
let slide3 = pptx.addSlide({ masterName: 'MASTER_SLIDE' });
slide3.addText('Tecnologia Escolhida', { x: 0.5, y: 1.0, w: 9.0, fontSize: 32, bold: true, color: '0F4C81' });
slide3.addText(
    '• Linguagem: Kotlin (Nativa Android)\n' +
    '• Interface (UI): Jetpack Compose (Declarativa e moderna)\n' +
    '• Arquitetura: MVVM (Model-View-ViewModel)\n' +
    '• Injeção de Dependência: Dagger Hilt\n' +
    '• Banco de Dados/Auth: Firebase (Firestore)\n' +
    '• Chamadas de Rede: Retrofit e Gson (Consumo REST)\n' +
    '• Assincronismo: Coroutines e StateFlow',
    { x: 0.5, y: 2.0, w: 9.0, h: 3.0, fontSize: 20, color: '363636', bullet: true }
);

// Slide 4: Aplicação no Contexto ESG
let slide4 = pptx.addSlide({ masterName: 'MASTER_SLIDE' });
slide4.addText('Aplicação no Contexto ESG', { x: 0.5, y: 1.0, w: 9.0, fontSize: 32, bold: true, color: '0F4C81' });
slide4.addText(
    '• E (Environmental - Ambiental): Telas "Economize" ajudam o usuário a adotar práticas sustentáveis em casa e economizar recursos.\n\n' +
    '• S (Social): Busca e navegação automatizada para os CRAS (Centro de Referência de Assistência Social), além da tela "SocialScreen" e "Education".\n\n' +
    '• G (Governance - Governança): Tela "Finanças" para gestão financeira, promovendo transparência e governança a nível individual.',
    { x: 0.5, y: 2.0, w: 9.0, h: 3.0, fontSize: 20, color: '363636' }
);

// Helper function to create screen slides with image placeholders
function addScreenSlide(title, description, screenName) {
    let slide = pptx.addSlide({ masterName: 'MASTER_SLIDE' });
    slide.addText(title, { x: 0.5, y: 1.0, w: 9.0, fontSize: 28, bold: true, color: '0F4C81' });
    slide.addText(description, { x: 0.5, y: 1.6, w: 4.5, h: 3.5, fontSize: 18, color: '363636' });
    
    // Placeholder para a Imagem Obrigatória
    slide.addShape(pptx.ShapeType.rect, { x: 5.5, y: 1.2, w: 3.5, h: 4.0, fill: { color: 'F0F0F0' }, line: { color: '999999', dashType: 'dash', width: 2 } });
    slide.addText('COLAR IMAGEM OBRIGATÓRIA AQUI\n\n(' + screenName + ')', { x: 5.5, y: 1.2, w: 3.5, h: 4.0, fontSize: 14, color: '999999', align: 'center', valign: 'middle' });
}

// Slides das Telas
addScreenSlide('1. Tela de Autenticação (Login / Cadastro)', 
    'Funcionalidade: Permite a entrada segura do usuário no aplicativo.\n\n' +
    'Comentários Relevantes:\n' +
    '- Integração com Firebase Auth.\n' +
    '- ViewModels controlando validações de campos.\n' +
    '- UI moderna com botões focados em UX e StateFlow para reatividade.', 'LoginTela.kt / CadastroTela.kt');

addScreenSlide('2. Dashboard Principal', 
    'Funcionalidade: Centro de navegação do aplicativo, mostrando o resumo do perfil.\n\n' +
    'Comentários Relevantes:\n' +
    '- Uso da barra de navegação principal (BarraNavegacaoElo).\n' +
    '- Ponto de partida para Finanças, Social e Economia.', 'DashboardTela.kt');

addScreenSlide('3. Busca de CRAS (Centro de Referência)', 
    'Funcionalidade: Permite buscar unidades de CRAS e traçar rotas diretas.\n\n' +
    'Comentários Relevantes:\n' +
    '- Usuário digita CEP, consumindo API ViaCEP.\n' +
    '- Exibe resultados de assistência social (JSON/Firebase).\n' +
    '- Integração "One-Click" com URL de Navegação do Google Maps (Directions API).', 'CrasSearchScreen.kt');

addScreenSlide('4. Finanças e Economize', 
    'Funcionalidade: Gestão monetária (G) e práticas sustentáveis (E).\n\n' +
    'Comentários Relevantes:\n' +
    '- FinancasTela.kt: Entradas e saídas financeiras.\n' +
    '- EconomizeScreen.kt: Guias para redução do consumo elétrico e hídrico.\n' +
    '- ConquistasScreen.kt: Gamificação (pontuação por atitudes ESG).', 'FinancasTela.kt / EconomizeScreen.kt');

addScreenSlide('5. Social e Educação', 
    'Funcionalidade: Acesso a conteúdo comunitário e trilhas de estudo (S).\n\n' +
    'Comentários Relevantes:\n' +
    '- EducationScreen.kt: Componente vitalício de melhoria educacional.\n' +
    '- SocialScreen.kt: Impacto na comunidade e acesso a programas governamentais.', 'EducationScreen.kt / SocialScreen.kt');

// Slide: Serviços APIs
let slide10 = pptx.addSlide({ masterName: 'MASTER_SLIDE' });
slide10.addText('Serviços Consumidos (APIs HTTPS)', { x: 0.5, y: 1.0, w: 9.0, fontSize: 32, bold: true, color: '0F4C81' });
slide10.addText(
    'O ELO ESG consome os seguintes serviços e APIs externas para seu funcionamento:\n\n' +
    '1. ViaCEP API (Busca de Endereços):\n' +
    '• Endereço: https://viacep.com.br/ws/\n' +
    '• Uso: Converte o CEP digitado no CRAS em endereço completo.\n\n' +
    '2. Google Maps Navigation API:\n' +
    '• Endereço: https://www.google.com/maps/dir/\n' +
    '• Uso: Redireciona o usuário (Intent) traçando a rota até o CRAS.\n\n' +
    '3. Firebase / Firestore:\n' +
    '• Endereço: APIs nativas do Google SDK (https://firebase.googleapis.com/)\n' +
    '• Uso: Autenticação e sincronismo do banco de dados na nuvem.',
    { x: 0.5, y: 2.0, w: 9.0, h: 4.0, fontSize: 18, color: '363636' }
);

pptx.writeFile({ fileName: 'Apresentacao_ELO_ESG.pptx' })
  .then(fileName => {
    console.log(`Presentation gerada com sucesso: ${fileName}`);
  })
  .catch(err => {
    console.log(`Erro ao gerar: ${err}`);
  });
