import { useState } from 'react';

const CommentItem = (props) => {
  const comment = props.comment;
  const [isclosed, setIsClosed] = useState(false);
  const handlerBtn = () => {
    setIsClosed(!isclosed);
  };

  return (
    <div className="comment-item">
      <div className="item-id">{comment.memberNickname}</div>
      <div className="item-content">
        <div
          className={isclosed ? 'content-open' : 'content-close'}
          onClick={handlerBtn}
        >
          {comment.commentContent}
        </div>
      </div>
    </div>
  );
};

export default CommentItem;
