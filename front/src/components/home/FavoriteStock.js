import './css/FavoriteStock.css';
import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { memberAPI } from '../../api/api';
import { userStore, favoriteStore } from '../../store';
const FavoriteStock = () => {
  const navigate = useNavigate();
  const { user } = userStore((state) => state);
  const { favoriteStocks, setFavoriteStocks } = favoriteStore((state) => state);
  useEffect(() => {
    const myFavorite = async () => {
      await memberAPI
        .intersetStocks(user.memberassetCurrentTime)
        .then((request) => {
          setFavoriteStocks(request.data);
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
          navigate('/favorite');
        }}
      >
        관심주식　〉{' '}
      </div>
      {favoriteStocks.slice(0, 5).map((stock, i) => {
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
