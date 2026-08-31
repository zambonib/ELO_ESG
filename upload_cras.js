const { initializeApp, cert } = require('firebase-admin/app');
const { getFirestore } = require('firebase-admin/firestore');

const serviceAccount = require('./elo-esg-firebase-adminsdk-fbsvc-312553a4f9.json');

initializeApp({
  credential: cert(serviceAccount)
});

const db = getFirestore();
const crasData = require('./cras_brasil.json');

async function uploadData() {
  console.log(`Iniciando o upload de ${crasData.length} registros para o Firestore... (Usando Merge para não duplicar dados)`);
  
  const collectionRef = db.collection('cras');
  let batch = db.batch();
  let count = 0;
  let batchCount = 0;

  for (let i = 0; i < crasData.length; i++) {
    const item = crasData[i];
    
    if (item.georef_location) {
        let cleanLocation = item.georef_location.replace(/["\\]/g, '');
        let parts = cleanLocation.split(',');
        if (parts.length === 2) {
            item.latitude = parseFloat(parts[0]);
            item.longitude = parseFloat(parts[1]);
        }
    }

    // Se o id tiver uma barra (/), o Firestore quebra. Vamos substituir por traço ou ignorar.
    let docId = item.id_equipamento;
    if (docId && typeof docId === 'string') {
        docId = docId.replace(/\//g, '-');
    }

    const docRef = docId ? collectionRef.doc(docId) : collectionRef.doc();
    
    // Usa o merge para sobrescrever em vez de duplicar
    batch.set(docRef, item, { merge: true });
    count++;

    if (count === 490) {
      await batch.commit();
      batchCount++;
      console.log(`Lote ${batchCount} gravado com sucesso! (490 registros)`);
      
      batch = db.batch();
      count = 0;
    }
  }

  if (count > 0) {
    await batch.commit();
    console.log(`Lote final gravado com sucesso! (${count} registros)`);
  }

  console.log('Upload concluído com sucesso!');
}

uploadData().catch(console.error);
