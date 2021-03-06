import React, { useContext, useCallback, useEffect } from 'react';
import { Normaltekst, Ingress, Sidetittel } from 'nav-frontend-typografi';
import Datovelger from 'nav-datovelger/dist/datovelger/Datovelger';
import 'nav-datovelger/dist/datovelger/styles/datovelger.css';
import { Input } from 'nav-frontend-skjema';
import { Hovedknapp } from 'nav-frontend-knapper';
import { searchStillinger } from '../api/api';
import { StoreContext } from '../context/StoreContext';
import Result from '../result/Result';


const Search = () => {
    const { actions, state } = useContext(StoreContext);
    const { from, to, employer, searchResult } = state;

    const onSearch = useCallback(() => {
        const fetchData = async () => {
            const result = await searchStillinger(from, to, employer);
            actions.setSearchResult(result);
        };

        fetchData();
    }, [from, to, employer]);

    useEffect(() => {
        onSearch();
    }, []);

    return (
        <article className="SearchPage">
            <header>
                <Sidetittel tag="h1" className="blokk-s">Stillingsannonser</Sidetittel>
                <Normaltekst className="blokk-m">
                    Her vises stillingsannonser som er mottatt. Du kan søke fram alle stillingsannonser som har kommet
                    inn for en gitt periode, eller søke på alle stillingsannonser som er sendt inn av en leverandør
                    eller arbeidsgiver eller som har en gitt referanse.
                </Normaltekst>
            </header>
            <section className="blokk-s">
                <div className="DatoWrapper">
                    <Normaltekst className="DatoLabel">Fra dato:</Normaltekst>
                    <Datovelger
                        valgtDato={from}
                        input={{
                            id: 'fra',
                            name: 'datoFra',
                            placeholder: 'dd.mm.åååå',
                            ariaLabel: 'datoFra'
                        }}
                        onChange={(fra) => { actions.setFromDate(fra); }}
                    />
                </div>
                <div className="DatoWrapper">
                    <Normaltekst className="DatoLabel">Til dato:</Normaltekst>
                    <Datovelger
                        valgtDato={to}
                        input={{
                            id: 'fra',
                            name: 'frist',
                            placeholder: 'dd.mm.åååå',
                            ariaLabel: 'Frist:'
                        }}
                        id="fristDatovelger"
                        onChange={(til) => { actions.setToDate(til); }}
                    />
                </div>
                <Input
                    label="Søk på leverandør, arbeidsgiver eller referanse"
                    value={employer}
                    onChange={(e) => actions.setEmployer(e.target.value)}
                    bredde="XL"
                    className="blokk-s"
                />
                <Hovedknapp className="blokk-m" onClick={onSearch}>Søk</Hovedknapp>
                {searchResult && searchResult.length >= 0 &&
                    <Ingress className="blokk-s">{`${searchResult.length} treff`}</Ingress>
                }
            </section>
            <Result items={searchResult} />
        </article>
    );
};

export default Search;
