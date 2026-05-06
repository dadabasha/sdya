function money(v){return new Intl.NumberFormat('en-IN',{style:'currency',currency:'INR'}).format(v||0)}
async function bluetoothPrint(text){
 try{
  if(!navigator.bluetooth){alert('Web Bluetooth is not supported in this browser. Use Chrome/Edge on Android/Windows, or Browser Print.');return;}
  const device=await navigator.bluetooth.requestDevice({acceptAllDevices:true,optionalServices:['000018f0-0000-1000-8000-00805f9b34fb','0000ffe0-0000-1000-8000-00805f9b34fb','49535343-fe7d-4ae5-8fa9-9fafd205e455']});
  const server=await device.gatt.connect();
  const services=await server.getPrimaryServices();
  let writable=null;
  for(const s of services){const chars=await s.getCharacteristics();for(const c of chars){if(c.properties.write||c.properties.writeWithoutResponse){writable=c;break}}if(writable)break}
  if(!writable){alert('Printer connected, but writable service not found. Pair the printer in system Bluetooth and try Browser Print.');return;}
  const enc=new TextEncoder();
  const receipt='\x1B@\x1B!\x08'+text+'\n\n\n\x1DVA\x00';
  const data=enc.encode(receipt);
  for(let i=0;i<data.length;i+=180){await writable.writeValue(data.slice(i,i+180));}
  alert('Receipt sent to thermal printer.');
 }catch(e){alert('Bluetooth print failed: '+e.message+'\nTip: turn on printer, pair it, use Chrome/Edge, then try again.');}
}
function renderBar(id,labels,values){const c=document.getElementById(id);if(!c)return;new Chart(c,{type:'bar',data:{labels:labels,datasets:[{label:'Amount',data:values,borderWidth:1}]},options:{responsive:true,plugins:{legend:{display:false}},scales:{y:{beginAtZero:true}}}})}
