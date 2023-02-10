import './css/Favorite.css';
import { AiFillStar } from 'react-icons/ai';
import { useState } from 'react';
import { memberAPI } from '../../api/api';

const FavoriteItem = (props) => {
  const favorite = props.favorite;

  // 관심주식 체크 확인
  const [isFavorite, setIsFavorite] = useState(true);

  return (
    <div className="favorite-item">
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
            {Math.abs(favorite.stockDataPriceIncreasement).toLocaleString()}원 (
            {favorite.stockDataFlucauationRate.toFixed(2)}%)
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
              .then((request) => console.log(request))
              .catch((err) => console.log(err));
          }}
        />
      </div>
    </div>
  );
};

export default FavoriteItem;
