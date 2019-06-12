import React, { useState, useEffect } from 'react';
import { searchStillinger } from '../api/api';


const Result = () => {
    const [data, setData] = useState({});

    useEffect( () => {
        const fetchData = async () => {
            const result = await searchStillinger('2018-01-23','2018-01-23','');
            console.log('result', result)
            setData(result);
        };

        fetchData();
    }, []);

    return (
        <div>
            <dl>
                {data && data.length > 0 && data.map(item => (
                    <div key={item.stillingBatchId}>
                        <dt>stillingBatchId</dt>
                        <dd>
                            {item.stillingBatchId}
                        </dd>
                        <dt>eksternBrukerRef</dt>
                        <dd>
                            {item.eksternBrukerRef}
                        </dd>
                        <dt>stillingXml</dt>
                        <dd>
                            {item.stillingXml}
                        </dd>
                        <dt>mottattDato</dt>
                        <dd>
                            {item.mottattDato}
                        </dd>
                        <dt>behandletStatus</dt>
                        <dd>
                            {item.behandletStatus}
                        </dd>
                        <dt>arbeidsgiver</dt>
                        <dd>
                            {item.arbeidsgiver}
                        </dd>
                    </div>
                ))}
            </dl>
        </div>
    );
};

export default Result;