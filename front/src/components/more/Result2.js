import './css/Portfolio.css';
import Counter from '../home/countanimation';

const Result2 = (props) => {
  const RevenueData = props.state.RevenueData;
  const LossData = props.state.LossData;

  return (
    <div className="portfolio-result2">
      <div>
        <p> 유저닉네임/ 님의 BEST TOP3</p>

        {RevenueData.map((data, i) => {
          return (
            <div key={i} className="portfolio-best">
              <span> {data.StockName} </span>
              <span id="port-revenue">
                <Counter from={0} to={data.Revenue} />원
              </span>
            </div>
          );
        })}
      </div>
      <hr id="result-lines" />
      <div>
        <p> 유저닉네임/ 님의 WORST TOP3</p>
        {LossData.map((data, i) => {
          return (
            <div key={i} className="portfolio-worst">
              <span> {data.StockName} </span>
              <span id="port-loss">
                <Counter from={0} to={data.Loss} /> 원
              </span>
            </div>
          );
        })}
      </div>
    </div>
  );
};

export default Result2;
