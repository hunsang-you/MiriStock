import './css/Create.css';
import { TextField, Button } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import { useState } from 'react';
import { communityAPI } from '../../api/api';
import Swal from 'sweetalert2';

const Create = () => {
  const navigate = useNavigate();

  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');
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
            placeholder="제목을 입력하세요"
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
            placeholder="내용을 입력하세요."
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
                .createArticle(title, content)
                .then((request) => {
                  navigate('/community');
                })
                .catch((err) => console.log(err));
              //새로고침페이지
            } else {
              //스위트알람위치
              errAlert();
            }
          }}
        >
          작성
        </Button>
      </div>
    </div>
  );
};

export default Create;
