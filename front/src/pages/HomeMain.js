import {
  Simulation,
  AssetStatus,
  EquitiesValue,
  FavoriteStock,
  Rank,
} from '../components/home';
import { communityAPI, stockAPI, memberAPI, tradeAPI } from '../api/api';
import { useNavigate } from 'react-router-dom';
import { useEffect, useState } from 'react';
import { userStore } from '../store';

const HomeMain = () => {
  const navigate = useNavigate();
  const { user, setUser } = userStore((state) => state);
  const [userStock, setUserStock] = useState([]);
  //일단 마운트될때마다로 설정 추후에 데이변할때 하게 해야함
  useEffect(() => {
    const getMember = async () => {
      await memberAPI
        .asset()
        .then((request) => {
          setUser(request.data);
        })
        .catch((err) => {
          console.log(err);
        });
    };
    const getMyStocks = async () => {
      await memberAPI
        .stocks()
        .then((request) => {
          setUserStock(request.data);
        })
        .catch((err) => {
          console.log(err);
        });
    };
    getMember();
    getMyStocks();
  }, []); //추후에 데이트 값
  return (
    <div>
      <Simulation />
      <AssetStatus />
      <EquitiesValue userStock={userStock} />
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
            .intersetStocks(20180102)
            .then((request) => console.log(request.data))
            .catch((err) => console.log(err));
        }}
      >
        보유주식목록
      </button>
      <button
        onClick={() => {
          memberAPI
            .asset()
            .then((request) => console.log(request))
            .catch((err) => console.log(err));
        }}
      >
        멤버
      </button>
    </div>
  );
};

export default HomeMain;
