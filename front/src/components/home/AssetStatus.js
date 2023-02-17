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
  const rateReturn = user.memberassetTotalAsset - 100000000;
  const rateReturnPer = (user.memberassetTotalAsset / 100000000) * 100 - 100;
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
        <div>총 자산 현황</div>
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
                : user.memberassetStockAsset + user.memberassetAvailableAsset
            }
          />
        </span>
        <span className="unit">원</span>
      </div>
      <div
        className="change"
        style={
          rateReturnPer >= 0 || rateReturn === undefined
            ? { color: '#D2143C' }
            : { color: '#1E90FF' }
        }
      >
        {rateReturn >= 0 || rateReturn === undefined ? '▲' : '▼'}
        <Counter
          from={0}
          to={rateReturn === undefined ? 0 : Math.abs(rateReturn)}
        />
        원 ({rateReturnPer >= 0 || rateReturnPer === undefined ? '+' : null}
        <CounterPer
          from={0}
          to={rateReturnPer === undefined ? 0 : rateReturnPer}
        />
        )
      </div>
      <hr id="lines" />
    </div>
  );
};

export default AssetStatus;
