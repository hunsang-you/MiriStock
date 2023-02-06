import FavoriteItem from './FavoriteItem';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { MdOutlineArrowBackIos } from 'react-icons/md';
import './css/Favorite.css';

const FavoriteList = () => {
  const navigate = useNavigate();
  const [favorites, setFavorites] = useState([
    {
      name: '삼성전자',
      price: '65,304',
      code: '005930',
      change: '▲400원 (+1.66%)',
    },
    {
      name: 'LG엔솔',
      price: '65,304',
      code: '034220',
      change: '▲400원 (+1.66%)',
    },
    {
      name: '똥카오',
      price: '65,304',
      code: '373220',
      change: '▼400원 (-1.66%)',
    },
    {
      name: '삼성카드',
      price: '55,555',
      code: '029780',
      change: '▼400원 (-0.71%)',
    },
  ]);

  return (
    <div className="favorite-page">
      <div className="favorite-title">
        <p
          onClick={() => {
            navigate(-1);
          }}
        >
          〈 관심주식
        </p>
      </div>
      <div className="favorite-list2">
        {favorites.map((favorite, i) => {
          return (
            <div key={i}>
              <FavoriteItem favorite={favorite} i={i} />
            </div>
          );
        })}
      </div>
    </div>
  );
};

export default FavoriteList;
