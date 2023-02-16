import './css/EquitiesValue.css';
import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { dateStore } from '../../store';
import { memberAPI } from '../../api/api';
const EquitiesValue = () => {
  const [userStock, setUserStock] = useState([]);
  const { date } = dateStore((state) => state);
  useEffect(() => {
    const getUserStock = async () => {
      await memberAPI
        .stocks()
        .then((request) => {
          setUserStock(request.data.slice(0, 5)); //짜르기
        })
        .catch((err) => {
          console.log(err);
        });
    };
    getUserStock();
  }, [date]);
  const navigate = useNavigate();
  return (
    <div className="equities-container">
      <div className="evaluation">평가금액순</div>
      {userStock.map((stock, i) => {
        return (
          <div
            className="stock"
            key={i}
            onClick={() => {
              navigate(`stock/${stock.stockCode}`, {
                state: { stockName: stock.stockName },
              });
            }}
          >
            <div className="stock-top">
              <span>{stock.stockName}</span>
              <span>{stock.stockDataClosingPrice.toLocaleString()}원</span>
            </div>
            <div className="stock-bottom">
              <span>{stock.memberStockAmount}주</span>
              {stock.stockDataFlucauationRate >= 0 ? (
                <span style={{ color: '#D2143C' }}>
                  ▲ {stock.stockDataPriceIncreasement.toLocaleString()}원 ( +
                  {stock.stockDataFlucauationRate.toFixed(2).toLocaleString()}
                  %)
                </span>
              ) : (
                <span style={{ color: '#1E90FF' }}>
                  ▼{' '}
                  {Math.abs(stock.stockDataPriceIncreasement).toLocaleString()}
                  원 (
                  {stock.stockDataFlucauationRate.toFixed(2).toLocaleString()}
                  %)
                </span>
              )}
            </div>
          </div>
        );
      })}

      <hr id="lines" />
    </div>
  );
};

export default EquitiesValue;
