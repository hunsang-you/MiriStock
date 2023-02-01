import { Routes, Route } from 'react-router-dom';
import {
  Home,
  Search,
  Login,
  Asset,
  Community,
  More,
  StockDetail,
} from './pages';
import TestChart from './components/chart/TestChart';
import TestSample from './components/chart/TestSample';
import React from 'react';

const Router = () => {
  return (
    <Routes>
      <Route path="/" element={<Home />} />
      <Route path="/stockdetail/" element={<StockDetail />}></Route>
      <Route path="/login/*" element={<Login />}></Route>
      <Route path="/search" element={<Search />}></Route>
      <Route path="/asset" element={<Asset />}></Route>
      <Route path="/community" element={<Community />}></Route>
      <Route path="/more" element={<More />}></Route>
      <Route path="/testchart" element={<TestChart />}></Route>
      <Route path="/testsample" element={<TestSample />}></Route>
      <Route></Route>
    </Routes>
  );
};

export default Router;
