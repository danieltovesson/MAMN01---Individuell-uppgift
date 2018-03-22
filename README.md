# MAMN01 - Individuell uppgift
Appen består av 3 vyer:

* En startskärm med två knappar som tar dig till de andra vyerna. Knappen ”Compass” tar dig till vyn för att se en kompass, knappen ”Accelerometer” tar dig till vyn för accelerometern.

* Kompassvyn visar vilken grad mobilen pekar åt och vibrerar och avger ljud om den pekar norrut. 

* Accelerometervyn visar vilken x-, y- och z-riktning mobilen har. Håller man telefonen platt så kommer skärmen att lysa grönt. Om man roterar telefonen kommer man se vilken riktning man roterar åt.

Använde denna guiden för att skapa kompassen  
https://www.wlsdevelop.com/index.php/en/blog?option=com_content&view=article&id=38  
*Ändrade även så appen inte kan rotera när telefonen roteras. Döpte om en hel del variabler så de var mer förklarande. Lade till så att lastAccelerometerSet och lastMagnetFieldSet blir satta till false när koordinater uppdaterats. Lade även till en koll om telefonen pekar norrut.*

Använde detta svaret för att få telefonen att vibrera  
https://stackoverflow.com/a/13950364/1206388

Använde denna guiden för att få telefonen att spela upp ljud  
https://developer.android.com/guide/topics/media/mediaplayer.html  
*Lade även till så att den inte kan spela när den redan spelar upp ett ljud genom att kolla .isPlaying().*

Använde detta svaret för att sätta strängar korrekt  
https://stackoverflow.com/a/40715374/1206388

Använde denna guiden för att visa accelerometerns koordinater  
https://developer.android.com/reference/android/hardware/SensorEvent.html  
*Använde bara event.values[i] som jag skrev in i textfälten.*

Använde detta svaret för att visa riktningen telefonen lutar  
https://stackoverflow.com/a/10477107/1206388  
*Skrev om strängarna så att den mer förklarande visar hur telefonen lutar*
