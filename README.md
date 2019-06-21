# pam-xmlstilling-admin
Applikasjonen tilbyr oppslag på meldinger mottatt via WS (pam-xmlstilling-ws).

## Kjøre lokalt
Start DevBootstrap. Det vil kjøres opp en in-mem database med litt testdata.

API-kall for søk gjøres på formen `/search/{fra-dato}/{til-dato}/{søketekst}`
* fra- og til-dato er påkrevet.
* søketekst er valgfritt
* søketekst søker i utgangspunktet på leverandør og arbeidsgiver. Case-insensitivt.
* søketekst kan også være referense (`<ID>_<ARBEIDSGIVER>_<LEVERANDØR>`) på annonsen. Identifiseres som referanse dersom det er minst to underscores
i søketeksten. Bruker da id og leverandør i søket, men ignorerer arbeidsgiver.

Eksempel på API-kall:
* http://localhost:9024/search/2018-01-23/2018-01-23
* http://localhost:9024/search/2018-01-23/2018-01-23/karriereno
* http://localhost:9024/search/2018-01-11/2018-02-03/222_Jernia_webcruiter
* http://localhost:9024/get/7


## Frontend
### Kjøre lokalt

```sh 
npm install
npm start
```

### Deploy av frontend
Dette krever litt manuelt arbeid. 
Bygg først lokalt
```sh 
npm install
npm run build
```

Deretter må folderen `static` (med main.js), og fila `index.html`, kopieres over til `pam-xmlstilling-admin/src/main/resources/`,

