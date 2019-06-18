import React from 'react';
import moment from 'moment';

const getStatusText = (code) => {
    return code === '0' ?
        'Sendingen er mottatt og lagret til database. Vil bli forsøkt levert videre til PAM-annonsemottak' :
        'Den mottatte XML\'en er ugyldig';
};

const Result = ({items, showDetailsLink=true}) => {
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
                        </td><td>
                            {item.arbeidsgiver}
                        </td>
                        <td>
                            {moment(item.mottattDato).local().format('DD.MM.YYYY')}
                        </td>
                        <td>
                            {moment(item.mottattDato).local().format('HH:mm')}
                        </td>
                        {showDetailsLink && (
                            <td><a href={`/${item.stillingBatchId}`}>Se detaljer</a></td>
                        )}
                    </tr>
                ))}
            </tbody>
        </table>
    );
};

export default Result;
