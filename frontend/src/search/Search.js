import React, { useState } from 'react';
import PropTypes from 'prop-types';
import { Normaltekst } from 'nav-frontend-typografi';
import Datovelger from 'nav-datovelger/dist/datovelger/index';
import { Input } from 'nav-frontend-skjema';


const Search = () => {
    return <div></div>
        /*const [from, setFrom] = useState("");
    const [to, setTo] = useState("");
    const [employer, setEmployer] = useState("");
    return (
        <>
            <div>
                <Normaltekst>Fra</Normaltekst>
                <Datovelger
                    dato={formatISOString(to, 'DD.MM.YYYY') || ''}
                    onChange={setTo}
                    inputProps={{ placeholder: 'dd.mm.åååå' }}
                />
            </div>
            <div>
                <Normaltekst>Til</Normaltekst>
                <Datovelger
                    dato={formatISOString(from, 'DD.MM.YYYY') || ''}
                    onChange={setFrom}
                    inputProps={{ placeholder: 'dd.mm.åååå' }}
                />
            </div>
            <Input
                label="Søk på leverandør eller arbeidsgiver"
                value={employer}
                onChange={setEmployer}
            />
        </>
    )
    */

}

export default Search;