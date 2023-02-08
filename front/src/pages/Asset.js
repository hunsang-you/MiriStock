import './css/Asset.css';
import { AssetStauts, AssetTotal, StockTrade } from '../components/asset';
import { tradeAPI } from '../api/api';

const Asset = () => {
  return (
    <div className="asset-container">
      <AssetStauts />
      <AssetTotal />
      <StockTrade />
    </div>
  );
};

export default Asset;
