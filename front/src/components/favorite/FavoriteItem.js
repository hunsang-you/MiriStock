import './css/Favorite.css';
import { AiFillStar } from 'react-icons/ai';
import { useState, useEffect } from 'react';
import { memberAPI } from '../../api/api';
import { favoriteStore, userStore } from '../../store';
const FavoriteItem = () => {
  const [updateFavorite, setUpdateFavorite] = useState(0);
  const { user } = userStore((state) => state);
  const { favoriteStocks, setFavoriteStocks } = favoriteStore((state) => state);
  // 관심주식 체크 확인
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
  }, [updateFavorite]);
  return (
    <div>
      {favoriteStocks.map((favorite, i) => {
        return (
          <div className="favorite-item" key={i}>
            <div className="favorite-stock">
              <span>{favorite.stockName}</span>
              <span id="favorite-code">{favorite.stockCode}</span>
            </div>
            <div className="favorite-info">
              <span>{favorite.stockDataClosingPrice.toLocaleString()}원</span>
              {favorite.stockDataFlucauationRate >= 0 ? (
                <span style={{ color: '#D2143C' }}>
                  {favorite.stockDataPriceIncreasement.toLocaleString()}원 (
                  {favorite.stockDataPriceIncreasement === 0 ? null : '+'}
                  {favorite.stockDataFlucauationRate.toFixed(2)}%)
                </span>
              ) : (
                <span style={{ color: '#1E90FF' }}>
                  {Math.abs(
                    favorite.stockDataPriceIncreasement,
                  ).toLocaleString()}
                  원 ({favorite.stockDataFlucauationRate.toFixed(2)}%)
                </span>
              )}
            </div>
            <div className="favorite-icon">
              <AiFillStar
                // 클릭시 관심주식 on / off
                id="favorite-icon"
                size={40}
                style={{ color: '#FFCC00' }}
                onClick={() => {
                  memberAPI
                    .deleteIntersetStocks(favorite.stockCode)
                    .then((request) => {
                      setUpdateFavorite(updateFavorite + 1);
                      console.log(request);
                    })
                    .catch((err) => console.log(err));
                }}
              />
            </div>
          </div>
        );
      })}
    </div>
  );
};

export default FavoriteItem;