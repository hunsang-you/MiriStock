import './css/AssetStatus.css';
import { useState } from 'react';

const AssetStatus = () => {
  let [isUpDown, setIsUpDown] = useState(true);

  return (
    <div className="asset">
      <div className="asset-status">보유 현황</div>
      <div className="money-unit">
        <span className="money">142,342,560</span>
        <span className="unit">원</span>
      </div>
      <div
        className="change"
        style={isUpDown ? { color: '#D2143C' } : { color: '#1E90FF' }}
      >
        3,086,500원 (11.28%)
      </div>
      <hr id="lines" />
    </div>
  );
};

export default AssetStatus;
