import './css/AssetStatus.css';
import { useState } from 'react';
import Counter from './countanimation';
import CounterPer from './counterperanimation';
import { userStore } from '../../store';
import { AiOutlineInfoCircle } from 'react-icons/ai';
import Swal from 'sweetalert2';
import mainImg from '../.././static/Main_Guide.png';
const AssetStatus = (props) => {
  const { user } = userStore((state) => state);
  const userAssetChanged = props.userAssetChanged;
  const guideLine = () => {
    Swal.fire({
      imageUrl: mainImg,
      imageHeight: 450,
      imageWidth: 340,
      showConfirmButton: false,
      showCancelButton: true,
      cancelButtonText: '닫기',
      cancelButtonColor: '#6DCEF5',
    });
  };
  return (
    <div className="asset">
      <div className="asset-status">
        <div>보유 주식 현황</div>
        <div style={{ paddingTop: '7px' }}>
          <AiOutlineInfoCircle
            onClick={() => {
              guideLine();
            }}
          />
        </div>
      </div>
      <div className="money-unit">
        <span className="money">
          <Counter
            from={0}
            to={
              user.memberassetStockAsset === undefined
                ? 0
                : user.memberassetStockAsset
            }
          />
        </span>
        <span className="unit">원</span>
      </div>
      <div
        className="change"
        style={
          userAssetChanged.stockDataPriceIncreasement >= 0 ||
          userAssetChanged.stockDataPriceIncreasement === undefined
            ? { color: '#D2143C' }
            : { color: '#1E90FF' }
        }
      >
        {userAssetChanged.stockDataPriceIncreasement >= 0 ||
        userAssetChanged.stockDataPriceIncreasement === undefined
          ? '▲'
          : '▼'}
        <Counter
          from={0}
          to={
            userAssetChanged.stockDataPriceIncreasement === undefined
              ? 0
              : Math.abs(userAssetChanged.stockDataPriceIncreasement)
          }
        />
        원 (
        {userAssetChanged.stockDataFlucauationRateSum >= 0 ||
        userAssetChanged.stockDataPriceIncreasement === undefined
          ? '+'
          : null}
        <CounterPer
          from={0}
          to={
            userAssetChanged.stockDataFlucauationRateSum === undefined
              ? 0
              : userAssetChanged.stockDataFlucauationRateSum
          }
        />
        )
      </div>
      <hr id="lines" />
    </div>
  );
};

export default AssetStatus;
