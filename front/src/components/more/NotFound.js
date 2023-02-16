import notfound from '../../static/notfound.png';
import mirilogo2 from '../../static/mirilogo2.png';
import { Button } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import './css/NotFound.css';

const NotFound = () => {
  const navigate = useNavigate();
  return (
    <div className="notfound-page">
      <div className="notlogo">
        <img src={mirilogo2} id="notfoundmiri" alt="mirilogo" />
      </div>
      <div>
        <img src={notfound} id="notfoundimg" alt="404NotFound" />
      </div>
      <div className="notfoundment">
        <p> 페이지를 찾을 수 없습니다</p>
        <span> 주소가 다르거나 잘못된 경로로 접속하여 </span>
        <span> 현재 페이지를 이용할 수 없습니다 </span>
        <span> 주소가 정확한지 다시 한번 확인해 주세요 </span>
      </div>
      <div className="notfoundback">
        <Button
          id="back404btn"
          variant="contained"
          onClick={() => navigate(-1)}
        >
          이전페이지로 돌아가기
        </Button>
      </div>
    </div>
  );
};

export default NotFound;
