# National Defence Management System

## Getting Started

For further reference, please consider the following sections:


## Entities

### Panoramica

Le Entities rappresentano il modello dati persistente del sistema **National Defence Management System**.

Ogni Entity è mappata ad una tabella del database tramite JPA/Hibernate e costituisce il livello più basso dell'architettura applicativa.

Responsabilità delle Entities:

- Definire la struttura dei dati persistiti.
- Rappresentare le tabelle del database.
- Gestire le relazioni tra le diverse entità.
- Costituire il modello utilizzato dai Repository.

Le Entities **non vengono esposte direttamente tramite API REST**. Per la comunicazione con il client vengono utilizzati i relativi DTO.

---

## ArmedForce

### Scopo

Rappresenta una forza armata gestita dal sistema. Ogni base militare appartiene ad una forza armata.

### Tabella

```sql
armed_forces
```

### Campi

| Campo | Tipo | Descrizione |
|---------|---------|---------|
| id | Integer | Identificativo univoco |
| name | String | Nome della forza armata |

### Vincoli

- `name` obbligatorio
- `name` univoco

### Relazioni

```text
ArmedForce
    ↑
    |
Base (ManyToOne)
```

---

## Base

### Scopo

Rappresenta una base militare operativa.

Una base può ospitare operatori e veicoli appartenenti ad una specifica forza armata.

### Tabella

```sql
bases
```

### Campi

| Campo | Tipo | Descrizione |
|---------|---------|---------|
| id | Integer | Identificativo univoco |
| name | String | Nome della base |
| city | String | Città |
| address | String | Indirizzo |
| armedForce | ArmedForce | Forza armata di appartenenza |

### Vincoli

- `name` obbligatorio
- `address` obbligatorio
- `armedForce` obbligatorio

### Relazioni

```text
Base
 ├── ManyToOne → ArmedForce
 ├── OneToMany ← Operator
 └── OneToMany ← Vehicle
```

---

## Operator

### Scopo

Rappresenta il personale operativo appartenente ad una base militare.

### Tabella

```sql
operators
```

### Campi

| Campo | Tipo |
|---------|---------|
| id | Integer |
| serviceNumber | String |
| firstName | String |
| lastName | String |
| rank | String |
| heightInCm | Integer |
| weightInKg | Integer |
| status | OperatorStatus |
| base | Base |

### Vincoli

- `serviceNumber` univoco
- `serviceNumber` obbligatorio
- `firstName` obbligatorio
- `lastName` obbligatorio
- `rank` obbligatorio
- `heightInCm` obbligatorio
- `weightInKg` obbligatorio
- `status` obbligatorio
- `base` obbligatorio

### Relazioni

```text
Operator
 └── ManyToOne → Base
```

### Stati disponibili

| Stato |
|---------|
| ACTIVE |
| ON_MISSION |
| ON_LEAVE |

---

Le Entities **non vengono esposte direttamente tramite API REST**. Per la comunicazione con il client vengono utilizzati i relativi DTO.

---

## DTOs

### Panoramica

I Data Transfer Object (DTO) rappresentano il contratto di comunicazione tra il backend e il client.

I DTO vengono utilizzati per:

- esporre dati tramite API REST;
- ricevere dati dal client;
- evitare l'esposizione diretta delle Entity;
- semplificare la serializzazione JSON;
- ridurre il coupling tra database e API.

Una caratteristica importante del progetto è che le relazioni tra Entity vengono rappresentate tramite identificativi numerici anziché tramite oggetti completi.

Esempio:

```text
Operator
 └── Base

↓

OperatorDto
 └── baseId
```

Questo approccio semplifica la gestione delle richieste REST e riduce il payload trasmesso.

---

## ArmedForceDto

### Scopo

DTO utilizzato per rappresentare una forza armata.

### Campi

| Campo | Tipo |
|---------|---------|
| id | Integer |
| name | String |

---

## BaseDto

### Scopo

DTO utilizzato per rappresentare una base militare.

### Campi

| Campo | Tipo |
|---------|---------|
| id | Integer |
| name | String |
| city | String |
| address | String |
| armedForceId | Integer |

### Relazioni

```text
Base
 └── ArmedForce

↓

BaseDto
 └── armedForceId
```

---

## OperatorDto

### Scopo

DTO utilizzato per rappresentare un operatore militare.

### Campi

| Campo | Tipo |
|---------|---------|
| id | Integer |
| serviceNumber | String |
| firstName | String |
| lastName | String |
| heightInCm | Integer |
| weightInKg | Integer |
| rank | String |
| status | OperatorStatus |
| baseId | Integer |

### Relazioni

```text
Operator
 └── Base

↓

OperatorDto
 └── baseId
```

---

## VehicleCategoryDto

### Scopo

DTO utilizzato per rappresentare una categoria di veicolo.

### Campi

| Campo | Tipo |
|---------|---------|
| id | Integer |
| name | String |

---

## VehicleDto

### Scopo

DTO utilizzato per rappresentare un veicolo.

### Campi

| Campo | Tipo |
|---------|---------|
| id | Integer |
| matricola | UUID |
| modello | String |
| stato | VehicleStatus |
| vehicleCategoryId | Integer |
| baseId | Integer |

### Relazioni

```text
Vehicle
 ├── VehicleCategory
 └── Base

↓

VehicleDto
 ├── vehicleCategoryId
 └── baseId
```

---

## EquipmentDto

### Scopo

DTO utilizzato per rappresentare equipaggiamento militare.

### Campi

| Campo | Tipo |
|---------|---------|
| id | Integer |
| name | String |
| model | String |
| condition | EquipmentCondition |
| status | EquipmentStatus |
| fireArm | boolean |
| ammunitionType | String |
| operatorId | Integer |

### Relazioni

```text
Equipment
 └── Operator

↓

EquipmentDto
 └── operatorId
```

---

## MaintenanceDto

### Scopo

DTO utilizzato per rappresentare una manutenzione.

