import mirilogo from '../../static/mirilogo.png';
import kakaobtn from '../../static/kakaobtn.png';
import naverbtn from '../../static/naverbtn.png';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import './nickname.css';

const BASE_URL = process.env.REACT_APP_BASE_UR;
const KAKAO_URL = `${BASE_URL}/oauth2/authorization/kakao`;
const LoginBtn = () => {
  const navigate = useNavigate();

  return (
    <div className="loginpage">
      <div className="logo">
        <img
          src={mirilogo}
          style={{ width: '100%', height: '100%', objectFit: 'fill' }}
          alt="logo"
        />
      </div>

      {/*  소셜 로그인 버튼 */}
      <div className="loginbtn">
        <div className="kakaologin">
          <img
            src={kakaobtn}
            className="btn"
            alt="kakao"
            onClick={() => {
              // navigate('nickname');
              // window.open('http://192.168.31.160/oauth2/authorization/kakao');
              // axios
              //   .get(`${BASE_URL}/oauth2/authorization/kakao`)
              //   .then((request) => {
              //     console.log(request);
              //   })
              //   .catch((err) => {
              //     console.log(err);
              //   });
              window.location.href = KAKAO_URL;
            }}
          />
        </div>

        <div className="naverlogin">
          <img
            src={naverbtn}
            className="btn"
            alt="naver"
            onClick={() => {
              navigate('nickname');
            }}
          />
        </div>
      </div>
    </div>
  );
};
export default LoginBtn;
