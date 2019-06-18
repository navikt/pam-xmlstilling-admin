import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { Normaltekst } from 'nav-frontend-typografi';
import {getStillingByBatchId} from '../api/api';
import Result from './Result';
import { Systemtittel } from 'nav-frontend-typografi';
//import SyntaxHighlighter from 'react-syntax-highlighter';
//import { googlecode } from 'react-syntax-highlighter/dist/styles/hljs';
import '../styles.less';


const Details = (props) => {

    const [xml, setXml] = useState(undefined);

    useEffect(() => {
        let id = props.match.params.id;
        if (id) {
            const fetchData = async () => {

                const result = await getStillingByBatchId(id);
                console.log('heir',result)
                setXml(result)
            };

            fetchData();
        }

    }, [props.match.params.id]);
    {/*
                    <SyntaxHighlighter language="HTML" style={googlecode} >
                {vkbeautify.xml(xml)}
            </SyntaxHighlighter>
            */}


    if(!xml){
        return null;
    }

    return (
        <div>
            <Link
                to='/'
                className="lenke"
            >
                <Normaltekst className="blokk-s"> Tilbake til søk</Normaltekst>
            </Link>
            <Systemtittel className="blokk-m">Stillingsannonse detaljer</Systemtittel>
            <Result items={[xml]} showDetailsLink={false}/>
            <div className="StillingXml">
                {xml.stillingXml}
            </div>

        </div>
    );
};

export default Details;