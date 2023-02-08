import { Routes, Route } from 'react-router-dom';
import {
  Home,
  Search,
  Login,
  Asset,
  Community,
  More,
  Stock,
  Redirect,
} from './pages';
import React from 'react';

const Router = () => {
  return (
    <Routes>
      <Route path="/*" element={<Home />} />
      <Route path="/stock/:stockCode/*" element={<Stock />}></Route>
      <Route path="/login/*" element={<Login />}></Route>
      <Route path="/search" element={<Search />}></Route>
      <Route path="/asset" element={<Asset />}></Route>
      <Route path="/community/*" element={<Community />}></Route>
      <Route path="/more/*" element={<More />}></Route>
      <Route path="/redirect" element={<Redirect />} />
      <Route></Route>
    </Routes>
  );
};

export default Router;
