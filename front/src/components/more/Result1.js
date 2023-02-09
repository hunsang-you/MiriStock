import './css/Portfolio.css';
import Counter from './Counter';
import CounterPer from './CounterPer';
import { useState } from 'react';

const Result1 = () => {
  const [state, setState] = useState();

  return (
    <div className="portfolio-asset">
      <div className="portfolio-result1">
        <p id="start-asset">초기자산</p>
        <p>
          <Counter from={0} to={50000000} />원
        </p>
        <p id="end-asset">최종자산</p>
        <p>
          <Counter from={0} to={142843350} />원
        </p>
      </div>
      <div className="portfolio-result1">
        <p id="end-profit">수익</p>
        <p>
          <Counter from={0} to={92843350} /> 원
        </p>
        <p id="end-rate">수익률</p>
        <p>
          <CounterPer from={0} to={11.23} />
        </p>
      </div>
    </div>
  );
};

export default Result1;
