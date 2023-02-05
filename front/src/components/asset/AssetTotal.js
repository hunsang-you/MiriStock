import './css/assettotal.css';
import Counter from '../home/countanimation';
import CounterPer from '../home/counterperanimation';

const AssetTotal = () => {
  return (
    <div className="asset-total">
      <div className="asset-cash">
        <div>현금</div>
        <div>
          <Counter from={0} to={44462050} />원
        </div>
      </div>
      <div className="asset-cash">
        <div>주식</div>
        <div>
          <Counter from={0} to={98381300} />원
        </div>
      </div>
      <div className="asset-cash">
        <div>수익률</div>
        <div>
          <Counter from={0} to={92843350} />원 (
          <CounterPer from={0} to={285.4} />)
        </div>
      </div>
    </div>
  );
};

export default AssetTotal;
