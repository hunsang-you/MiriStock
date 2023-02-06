// import { TextField } from '@mui/material';
import './css/Comment.css';
import CommentItem from './CommentItem';
import { TextField, Button } from '@mui/material';
import { useState } from 'react';

const Comment = (props) => {
  const item = props.item;
  const comments = props.comment;

  // 댓글하나씩
  const EachComment = () =>
    comments.map((comment, i) => {
      if (item.id === comment.articleNo) {
        return (
          <div key={i}>
            <CommentItem comment={comment} />
          </div>
        );
      }
    });

  // 댓글입력창
  const [text, setText] = useState('');
  const ChangeText = (e) => {
    setText(e.target.value);
  };

  return (
    <div>
      <div className="comment-box">
        <div className="create-comment">
          <TextField
            sx={{ width: { xs: 288, sm: 400, md: 500, lg: 700, xl: 1300 } }}
            id="create-commentbox"
            placeholder="댓글을 입력하세요."
            variant="outlined"
            onChange={ChangeText}
          />
          <div className="comment-createbtn">
            <Button id="comment-btn" variant="outlined" size="middle">
              등록
            </Button>
          </div>
        </div>
        <div>{EachComment()}</div>
      </div>
    </div>
  );
};

export default Comment;
