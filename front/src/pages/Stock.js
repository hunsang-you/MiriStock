import { Routes, Route } from 'react-router-dom';
import './css/Home.css';
import StockDetail from './StockDetail';

const Stock = () => {
  return (
    <div className="main-container">
      <Routes>
        <Route path="" element={<StockDetail />} />;
      </Routes>
    </div>
  );
};

export default Stock;
