import './css/Community.css';
import { Routes, Route } from 'react-router-dom';
import ArticleList from '../components/community/ArticleList';
import ArticleCreate from '../components/community/ArticleCreate';
import Update from '../components/community/Update';

const Community = () => {
  return (
    <div className="main-container">
      <Routes>
        <Route path="" element={<ArticleList />} />
        ;
        <Route path="create" element={<ArticleCreate />} />;
        <Route path="update/:articleNo" element={<Update />} />;
      </Routes>
    </div>
  );
};

export default Community;
