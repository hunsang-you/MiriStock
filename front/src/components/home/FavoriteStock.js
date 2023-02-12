import './css/FavoriteStock.css';
import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { memberAPI } from '../../api/api';
import { userStore } from '../../store';
const FavoriteStock = () => {
  const navigate = useNavigate();
  const { user } = userStore((state) => state);
  let [isUpDown, setIsUpDown] = useState([true, true, false]);
  let [favoriteStock, setFavoriteStock] = useState([]);
  useEffect(() => {
    const myFavorite = async () => {
      await memberAPI
        .intersetStocks(user.memberassetCurrentTime)
        .then((request) => {
          let topFavorite = request.data.sort(
            (a, b) => b.stockDataFlucauationRate - a.stockDataFlucauationRate,
          );

          setFavoriteStock(topFavorite.slice(0, 5));
        })
        .catch((err) => console.log(err));
    };
    myFavorite();
  }, [user]);

  return (
    <div className="favorite-container">
      <div
        className="favorite"
        onClick={() => {
          navigate('/homefavorite');
        }}
      >
        관심주식　〉{' '}
      </div>
      {favoriteStock.map((stock, i) => {
        return (
          <div
            className="favorite-list"
            key={i}
            onClick={() => {
              navigate(`stock/${stock.stockCode}`, {
                state: { stockName: stock.stockName },
              });
            }}
          >
            <div className="favorite-top">
              <span>{stock.stockName}</span>
              <span>{stock.stockDataClosingPrice.toLocaleString()}원</span>
            </div>
            <div className="favorite-bottom">
              <span>{stock.stockCode}</span>
              {stock.stockDataFlucauationRate >= 0 ? (
                <span style={{ color: '#D2143C' }}>
                  ▲ {stock.stockDataPriceIncreasement.toLocaleString()}원 ( +
                  {stock.stockDataFlucauationRate.toFixed(2)}%)
                </span>
              ) : (
                <span style={{ color: '#1E90FF' }}>
                  ▼{' '}
                  {Math.abs(stock.stockDataPriceIncreasement).toLocaleString()}
                  원 ({stock.stockDataFlucauationRate.toFixed(2)}%)
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

export default FavoriteStock;
