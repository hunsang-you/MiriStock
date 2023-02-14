import PoChart from './PoChart';
import Result1 from './Result1';
import Result2 from './Result2';
import './css/Portfolio.css';
import { Button } from '@mui/material';
import { useState } from 'react';
import { simulAPI } from '../../api/api';
import { useNavigate } from 'react-router-dom';

const Portfolio = () => {
  const [state, setState] = useState({
    RevenueData: [
      {
        StockName: '삼성전자',
        Revenue: 3333333,
        Rate: 21.26,
      },
      {
        StockName: '삼성카드',
        Revenue: 2222222,
        Rate: 11.62,
      },
      {
        StockName: 'SK하이닉스',
        Revenue: 1822828,
        Rate: 13.19,
      },
    ],
    LossData: [
      {
        StockName: '카카오뱅크',
        Loss: 44444,
        Rate: 21.26,
      },
      {
        StockName: '룰루',
        Loss: 33333,
        Rate: 11.62,
      },
      {
        StockName: '랄라',
        Loss: 22222,
        Rate: 13.19,
      },
    ],
  });

  const navigate = useNavigate();

  return (
    <div className="portfolio-page">
      <div className="port-name">
        <p> 주식고수 김싸피 님의 게임 결과</p>
      </div>
      <div className="portfolio-charts">
        <PoChart />
      </div>
      <div className="port-result">
        <Result1 />
        <Result2 state={state} />
      </div>
      <div className="restart-btn">
        <Button
          id="restart"
          variant="contained"
          size="large"
          onClick={() => {
            navigate('/');
            simulAPI
              .restart()
              .then((request) => console.log(request.data, '시뮬레이션 종료'))
              .catch((err) => console.log(err));
          }}
        >
          게임 재시작
        </Button>
      </div>
    </div>
  );
};

export default Portfolio;
