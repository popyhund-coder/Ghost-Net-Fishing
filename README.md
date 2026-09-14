# Ghost Net Fishing

Prototypische Java-EE-Webanwendung zur Meldung und Bergung von Geisternetzen.

## Sprint-Auswahl

Im ersten Sprint werden fünf Backlog-Anforderungen prototypisch umgesetzt:

1. **MUST:** Geisternetze anonym erfassen.
2. **MUST:** Bergende Personen für die Bergung eines Netzes eintragen.
3. **MUST:** Noch zu bergende Netze anzeigen.
4. **MUST:** Geisternetze als geborgen melden.
5. **COULD:** Geisternetze als verschollen melden.

Die optionale Weltkarte und die Anzeige aller Zuordnungen zwischen Personen und Netzen sind für diesen Sprint nicht umgesetzt.

## Technologiestack

- Jakarta EE 10
- Jakarta Faces (JSF) 4.0
- CDI / Jakarta Beans
- Jakarta Persistence (JPA)
- relationale MySQL-Datenbank
- Maven
- CSS ohne zusätzliche UI-Komponentenbibliothek

Jakarta EE 10 enthält unter anderem CDI 4.0, Jakarta Faces 4.0 und Jakarta Persistence. Die Anwendung nutzt die containerseitige JPA-Implementierung des verwendeten Jakarta-EE-Servers.

## Voraussetzungen

- Java 17+
- Maven 3.9+
- MySQL 8.4+
- Jakarta-EE-10-kompatibler Application Server, z. B. Payara 6 oder ein vergleichbarer Server

## Datenbank

1. MySQL starten.
2. `database/create-database.sql` ausführen.
3. Die in `DemoDataInitializer` definierte DataSource nutzt standardmäßig:
   - Datenbank: `ghost_net`
   - Benutzer: `ghostnet`
   - Passwort: `ghostnet`
4. Falls andere Zugangsdaten verwendet werden sollen, muss die `@DataSourceDefinition` angepasst werden.

Die JPA-Konfiguration legt die Tabellen bei der ersten Bereitstellung an bzw. erweitert sie. Anschließend bleiben Meldungen und Zuordnungen in MySQL erhalten.

## Bauen

```bash
mvn clean package
```

Danach liegt die WAR-Datei unter:

```text
target/ghost-net-fishing.war
```

Die WAR-Datei auf dem verwendeten Jakarta-EE-Server deployen.

## Seiten

- `/index.xhtml` – Übersicht
- `/report.xhtml` – Geisternetz melden
- `/rescue.xhtml` – Bergung übernehmen
- `/recover.xhtml` – geborgen melden
- `/lost.xhtml` – verschollen melden

## Sicherheit

Die Anwendung nutzt serverseitige JSF-Validierung, begrenzt Eingabelängen und verarbeitet Benutzereingaben als Werte. Für Datenbankzugriffe werden JPA-Abfragen mit Parametern verwendet. Es wird kein dynamisches HTML aus Benutzereingaben erzeugt.

## Hinweis zu den Daten

Die im Projekt initialisierten Datensätze dienen der Demonstration des Prototyps. Sie sind nicht als reale Mess- oder Einsatzdaten gedacht.