### Campi

| Campo | Tipo |
|---------|---------|
| id | Integer |
| vehicleId | Integer |
| equipmentId | Integer |
| description | String |
| startDate | LocalDate |
| endDate | LocalDate |
| estimatedMaintenanceDays | Integer |
| cost | BigDecimal |

### Relazioni

```text
Maintenance
 ├── Vehicle
 └── Equipment

↓

MaintenanceDto
 ├── vehicleId
 └── equipmentId
```

---

## DocumentsDto

### Scopo

DTO utilizzato per rappresentare documentazione associata ad operatori e veicoli.

### Campi

| Campo | Tipo |
|---------|---------|
| id | Integer |
| title | String |
| filePath | String |
| operatorId | Integer |
| vehicleId | Integer |

### Relazioni

```text
Documents
 ├── Operator
 └── Vehicle

↓

DocumentsDto
 ├── operatorId
 └── vehicleId
```

---

## UserDto

### Scopo

DTO utilizzato per l'esposizione dei dati utente.

### Campi

| Campo | Tipo |
|---------|---------|
| id | Integer |
| username | String |
| email | String |
| role | Role |
| enabled | boolean |

### Sicurezza

La password non viene mai esposta tramite UserDto.

---

## RegisterUserDto

### Scopo

DTO utilizzato durante la registrazione di un nuovo utente.

### Campi

| Campo | Tipo |
|---------|---------|
| username | String |
| email | String |
| password | String |

### Utilizzo

Utilizzato esclusivamente nelle operazioni di registrazione utente.

## Filter DTOs

### Panoramica

I FilterDTO vengono utilizzati per costruire ricerche dinamiche all'interno del sistema.

A differenza dei DTO tradizionali, che rappresentano dati da esporre o ricevere tramite API REST, i FilterDTO rappresentano criteri di ricerca opzionali.

Ogni proprietà valorizzata viene trasformata in una condizione di filtro dal relativo Service.

Esempio:

```text
GET /operators?firstName=Mario&status=ACTIVE
```

↓

```java
OperatorFilterDto
```

↓

```java
OperatorService.index(...)
```

↓

```java
Specification<Operator>
```

↓

```sql
SELECT *
FROM operators
WHERE first_name = 'Mario'
AND status = 'ACTIVE'
```

---

### BaseFilterDto

#### Scopo

Permette la ricerca delle basi militari tramite filtri dinamici.

#### Campi

| Campo | Tipo |
|---------|---------|
| name | String |
| city | String |
| address | String |
| armedForceId | Integer |

#### Utilizzo

Consente di filtrare le basi per:

- nome
- città
- indirizzo
- forza armata di appartenenza

---

### OperatorFilterDto

#### Scopo

Permette la ricerca avanzata degli operatori.

#### Campi

| Campo | Tipo |
|---------|---------|
| rank | String |
| serviceNumber | String |
| firstName | String |
| lastName | String |
| minHeightInCm | Integer |
| maxHeightInCm | Integer |
| minWeightInKg | Integer |
| maxWeightInKg | Integer |
| baseId | Integer |
| status | OperatorStatus |

#### Utilizzo

Consente di filtrare gli operatori per:

- grado
- matricola di servizio
- nome
- cognome
- base militare
- stato operativo
- intervallo di altezza
- intervallo di peso

#### Esempio

```java
OperatorFilterDto filter = new OperatorFilterDto();

filter.setStatus(OperatorStatus.ACTIVE);
filter.setBaseId(3);
filter.setMinHeightInCm(175);
```

---

### VehicleFilterDto

#### Scopo

Permette la ricerca dei veicoli.

#### Campi

| Campo | Tipo |
|---------|---------|
| matricola | UUID |
| modello | String |
| stato | VehicleStatus |
| vehicleCategoryId | Integer |
| baseId | Integer |

#### Utilizzo

Consente di filtrare i veicoli per:

- matricola
- modello
- stato
- categoria
- base militare

---

### EquipmentFilterDto

#### Scopo

Permette la ricerca degli equipaggiamenti.

#### Campi

| Campo | Tipo |
|---------|---------|
| name | String |
| model | String |
| condition | EquipmentCondition |
| status | EquipmentStatus |
| fireArm | Boolean |
| ammunitionType | String |
| operatorId | Integer |

#### Utilizzo

Consente di filtrare l'equipaggiamento per:

- nome
- modello
- stato operativo
- condizione
- tipologia arma
- munizionamento
- operatore assegnato

---

### MaintenanceFilterDto

#### Scopo

Permette la ricerca delle manutenzioni.

#### Campi

| Campo | Tipo |
|---------|---------|
| vehicleId | Integer |
| equipmentId | Integer |
| description | String |
| startDate | LocalDate |
| endDate | LocalDate |
| estimatedMaintenanceDays | Integer |
| cost | BigDecimal |

#### Utilizzo

Consente di filtrare le manutenzioni per:

- mezzo associato
- equipaggiamento associato
- descrizione
- intervallo temporale
- durata prevista
- costo

---

### DocumentFilterDto

#### Scopo

Permette la ricerca della documentazione caricata nel sistema.

#### Campi

| Campo | Tipo |
|---------|---------|
| title | String |
| operatorId | Integer |
| operatorFirstName | String |
| operatorLastName | String |
| operatorServiceNumber | String |
| vehicleId | Integer |

#### Utilizzo

Consente di filtrare i documenti tramite:

- titolo
- operatore associato
- nome operatore
- cognome operatore
- matricola operatore
- veicolo associato

---

### UserFilterDto

#### Scopo

Permette la ricerca degli utenti applicativi.

#### Campi

| Campo | Tipo |
|---------|---------|
| username | String |
| email | String |
| role | Role |
| enabled | Boolean |

#### Utilizzo

Consente di filtrare gli utenti per:

- username
- email
- ruolo
- stato di abilitazione

---

### Linee Guida

#### Filtri opzionali

