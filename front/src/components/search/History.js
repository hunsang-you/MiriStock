import './css/History.css';
import { searchStore } from '../../store';
import { searchAPI } from '../../api/api';
import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import cancel from '../../static/cancel.png';

const History = () => {
  const [searchHistory, setSearchHistory] = useState([]);
  const navigate = useNavigate();
  useEffect(() => {
    const getHistory = async () => {
      await searchAPI
        .getSearchHis()
        .then((request) => {
          setSearchHistory(request.data);
        })
        .catch((err) => console.log(err));
    };
    getHistory();
  }, []);
  return (
    <div className="watch-list">
      <div className="watch-title">
        <h3> 최근 조회한 종목</h3>
      </div>
      <div className="watch-items">
        {searchHistory.map((stock, i) => {
          return (
            <div
              className="watch-item"
              key={i}
              onClick={() => {
                navigate(`/stock/${stock.stockCode}`, {
                  state: { stockName: stock.stockName },
                });
              }}
            >
              <div>{stock.stockName}</div>
              <div>{stock.stockCode}</div>
              <button
                onClick={(e) => {
                  e.stopPropagation();
                  console.log(stock.searchNo);
                  searchAPI
                    .deleteSearchHis(stock.searchNo)
                    .then((request) => console.log(request.data))
                    .catch((err) => console.log(err));
                }}
              >
                {/* <img src={cancel} id="cancel-img" /> */}X
              </button>
            </div>
          );
        })}
      </div>
    </div>
  );
};

export default History;
