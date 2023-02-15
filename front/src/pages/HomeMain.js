import {
  Simulation,
  AssetStatus,
  EquitiesValue,
  FavoriteStock,
  Rank,
} from '../components/home';
import { memberAPI } from '../api/api';
import { useEffect, useState } from 'react';
import { userStore, dateStore } from '../store';

const HomeMain = () => {
  const { setUser } = userStore((state) => state);
  const { date } = dateStore((state) => state);
  const [userAssetChanged, setUserAssetChanged] = useState([]);
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
      await memberAPI
        .assetChanged()
        .then((request) => {
          setUserAssetChanged(request.data);
        })
        .catch((err) => console.log(err));
    };
    getMember();
  }, [date, setUser]); //추후에 데이트 값
  return (
    <div>
      <Simulation />
      <AssetStatus userAssetChanged={userAssetChanged} />
      <EquitiesValue />
      <FavoriteStock />
      <Rank />
    </div>
  );
};

export default HomeMain;
