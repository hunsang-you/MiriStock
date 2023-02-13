import { Button } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import './css/GameExit.css';
const GameExit = () => {
  const navigate = useNavigate();

  return (
    <div className="exit-page">
      <div className="exit-ment">
        <p> 진행중인 게임을 종료하고 </p>
        <p> 새로운 게임을 시작합니다</p>
      </div>
      <div className="exit-btn">
        <div className="exit-backbtn">
          <Button
            id="exit-back"
            variant="outlined"
            size="large"
            onClick={() => {
              navigate(-1);
            }}
          >
            아니오
          </Button>
        </div>
        <div className="exit-submitbtn">
          <Button
            id="exit-submit"
            variant="outlined"
            size="large"
            onClick={() => {
              navigate('/more/result');
            }}
          >
            예
          </Button>
        </div>
      </div>
    </div>
  );
};

export default GameExit;
