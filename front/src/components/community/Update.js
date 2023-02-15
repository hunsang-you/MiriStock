import { communityAPI } from '../../api/api';
import { TextField, Button } from '@mui/material';
import { useNavigate, useLocation } from 'react-router-dom';
import { useState } from 'react';
import './css/Create.css';

const Update = () => {
  const location = useLocation();

  const article = location.state;
  const navigate = useNavigate();

  const articleNo = article.articleNo;

  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');
  const ChangeTitle = (e) => {
    setTitle(e.target.value);
  };
  const ChangeContent = (e) => {
    setContent(e.target.value);
  };

  return (
    <div className="create-page">
      <div className="create-form">
        <div className="create-title">
          <TextField
            id="article-title"
            placeholder={article.articleTitle}
            inputProps={{
              style: {
                height: '20px',
                width: '250px',
              },
            }}
            onChange={ChangeTitle}
          />
        </div>
        <div className="create-content">
          <TextField
            id="article-content"
            placeholder={article.articleContent}
            multiline
            rows={10}
            inputProps={{
              style: {
                height: '250px',
                width: '250px',
              },
            }}
            onChange={ChangeContent}
          />
        </div>
      </div>
      <div className="btn-form">
        <Button
          id="cancel-btn"
          variant="outlined"
          size="large"
          onClick={() => {
            navigate(-1);
          }}
        >
          취소
        </Button>

        <Button
          id="create-btn"
          variant="outlined"
          size="large"
          onClick={() => {
            communityAPI
              .updateArticle(articleNo, title, content)
              .then((request) => {})
              .catch((err) => console.log(err));
            //새로고침페이지
            window.location.replace('/community');
          }}
        >
          수정
        </Button>
      </div>
    </div>
  );
};

export default Update;
