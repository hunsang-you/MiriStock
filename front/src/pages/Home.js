import './css/Home.css';
import HomeFavorite from './HomeFavorite';
import HomeMain from './HomeMain';
import { Routes, Route } from 'react-router-dom';

const Home = () => {
  return (
    <div className="main-container">
      <Routes>
        <Route path="" element={<HomeMain />} />;
        <Route path="homeFavorite" element={<HomeFavorite />} />;
      </Routes>
    </div>
  );
};

export default Home;
