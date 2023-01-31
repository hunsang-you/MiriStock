import './App.css';
import Router from './Router.js';
import { useNavigate, useLocation } from 'react-router-dom';
import { React, useEffect } from 'react';
import BottomNav from './components/home/BottonNav.js';

function App() {
  const location = useLocation();
  const pathName = location.pathname;
  const navigate = useNavigate();
  function setScreenSize() {
    let vh = window.innerHeight * 0.01;
    document.documentElement.style.setProperty('--vh', `${vh}px`);
  }
  useEffect(() => {
    setScreenSize();
  });

  return (
    <div className="App">
      <div>
        <Router />
      </div>
      {pathName.indexOf('login') === -1 ? (
        <BottomNav location={location} />
      ) : null}
    </div>
  );
}

export default App;
