# Backend

Das backend hat eine hexagonale Architektur und besteht aus vier Modulen:
* Configuration
* Domain
* Persistence_out_adapter
* Web_in_adapter.

Die Struktur ist basierend auf Tom Hombergs "Get your hands dirty on clean architecutre".

Die Architektur wird durch einen Maven-Multi-Modul-Ansatz unterstützt.
Im Gegensatz zur Verwendung von Arch-Unit hat der Multi-Modul-Ansatz den Vorteil, dass Verletzungen weniger leicht passieren können.

Die verschiednen Adapter sollen sich untereinander nicht kennen.

Das `docker-compose.yml`-File erlaubt das Starten einer DB, sodass die Applikation lokal ausprobiert werden kann.

> Aktuell werden alle Dependencies in der 'globalen' pom.xml definiert.
> Schöner wäre an dieser Stelle, wenn für jeden Adapter die individuell notwendigen Abhängigkeiten definiert würden.
> Der Einfachheit lass ich das aber so.

### Configuration-Modul
Dieses Modul baut die Web-Applikation zusammen.
Es hat Abhängigkeiten zu allen anderen Modulen.
Es enthält die Application.java und die Möglichkeit End-2-End-Tests (E2E-Tests) für die ganze Application anzubieten.
Für die E2E-Tests wird mit Hilfe eines Postgres-Test-Containers eine DB zum Testzeitpunkt hochgezogen und damit dann die Tests ausgeführt.
So wird sichergestellt, daß E2E-Tests auf jedem (Docker-) Host ausgeführt werden können - ohne Abhängigkeit zu einer Integrations-Test-Datenbank.

Normalerweise könnte hier die Spring-Konfigurationen für jeden Adapter abgelegt werden. 
So könnte man die Domäne frei von einer Spring-Abhängigkeit halten.
Wir verzichten hier aber darauf und verwenden in der Domäne die Spring-Annotationen.

Das Configuration-Modul enthält die Archunit-Tests. 
Diese prüfen auf Zyklenfreiheit und (unnötigerweise) auf die Einhaltung der Architektur.
Dieses Modul hat Abhängigkeiten zu allen verwendeten Modulen und ist somit perfekt für die zentrale Prüfung auf Paketzyklen geeignet.

In diesem Adapter doppelt sich die Existenz der Klassen `WithArchitectureDemoPostgresTestContainer` und `ArchitectureDemoPostgreSqlTestContainer`.
Dieser Umstand ist der Tatsache geschuldet, daß Klassen aus dem Test-Bereich eines Maven-Moduls nicht auf Test-Klassen eines anderen Maven-Moduls zugreifen dürfen.

> Evtl  Archunit tests umziehen!


### Domaine
Die Domäne enthält die Business-Logik der Applikation.
Üblicherweise sollte die Domäne frei von irgendwelchen Frameworks sein.
Aber wir erlauben hier Lombok und einige Spring-Annotation.
So Gestalten sich die Tests im Domänenbereich als sehr einfach.
Es gibt vier ?Aggregates? in der Domaine:
* grantedAuthorithies: Andere Dienste erfragen hier die Rechte für einen User
* privilege: Das Anlegen und Verwalten von Rechten
* role: Das Anlegen und Verwalten von Rollen
* user: Das Anlegen und Verwalten von Usern

Eingehende Schnittstellen (z.B. Web-Adapter) auf Aggregates werden als `Usecases` bezeichnet und durch ein `...Usecase-Suffix` markiert.
Alle solche Schnittstellen werden in einem Paket `in` gesammelt.
Für Usecases müssten eigentlich keine separaten Schnittstellen eingeführt werden, 
weil die Abhängigkeitsrichtung der implementierenden Klassen schon in die Richtige Richtung - hin zur Domäne -  zeigt.
Wir markieren solche Schnittstellen dennoch explizit - um den Eintritt in die Domäne zu kennzeichnen und um unnötig lange Abhängigkeitsketten zu vermeiden.

Ausgehende Schnittstellen (z.B. zur Persistenz) werden als `Ports` bezeichnet und durch ein `...Port-Suffix` markiert.
Alle Port-Schnittstellen werden in einem Paket `out` gesammelt.
Im bereitstellenden Adapter werden die jeweiligen Implementierungen der Ports durch `Adapter` realisiert.
Die jeweiligen Klassen tragen ein `...Adapter-Suffix`.

> Die Klasse `DataPopulator.java` ist (leider) in die Domäne gerutscht. 
> Fraglich ist, ob diese Klasse tatsächlich verwendet wird. 
> Aber löschen ist so eine Sache...

### Web-In-Adapter
Der Web-In-Adapter ist ein eingehender Adapter.
Er nimmt Http-Requests entgegen, überführt die, für die Kommunikation verwendeten, Strukturen in Domänen-Objekte und triggert Usecases in der Domäne.
Alle Klassen des Web-Adapters sind im Paket `(*).web` abgelegt.
Neben den Aggregates der Domäne stellt dieser Adapter noch Actuatoren zur Verfügung:
* Version
* Info (benutzt?)
* Health

> Der direkte Zugriff auf die Version im Version-Actuator ist zweifelhaft. 
> Offensichtlich ist die Versionsinformation eine Funktionalität, die das Backend zur Verfügung stellt.
> Eigentlich müsste deshalb die Version über die Domäne aus einem eigenen Adapter geladen werden :-/

Die Konfiguration von Spring-Security ist im Paket `security` abgelegt.

Um die Pact-Tests starten zu können liegt im `it`-Ordner eine Datei `WebConfiguration`.
Diese brauchen die Spring-Boot-Tests im `it`-Ordner um starten zu können.
Die Datenbank-Konfiguration ist in dieser Konfigurationsdatei deaktiviert.
Im IntegrationsTest-Ordner `it` sollen Integrationstests abgelegt werden, die maximal die Domäne miteinbeziehen.
Umfangreichere Tests müssten im Configuration-Modul abgelegt werden.

... vermutlich könnte man die `application.properties` noch stark reduzieren.

> Kann man die application properties aggregieren?
> 

### Persistenz-Out-Adapter
Hier ist die Anbindung an die Datenbank abgelegt. 
Die Klassen, die Ports (aus der Domäne) implementieren sind mit `Adapter` ge-suffixt.
Dieses Modul setzt `Spring Data Jpa` ein und enthält deshalb Repository-Interfaces.

In den Integrationstest befindet sich eine eigene PersistenceConfiguration, die explizit den MVC-Teil ausschließt.
Es sollen hier nur Integrationstests abgelegt werden, die maximal die Domäne mit einschließen.
Umfangreichere Tests müssen im Configuration-Modul abgelegt werden.

In diesem Adapter doppelt sich die Existenz der Klassen `WithArchitectureDemoTestContainer` und `ArchitectureDemoPostgreSqlTestContainer`.
Dieser Umstand ist der Tatsache geschuldet, dass Klassen aus dem Test-Bereich eines Maven-Moduls nicht auf Test-Klassen eines anderen Maven-Moduls zugreifen dürfen.

> Maven Test Jar?

