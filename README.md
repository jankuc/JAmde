JAmde
=====

java implementation of minimum distance estimators

Manual JAmde

Everything regarding building the application is clear from the Maven project file pom.xml .


Parametry pro spouštění aplikace:

zadává se vždy druh název parky ametru následovaný jeho hodnotou:
infile - absolutní cesta ke vstupnímu konfiguračnímu souboru.
outfile - na tomto umístění program vytvoří složku a výsledky ukládá do ní.
threads - počet jader, na kterých bude program počítat.
email - program neočekává nic za tímto parametrem. Měl by poslat mail na adresu username@fjfi.cvut.cz, přičemž username určí podle toho, jak je uživatel přihlášen na vkstatu.
print - charakterizuje druh výstupu. Ten je určen právě jedním z následujících. Pokud je požadováno více druhů výstupů, je nutné zadat print raw print classic ...
raw - výsledkem je soubor obsahující vektor s výsledky všech K odhadů.
classic - tabulka obsahuje střední hodnotu, rozptyl a eficienci odhadů.
distance/distances - vypíše a vykreslí soubor vzdálenosti v závislosti na volbě parametrů rozdělení.

Příklad příkazu pro spuštění:
jamde infile /home/kucerj28/JAmde-config threads 20 outfile ./defaultTable print distances print classic print raw email

Tento příkaz spustí program, který načte konfigurační soubor /home/kucerj28/JAmde-config, bude počítat na 20 jádrech, vytvoří v aktuální složce (./) složku defaultTable, do ní uloží všechny druhy výstupu a po ukončení pošle email na adresu username@fjfi.cvut.cz.
Konfigurační soubor:
Každá “tabulka” je oddělena # na samostatném řádku. Každá tabulka je pak charakterizována čtyřmi povinnými a pak n volitelnými řádky.

První řádek - zdroj dat
První řádek vždy specifikuje zdroj dat, na kterých se bude odhadovat.

Normal 0 1 0 Weibull 1 10 1 0.2
TypeA parA1 parA2 parA3 TypeB parB1 parB2 parB3 epsilon

data jsou generována jako směs znečišťovaného Normálního(0,1) a znečisťujícího Weibullova(1,10,1) rozdělení se znečištěním  = 0.2. 
Normal 0 1 0 error 0.1 0.05 100 0.02
Type par1 par2 par3 error err1 prob1 … errN probN
data jsou generována jako Normální rozdělení s tím, že byly simulovány chyby v řádu (špatný přepis desetinné čárky. Po slově error následuje libovolně dlouhý seznam změn následovaných jejich pravděpodobností. V tomto případě tedy bude 5% hodnot násobeno koeficientem 0.1 a 2% hodnot násobeno koeficientem 100.

file ~/tabulky/kVypoctu Normal 0 1 0
Program data načte ze souboru (očekává jedno datum na každém řádku) a pro odhady, které to potřebují jim řekne, který typ rozdělení mají očekávat. 	

Druhý řádek - odhadované parametry a počet odhadů

both 1000
Obsahuje příznak určující, které parametry se budou odhadovat následovaný počtem odhadů K.
Příznaky mohou být: both, first, second, all (pro odhad všech tří parametrů Weibulla).

Třetí řádek - velikost datových souborů
20 50 100 200 500
Obsahuje libovolně dlouhý seznam počtů dat, ze kterých se bude rozdělení odhadovat.
Čtvrtý až n-tý řádek
Renyi 0
Renyi 0.3
CramGen 34 19 
KolmCramM 34 19 200
Kolmogorov
Obsahuje libovolně dlouhý seznam odhadů následovaných hodnotami jejich parametrů. 

Příklad konfiguračního souboru:
#
Normal 0 1 0 Normal 0 10 0 0¨
both 1000
20 50 100 200 500
Renyi 0
Renyi 0.3
Kolmogorov
CramGen 34 19 
KolmCramM 34 19 200
KolmCramBeta 34 19  1
KolmCramRandM 34 19 200 1 1
KolmCramRandF 34 19  1 1 1
KolmCramRandBeta 34 19  1 1 1
#
Normal 0 1 0 Normal 0 10 0 0.1
both 1000
20 50 100 200 500
Renyi 0
Renyi 0.3
Kolmogorov
CramGen 34 19 
KolmCramM 34 19 200
KolmCramBeta 34 19  1
KolmCramRandM 34 19 200 1 1
KolmCramRandF 34 19  1 1 1
KolmCramRandBeta 34 19  1 1 1
#
Odhady			Jejich parametry
Renyi 				alpha
Kolmogorov 
Power				alpha
CramGen			p	q
KolmCramM			p	q	m
KolmCramBeta		p	q 	beta
KolmCramRandM		p	q	m	k	exp
KolmCramRandF		p	q	f	k	exp
KolmCramRandBeta		p	q	beta	k	exp
Rozdělení			Jejich parametry
	 	 	
Normal 			location	standard deviation
Cauchy 			location	scale
Laplace			location	scale
Logistic			location	scale
Uniform			a		b
Weibull				location	scale		shape
Exponential			location	scale
Alternative			location


