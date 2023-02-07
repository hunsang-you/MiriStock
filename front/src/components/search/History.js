import './css/History.css';
import { searchStore } from '../../store';
import { searchAPI } from '../../api/api';
import { useEffect, useState } from 'react';

const History = () => {
  const [searchHistory, setSearchHistory] = useState([]);
  useEffect(() => {
    searchAPI
      .getSearchHis()
      .then((request) => {
        setSearchHistory(request.data);
      })
      .catch((err) => console.log(err));
  }, []);
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
              <button
                onClick={() => {
                  console.log(data.searchNo);
                  searchAPI
                    .deleteSearchHis(data.searchNo)
                    .then((request) => console.log(request.data))
                    .catch((err) => console.log(err));
                }}
              >
                X
              </button>
            </div>
          );
        })}
      </div>
    </div>
  );
};

export default History;
