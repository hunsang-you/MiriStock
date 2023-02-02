import { Routes, Route } from 'react-router-dom';
import { Home, Search, Login, Asset, Community, More } from './pages';
import React from 'react';

const Router = () => {
  return (
    <Routes>
      <Route path="/" element={<Home />} />
      <Route path="/stockdetail/"></Route>
      <Route path="/login/*" element={<Login />}></Route>
      <Route path="/search" element={<Search />}></Route>
      <Route path="/asset" element={<Asset />}></Route>
      <Route path="/community/*" element={<Community />}></Route>
      <Route path="/more" element={<More />}></Route>
      <Route></Route>
      <Route></Route>
    </Routes>
  );
};

export default Router;
