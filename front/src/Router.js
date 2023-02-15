import { Routes, Route } from 'react-router-dom';
import { lazy, Suspense } from 'react';
import React from 'react';
import Loading from './components/Loading';
const Home = lazy(() => import('./pages/Home'));
const Search = lazy(() => import('./pages/Search'));
const Login = lazy(() => import('./pages/Login'));
const Asset = lazy(() => import('./pages/Asset'));
const Community = lazy(() => import('./pages/Community'));
const More = lazy(() => import('./pages/More'));
const Stock = lazy(() => import('./pages/Stock'));
const Redirect = lazy(() => import('./pages/Redirect'));
const notFound = lazy(() => import('./components/more/NotFound'));
const Router = () => {
  return (
    <Routes>
      <Route
        path="/"
        element={
          <Suspense fallback={<Loading />}>
            <Home />
          </Suspense>
        }
      />
      <Route
        path="/stock/:stockCode/*"
        element={
          <Suspense fallback={<Loading />}>
            <Stock />
          </Suspense>
        }
      ></Route>
      <Route
        path="/login/*"
        element={
          <Suspense fallback={<Loading />}>
            <Login />
          </Suspense>
        }
      ></Route>
      <Route
        path="/search"
        element={
          <Suspense fallback={<Loading />}>
            <Search />
          </Suspense>
        }
      ></Route>
      <Route
        path="/asset"
        element={
          <Suspense fallback={<Loading />}>
            <Asset />
          </Suspense>
        }
      ></Route>
      <Route
        path="/community/*"
        element={
          <Suspense fallback={<Loading />}>
            <Community />
          </Suspense>
        }
      ></Route>
      <Route
        path="/more/*"
        element={
          <Suspense fallback={<Loading />}>
            <More />
          </Suspense>
        }
      ></Route>
      <Route
        path="/redirect"
        element={
          <Suspense fallback={<Loading />}>
            <Redirect />
          </Suspense>
        }
      />
      <Route
        path="*"
        element={
          <Suspense fallback={<Loading />}>
            <notFound />
          </Suspense>
        }
      ></Route>
    </Routes>
  );
};

export default Router;
//<Route path="homeFavorite" element={<HomeFavorite />} />;
