import ArticleItem from './ArticleItem';
// import { IoCreateOutline } from 'react-icons/io5';
import { useNavigate } from 'react-router-dom';
import { useEffect, useState } from 'react';
import { communityAPI } from '../../api/api';
import { Button } from '@mui/material';
import write from '../../static/write.jpg';
import QnAimg from '../../static/QnAimg.png';
import './css/ArticleList.css';

import FilterArticle from './ArticleSearch';

const ArticleList = () => {
  const navigate = useNavigate();
  const [articles, setArticles] = useState([]);
  const [articleNo, setArticleNo] = useState(0);
  useEffect(() => {
    communityAPI
      .getArticle()
      .then((request) => {
        setArticles(request.data.reverse());
      })
      .catch((err) => console.log(err));
  }, [articleNo]);

  return (
    <div className="article-list">
      <div className="board">
        <img src={QnAimg} id="QnALogo" alt="QnALogo" />

        <div className="create-item">
          {/* <IoCreateOutline
            onClick={() => {
              navigate('create');
            }}
          /> */}
          <Button
            id="article-createbtn"
            variant="outlined"
            onClick={() => {
              navigate('create');
            }}
          >
            <img src={write} id="article-createimg" />
            <span>글쓰기 </span>
          </Button>
        </div>
      </div>
      <div>
        <FilterArticle articles={articles} />
      </div>

      {articles.map((article, i) => {
        return (
          <div key={i}>
            <ArticleItem
              article={article}
              setArticles={setArticles}
              setArticleNo={setArticleNo}
            />
          </div>
        );
      })}
    </div>
  );
};

export default ArticleList;
