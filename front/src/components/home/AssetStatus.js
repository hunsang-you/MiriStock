import './css/AssetStatus.css';
import { useState } from 'react';
import Counter from './countanimation';
import CounterPer from './counterperanimation';
import { userStore } from '../../store';

const AssetStatus = () => {
  let [isUpDown, setIsUpDown] = useState(true);
  const { user } = userStore((state) => state);
  return (
    <div className="asset">
      <div className="asset-status">보유 현황</div>
      <div className="money-unit">
        <span className="money">
          <Counter from={0} to={user.memberassetTotalAsset} />
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
