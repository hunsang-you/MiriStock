import './css/Favorite.css';
import { AiFillStar } from 'react-icons/ai';
import { useState } from 'react';

const FavoriteItem = (props) => {
  const favorite = props.favorite;

  // 주식 상승 하락 확인
  const [isUpDown, setIsUpDown] = useState([true, true, false]);

  // 관심주식 체크 확인
  const [isFavorite, setIsFavorite] = useState(true);

  return (
    <div className="favorite-item">
      <div className="favorite-stock">
        <span>{favorite.name}</span>
        <span id="favorite-code">{favorite.code}</span>
      </div>
      <div className="favorite-info">
        <span>{favorite.price}원</span>
        <span
          style={
            isUpDown[props.i] ? { color: '#D2143C' } : { color: '#1E90FF' }
          }
        >
          {favorite.change}
        </span>
      </div>
      <div className="favorite-icon">
        <AiFillStar
          // 클릭시 관심주식 on / off
          id="favorite-icon"
          size={40}
          style={isFavorite ? { color: '#FFCC00' } : { color: '#AAA7A7' }}
          onClick={() => {
            setIsFavorite(!isFavorite);
          }}
        />
      </div>
    </div>
  );
};

export default FavoriteItem;
