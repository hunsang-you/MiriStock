import mirilogo from '../../static/mirilogo.png';
import { Button } from '@mui/material';
import { BsFillPersonLinesFill } from 'react-icons/bs';
import { ImMap2, ImExit } from 'react-icons/im';
import { MdOutlineRestartAlt } from 'react-icons/md';
import { useNavigate } from 'react-router-dom';
import './css/MoreMain.css';

const MoreMain = () => {
  const navigate = useNavigate();

  return (
    <div className="more-page">
      <div className="more-logo">
        <img src={mirilogo} width="200px" alt="Logo" />
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
            navigate('result');
          }}
        >
          <ImMap2 size={50} id="more-icon" />
          <div>튜토리얼</div>
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

          <div>게임종료</div>
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
          <ImExit size={50} id="more-icon" />

          <div>회원탈퇴</div>
        </Button>
      </div>
      <div className="more-des">
        <p> 버그 신고는 google@google.com 으로 제보바랍니다</p>
        <p id="more-copy"> Copyright 2023.B111 All right reserved </p>
      </div>
    </div>
  );
};

export default MoreMain;
