import Chart from './Chart';
import './css/Portfolio.css';
import { Button } from '@mui/material';
import Result1 from './Result1';
import Result2 from './Result2';

const Portfolio = () => {
  return (
    <div className="portfolio-page">
      <div className="portfolio-charts">
        <Chart />
      </div>
      <Result1 />
      <Result2 />
      <div className="restart-btn">
        <Button id="restart" variant="outlined" size="large">
          게임 재시작
        </Button>
      </div>
    </div>
  );
};

export default Portfolio;
