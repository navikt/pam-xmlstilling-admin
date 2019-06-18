import React from 'react';
import moment from 'moment';
import PropTypes from 'prop-types';
import { Link } from 'react-router-dom';
import { Normaltekst } from 'nav-frontend-typografi';

const getStatusText = (code) => {
    if (code === '0') {
        return 'Sendingen er mottatt og lagret til database. Vil bli forsøkt levert videre til PAM-annonsemottak';
    }
    if (code === '-1') {
        return 'Den mottatte XML\'en er ugyldig';
    }
    return 'Ukjent status';
};

const Result = ({ items, showDetailsLink }) => {
    return (
        <table>
            <thead>
                <tr>
                    <th>Statuskode</th>
                    <th>Statustekst</th>
                    <th>Leverandør</th>
                    <th>Arbeidsgiver</th>
                    <th>Dato mottatt</th>
                    <th>Kl</th>
                    {showDetailsLink && <th>Detaljer</th>}
                </tr>
            </thead>
            <tbody>
                {items.length > 0 && items.map((item) => (
                    <tr key={item.stillingBatchId}>
                        <td className="text-center">
                            {item.behandletStatus}
                        </td>
                        <td>
                            {getStatusText(item.behandletStatus)}
                        </td>
                        <td>
                            {item.eksternBrukerRef}
                        </td>
                        <td>
                            {item.arbeidsgiver}
                        </td>
                        <td>
                            {moment(item.mottattDato).local().format('DD.MM.YYYY')}
                        </td>
                        <td>
                            {moment(item.mottattDato).local().format('HH:mm')}
                        </td>
                        {showDetailsLink && (
                            <td>
                                <Link to={`/${item.stillingBatchId}`} className="lenke">
                                    <Normaltekst className="blokk-s">Se detaljer</Normaltekst>
                                </Link>
                            </td>
                        )}
                    </tr>
                ))}
            </tbody>
        </table>
    );
};


Result.defaultProps = {
    showDetailsLink: true
};

Result.propTypes = {
    showDetailsLink: PropTypes.bool,
    items: PropTypes.arrayOf(
        PropTypes.shape({
            stillingBatchId: PropTypes.number,
            arbeidsgiver: PropTypes.string,
            eksternBrukerRef: PropTypes.string,
            mottattDato: PropTypes.string
        })
    ).isRequired
};

export default Result;
