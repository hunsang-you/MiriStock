import {
  Simulation,
  AssetStatus,
  EquitiesValue,
  FavoriteStock,
  Rank,
} from '../components/home';
import { rankAPI, stockAPI, memberAPI } from '../api/api';

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
            .todayTop(20210524)
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
          stockAPI
            .financialStatement('005930')
            .then((request) => {
              console.log(request.data);
            })
            .catch((err) => {
              console.log(err);
            });
        }}
      >
        재무
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
        멤버
      </button>
    </div>
  );
};

export default HomeMain;
