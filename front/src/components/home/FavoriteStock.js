import './css/FavoriteStock.css';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';

const FavoriteStock = () => {
  const navigate = useNavigate();
  let [isUpDown, setIsUpDown] = useState([true, true, false]);
  let [favoriteStock, setFavoriteStock] = useState([
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
  ]);

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
      {favoriteStock.map((dat, i) => {
        return (
          <div className="favorite-list" key={i}>
            <div className="favorite-top">
              <span>{dat.name}</span>
              <span>{dat.price}원</span>
            </div>
            <div className="favorite-bottom">
              <span>{dat.code}</span>
              <span
                style={
                  isUpDown[i] ? { color: '#D2143C' } : { color: '#1E90FF' }
                }
              >
                {dat.change}
              </span>
            </div>
          </div>
        );
      })}
      <hr id="lines" />
    </div>
  );
};

export default FavoriteStock;
