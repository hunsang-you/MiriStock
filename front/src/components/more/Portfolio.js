import PoChart from './PoChart';
import './css/Portfolio.css';
import { Button } from '@mui/material';
import Result1 from './Result1';
import Result2 from './Result2';

const Portfolio = () => {
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
        <Result2 />
      </div>
      <div className="restart-btn">
        <Button id="restart" variant="outlined" size="large">
          게임 재시작
        </Button>
      </div>
    </div>
  );
};

export default Portfolio;
