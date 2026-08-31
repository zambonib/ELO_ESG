const fs = require('fs');
const readline = require('readline');
const path = require('path');

const inputFilePath = 'C:\\Users\\profc\\Downloads\\CRAS-Brasil.txt';
const outputFilePath = path.join(__dirname, 'cras_brasil.json');

async function processLineByLine() {
    const fileStream = fs.createReadStream(inputFilePath, { encoding: 'utf8' });
    const rl = readline.createInterface({
        input: fileStream,
        crlfDelay: Infinity
    });

    let headers = [];
    let isFirstLine = true;
    const records = [];

    // Simple CSV parser function to handle quotes
    function parseCsvLine(line) {
        const result = [];
        let current = '';
        let inQuotes = false;
        
        for (let i = 0; i < line.length; i++) {
            const char = line[i];
            if (char === '"') {
                inQuotes = !inQuotes;
            } else if (char === ',' && !inQuotes) {
                result.push(current);
                current = '';
            } else {
                current += char;
            }
        }
        result.push(current);
        return result;
    }

    for await (const line of rl) {
        if (!line.trim()) continue;
        
        const row = parseCsvLine(line);
        
        if (isFirstLine) {
            headers = row.map(h => h.trim());
            isFirstLine = false;
        } else {
            const record = {};
            row.forEach((value, index) => {
                if (headers[index]) {
                    record[headers[index]] = value.trim();
                }
            });
            records.push(record);
        }
    }

    fs.writeFileSync(outputFilePath, JSON.stringify(records, null, 2));
    console.log(`Conversão concluída! Arquivo salvo em: ${outputFilePath}`);
    console.log(`Total de CRAS processados: ${records.length}`);
}

processLineByLine().catch(console.error);
