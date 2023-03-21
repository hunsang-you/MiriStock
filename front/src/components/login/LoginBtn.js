import mirilogo from '../../static/mirilogo.png';
import kakaobtn from '../../static/kakaobtn.png';
import naverbtn from '../../static/naverbtn.png';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import './nickname.css';
const BASE_URL = 'https://miristockserverurl/api';
const KAKAO_URL = `${BASE_URL}/oauth2/authorization/kakao`;
const NAVER_URL = `${BASE_URL}/oauth2/authorization/naver`;
const LoginBtn = () => {
  const navigate = useNavigate();
  return (
    <div className="loginpage">
      <div className="logo">
        <img
          src={mirilogo}
          style={{ width: '100%', height: '100%', objectFit: 'fill' }}
          alt="l ogo"
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
              // window.open('http://localhost/oauth2/authorization/kakao');
              // axios
              //   .get(`${BASE_URL}/oauth2/authorization/kakao`)
              //   .then((request) => {
              //     console.log(request);
              //   })
              //   .catch((err) => {
              // console.log(err);
              //   });
              setTimeout(() => (window.location.href = KAKAO_URL), 300);
            }}
          />
        </div>

        {/* <div className="naverlogin">
          <img
            src={naverbtn}
            className="btn"
            alt="naver"
            onClick={() => {
              setCheckSocial(2);
              setTimeout(() => (window.location.href = NAVER_URL), 300);
            }}
          />
        </div> */}
      </div>
    </div>
  );
};
export default LoginBtn;
