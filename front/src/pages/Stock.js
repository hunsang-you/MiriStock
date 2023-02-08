import { Routes, Route } from 'react-router-dom';
import './css/Home.css';
import StockDetail from './StockDetail';
import BuyStock from './BuyStock';
import SellStock from './SellStock';

const Stock = () => {
  return (
    <div className="main-container">
      <Routes>
        <Route path="" element={<StockDetail />} />;
        <Route path="buyStock" element={<BuyStock />} />
        <Route path="sellStock" element={<SellStock />} />
      </Routes>
    </div>
  );
};

export default Stock;
