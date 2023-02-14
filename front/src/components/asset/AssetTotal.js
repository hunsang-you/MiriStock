import './css/assettotal.css';
import Counter from '../home/countanimation';
import CounterPer from '../home/counterperanimation';
import { userStore } from '../../store';

const AssetTotal = () => {
  const { user } = userStore((state) => state);
  //memberassetAvailableAsset memberassetStockAsset memberassetTotalAsset
  const rateReturn = user.memberassetTotalAsset - 50000000;
  const rateReturnPer = (user.memberassetTotalAsset / 50000000) * 100 - 100;
  return (
    <div className="asset-total">
      <div className="asset-cash">
        <div>현금</div>
        <div>
          <Counter from={0} to={user.memberassetAvailableAsset} />원
        </div>
      </div>
      <div className="asset-cash">
        <div>주식</div>
        <div>
          <Counter from={0} to={user.memberassetStockAsset} />원
        </div>
      </div>
      <div className="asset-cash">
        <div>수익률</div>
        <div>
          <Counter from={0} to={rateReturn} />원 (
          <CounterPer from={0} to={rateReturnPer} />)
        </div>
      </div>
    </div>
  );
};

export default AssetTotal;