Tutti i campi presenti nei FilterDTO sono opzionali.

Un campo nullo non genera alcuna condizione di ricerca.

Esempio:

```java
OperatorFilterDto filter = new OperatorFilterDto();

filter.setFirstName("Mario");
```

Genererà solamente il filtro:

```sql
WHERE first_name LIKE '%Mario%'
```

#### Ricerca combinata

Più filtri valorizzati vengono combinati tramite operatore logico AND.

Esempio:

```java
filter.setFirstName("Mario");
filter.setStatus(OperatorStatus.ACTIVE);
```

↓

```sql
WHERE first_name LIKE '%Mario%'
AND status = 'ACTIVE'
```

Questo approccio permette la costruzione di query dinamiche senza dover creare un metodo Repository per ogni combinazione possibile di filtri.

---

## Mappers

### Panoramica

I Mapper costituiscono il livello responsabile della conversione tra Entity e DTO.

Il loro scopo è isolare la logica di trasformazione dei dati dal resto dell'applicazione, evitando che Service e Controller debbano occuparsi direttamente della conversione tra modelli persistenti e oggetti esposti tramite API.

Il flusso di conversione adottato dal progetto è il seguente:

```text
Request
    ↓
DTO
    ↓
Mapper
    ↓
Entity
    ↓
Repository
    ↓
Database
```

e nel percorso inverso:

```text
Database
    ↓
Entity
    ↓
Mapper
    ↓
DTO
    ↓
Response
```

---

### Converter

L'interfaccia `Converter<Entity, DTO>` definisce il contratto comune che tutti i Mapper del sistema devono rispettare.

#### Metodi

| Metodo | Descrizione |
|----------|----------|
| toEntity(DTO dto) | Converte un DTO in Entity |
| toDTO(Entity entity) | Converte una Entity in DTO |
| toDTOList(Iterable<Entity>) | Converte una collezione di Entity in DTO |
| toEntityList(Iterable<DTO>) | Converte una collezione di DTO in Entity |

#### Obiettivo

Garantire uniformità nell'implementazione di tutti i Mapper del sistema.

---

### AbstractConverter

`AbstractConverter<Entity, DTO>` fornisce l'implementazione comune dei metodi di conversione delle collezioni.

#### Responsabilità

Gestire automaticamente la conversione di liste di Entity e DTO.

#### Metodi implementati

| Metodo | Descrizione |
|----------|----------|
| toDTOList(...) | Converte una collezione di Entity in DTO |
| toEntityList(...) | Converte una collezione di DTO in Entity |

L'implementazione delega la conversione dei singoli elementi ai metodi:

```java
toDTO(...)
toEntity(...)
```

che vengono implementati dai Mapper concreti.

#### Vantaggi

- Eliminazione di codice duplicato.
- Standardizzazione della conversione delle collezioni.
- Riduzione della complessità dei Mapper concreti.

---

### Mapper Concreti

I Mapper concreti ereditano da `AbstractConverter` e implementano esclusivamente la conversione tra una specifica Entity ed il relativo DTO.

#### Mapper presenti nel sistema

| Mapper | Conversione |
|----------|----------|
| ArmedForceMapper | ArmedForce ↔ ArmedForceDto |
| BaseMapper | Base ↔ BaseDto |
| OperatorMapper | Operator ↔ OperatorDto |
| EquipmentMapper | Equipment ↔ EquipmentDto |
| DocumentMapper | Documents ↔ DocumentsDto |
| MaintenanceMapper | Maintenance ↔ MaintenanceDto |
| VehicleMapper | Vehicle ↔ VehicleDto |
| VehicleCategoryMapper | VehicleCategory ↔ VehicleCategoryDto |
| UserMapper | User ↔ UserDto |



---

### Utilizzo di ModelMapper

La maggior parte dei Mapper utilizza la libreria `ModelMapper` per effettuare automaticamente la conversione tra Entity e DTO.

Esempio semplificato:

```java
@Override
public BaseDto toDTO(Base entity) {
    return mapper.map(entity, BaseDto.class);
}
```

#### Vantaggi

- Riduzione del codice boilerplate.
- Maggiore leggibilità.
- Facilità di manutenzione.
- Conversione automatica delle proprietà con lo stesso nome.

---

### Mapper con Conversione Manuale

Alcuni Mapper utilizzano una conversione manuale anziché `ModelMapper`.

Attualmente:

- VehicleMapper
- VehicleCategoryMapper
- MaintenanceMapper



#### Motivazioni

La conversione manuale viene utilizzata quando:

- è necessario gestire relazioni tramite ID;
- è richiesta una logica di trasformazione personalizzata;
- si vuole evitare il mapping automatico di oggetti complessi.

---

### RegisterUserMapper

`RegisterUserMapper` rappresenta un caso particolare.

A differenza degli altri Mapper:

- non estende `AbstractConverter`;
- non implementa l'interfaccia `Converter`;
- supporta esclusivamente la conversione:

```text
RegisterUserDto
        ↓
      User
```

#### Motivazione

Durante la registrazione di un nuovo utente non è necessario convertire l'entità `User` nel DTO di registrazione, poiché tale DTO viene utilizzato esclusivamente come input.

---

### Linee Guida

#### Responsabilità dei Mapper

I Mapper devono limitarsi esclusivamente alla conversione dei dati.

Non devono:

- eseguire query sul database;
- contenere logica di business;
- effettuare validazioni;
- gestire autorizzazioni.

Queste responsabilità appartengono ai Service.

#### Principio Architetturale

```text
Controller
    ↓
DTO
    ↓
Mapper
    ↓
Entity
    ↓
Service
    ↓
Repository
```

Ogni livello dell'applicazione deve avere una responsabilità specifica e ben definita.

---

## Repositories

### Panoramica

I Repository costituiscono il livello di accesso ai dati dell'applicazione.

Sono responsabili dell'interazione con il database e rappresentano l'unico punto autorizzato ad eseguire operazioni di persistenza sulle Entity.

