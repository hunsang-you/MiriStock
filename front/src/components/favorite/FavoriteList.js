import FavoriteItem from './FavoriteItem';
import { useNavigate } from 'react-router-dom';
import { userStore } from '../../store';
import './css/Favorite.css';

const FavoriteList = () => {
  const navigate = useNavigate();
  const { user } = userStore((state) => state);

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
        <FavoriteItem />
      </div>
    </div>
  );
};

export default FavoriteList;
