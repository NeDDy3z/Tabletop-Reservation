# Checkpoint 02
## Projekt: Aplikace pro nabízení a rezervaci míst určených k hraní deskových her

---

## Checklist
### Dokumentace
Zpráva o projektu (cca 1 A4)
- [ ] Popis aplikace a její struktury
- [ ] Návod, jak aplikaci nainstalovat (stručný, ale kompletní, včetně případného vytvoření testovacích dat a konfigurace aplikačního serveru) 
- [ ] Vypíchněte především zkušenosti získané během této SP - “Vyzkoušel jsem si tu a tu technologii a má tyto výhody/nevýhody oproti jiné technologii.” , nebo “Měl jsem ty a ty neočekávané problémy a řešil jsem je tak a tak.”, apod.


### Základy
- [ ] Projekt je sestavitelný Mavenem a build prochází včetně testů (tj. mvn clean package proběhne úspěšně) 
- [ ] Vyhovuje SRS vytvořené v rámci checkpointu 1

### Manipulace s daty
- [x] Používá persistentní vrstvu, navrženou a schválenou v rámci checkpointu 1, obohacenou o alespoň tři z následujících technik/funkcí:
  - [x] Ordering (uspořádání kolekce podle atributu - @OrderBy) - `GameTable.java`
  - [x] Pojmenované dotazy (@NamedQuery) - `Reservation.java`
  - [x] Kaskádní persist/update/merge/remove - `User.java`, `Venue.java`, `GameTable.java`, `Reservation.java`
  - [x] Složené primární klíče - `Rating.java` s `RatingId.java`
  - [ ] Mapování výsledků JPQL/native dotazu (@SqlResultSetMapping)
  - [x] Criteria API - `BaseRepository.java - findAll()`, `DeskGameRepository.java - findByTitle()`
  - [ ] Metamodel API 
- [ ] Kompletní CRUD části datového modelu. Tento CRUD musí být netriviální, tedy přes více entit spojených vazbami 
- [ ] Využít transakční zpracování
  
### REST rozhraní
- [x] REST rozhraní využívající business logiku aplikace 
- [x] Použít security podporu (autentizace, autorizace, omezení přístupu k metodám beany, funkcionalita aplikace podle role uživatele)

### Testování
- [ ] Ověřitelnost funkcionality aplikace, a to pomocí alespoň jedné z následujících možností 
  - [ ] Integrace REST rozhraní s jinou aplikací, a to jednou z těchto možností 
    - [ ] Integrace s REST rozhraním ukázkového projektu (tedy konzumací REST rozhraní e-shop aplikace)
    - [ ] Integrace s REST rozhraním aplikace z jiné skupiny
    - [ ] Integrace s rozhraním jiné aplikace dostupné na webu (nemusí být REST)
  - [x] Sadou HTTP dotazů a testovacích dat, kterou lze použít pro otestování REST rozhraní aplikace (např. v Postmanu) 
    - [x] Testovací dotazy musí být sdružené do scénářů, které umožňují vyzkoušet business logiku aplikace. Není třeba pokrýt veškerou funkcionalitu, ale scénáře by měly zahrnovat CRUD a netriviální logiku 
  - [ ] Prezentační vrstva

#### Obecněji
- [x] Controller (REST) testy
- [x] Hoppscotch/Postman testy
- [x] Business logika testy

### Bonus
Bonusové body lze získat za použití dalších souvisejících technologií:
- [ ] WebSockets
- [ ] použití Criteria API vč. statického metamodelu
- [ ] použití rich domain modelu
- [ ] použití JSON-LD ve webových službách
- [ ] více scope pro beany BL
- [ ] použití externích autentizačních služeb (Google, Facebook, SSO apod.)
- [x] použití Dockeru

    Popis aplikace a její struktury,
    Návod, jak aplikaci nainstalovat (stručný, ale kompletní, včetně případného vytvoření testovacích dat a konfigurace aplikačního serveru),
    Vypíchněte především zkušenosti získané během této SP - “Vyzkoušel jsem si tu a tu technologii a má tyto výhody/nevýhody oproti jiné technologii.” , nebo “Měl jsem ty a ty neočekávané problémy a řešil jsem je tak a tak.”, apod.

---