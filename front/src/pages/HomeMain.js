import {
  Simulation,
  AssetStatus,
  EquitiesValue,
  FavoriteStock,
  Rank,
} from '../components/home';
import {
  memberAPI,
  tradeAPI,
  rankAPI,
  simulAPI,
  communityAPI,
} from '../api/api';
import { useNavigate } from 'react-router-dom';
import { useEffect, useState } from 'react';
import { userStore } from '../store';

const HomeMain = () => {
  const navigate = useNavigate();
  const { user, setUser } = userStore((state) => state);
  const [userStock, setUserStock] = useState([]);
  const [userAssetChanged, setUserAssetChanged] = useState([]);
  const [loading, setLoading] = useState(true); //로딩창
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
          console.log(request.data);
        })
        .catch((err) => {
          console.log(err);
        });
    };
    const getMemberAssetChanged = async () => {
      await memberAPI
        .assetChanged()
        .then((request) => {
          setUserAssetChanged(request.data);
        })
        .catch((err) => console.log(err));
    };
    getMember();
    getMyStocks();
    getMemberAssetChanged();
  }, []); //추후에 데이트 값
  return (
    <div>
      <Simulation />
      <AssetStatus userAssetChanged={userAssetChanged} />
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
          simulAPI
            .changeDate(1)
            .then((request) => console.log(request.data))
            .catch((err) => console.log(err));
        }}
      >
        날짜변경
      </button>
      <button
        onClick={() => {
          memberAPI
            .asset()
            .then((request) => console.log(request.data))
            .catch((err) => console.log(err));
        }}
      >
        멤버
      </button>
      <button
        onClick={() => {
          communityAPI
            .getArticle()
            .then((request) => console.log(request.data))
            .catch((err) => console.log(err));
        }}
      >
        커뮤니티
      </button>
    </div>
  );
};

export default HomeMain;
