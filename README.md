# HotelReservation_project_JAVA

- aplicatie in JAVA (+ Springboot) de rezervare a camerelor unui lant hotelier
- pentru realizarea acestei aplicatii, am utilizat 6 entitati diferite: Hotel, Room, Client, Reservation, Payment, Review.
- pentru fiecare dintre cele 6 entitati am implementat functionalitati de tip CRUD.
- relatiile dintre entitati sunt prezentate in figura diagrama_lant_hotelier.

# Scurta prezentare:
Lantul hotelier este format din unul sau mai multe hoteluri situate in diferite orase. Aceste hoteluri pot avea mai multe tipuri de camere (SINGLE, TWIN, DOUBLE, SUITE).
Un client poate avea mai multe rezervari la diferite hoteluri si poate adauga review-uri.

Rezervarea este realizata in functie de tipul camerei si de adresa hotelului. Daca nu este gasita nicio camera conform cerintelor clientului, rezervarea NU este realizata.
In acest sens, daca intr-un anumit hotel nu este libera nicio camera in perioada dorita, clientul nu va putea face o rezervare.

Ulterior, dupa ce rezervarea a fost realizata, este efectuata plata. Aceasta poate fi de 2 tipuri: EFFECTUATED (platita integral) sau PARTIALLY_EFFECTUATED (in cazul unui avans).
Daca suma platita corespunde cu totalul de plata pentru rezervare (numarul de zile x cost/zi), atunci plate este considerata efectuata.

# Reguli de business:
1. Client este unic, cu email unic;
2. Clientul trebuie sa aiba neaparat nume si prenume;
3. Hotel are o adresa unica, nu exista un complex de hoteluri;
4. Numarul unei camere este unic; nu pot exista 2 camere in acelasi hotel cu acelasi numar asignat;
5. Numarul de camere adaugate nu trebuie sa depaseasca numarul maxim de camere dintr-un hotel;
6. Numarul maxim de camere nu poate fi modificat astfel incat sa fie mai mic decat numarul de camere utlizate in prezent in hotel;
7. Clientul nu poate adauga un review daca nu este ales un rate pentru hotel;
8. Rate-ul maxim pentru hotel este 5 (rate <= 5);
9. Valoarea unei plati este considerate PARTIALLY_EFFECTUATED daca este mai mica decat costul total al rezervarii (numarul de zile x cost/zi);
10. Clientul este nevoit sa plateasca inainte de check-out (paymentDate < endDate);
11. Plata trebuie sa aiba precizata moneda (RON, USD, EUR) si metoda de realizare a platii (CASH, CARD);
12. Clientul trebuie sa se cazeze dupa ora 14:00 (check-in > 14) si sa elibereze camera pana la ora 12:00 (check-out < 12);
13. Rezervarea se face in functie de tipul camerei (SINGLE, DOUBLE, TWIN, SUITE) si adresa hotelului in limitele intervalului orar. 
Daca nicio camera nu este libera in perioada respectiva, atunci rezervarea nu va fi efectuata.

De asemenea, trebuie luate in considerare, regulile implementate prin intermediul bazei de date, respectiv, a cheilor externe FK (spre exemplu, nu pot adauga o camera daca 
nu exista hotelul respectiv, nu pot efectua o plata daca nu exista o rezervare sau un client). Implicit, la stergerea unui hotel vor fi sterse toate campurilor subordonate lui.

# Realizarea proiectului:
1. Crearea celor 6 entitati si a celor 6 clase de tip model;
2. Crearea unor interfete Repository care sa extinda JPARepository (unde sunt implementate deja metodele clasice de cautare si salvare a obiectelor);
3. Crearea Service-urilor, pentru a implementa regulile de business -> CRUD;
4. Crearea unor clase de tip Controller pentru a realiza conexiunea dintre service si pagina web;
5. Crearea claselor prin care vor fi gestionate erorile
6. Implementarea unor teste unitare pentru Controller si Service.
