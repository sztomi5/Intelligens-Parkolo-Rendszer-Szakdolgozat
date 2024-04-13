#include <WiFiNINA.h>
#include <Config.h>
#include <Firebase.h>
#include <Firebase_Arduino_WiFiNINA.h>
#include <Firebase_TCP_Client.h>
#include <WCS.h>
#include <LiquidCrystal.h>
#include <SPI.h>

//Kapcsolódási azonosítók
#define FIREBASE_HOST ""		//Adatbázis elérési útvonala
#define FIREBASE_AUTH ""		//Adatbázishoz szükséges autentikációs kód
#define WIFI_SSID ""			//Wi-Fi hálózat SSID-ja
#define WIFI_PASSWORD ""		//Wi-Fi hálózat jelszava

FirebaseData fbdo;		//Adatbázisra hivatkozás azonosítója

int slotsPins[] = {2, 3, 4, 5};		//Panel szenzorhoz kapcsolódó lábainak tömbje
int slots = 20;						//Parkolóhelyek száma
bool states[20];					//Parkolóhelyek foglaltsági állapotait tároló tömbje
String names[20];					//Parkolóhelyek azonosítóit tároló tömbje
int available = 0;					//Szabad parkolóhelyeket tároló változó

LiquidCrystal lcd(12, 11, 10, 9, 8, 7);		//Kijelző panelhez kapcsolódásának pontjai (RS, EN, data4, data5, data6, data7)

void setup() {
  int stry = 0;				//Soros port indulásának ellenőrzését szabályozó változó
  Serial.begin(9600);		//Soros port indítása
  //Soros port állapotának ellenőrzése 
  while ((!Serial) && (stry < 10)) {
    delay(500);
    stry++;
  }
  
  lcd.begin(16, 2);		//LCD kijelző konfigurálása és indítása
  lcd.print("Starting!");		//Kezdő üzenet küldése a kijelzőre
  lcd.noCursor();		//Kijelző kurzor megjelenítésének letiltása

  connectToAP();		//Kapcsolódás a Wi-Fi hálózathoz és adatbázishoz

  randomSeed(millis());		//Véletlenszám generátor felkonfogurálása

  //Panel szenzorhoz kapcsolódó lábainak bemenetként való konfigurálása
  for(int i : slotsPins){
    pinMode(i, INPUT);
  }
  
  //Parkolóhelyek azonosítóinak legenerálása s eltárolása tömbben
  for(int i = 0; i < 2; i++){
    for(int j = 0; j < (slots/2); j++){
      int s = j + (i * (slots/2));
      names[s] = (j < 9) ? "P" + String(i + 1) + "0" + String(j + 1) : "P" + String(i + 1) + String(j + 1);
    }
  }

  //Kezdő állapotok beállítása
  read();
}

void connectToAP() {
  int wtry = 0;		//Hálózathoz kapcsolódás számát szabályozó változó
  
  //Hálózathoz kapcsolódási kísérletek szabályozása
  while ((WiFi.status() != WL_CONNECTED) && (wtry < 5)) {
    if(Serial){
      Serial.print("Attempting to connect to SSID: ");
      Serial.println(WIFI_SSID);
    }
    
    WiFi.begin(WIFI_SSID, WIFI_PASSWORD);		//Kapcsolódás a Wi-Fi hálózathoz

    wtry++;
    delay(1000);
  }

  if(WiFi.status() == WL_CONNECTED){
    if(Serial) printWifiStatus();		//Wi-Fi hálózathoz kapcsolódás állapota

    Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH, WIFI_SSID, WIFI_PASSWORD);		//Kapcsolódás az adatbázishoz
    Firebase.reconnectWiFi(true);
  }
}
 
// Print WiFi connection results to serial monitor  
void printWifiStatus() { 
  Serial.println("Connected...");

  Serial.print("SSID: ");
  Serial.println(WiFi.SSID());
  
  IPAddress ip = WiFi.localIP();		//Eszköz IP címét eltároló változó
  Serial.print("IP Address: ");
  Serial.println(ip);
}

void read(){
  available = 0;

  //Szenzorok állapotának olvasása a bemenetről
  for (int i = 0; i < 4; i++) {
    states[i] = digitalRead(slotsPins[i]);		//Szenzorok állapotának eltárolása tömbben
  }
  
  //Virtuális parkolóhelyek állapotának generálása
  for(int i = 4; i < slots; i++){
    states[i] = random(2);
  }

  for(int j = 0; j < slots; j++){
    if (states[j] == true) {		//Parkolóhely szabadságának ellenőrzése
      available++;		//Szabad parkolóhelyek számának növelése eggyel
    }

    Firebase.setBool(fbdo, ("/Parking system/Slots/" + names[j]), states[j]);		//Parkolóhely azonosítójának és állapotának frissítése az adatbázisban
  }

  lcd.clear();						//Kijelző tartalmának törlése
  lcd.setCursor(0,0);				//Kurzor mozgatása az első sor első oszlopába
  lcd.print("Szabad helyek:");		//Kijelzőre való kiíratás
  lcd.setCursor(0,1);				//Kurzor mozgatása egy sorral lejebb
  lcd.print(available);				//Szabd parkolóhelyek számának kijelzőn való megjelenítése

  Firebase.setInt(fbdo, "/Parking system/Available", available);		//Szabad parkolóhelyek számának frissítése az adatbázisban
}

void loop() {
  //Szenzorok állapot változásának ellenőrzése
  for (int i = 0; i < 4; i++) {
    if (states[i] != digitalRead(slotsPins[i])) read();		//Parkolóhelyek adatait frissítő függvény meghívása	
  }
  delay(1000);
}