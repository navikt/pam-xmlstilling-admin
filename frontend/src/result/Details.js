import React, { useEffect, useState } from 'react';
import PropTypes from 'prop-types';
import { Link } from 'react-router-dom';
import { Normaltekst, Systemtittel } from 'nav-frontend-typografi';
import { getStillingByBatchId } from '../api/api';
import Result from './Result';
import '../styles.less';


const Details = ({ match }) => {
    const [xml, setXml] = useState(undefined);

    useEffect(() => {
        const { id } = match.params;
        if (id) {
            const fetchData = async () => {
                const result = await getStillingByBatchId(id);
                setXml(result);
            };

            fetchData();
        }
    }, [match.params.id]);

    if (!xml) {
        return null;
    }

    return (
        <div>
            <Link
                to="/"
                className="lenke"
            >
                <Normaltekst className="blokk-s">Tilbake til søk</Normaltekst>
            </Link>
            <Systemtittel className="blokk-m">Stillingsannonse detaljer</Systemtittel>
            <Result items={[xml]} showDetailsLink={false} />
            <div className="StillingXml">
                {xml.stillingXml}
            </div>

        </div>
    );
};

Details.propTypes = {
    match: PropTypes.shape({
        params: PropTypes.shape({
            id: PropTypes.string
        })
    }).isRequired
};

export default Details;
