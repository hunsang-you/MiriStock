import HeartBtn from './HeartBtn';
import { FaRegCommentDots } from 'react-icons/fa';
import Comment from './Comment';
import { Button } from '@mui/material';
import { useState } from 'react';
import { motion } from 'framer-motion';
import './css/ArticleItem.css';

const ArticleItem = (props) => {
  // const navigate = useNavigate();
  const item = props.item;
  const comment = props.comment;

  // content 글자 제한, 더보기
  const [isclosed, setIsClosed] = useState(false);
  const handlerBtn = () => {
    setIsClosed(!isclosed);
  };

  // comment list 활성화
  const [iscomment, setIsComment] = useState(false);
  const CommentBtn = () => {
    if (iscomment === true) {
      setIsComment(!iscomment);
    } else {
      setIsComment(!iscomment);
    }
  };

  // 댓글창 출력
  const CommentBox = () => {
    if (iscomment === true) {
      return <Comment comment={comment} item={item} />;
    }
  };

  // 좋아요 확인
  const [like, setLike] = useState(false);
  const likeVariant = {
    click: {
      opacity: 0.6,
      scale: 1.0,
      backgroundColor: '#D2143C',
    },
  };

  return (
    <div className="article-item">
      {/* 제목, 작성시간 */}
      <div className="userid">
        <span id="item-userId"> {item.userId} </span>
        <div>
          <span id="item-createAt"> {item.createAt} </span>
        </div>
      </div>

      {/* 제목 */}
      <div className="title" onClick={handlerBtn}>
        <span> {item.title} </span>
      </div>
      {/* 내용 */}
      <div className="content" onClick={handlerBtn}>
        <span className={isclosed ? 'content-open' : 'content-close'}>
          {item.content}
        </span>
      </div>

      {/* 버튼 */}
      <div className="icon">
        <div
          className="like"
          onClick={() => {
            setLike(!like);
          }}
        >
          <motion.div variants={likeVariant} whileTap="click">
            <HeartBtn like={like} item={item} />
          </motion.div>
        </div>
        <div className="comment">
          <FaRegCommentDots onClick={CommentBtn} />
        </div>
        <div className="item-btn">
          <Button id="delete-btn" variant="outlined" size="middle">
            삭제
          </Button>
          {/* <Button id="update-btn" variant="outlined" size="large">
            수정
          </Button> */}
        </div>
      </div>
      <div> {CommentBox()} </div>
    </div>
  );
};

export default ArticleItem;
