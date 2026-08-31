const fs = require('fs');
const file = 'app/src/main/java/br/com/projeto/elo/ui/cras/CrasSearchScreen.kt';
let content = fs.readFileSync(file, 'utf8');

content = content.replace(/Buscar CRAS Pr[^"]*/, 'Buscar CRAS Próximo');
content = content.replace(/Digite seu CEP para encontrar as unidades do CRAS mais pr[^x]*ximas da sua resid[^n]*ncia\./, 'Digite seu CEP para encontrar as unidades do CRAS mais próximas da sua residência.');
content = content.replace(/text = "[^"]*?enderecoEncontrado",/, 'text = "📍 $${enderecoEncontrado}",');
content = content.replace(/Text\("[^"]*Toque para tra[^"]*ar a rota no mapa"/, 'Text("📍 Toque para traçar a rota no mapa"');

fs.writeFileSync(file, content, 'utf8');
