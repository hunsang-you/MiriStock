import './css/History.css';
import { searchStore } from '../../store';

const History = () => {
  const { searchHistory } = searchStore((state) => state);

  return (
    <div className="watch-list">
      <div className="watch-title">
        <h3> 최근 조회한 종목</h3>
      </div>
      <div className="watch-items">
        {searchHistory.map((data, i) => {
          return (
            <div className="watch-item" key={i}>
              <div>{data.stockName}</div>
              <div>{data.stockCode}</div>
            </div>
          );
        })}
      </div>
    </div>
  );
};

export default History;
