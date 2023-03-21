import mirilogo from '../../static/mirilogo.png';
import { Button } from '@mui/material';
import { BsFillPersonLinesFill } from 'react-icons/bs';
import { ImMap2, ImExit } from 'react-icons/im';
import { IoMdWarning } from 'react-icons/io';
import { MdOutlineRestartAlt } from 'react-icons/md';
import { useNavigate } from 'react-router-dom';
import { memberStore } from '../../store';
import './css/MoreMain.css';
import axios from 'axios';

const MoreMain = () => {
  const BASE_URL = 'https://miristockserverurl/api';
  const navigate = useNavigate();

  const api2 = axios.create({
    baseURL: BASE_URL,
    withCredentials: true,
    headers: {
      'Content-Type': 'application/json',
    },
  });

  api2.interceptors.request.use(function (config) {
    const token = localStorage.getItem('accessToken');
    config.headers.Authorization = token;

    return config;
  });
  const logoutAPI = {
    logout: () => api2.post(`/member/logout`),
  };
  // const { info, setInfo } = memberStore((state) => state);

  return (
    <div className="more-page">
      <div className="more-logo">
        <img src={mirilogo} width="50%" alt="Logo" />
      </div>
      <div className="more-btn">
        <Button
          id="more-btn"
          variant="outlined"
          size="large"
          sx={{
            width: 300,
            height: 88,
            margin: 2,
            borderRadius: 5,
            border: 2,
          }}
          onClick={() => {
            navigate('change');
          }}
        >
          <BsFillPersonLinesFill size={50} id="more-icon" />

          <div>닉네임 변경</div>
        </Button>
      </div>
      <div className="more-btn">
        <Button
          id="more-btn"
          variant="outlined"
          size="large"
          sx={{
            width: 300,
            height: 88,
            margin: 2,
            borderRadius: 5,
            border: 2,
          }}
          onClick={() => {
            navigate('exit');
          }}
        >
          <MdOutlineRestartAlt size={50} id="more-icon" />

          <div>시뮬레이션 종료</div>
        </Button>
      </div>
      <div className="more-btn">
        <Button
          id="more-btn"
          variant="outlined"
          size="large"
          sx={{
            width: 300,
            height: 88,
            margin: 2,
            borderRadius: 5,
            border: 2,
          }}
          onClick={() => {
            logoutAPI
              .logout()
              .then((request) => {
                console.log(request.data);
                window.localStorage.clear();
                navigate('/login');
              })
              .catch((err) => console.log(err));
          }}
        >
          <ImExit size={50} id="more-icon" />
          <div>로그아웃</div>
        </Button>
      </div>
      <div className="more-btn">
        <Button
          id="more-btn"
          variant="outlined"
          size="large"
          sx={{
            width: 300,
            height: 88,
            margin: 2,
            borderRadius: 5,
            border: 2,
          }}
          onClick={() => {
            navigate('erase');
          }}
        >
          <IoMdWarning size={50} id="more-icon" />

          <div>회원탈퇴</div>
        </Button>
      </div>
      <div className="more-des">
        <p> 버그 신고는 한재윤으로 제보바랍니다</p>
        <p id="more-copy"> Copyright 2023.중꺾마 All right reserved </p>
        <p id="more-copy">Dev. 한재윤 이도겸 배상현 유헌상 안효관</p>
      </div>
    </div>
  );
};

export default MoreMain;

