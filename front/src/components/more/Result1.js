import './css/Portfolio.css';
import Counter from './Counter';
import CounterPer from './CounterPer';
import { useState } from 'react';

const Result1 = (props) => {
  const portfol = props.portfol;

  const [state, setState] = useState();

  const earnPrice = portfol.memberAsset.memberassetAvailableAsset - 100000000;
  const earnRate = (earnPrice / 100000000) * 100;
  return (
    <div className="portfolio-asset">
      <div className="portfolio-result1">
        <p id="start-asset">초기자산</p>
        <p>
          <Counter from={0} to={100000000} />원
        </p>
        <p id="end-asset">최종자산</p>
        <p>
          <Counter
            from={0}
            to={portfol.memberAsset.memberassetAvailableAsset}
          />
          원
        </p>
      </div>
      <div className="portfolio-result1">
        <p id="end-profit">수익</p>
        <p style={earnPrice > 0 ? { color: ' #D2143C' } : { color: '#1E90FF' }}>
          <Counter from={0} to={earnPrice} />원
        </p>
        <p id="end-rate">수익률</p>
        <p style={earnRate > 0 ? { color: ' #D2143C' } : { color: '#1E90FF' }}>
          <CounterPer from={0} to={earnRate} />
        </p>
      </div>
    </div>
  );
};

export default Result1;
