import { Button } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import { profileAPI } from '../../api/api';
import { memberStore } from '../../store';

import './css/GameExit.css';
const EraseMember = () => {
  const navigate = useNavigate();
  const { info, setInfo } = memberStore((state) => state);

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
            variant="contained"
            style={{ color: 'white' }}
            size="large"
            onClick={() => {
              navigate('/login');
              profileAPI
                .deleteMember(info)
                .then((request) => console.log(request.data))
                .catch((err) => console.log(err));
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