Nel progetto viene utilizzato il framework Spring Data JPA, che consente di generare automaticamente la maggior parte delle operazioni CRUD senza richiedere implementazioni manuali.

---

### Ruolo nell'Architettura

I Repository si collocano tra il Service Layer e il Database.

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
Database
```

I Service utilizzano i Repository per:

- recuperare dati dal database;
- salvare nuove entità;
- aggiornare entità esistenti;
- eliminare record;
- eseguire ricerche filtrate.

---

### JpaRepository

Tutti i Repository del sistema estendono:

```java
JpaRepository<Entity, Integer>
```

Questa interfaccia fornisce automaticamente le principali operazioni CRUD.

#### Operazioni disponibili

| Metodo | Descrizione |
|----------|----------|
| save() | Inserimento o aggiornamento |
| findById() | Ricerca per identificativo |
| findAll() | Recupero di tutti i record |
| existsById() | Verifica esistenza |
| deleteById() | Eliminazione per identificativo |
| count() | Conteggio record |

Grazie a Spring Data JPA non è necessario implementare manualmente tali operazioni.

---

### JpaSpecificationExecutor

Tutti i Repository del progetto estendono inoltre:

```java
JpaSpecificationExecutor<Entity>
```

Questa interfaccia permette l'esecuzione di query dinamiche tramite il pattern Specification.

#### Operazioni disponibili

| Metodo | Descrizione |
|----------|----------|
| findAll(Specification) | Ricerca tramite criteri dinamici |
| count(Specification) | Conteggio risultati filtrati |
| exists(Specification) | Verifica esistenza tramite filtro |

---

### Integrazione con i FilterDTO

Le ricerche avanzate del sistema vengono realizzate tramite la combinazione di:

```text
FilterDTO
      ↓
Specification
      ↓
Repository
      ↓
Database
```

Esempio:

```java
OperatorFilterDto
      ↓
OperatorService.index()
      ↓
Specification<Operator>
      ↓
OperatorRepository.findAll(specification)
```

Questo approccio consente di costruire query dinamiche senza dover creare un metodo Repository per ogni possibile combinazione di filtri.

---

### Repository del Sistema

Attualmente il progetto utilizza un Repository dedicato per ciascuna Entity principale.

| Repository | Entity Gestita |
|----------|----------|
| ArmedForceRepository | ArmedForce |
| BaseRepository | Base |
| OperatorRepository | Operator |
| VehicleRepository | Vehicle |
| VehicleCategoryRepository | VehicleCategory |
| EquipmentRepository | Equipment |
| MaintenanceRepository | Maintenance |
| DocumentRepository | Documents |
| UserRepository | User |

Ogni Repository eredita le funzionalità comuni offerte da Spring Data JPA.

---

### Query Derivate

Spring Data JPA consente di generare automaticamente query partendo dal nome dei metodi.

Esempio:

```java
findByNameContainingIgnoreCase(String name)
```

Spring genera automaticamente una query equivalente a:

```sql
SELECT *
FROM armed_forces
WHERE LOWER(name) LIKE LOWER('%value%')
```

Questo approccio riduce drasticamente la quantità di codice necessario per l'accesso ai dati.

---

### Specifications

Le Specifications rappresentano il meccanismo utilizzato dal progetto per costruire query dinamiche in modo tipizzato e mantenibile.

Spring Data JPA mette a disposizione l'interfaccia:

```java
Specification<T>
```

che consente di definire criteri di ricerca dinamici tramite le API Criteria di JPA.

Nel progetto le Specifications vengono costruite all'interno dei Service partendo dai relativi FilterDTO.

Esempio semplificato:

```java
Specification<Operator> specification = (root, query, cb) -> {

    List<Predicate> predicates = new ArrayList<>();

    if(filter.getFirstName() != null) {

        predicates.add(
            cb.like(
                cb.lower(root.get("firstName")),
                "%" + filter.getFirstName().toLowerCase() + "%"
            )
        );
    }

    return cb.and(
        predicates.toArray(new Predicate[0])
    );
};
```

Successivamente la Specification viene passata al Repository:

```java
repository.findAll(specification);
```

---

#### Vantaggi

L'utilizzo delle Specifications offre diversi vantaggi:

- costruzione dinamica delle query;
- eliminazione di query duplicate;
- forte integrazione con Spring Data JPA;
- maggiore leggibilità rispetto a query SQL costruite manualmente;
- possibilità di combinare filtri opzionali senza creare decine di metodi Repository.

---

#### Flusso di Esecuzione

```text
Richiesta HTTP
       ↓
FilterDTO
       ↓
Service
       ↓
Specification
       ↓
Repository
       ↓
Database
```

---

#### Esempio Pratico

Supponiamo di voler cercare:

- operatori attivi;
- appartenenti alla base 3;
- con altezza minima di 175 cm.

Il Service costruirà automaticamente una Specification equivalente a:

```sql
SELECT *
FROM operators
WHERE status = 'ACTIVE'
AND base_id = 3
AND height_in_cm >= 175
```

senza la necessità di creare un metodo dedicato nel Repository.

---

#### Utilizzo nel Progetto

Le Specifications vengono utilizzate principalmente nei metodi:

- `OperatorService.index(...)`
- `BaseService.index(...)`
- tutti i futuri servizi che richiederanno filtri avanzati

Questo approccio consente di mantenere i Repository estremamente semplici e delegare la logica di ricerca ai Service.

---
### Responsabilità

I Repository devono limitarsi esclusivamente all'accesso ai dati.

Devono:

- recuperare dati dal database;
- salvare entità;
- eliminare entità;
- eseguire query.

Non devono:

- contenere logica di business;
- effettuare validazioni;
- gestire autorizzazioni;
- convertire Entity e DTO.

Tali responsabilità appartengono rispettivamente ai Service e ai Mapper.

---

### Principio Architetturale

```text
Controller
    ↓
DTO
    ↓
