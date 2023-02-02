import './App.css';
import Router from './Router.js';
import { useLocation } from 'react-router-dom';
import { React, useEffect } from 'react';
import BottomNav from './components/home/BottonNav.js';
import { navStore } from './store.js';

function App() {
  const { page, setPage } = navStore((state) => state);
  const location = useLocation();

  function setScreenSize() {
    let vh = window.innerHeight * 0.01;
    document.documentElement.style.setProperty('--vh', `${vh}px`);
  }
  useEffect(() => {
    setScreenSize();
  });
  useEffect(() => {
    if (
      location.pathname.indexOf('home') !== -1 ||
      location.pathname === '/' ||
      location.pathname.indexOf('search') !== -1 ||
      location.pathname.indexOf('asset') !== -1 ||
      location.pathname.indexOf('community') !== -1 ||
      location.pathname.indexOf('more') !== -1
    ) {
      setPage(location.pathname);
    }
  }, [location, setPage]);

  return (
    <div className="App">
      <div>
        <Router />
      </div>
      {page.indexOf('login') === -1 ? <BottomNav /> : null}
    </div>
  );
}

export default App;
