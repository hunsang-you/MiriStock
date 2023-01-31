import './css/Login.css';
import { Routes, Route } from 'react-router-dom';

import Nickname from '../components/login/Nickname';
import LoginBtn from '../components/login/LoginBtn';

function Login() {
  return (
    <div>
      <Routes>
        <Route path="" element={LoginBtn()} />;
        <Route path="nickname" element={<Nickname />} />;
      </Routes>
    </div>
  );
}

export default Login;