Mapper
    ↓
Service
    ↓
Repository
    ↓
Database
```

Ogni layer dell'applicazione ha una responsabilità specifica e indipendente dalle altre.

## Services

### Panoramica

Il Service Layer rappresenta il cuore della logica applicativa del sistema.

I Service hanno la responsabilità di coordinare:

- Mapper
- Repository
- DTO
- Entity
- Validazioni applicative

e fungono da intermediari tra Controller e Repository.

```text
Controller
    ↓
DTO
    ↓
Service
    ↓
Mapper
    ↓
Repository
    ↓
Database
```

I Service non si occupano della serializzazione JSON e non eseguono direttamente operazioni SQL.

---

### ServiceDto

Tutti i Service del progetto implementano indirettamente l'interfaccia:

```java
ServiceDto<T>
```

L'interfaccia definisce il contratto CRUD comune dell'applicazione.

#### Metodi

| Metodo | Descrizione |
|----------|----------|
| read(Integer id) | Recupera una risorsa tramite identificativo |
| insert(T dto) | Inserisce una nuova risorsa |
| update(T dto) | Aggiorna una risorsa esistente |
| delete(Integer id) | Elimina una risorsa |

---

### AbstractService

Per evitare duplicazione di codice tutti i Service principali estendono:

```java
AbstractService<Entity, DTO>
```



L'AbstractService implementa automaticamente le operazioni CRUD comuni.

#### Dipendenze

```java
JpaRepository<Entity,Integer>
Converter<Entity,DTO>
```

#### Funzionalità fornite

| Metodo | Funzionalità |
|----------|----------|
| insert() | Salvataggio generico tramite Mapper |
| read() | Recupero tramite id |
| update() | Aggiornamento generico |
| delete() | Eliminazione con verifica preventiva |

---

### Gestione delle Eccezioni

L'AbstractService implementa controlli comuni su:

- DTO nulli
- identificativi nulli
- entità inesistenti
- errori di persistenza



Eccezioni utilizzate:

| Eccezione | Caso |
|------------|------------|
| IllegalArgumentException | Parametri non validi |
| NoSuchElementException | Risorsa non trovata |
| RuntimeException | Errori applicativi o di persistenza |

---

### Service con Override

Alcuni Service effettuano l'override dei metodi CRUD dell'AbstractService.

Questo accade quando:

- il DTO contiene identificativi di relazioni;
- l'Entity richiede oggetti completi;
- sono necessarie validazioni aggiuntive;
- devono essere gestite regole di business specifiche.

Esempio:

```text
OperatorDto
    ↓
baseId
    ↓
OperatorService
    ↓
BaseRepository.findById(...)
    ↓
Operator.base
```



---

### Ricerca Dinamica

La maggior parte dei Service implementa un metodo:

```java
index(...)
```

responsabile delle operazioni di ricerca.

Le ricerche vengono costruite tramite:

```text
FilterDTO
      ↓
Specification
      ↓
Repository
      ↓
Database
```

Questo approccio consente di applicare esclusivamente i filtri valorizzati dal client. 

---

### ArmedForceService

#### Responsabilità

Gestisce le operazioni CRUD e le funzionalità di ricerca relative alle forze armate presenti nel sistema.

Il servizio eredita tutte le operazioni CRUD standard dall'AbstractService e introduce una funzionalità di ricerca per nome.



#### Dipendenze

| Dipendenza | Scopo |
|------------|------------|
| ArmedForceRepository | Accesso ai dati delle forze armate |
| ArmedForceMapper | Conversione tra Entity e DTO |

#### Metodi

##### index(String name)

Restituisce l'elenco delle forze armate.

Comportamento:

- se il parametro `name` è nullo o vuoto vengono restituite tutte le forze armate;
- se il parametro è valorizzato viene eseguita una ricerca parziale case-insensitive sul nome.



#### Flusso Operativo

```text
Controller
    ↓
ArmedForceService.index(name)
    ↓
ArmedForceRepository
    ↓
Entity List
    ↓
Mapper
    ↓
DTO List
```

---

### BaseService

#### Responsabilità

Gestisce le basi militari del sistema.

Oltre alle normali operazioni CRUD, il servizio si occupa della risoluzione della relazione tra una Base e la relativa ArmedForce.



#### Dipendenze

| Dipendenza | Scopo |
|------------|------------|
| BaseRepository | Gestione delle basi militari |
| ArmedForceRepository | Recupero della forza armata associata |
| BaseMapper | Conversione tra Entity e DTO |

#### Relazioni Gestite

Una Base appartiene sempre ad una ArmedForce.

Nel DTO la relazione viene rappresentata tramite:

```text
BaseDto
    ↓
armedForceId
```

mentre nell'Entity:

```text
Base
    ↓
ArmedForce
```

Il Service si occupa della conversione tra le due rappresentazioni.



#### Metodi

##### insert(BaseDto dto)

Workflow:

1. Conversione del DTO in Entity.
2. Recupero della ArmedForce tramite `armedForceId`.
3. Associazione della ArmedForce all'Entity.
4. Salvataggio della Base.
5. Conversione del risultato in DTO.



##### update(BaseDto dto)

Workflow:

1. Conversione del DTO in Entity.
2. Recupero della ArmedForce associata.
3. Aggiornamento dell'Entity.
4. Salvataggio sul database.
5. Restituzione del DTO aggiornato.



##### index(BaseFilterDto filter)

Permette la ricerca dinamica delle basi militari tramite Specification.

Filtri supportati:

- nome;
- città;
- indirizzo;
- forza armata di appartenenza.

Ogni filtro valorizzato viene convertito in un Predicate e combinato tramite operatore AND.



#### Gestione Errori

Durante insert e update viene verificata l'esistenza della ArmedForce associata.

In caso di assenza viene sollevata:

```java
RuntimeException
```

con il messaggio:

```text
Armed Force not found with id: {id}
```



#### Flusso Operativo

```text
BaseDto
    ↓
