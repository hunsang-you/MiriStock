import './css/Asset.css';
import { AssetStauts, AssetTotal, StockTrade } from '../components/asset';
import { memberAPI } from '../api/api';
import { useEffect } from 'react';
import { userStore, dateStore } from '../store';
const Asset = () => {
  const { setUser } = userStore((state) => state);
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
    getMember();
  }, []);
  return (
    <div className="asset-container">
      <AssetStauts />
      <AssetTotal />
      <StockTrade />
    </div>
  );
};

export default Asset;
