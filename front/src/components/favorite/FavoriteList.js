import FavoriteItem from './FavoriteItem';
import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { MdOutlineArrowBackIos } from 'react-icons/md';
import { memberAPI } from '../../api/api';
import { userStore } from '../../store';
import './css/Favorite.css';

const FavoriteList = () => {
  const navigate = useNavigate();
  const { user } = userStore((state) => state);
  const date = user.memberassetCurrentTime;
  const [favorites, setFavorites] = useState([]);
  useEffect(() => {
    const getFavorite = async () => {
      await memberAPI
        .intersetStocks(date)
        .then((request) => {
          let favorite = request.data.sort(
            (a, b) => b.stockDataFlucauationRate - a.stockDataFlucauationRate,
          );
          setFavorites(favorite);
        })
        .catch((err) => {
          console.log(err);
        });
    };
    getFavorite(date);
  }, [date]);

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