BaseService
    ↓
ArmedForceRepository.findById(...)
    ↓
Base Entity
    ↓
BaseRepository.save(...)
    ↓
BaseDto
```

---

### OperatorService

#### Responsabilità

Gestisce il personale operativo appartenente alle basi militari.

Oltre alle normali operazioni CRUD, il servizio si occupa della gestione della relazione tra Operator e Base e della ricerca avanzata tramite filtri dinamici.

#### Dipendenze

| Dipendenza | Scopo |
|------------|------------|
| OperatorRepository | Gestione degli operatori |
| BaseRepository | Recupero della base associata |
| OperatorMapper | Conversione tra Entity e DTO |

#### Relazioni Gestite

Ogni operatore appartiene ad una base militare.

Nel DTO:

```text
OperatorDto
    ↓
baseId
```

Nell'Entity:

```text
Operator
    ↓
Base
```

Il Service è responsabile della conversione tra le due rappresentazioni.

---

#### Metodi

##### insert(OperatorDto dto)

Workflow:

1. Conversione del DTO in Entity.
2. Recupero della Base tramite `baseId`.
3. Associazione della Base all'Operator.
4. Salvataggio dell'entità.
5. Conversione in DTO.

---

##### update(OperatorDto dto)

Workflow:

1. Conversione del DTO in Entity.
2. Recupero della Base associata.
3. Aggiornamento dell'entità.
4. Salvataggio sul database.
5. Restituzione del DTO aggiornato.

---

##### index(OperatorFilterDto filter)

Permette la ricerca avanzata degli operatori tramite Specification.

Filtri supportati:

- serviceNumber
- status
- baseId
- rank
- firstName
- lastName
- minHeightInCm
- maxHeightInCm
- minWeightInKg
- maxWeightInKg

I filtri vengono applicati esclusivamente se valorizzati.

---

#### Gestione Errori

Durante insert e update viene verificata l'esistenza della Base associata.

In caso contrario viene sollevata:

```java
RuntimeException
```

con il messaggio:

```text
Base not found with id: {id}
```

---

#### Flusso Operativo

```text
OperatorDto
    ↓
OperatorService
    ↓
BaseRepository.findById(...)
    ↓
Operator Entity
    ↓
OperatorRepository.save(...)
    ↓
OperatorDto
```

---

### VehicleCategoryService

#### Responsabilità

Gestisce le categorie dei veicoli presenti nel sistema.

Le categorie vengono utilizzate per classificare i mezzi militari.

Esempi:

- Tank
- APC
- Truck
- Helicopter
- UAV

Il servizio non introduce logiche di business particolari e si limita a fornire le operazioni CRUD ereditate e la consultazione delle categorie disponibili.

#### Dipendenze

| Dipendenza | Scopo |
|------------|------------|
| VehicleCategoryRepository | Gestione delle categorie |
| VehicleCategoryMapper | Conversione tra Entity e DTO |

---

#### Metodi

##### index()

Restituisce tutte le categorie registrate nel sistema.

Workflow:

1. Recupero di tutte le categorie.
2. Conversione della lista di Entity in DTO.
3. Restituzione del risultato.

---

#### Flusso Operativo

```text
Controller
    ↓
VehicleCategoryService.index()
    ↓
VehicleCategoryRepository.findAll()
    ↓
Entity List
    ↓
Mapper
    ↓
DTO List
```

---

#### Considerazioni Architetturali

A differenza di altri Service del sistema:

- non gestisce relazioni;
- non utilizza Specification;
- non richiede validazioni particolari;
- non effettua controlli aggiuntivi durante insert e update.

Le operazioni CRUD vengono completamente delegate all'AbstractService.

---

### VehicleService

#### Responsabilità

Gestisce il ciclo di vita dei veicoli militari del sistema.

A differenza degli altri servizi CRUD, VehicleService implementa regole di business aggiuntive necessarie alla gestione delle relazioni e dell'identificativo univoco del mezzo.



#### Dipendenze

| Dipendenza | Scopo |
|------------|------------|
| VehicleRepository | Gestione dei veicoli |
| VehicleCategoryRepository | Recupero della categoria associata |
| BaseRepository | Recupero della base associata |
| VehicleMapper | Conversione tra Entity e DTO |

---

#### Relazioni Gestite

Ogni veicolo è associato a:

- una categoria;
- una base militare.

Nel DTO:

```text
VehicleDto
 ├── vehicleCategoryId
 └── baseId
```

Nell'Entity:

```text
Vehicle
 ├── VehicleCategory
 └── Base
```

Il Service è responsabile della conversione tra le due rappresentazioni.

---

#### Gestione della Matricola

Ogni veicolo possiede una matricola univoca rappresentata da un UUID.

La matricola viene generata automaticamente durante la creazione del veicolo:

```java
entity.setMatricola(UUID.randomUUID());
```



##### Caratteristiche

- generata automaticamente dal sistema;
- univoca;
- non modificabile dall'utente;
- preservata durante gli aggiornamenti.

---

#### Metodi

##### insert(VehicleDto dto)

Workflow:

1. Validazione del DTO.
2. Recupero della categoria associata.
3. Recupero della base associata.
4. Generazione della matricola UUID.
5. Associazione delle relazioni.
6. Salvataggio del veicolo.
7. Restituzione del DTO.



---

##### update(VehicleDto dto)

Workflow:

1. Verifica presenza dell'id.
2. Recupero del veicolo esistente.
3. Recupero delle relazioni aggiornate.
4. Aggiornamento dei dati modificabili.
5. Salvataggio dell'entità.
6. Restituzione del DTO aggiornato.



##### Nota

La matricola UUID non viene mai modificata durante l'aggiornamento.

Questo garantisce la tracciabilità permanente del mezzo.



---

##### index(VehicleFilterDto filter)

Permette la ricerca dinamica dei veicoli.

Filtri supportati:

- matricola UUID;
- modello;
- stato;
- categoria;
- base militare.

I filtri vengono convertiti dinamicamente in Predicate e combinati tramite operatore AND.



---

#### Gestione Errori

Durante le operazioni di inserimento e aggiornamento vengono effettuati controlli aggiuntivi.

Possibili errori:

| Eccezione | Caso |
|------------|------------|
| IllegalArgumentException | DTO nullo |
| IllegalArgumentException | Id mancante durante update |
| NoSuchElementException | Veicolo inesistente |
| NoSuchElementException | Categoria inesistente |
| NoSuchElementException | Base inesistente |
| RuntimeException | Errori imprevisti di persistenza |



---

#### Flusso Operativo

```text
VehicleDto
      ↓
