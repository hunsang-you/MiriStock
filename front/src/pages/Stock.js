import { Routes, Route } from 'react-router-dom';
import './css/Home.css';
import StockDetail from './StockDetail';
import BuyStock from './BuyStock';
import SellStock from './SellStock';
import UpdateBuy from './UpdateBuy';
import UpdateSell from './UpdateSell';

const Stock = () => {
  return (
    <div className="main-container">
      <Routes>
        <Route path="" element={<StockDetail />} />;
        <Route path="buyStock" element={<BuyStock />} />
        <Route path="sellStock" element={<SellStock />} />
        <Route path="updateBuy" element={<UpdateBuy />} />
        <Route path="updateSell" element={<UpdateSell />} />
      </Routes>
    </div>
  );
};

export default Stock;
