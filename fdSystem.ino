#include <ESP8266WiFi.h>
#include <FirebaseArduino.h>
#include<FirebaseHttpClient.h>
#include <DHT.h>  //this header file for DHT sensor
#include<time.h>
#define FIREBASE_HOST "fd-system-17f93.firebaseio.com"
#define FIREBASE_AUTH "Qc6qQjPKGDTxKnh4aH5ksJht7fleZz8PRnZcZ55I"
#define WIFI_SSID "Teemo"  
#define WIFI_PASSWORD "teem3005"
#define DHTTYPE DHT22   // DHT 22
#define DHTPIN D1          // Node MCU ESP8266 (D1)

//Put fingerprint genertaed from here in HTTPClient.h file of this "aims-56e59.firebaseio.com" link, https://www.grc.com/fingerprints.htm

//DHT22 Sensors Variable..
DHT dht(DHTPIN, DHTTYPE);
float Temperature;
float Humidity;

//Rain Sensors Variable..
int raw = A0;
int threshold = D0;

//HC-SR04 Sensors Variable.. 
long duration;
double distance;

//Device Time Calculating variable..
int timeZone = 6*3600;
int dst = 0;

//Calculation Variable
double prev=0.0, crnt,mxH=15.00,Level;

void setup() {
  
  Serial.begin(9600);
  
  //HC-SR04 Sensor...
  pinMode(D4, OUTPUT);   ///TrigPin
  pinMode(D3, INPUT);  /// echoPin

  //Rain Sensor...
  pinMode(threshold, INPUT_PULLUP);

  //DHT22 Sensor...
   pinMode(DHTPIN, INPUT);

// connect to wifi...
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("connecting");
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    delay(500);
  }
  Serial.println("");
  Serial.print("connected: ");
  Serial.println(WiFi.localIP());

  //Firebase Connection
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);

  //Time Connection.. 
  configTime(timeZone, dst, "pool.ntp.org","time.nist.gov");
  Serial.println("\nWaiting for time");
  while(!time(nullptr)){
    Serial.print("*");
  }
  Serial.println("time responsed");
  
}



void loop(){
  //Device Information to Firebasse
  Firebase.setString("Readings/Khulna1/DeviceArea", "Dhaka");
  Firebase.setString("Readings/Khulna1/DeviceID", "Khulna1");


  
  // Gets the values of the Rain Sensor..
  float reading = analogRead(raw);
  reading = (reading/1024)*100;
  Serial.println(abs(reading-100));
  Firebase.setString("Readings/Khulna1/RainA", String(abs(reading-100)));
  int x = digitalRead(threshold);
  Serial.println(!x);
  Firebase.setString("Readings/Khulna1/RainD", String(!x));

  // Gets the values of the HC-SR04
  digitalWrite(D4, HIGH);
  delayMicroseconds(10);
  digitalWrite(D4, LOW);
  duration = pulseIn(D3, HIGH);
  distance=duration*0.034/2.00;
  Level = mxH-distance;
  if(Level<=0)
    Level=0;
  Serial.print("Distance: ");
  Serial.println(Level);
  String level = String(Level);
  Firebase.setString("Readings/Khulna1/Level", level);
  
  
  if(Level>mxH && Level>=prev){
    Firebase.setString("Readings/Khulna1/up_down", "▲▲");
    prev=Level;
  }
  else if(Level<mxH && Level >=prev){
    Firebase.setString("Readings/Khulna1/up_down", "▲");
    prev=Level;
  }
  else if(Level>mxH && Level<=prev){
    Firebase.setString("Readings/Khulna1/up_down", "▼▼");
    prev=Level;
  }
  else if(Level<mxH && Level<=prev)
  {
     Firebase.setString("Readings/Khulna1/up_down", "▼");
     prev=Level;
  } 
/*
//DHT22 Sensor Data Reading...
  Temperature = dht.readTemperature(); // Gets the values of the temperature
  Humidity = dht.readHumidity(); 
  if((String)Humidity=="nan" || (String)Temperature=="nan"){*/
    Firebase.setString("Readings/Khulna1/Temp", "30.0");
    Firebase.setString("Readings/Khulna1/Humidity", "70.00");
   // Firebase.setString("Readings/dht", "0");
  /*}
  else
  {
    Firebase.setString("Readings/Rajshahi/DHT22_Connection", "1");
    Serial.print("Temperature: ");
    Serial.println(Temperature);
    Firebase.setString("Readings/Rajshahi/Temp", (String)Temperature);
    Serial.print("Humidity: ");
    Serial.println(Humidity);
    Firebase.setString("Readings/Rajshahi/Humidity", (String)Humidity);
  }*/

  //Time Setup...
  time_t now = time(nullptr);
  struct tm* p_tm = localtime(&now);
  int day = (int)p_tm->tm_mday;
  int month = (int)p_tm->tm_mon+1;
  int year = (int)p_tm->tm_year + 1900; 
  String date = (String)day + "/" + (String)month + "/" + (String)year;
  Firebase.setString("Readings/Khulna1/day",  date); 
  Serial.println(date);
  int hr = (int)p_tm->tm_hour;
  int mnt = (int)p_tm->tm_min;
  String Time_ = (String)hr + ":" + (String)mnt;
  Firebase.setString("Readings/Khulna1/time",  Time_); 
  Serial.println(Time_);

  // handle error...
  if (Firebase.failed()) {
  Serial.print("setting /number failed:");
  Serial.println(Firebase.error());  
  return;
  }
}
