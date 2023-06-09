import PoChart from './PoChart';
import Result1 from './Result1';
import Result2 from './Result2';
import './css/Portfolio.css';
import { Button } from '@mui/material';
import { useState, useEffect } from 'react';
import { simulAPI } from '../../api/api';
import { useNavigate } from 'react-router-dom';

const Portfolio = () => {
  const [portfol, setPortfol] = useState({});

  useEffect(() => {
    const getPortfol = async () => {
      await simulAPI
        .theEnd()
        .then((request) => {
          setPortfol(request.data);
          simulAPI
            .restart()
            .then((request) => {})
            .catch((err) => console.log(err));
        })
        .catch((err) => console.log(err));
    };
    getPortfol();
  }, []);
  function isEmptyObj(obj) {
    if (obj.constructor === Object && Object.keys(obj).length === 0) {
      return true;
    }

    return false;
  }
  const navigate = useNavigate();

  return (
    <div className="portfolio-page">
      <div className="port-name">
        <div>
          {isEmptyObj(portfol) === false &&
            portfol.memberAsset.member.memberNickname}{' '}
          님의
        </div>
        <div> 투자 결과</div>
      </div>
      <div className="portfolio-charts">
        {isEmptyObj(portfol) === false &&
          portfol.highMemberStock.length !== 0 && <PoChart portfol={portfol} />}
      </div>
      <div className="port-result">
        {isEmptyObj(portfol) === false &&
          portfol.highMemberStock.length !== 0 && <Result1 portfol={portfol} />}
        {isEmptyObj(portfol) === false &&
          portfol.highMemberStock.length !== 0 && <Result2 portfol={portfol} />}
      </div>
      <div className="restart-btn">
        <Button
          id="restart"
          variant="contained"
          size="large"
          onClick={() => {
            navigate('/');
          }}
        >
          시뮬레이션 재시작
        </Button>
      </div>
    </div>
  );
};

export default Portfolio;
