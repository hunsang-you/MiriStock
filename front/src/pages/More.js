import MoreMain from '../components/more/MoreMain';
import ChangeName from '../components/more/ChangeName';
import GameExit from '../components/more/GameExit';
import Portfolio from '../components/more/Portfolio';
import { Routes, Route } from 'react-router-dom';
import './css/More.css';

const More = () => {
  return (
    <div className="main-container">
      <Routes>
        <Route path="" element={<MoreMain />} />;
        <Route path="/change" element={<ChangeName />} />;
        <Route path="/exit" element={<GameExit />} />;
        <Route path="/result" element={<Portfolio />} />;
      </Routes>
    </div>
  );
};

export default More;
