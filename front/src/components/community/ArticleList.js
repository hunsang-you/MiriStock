import ArticleItem from './ArticleItem';
import { useNavigate } from 'react-router-dom';
import { useEffect, useState } from 'react';
import { communityAPI } from '../../api/api';
import { Button, TextField } from '@mui/material';
import write from '../../static/write.jpg';
import QnAimg from '../../static/QnAimg.png';
import './css/ArticleList.css';
import { commentUpdateStore } from '../../store';

const ArticleList = () => {
  const navigate = useNavigate();
  const [articles, setArticles] = useState([]);
  const [state, setState] = useState(false);
  const [searchArticle, setSearchArticle] = useState([]);
  const { checkComment } = commentUpdateStore((state) => state);
  useEffect(() => {
    communityAPI
      .getArticle()
      .then((request) => {
        setArticles(request.data.reverse());
      })
      .catch((err) => console.log(err));
  }, [checkComment]);

  return (
    <div className="article-list">
      <div className="board">
        <img src={QnAimg} id="QnALogo" alt="QnALogo" />

        <div className="create-item">
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
      <div className="article-search">
        <TextField
          sx={{ width: { xs: 300, sm: 540, md: 720, lg: 960, xl: 1140 } }}
          id="search-bar"
          placeholder="제목 또는 내용을 검색하세요"
          variant="standard"
          onChange={(e) => {
            communityAPI
              .searchArticle(e.target.value)
              .then((request) => {
                setSearchArticle(request.data.reverse());
              })
              .catch((err) => console.log(err));
            if (e.target.value.length > 0) {
              setState(true);
            } else {
              setState(false);
            }
          }}
        />
      </div>
      {/* 키워드가 있는 글만 출력 */}
      <div>
        {searchArticle.map((article, i) => {
          if (state === true) {
            return (
              <div key={i}>
                <ArticleItem article={article} />
              </div>
            );
          }
        })}
      </div>

      {/* 검색 안할시 전체글 출력 */}
      {articles.map((article, i) => {
        if (state === false) {
          return (
            <div key={i}>
              <ArticleItem article={article} setArticles={setArticles} />
            </div>
          );
        }
      })}
    </div>
  );
};

export default ArticleList;
