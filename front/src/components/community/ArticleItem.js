import HeartBtn from './HeartBtn';
import { FaRegCommentDots } from 'react-icons/fa';
import Comment from './Comment';
import { Button } from '@mui/material';
import { useState } from 'react';
import { motion } from 'framer-motion';
import './css/ArticleItem.css';

const ArticleItem = (props) => {
  const article = props.article;
  const setArticles = props.setArticles;
  //서버에 9시간 늦게 저장돼있어 9시간만큼 빼줌
  let nowTime = new Date(article.articleDate).getTime() - 32400000;

  //api에 있는 detailPost.createdAt를 바꿔주는 것
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
      return <Comment article={article} setArticles={setArticles} />;
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
        <span id="item-userId"> {article.memberNickname} </span>
        <div>
          <span id="item-createAt"> {detailDate(nowTime)} </span>
        </div>
      </div>

      {/* 제목 */}
      <div className="title" onClick={handlerBtn}>
        <span> {article.articleTitle} </span>
      </div>
      {/* 내용 */}
      <div className="content" onClick={handlerBtn}>
        <span className={isclosed ? 'content-open' : 'content-close'}>
          {article.articleContent}
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
          {/* <motion.div variants={likeVariant} whileTap="click">
            <HeartBtn like={like} item={item} />
          </motion.div> */}
        </div>
        <div className="comment">
          <FaRegCommentDots onClick={CommentBtn} />
        </div>
        <div className="item-btn">
          <Button id="delete-btn" variant="outlined" size="middle">
            삭제
          </Button>
          <Button id="update-btn" variant="outlined" size="large">
            수정
          </Button>
        </div>
      </div>
      <div> {CommentBox()} </div>
    </div>
  );
};

export default ArticleItem;

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
