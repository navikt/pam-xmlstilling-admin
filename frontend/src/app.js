import React, { useState, useEffect } from 'react';
import ReactDOM from 'react-dom';
import { searchStillinger } from './api/api';
import Search from './search/Search';
import Result from './result/Result';
import styles from './styles.less'

const App = () => {
    return (
        <div>
            <Result />
        </div>
    );
};

ReactDOM.render(
    <App />,
    document.getElementById('app')
);