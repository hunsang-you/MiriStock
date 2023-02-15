import './css/Portfolio.css';
import Counter from '../home/countanimation';

const Result2 = (props) => {
  // const RevenueData = props.state.RevenueData;
  // const LossData = props.state.LossData;

  const portfol = props.portfol;
  const highMemberStock = portfol.highMemberStock;
  const lowMemberStock = portfol.lowMemberStock;

  return (
    <div className="portfolio-result2">
      <div>
        <p> {portfol.memberAsset.member.memberNickname} 님의 BEST TOP3</p>
        {highMemberStock.map((data, i) => {
          if (highMemberStock[i].memberStockEarnBoolean === 'TRUE') {
            return (
              <div key={i} className="portfolio-best">
                <span> {data.stockName} </span>
                <span id="port-revenue">
                  ▲ <Counter from={0} to={data.memberStockEarnPrice} />원
                </span>
              </div>
            );
          } else {
            return (
              <div key={i} className="portfolio-best">
                <span> {data.stockName} </span>
                <span id="port-revenue">
                  ▼ <Counter from={0} to={data.memberStockEarnPrice} />원
                </span>
              </div>
            );
          }
        })}
      </div>
      <hr id="result-lines" />
      <div>
        <p> {portfol.memberAsset.member.memberNickname} 님의 WORST TOP3</p>
        {lowMemberStock.map((data, i) => {
          if (lowMemberStock[i].memberStockEarnBoolean === 'FALSE') {
            return (
              <div key={i} className="portfolio-worst">
                <span> {data.stockName} </span>
                <span id="port-loss">
                  ▼ <Counter from={0} to={data.memberStockEarnPrice} /> 원
                </span>
              </div>
            );
          } else {
            return (
              <div key={i} className="portfolio-worst">
                <span> {data.stockName} </span>
                <span id="port-loss">
                  ▲ <Counter from={0} to={data.memberStockEarnPrice} /> 원
                </span>
              </div>
            );
          }
        })}
      </div>
    </div>
  );
};

export default Result2;
