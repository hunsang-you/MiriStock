import { Button } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import './css/GameExit.css';
const EraseMember = () => {
  const navigate = useNavigate();

  return (
    <div className="exit-page">
      <div className="exit-ment">
        <p> 진행중인 게임을 종료하고 </p>
        <p> 회원 탈퇴를 진행합니다</p>
        <span> 삭제된 정보는 복구가 불가능합니다.</span>
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
              navigate('/');
            }}
          >
            예
          </Button>
        </div>
      </div>
    </div>
  );
};

export default EraseMember;
