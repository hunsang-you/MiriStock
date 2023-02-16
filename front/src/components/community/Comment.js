// import { TextField } from '@mui/material';
import './css/Comment.css';
import CommentItem from './CommentItem';
import { TextField, Button } from '@mui/material';
import { useState, useEffect, useMemo } from 'react';
import { communityAPI } from '../../api/api';
import Swal from 'sweetalert2';
const Comment = (props) => {
  const article = props.article;
  const setArticles = props.setArticles;
  const [comNo, setComNo] = useState(0);

  let comm = article.comments;
  useEffect(() => {
    communityAPI
      .getArticle()
      .then((request) => setArticles(request.data.reverse()))
      .catch((err) => console.log(err));
  }, [comNo]);
  // 댓글하나씩

  // 댓글입력창
  const [text, setText] = useState('');
  const ChangeText = (e) => {
    setText(e.target.value);
  };
  const errAlert = () => {
    Swal.fire({
      text: '댓글을 입력해 주세요',
      showClass: {
        popup: 'animate__animated animate__fadeInDown',
      },
      hideClass: {
        popup: 'animate__animated animate__fadeOutUp',
      },
    });
  };
  // className=`{기존클래스 ${addClass === true ? "클래스" : "클래스"} }`
  return (
    <div className="comment-box">
      <div className="create-comment">
        <TextField
          sx={{ width: { xs: 288, sm: 400, md: 500, lg: 700, xl: 1300 } }}
          id="create-commentbox"
          placeholder="댓글을 입력하세요."
          variant="outlined"
          value={text}
          onChange={(e) => {
            ChangeText(e);
          }}
        />
        <div className="comment-createbtn">
          <Button
            id="comment-btn"
            variant="outlined"
            size="middle"
            onClick={() => {
              if (text.trim().length !== 0) {
                communityAPI
                  .createComment(article.articleNo, text)
                  .then((request) => {
                    setComNo(request.data);
                    setText('');
                  })
                  .catch((err) => console.log(err));
              } else {
                errAlert();
              }
            }}
          >
            등록
          </Button>
        </div>
      </div>
      <div>
        {comm
          .slice()
          .reverse()
          .map((comment, i) => {
            return (
              <div key={i}>
                <CommentItem comment={comment} />
              </div>
            );
          })}
      </div>
    </div>
  );
};

export default Comment;
