import {
  Simulation,
  AssetStatus,
  EquitiesValue,
  FavoriteStock,
  Rank,
} from '../components/home';
import { communityAPI, stockAPI, memberAPI } from '../api/api';

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
          communityAPI
            .getCom()
            .then((request) => {
              console.log(request.data);
            })
            .catch((err) => {
              console.log(err);
            });
        }}
      >
        글불러오기테스트
      </button>
      <button
        onClick={() => {
          communityAPI
            .createCom('dddd', 'ddddd')
            .then((request) => {
              console.log(request.data);
            })
            .catch((err) => {
              console.log(err);
            });
        }}
      >
        글작성 테스트
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
