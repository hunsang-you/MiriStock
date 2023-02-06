import {
  Simulation,
  AssetStatus,
  EquitiesValue,
  FavoriteStock,
  Rank,
} from '../components/home';
import { communityAPI, stockAPI, memberAPI, tradeAPI } from '../api/api';

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
          tradeAPI
            .getAllTrades()
            .then((request) => {
              console.log(request.data);
            })
            .catch((err) => {
              console.log(err);
            });
        }}
      >
        거래내역테스트
      </button>
      <button
        onClick={() => {
          tradeAPI
            .buyStock('000660', '하이닉스', 2, 500000, 200, 'SELL')
            .then((request) => {
              console.log(request.data);
            })
            .catch((err) => {
              console.log(err);
            });
        }}
      >
        구매테스트
      </button>
      <button
        onClick={() => {
          tradeAPI
            .checkTrades()
            .then((request) => {
              console.log(request.data);
            })
            .catch((err) => {
              console.log(err);
            });
        }}
      >
        거래예정
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
