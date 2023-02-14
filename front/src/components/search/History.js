import './css/History.css';
import { searchAPI } from '../../api/api';
import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

const History = () => {
  const [searchHistory, setSearchHistory] = useState([]);
  const [refreshHistory, setRefreshHistory] = useState(0);
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
  }, [refreshHistory]);
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
              <div>
                {stock.stockCode}
                <button
                  id="deletesearch-btn"
                  onClick={(e) => {
                    e.stopPropagation();
                    searchAPI
                      .deleteSearchHis(stock.stockCode)
                      .then((request) => {
                        setRefreshHistory(refreshHistory + 1);
                      })
                      .catch((err) => console.log(err));
                  }}
                >
                  X
                </button>
              </div>
            </div>
          );
        })}
      </div>
    </div>
  );
};

export default History;
