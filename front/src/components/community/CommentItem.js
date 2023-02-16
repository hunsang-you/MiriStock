import { useState } from 'react';
import { Button } from '@mui/material';
import { userStore, commentUpdateStore } from '../../store';
import { communityAPI } from '../../api/api';
const CommentItem = (props) => {
  const comment = props.comment;
  const { user } = userStore((state) => state);
  const [isclosed, setIsClosed] = useState(false);
  const { setCheckComment } = commentUpdateStore((state) => state);
  const handlerBtn = () => {
    setIsClosed(!isclosed);
  };
  let nowTime = new Date(comment.commentCreateDate).getTime() + 32400000;

  return (
    <div className="comment-item">
      <div className="item-id">
        <div style={{ display: 'flex', justifyContent: 'space-between' }}>
          {comment.memberNickname === null
            ? '탈퇴유저'
            : comment.memberNickname}{' '}
          {user.memberNo === comment.memberNo || user.memberNo === 1 ? (
            <Button
              id="comment-btn"
              variant="outlined"
              size="small"
              color="red"
              onClick={() => {
                communityAPI
                  .deleteComment(comment.commentNo)
                  .then((request) => {
                    setCheckComment();
                  })
                  .catch((err) => console.log(err));
              }}
            >
              삭제
            </Button>
          ) : null}
        </div>

        <span id="item-createAt"> {detailDate(nowTime)} </span>
      </div>
      <div className="item-content">
        <div
          className={isclosed ? 'comment-open' : 'comment-close'}
          onClick={handlerBtn}
        >
          <span> {comment.commentContent} </span>
        </div>
      </div>
    </div>
  );
};

export default CommentItem;

//현재시간 변환해주는 함수
const detailDate = (a) => {
  const milliSeconds = new Date() - a;
  const seconds = milliSeconds / 1000;
  if (seconds < 60) return `방금 전`;
  const minutes = seconds / 60;
  if (minutes < 60) return `${Math.floor(minutes)}분 전`;
  const hours = minutes / 60;
  if (hours < 24) return `${Math.floor(hours)}시간 전`;
  const days = hours / 24;
  if (days < 7) return `${Math.floor(days)}일 전`;
  const weeks = days / 7;
  if (weeks < 5) return `${Math.floor(weeks)}주 전`;
  const months = days / 30;
  if (months < 12) return `${Math.floor(months)}개월 전`;
  const years = days / 365;
  return `${Math.floor(years)}년 전`;
};
