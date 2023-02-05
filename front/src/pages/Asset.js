import './css/Asset.css';
import { AssetStauts, AssetTotal, StockTrade } from '../components/asset';
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
