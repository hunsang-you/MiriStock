import ArticleItem from './ArticleItem';
import { IoCreateOutline } from 'react-icons/io5';
import { useNavigate } from 'react-router-dom';
import { useEffect, useState } from 'react';
import { communityAPI } from '../../api/api';

const ArticleList = () => {
  const navigate = useNavigate();
  const [articles, setArticles] = useState([]);
  useEffect(() => {
    communityAPI
      .getCom()
      .then((request) => {
        setArticles(request.data);
      })
      .catch((err) => console.log(err));
  }, []);

  return (
    <div className="article-list">
      <div className="board">
        <h2>질문방</h2>
        <div className="create-item">
          <IoCreateOutline
            onClick={() => {
              navigate('create');
            }}
          />
        </div>
      </div>

      {articles.reverse().map((article, i) => {
        return (
          <div key={i}>
            <ArticleItem article={article} setArticles={setArticles} />
          </div>
        );
      })}
    </div>
  );
};

export default ArticleList;
