import './css/AssetStatus.css';
import { useState } from 'react';
import Counter from './countanimation';
import CounterPer from './counterperanimation';
import { userStore } from '../../store';

const AssetStatus = (props) => {
  const { user } = userStore((state) => state);
  const userAssetChanged = props.userAssetChanged;
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
        style={
          userAssetChanged.stockDataPriceIncreasement >= 0
            ? { color: '#D2143C' }
            : { color: '#1E90FF' }
        }
      >
        {userAssetChanged.stockDataPriceIncreasement >= 0 ? '▲' : '▼'}
        <Counter
          from={0}
          to={Math.abs(userAssetChanged.stockDataPriceIncreasement)}
        />
        원 ({userAssetChanged.stockDataFlucauationRateSum >= 0 ? '+' : null}
        <CounterPer
          from={0}
          to={userAssetChanged.stockDataFlucauationRateSum}
        />
        )
      </div>
      <hr id="lines" />
    </div>
  );
};

export default AssetStatus;
