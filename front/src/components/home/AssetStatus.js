import './css/AssetStatus.css';
import { useState } from 'react';
import Counter from './countanimation';
import CounterPer from './counterperanimation';

const AssetStatus = () => {
  let [isUpDown, setIsUpDown] = useState(true);
  return (
    <div className="asset">
      <div className="asset-status">보유 현황</div>
      <div className="money-unit">
        <span className="money">
          <Counter from={0} to={142342560} />
        </span>
        <span className="unit">원</span>
      </div>
      <div
        className="change"
        style={isUpDown ? { color: '#D2143C' } : { color: '#1E90FF' }}
      >
        <Counter from={0} to={3086500} />원 (<CounterPer from={0} to={11.28} />)
      </div>
      <hr id="lines" />
    </div>
  );
};

export default AssetStatus;
