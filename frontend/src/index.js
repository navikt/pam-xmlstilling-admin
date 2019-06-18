import React from 'react';
import ReactDOM from 'react-dom';
import { BrowserRouter , Route, Switch } from 'react-router-dom';
import Search from './search/Search';
import Details from './result/Details';
import { StoreProvider } from "./context/StoreContext";
import './styles.less'


const Index = () => {

    return (
        <main>
            <Switch>
                <Route exact path="/" component={Search}/>
                <Route exact path="/:id" component={Details}/>
            </Switch>
        </main>

    );
};

ReactDOM.render(
    <StoreProvider>
        <BrowserRouter >
            <Index />
        </BrowserRouter >
    </StoreProvider>,
    document.getElementById('app')
);
