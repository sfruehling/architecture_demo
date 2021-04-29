# Hexagonale Architektur
Dieses Projekt erlaubt es, einzelne User anzulegen und ihnen Rollen zuzuweisen. 
Rollen definieren sich daher, daß man bestimmt Rechte/Privilegien inne hat.
Den Rollen können Rechte/Privilegien zugewiesen werden.
Die Schnittstelle `grantedAuthorities` erlaubt es die jeweiligen Rechte für einen User abzufragen.
Ausserdem stellt das Projekt noch 3 Actuatoren zur Verfügung.

Du findest hier eine (grobe) hexagonale Struktur vor.
Alle Klassen sind jedoch in einem Paket `de.andrena.basepackage` im Modul `domain`.

* Auf Web-Security wurde komplett verzichtet.
* Tests sind der Einfachheit halber weggelassen.
* pom.xml sollten nicht geändert werden müssen (ausser in Aufgabe 2 :)


### Aufgaben
1) Verteile die einzelnen Klassen aus `de.andrena.basepackage` auf die vorgegebenen Maven-Module
2) Wie könnte man die einzelnen Module tiefer strukturieren?
3) Befreie die Domäne von Spring Annotationen!
4) Entspricht der VersionEndpoint der Hexagonalen Architektur? Warum? Wie müsste man die Applikation ändern?
5) Welche Aufgabe können Interfaces haben? Welche haben sie in diesem Projekt?

### Feedback
1) Lauffähig wäre schön, mind. lauffähige Tests
2) Den hexagonalen Ansatz sieht man nur bedingt. Besser wären mehrere Adapter (z.B. Excel-Export, Trigger,...)



