import {
  Simulation,
  AssetStatus,
  EquitiesValue,
  FavoriteStock,
  Rank,
} from '../components/home';
import { rankAPI, memberAPI } from '../api/api';

const HomeMain = () => {
  return (
    <div>
      <Simulation />
      <AssetStatus />
      <EquitiesValue />
      <FavoriteStock />
      <Rank />
      <button
        onClick={() => {
          rankAPI
            .increase()
            .then((request) => {
              console.log(request.data);
            })
            .catch((err) => {
              console.log(err);
            });
        }}
      >
        increase
      </button>
      <button
        onClick={() => {
          rankAPI
            .decrease()
            .then((request) => {
              console.log(request.data);
            })
            .catch((err) => {
              console.log(err);
            });
        }}
      >
        decrease
      </button>
      <button
        onClick={() => {
          memberAPI
            .asset()
            .then((request) => {
              console.log(request.data);
            })
            .catch((err) => {
              console.log(err);
            });
        }}
      >
        stockdetail
      </button>
    </div>
  );
};

export default HomeMain;
