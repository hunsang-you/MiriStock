import { useState } from 'react';

const CommentItem = (props) => {
  const comment = props.comment;
  const [isclosed, setIsClosed] = useState(false);
  const handlerBtn = () => {
    setIsClosed(!isclosed);
  };

  let nowTime = new Date(comment.commentCreateDate).getTime();

  return (
    <div className="comment-item">
      <div className="item-id">
        <span>{comment.memberNickname} </span>
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
