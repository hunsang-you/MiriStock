import { Routes, Route, BrowserRouter } from 'react-router-dom';
import { Home } from './pages';
import React from 'react';

const Router = () => {

    return (
        <BrowserRouter>
            <Routes>
                <Route path='/' element={ <Home/> }>
                    <Route path='detail'/>
                    <Route path='likes'/>
                </Route>
                <Route path='/stockdetail'>

                </Route>
                <Route path='/login'>

                </Route>
                <Route></Route>
                <Route></Route>
                <Route></Route>
                <Route></Route>
                <Route></Route>
                <Route></Route>
            </Routes>
        </BrowserRouter>
    )
}

export default Router;