VehicleService
      ↓
VehicleCategoryRepository
      ↓
BaseRepository
      ↓
UUID Generation
      ↓
VehicleRepository
      ↓
VehicleDto
```

---

### UserService

#### Responsabilità

Gestisce gli utenti applicativi del sistema.

Oltre alle normali operazioni CRUD eredidate dall'AbstractService, introduce la gestione della registrazione utenti e la ricerca avanzata.



#### Dipendenze

| Dipendenza | Scopo |
|------------|------------|
| UserRepository | Gestione utenti |
| UserMapper | Conversione User ↔ UserDto |
| RegisterUserMapper | Conversione RegisterUserDto → User |

---

#### Registrazione Utenti

A differenza degli altri servizi, UserService espone un metodo dedicato alla registrazione.

```java
register(RegisterUserDto dto)
```



Durante la registrazione:

1. il DTO viene convertito in User;
2. viene assegnato automaticamente il ruolo predefinito;
3. l'utente viene abilitato;
4. l'utente viene salvato;
5. viene restituito il relativo UserDto.



---

#### Ruolo Predefinito

Gli utenti registrati ricevono automaticamente il ruolo:

```text
ROLE_OPERATOR
```



Questo comportamento garantisce che nessun utente possa assegnarsi privilegi elevati durante la registrazione.

---

#### Abilitazione Automatica

Durante la registrazione viene impostato:

```java
user.setEnabled(true);
```



L'utente risulta quindi immediatamente attivo all'interno del sistema.

---

#### Metodi

##### register(RegisterUserDto dto)

Workflow:

1. Conversione del DTO in User.
2. Assegnazione del ruolo predefinito.
3. Abilitazione dell'utente.
4. Salvataggio sul database.
5. Restituzione del DTO.



---

##### index(UserFilterDto filter)

Permette la ricerca dinamica degli utenti.

Filtri supportati:

- username;
- email;
- ruolo;
- stato di abilitazione.

Ogni filtro valorizzato genera un Predicate che viene combinato tramite AND.



---

#### Sicurezza

Le responsabilità di UserService sono limitate alla gestione dell'utente applicativo.

L'autenticazione e l'autorizzazione verranno gestite da componenti dedicati del sistema di sicurezza.

---

#### Flusso Operativo

```text
RegisterUserDto
        ↓
RegisterUserMapper
        ↓
User
        ↓
ROLE_OPERATOR
        ↓
enabled = true
        ↓
UserRepository
        ↓
UserDto
```

---

### MaintenanceService

#### Responsabilità

Gestisce le operazioni di manutenzione del sistema.

Una manutenzione può essere associata:

- ad un veicolo;
- ad un equipaggiamento.

Il servizio implementa regole di business specifiche per garantire la coerenza dei dati e impedire associazioni non valide.



#### Dipendenze

| Dipendenza | Scopo |
|------------|------------|
| MaintenanceRepository | Gestione delle manutenzioni |
| VehicleRepository | Recupero dei veicoli associati |
| EquipmentRepository | Recupero degli equipaggiamenti associati |
| MaintenanceMapper | Conversione tra Entity e DTO |

---

#### Relazioni Gestite

Nel DTO:

```text
MaintenanceDto
 ├── vehicleId
 └── equipmentId
```

Nell'Entity:

```text
Maintenance
 ├── Vehicle
 └── Equipment
```

Il Service si occupa della risoluzione delle relazioni e del recupero delle entità associate.

---

#### Regole di Business

##### Associazione Obbligatoria

Ogni manutenzione deve essere associata ad una sola risorsa.

Sono consentiti:

```text
✔ Vehicle
✔ Equipment
```

Non sono consentiti:

```text
✘ Nessuna associazione
✘ Vehicle + Equipment contemporaneamente
```



---

##### Validazione delle Date

La data di fine non può precedere la data di inizio.

Esempio valido:

```text
01/01/2025
     ↓
10/01/2025
```

Esempio non valido:

```text
10/01/2025
     ↓
01/01/2025
```

In caso di violazione viene generata una:

```java
IllegalArgumentException
```



---

#### Metodi

##### insert(MaintenanceDto dto)

Workflow:

1. Validazione delle date.
2. Validazione dell'associazione.
3. Conversione DTO → Entity.
4. Recupero del veicolo o dell'equipaggiamento.
5. Associazione della risorsa.
6. Salvataggio della manutenzione.
7. Conversione in DTO.



---

##### update(MaintenanceDto dto)

Workflow:

1. Verifica presenza dell'id.
2. Recupero della manutenzione esistente.
3. Validazione delle date.
4. Validazione dell'associazione.
5. Aggiornamento dei campi modificabili.
6. Aggiornamento della relazione.
7. Salvataggio.
8. Restituzione del DTO aggiornato.



---

##### index(MaintenanceFilterDto filter)

Permette la ricerca dinamica delle manutenzioni.

Filtri supportati:

- vehicleId
- equipmentId
- description
- startDate
- endDate
- estimatedMaintenanceDays
- cost

I filtri vengono convertiti dinamicamente in Predicate tramite Specification.



---

#### Gestione Errori

| Eccezione | Caso |
|------------|------------|
| IllegalArgumentException | Data finale precedente alla data iniziale |
| IllegalArgumentException | Nessuna associazione presente |
| IllegalArgumentException | Associazione multipla veicolo/equipaggiamento |
| IllegalArgumentException | Id assente durante update |
| RuntimeException | Veicolo inesistente |
| RuntimeException | Equipaggiamento inesistente |
| RuntimeException | Manutenzione inesistente |



---

#### Flusso Operativo

```text
MaintenanceDto
        ↓
