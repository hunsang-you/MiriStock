import './App.css';
import Router from './Router.js';
import { useNavigate, useLocation } from 'react-router-dom';
import { React } from 'react';
import BottomNav from './components/home/BottonNav.js';

function App() {
  const location = useLocation();
  const pathName = location.pathname;
  const navigate = useNavigate();

  return (
    <div className="App">
      <h1>개발쉬작~</h1>
      <button
        onClick={() => {
          navigate('/login');
        }}
      >
        로그인테스트
      </button>
      <Router />
      {pathName.indexOf('login') === -1 ? (
        <BottomNav location={location} />
      ) : null}
    </div>
  );
}

export default App;
