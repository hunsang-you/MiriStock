import { communityAPI } from '../../api/api';
import { TextField, Button } from '@mui/material';
import { useNavigate, useLocation } from 'react-router-dom';
import { useState } from 'react';
import { updateStore } from '../../store';
import './css/Create.css';
import Swal from 'sweetalert2';

const Update = () => {
  const location = useLocation();
  const article = location.state;
  const navigate = useNavigate();
  const articleNo = article.articleNo;
  const [title, setTitle] = useState(article.articleTitle);
  const [content, setContent] = useState(article.articleContent);
  const ChangeTitle = (e) => {
    setTitle(e.target.value);
  };
  const ChangeContent = (e) => {
    setContent(e.target.value);
  };

  const errAlert = () => {
    Swal.fire({
      text: '제목 또는 내용을 입력해 주세요',
      showClass: {
        popup: 'animate__animated animate__fadeInDown',
      },
      hideClass: {
        popup: 'animate__animated animate__fadeOutUp',
      },
    });
  };

  return (
    <div className="create-page">
      <div className="create-form">
        <div className="create-title">
          <TextField
            id="article-title"
            defaultValue={article.articleTitle}
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
            defaultValue={article.articleContent}
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
            if (title.trim().length !== 0 && content.trim().length !== 0) {
              communityAPI
                .updateArticle(articleNo, title, content)
                .then((request) => {
                  navigate('/community');
                })
                .catch((err) => console.log(err));
            } else {
              //스위트알람위치
              errAlert();
            }
          }}
        >
          수정
        </Button>
      </div>
    </div>
  );
};

export default Update;