Validazione Date
        ↓
Validazione Associazione
        ↓
VehicleRepository
        oppure
EquipmentRepository
        ↓
MaintenanceRepository
        ↓
MaintenanceDto
```

---

### DocumentService

#### Responsabilità

Gestisce la documentazione associata agli operatori e ai veicoli del sistema.

Un documento può essere associato:

- ad un operatore;
- ad un veicolo;
- ad entrambi.



#### Dipendenze

| Dipendenza | Scopo |
|------------|------------|
| DocumentRepository | Gestione documenti |
| OperatorRepository | Recupero operatori associati |
| VehicleRepository | Recupero veicoli associati |
| DocumentMapper | Conversione tra Entity e DTO |

---

#### Relazioni Gestite

Nel DTO:

```text
DocumentsDto
 ├── operatorId
 └── vehicleId
```

Nell'Entity:

```text
Documents
 ├── Operator
 └── Vehicle
```

Il Service risolve automaticamente gli identificativi presenti nel DTO recuperando le entità corrispondenti.



---

#### Metodi

##### insert(DocumentsDto dto)

Workflow:

1. Conversione DTO → Entity.
2. Recupero dell'operatore associato.
3. Recupero del veicolo associato.
4. Associazione delle relazioni.
5. Salvataggio del documento.
6. Conversione in DTO.



---

##### update(DocumentsDto dto)

Workflow:

1. Conversione DTO → Entity.
2. Aggiornamento dell'operatore associato.
3. Aggiornamento del veicolo associato.
4. Gestione delle relazioni rimosse.
5. Salvataggio del documento.
6. Restituzione del DTO aggiornato.



##### Nota

Durante l'aggiornamento:

```java
operatorId == null
```

comporta:

```java
entity.setOperator(null)
```

e analogamente per il veicolo.

Questo consente la rimozione esplicita delle associazioni esistenti.



---

##### index(DocumentFilterDto filter)

Permette la ricerca dinamica dei documenti.

Filtri supportati:

- titolo;
- nome operatore;
- cognome operatore;
- matricola operatore;
- veicolo associato.



---

#### Gestione Errori

| Eccezione | Caso |
|------------|------------|
| RuntimeException | Operatore inesistente |
| RuntimeException | Veicolo inesistente |



---

#### Flusso Operativo

```text
DocumentsDto
       ↓
DocumentService
       ↓
OperatorRepository
       ↓
VehicleRepository
       ↓
DocumentRepository
       ↓
DocumentsDto
```

---

### EquipmentService

#### Responsabilità

Gestisce l'equipaggiamento militare del sistema.

L'equipaggiamento può essere assegnato ad un operatore oppure mantenuto non assegnato.

Il servizio si occupa della gestione dell'associazione tra Equipment e Operator e della ricerca avanzata tramite filtri dinamici.

---

#### Dipendenze

| Dipendenza | Scopo |
|------------|------------|
| EquipmentRepository | Gestione dell'equipaggiamento |
| OperatorRepository | Recupero dell'operatore associato |
| EquipmentMapper | Conversione tra Entity e DTO |

---

#### Relazioni Gestite

Nel DTO:

```text
EquipmentDto
    ↓
operatorId
```

Nell'Entity:

```text
Equipment
    ↓
Operator
```

Il Service si occupa della risoluzione della relazione tramite l'identificativo dell'operatore.

---

#### Gestione delle Assegnazioni

L'assegnazione di un operatore è opzionale.

Sono consentiti:

```text
✔ Equipment assegnato ad un operatore
✔ Equipment non assegnato
```

Durante gli aggiornamenti l'associazione può essere rimossa impostando:

```java
operatorId = null
```

In tal caso:

```java
entity.setOperator(null);
```

:contentReference[oaicite:1]{index=1}

---

#### Metodi

##### insert(EquipmentDto dto)

Workflow:

1. Conversione DTO → Entity.
2. Verifica presenza dell'operatorId.
3. Recupero dell'operatore associato.
4. Associazione dell'operatore.
5. Salvataggio dell'equipaggiamento.
6. Conversione in DTO.

Se non viene specificato alcun operatore, l'equipaggiamento viene salvato senza assegnazione.

:contentReference[oaicite:2]{index=2}

---

##### update(EquipmentDto dto)

Workflow:

1. Conversione DTO → Entity.
2. Verifica dell'operatorId.
3. Recupero dell'operatore associato.
4. Aggiornamento della relazione.
5. Eventuale rimozione dell'associazione.
6. Salvataggio dell'entità.
7. Restituzione del DTO aggiornato.

:contentReference[oaicite:3]{index=3}

---

##### index(EquipmentFilterDto filter)

Permette la ricerca dinamica dell'equipaggiamento tramite Specification.

Filtri supportati:

- name;
- model;
- condition;
- status;
- fireArm;
- ammunitionType.

:contentReference[oaicite:4]{index=4} :contentReference[oaicite:5]{index=5}

I filtri vengono convertiti dinamicamente in Predicate e combinati tramite operatore AND.

---

#### Gestione Errori

Durante insert e update viene verificata l'esistenza dell'operatore associato.

In caso di assenza viene generata:

```java
RuntimeException
```

con il messaggio:

```text
Operator not found with id: {id}
```

:contentReference[oaicite:6]{index=6}

---

#### Flusso Operativo

```text
EquipmentDto
      ↓
EquipmentService
      ↓
OperatorRepository.findById(...)
      ↓
Equipment Entity
      ↓
EquipmentRepository.save(...)
      ↓
EquipmentDto
```