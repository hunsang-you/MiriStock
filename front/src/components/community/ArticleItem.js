// import HeartBtn from './HeartBtn';
// import { FaRegCommentDots } from 'react-icons/fa';
import { AiOutlineComment } from 'react-icons/ai';
import Comment from './Comment';
import { Button } from '@mui/material';
import { useState } from 'react';
import { userStore } from '../../store';
import { communityAPI } from '../../api/api';
import { useNavigate } from 'react-router-dom';
import './css/ArticleItem.css';

const ArticleItem = (props) => {
  const article = props.article;
  const setArticles = props.setArticles;
  const { user, setUser } = userStore((state) => state);

  const navigate = useNavigate();

  //서버에 9시간 늦게 저장돼있어 9시간만큼 빼줌
  let nowTime = new Date(article.articleCreateDate).getTime() + 32400000;
  // let modifyTime = new Date(article.articleModifyDate).getTime() + 32400000;
  console.log(nowTime);
  console.log(article);
  //api에 있는 detailPost.createdAt를 바꿔주는 것
  // content 글자 제한, 더보기
  const [isclosed, setIsClosed] = useState(false);
  const handlerBtn = () => {
    setIsClosed(!isclosed);
  };

  // comment list 활성화
  const [isComment, setIsComment] = useState(false);
  const CommentBtn = () => {
    setIsComment(!isComment);
  };

  // 댓글창 출력
  let CommentBox = () => {
    if (isComment === true) {
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
        <span id="item-userId"> {article.articleTitle} </span>
        <div>
          {1 ? (
            <span id="item-createAt"> {detailDate(nowTime)} </span>
          ) : (
            <span id="item-createAt"> {detailDate(modifyTime)} 수정</span>
          )}
        </div>
      </div>

      {/* 제목 */}
      <div className="article-title" onClick={handlerBtn}>
        <span> {article.memberNickname} </span>
      </div>
      {/* 내용 */}
      <div className="article-content" onClick={handlerBtn}>
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
        ></div>
        <div className="comment">
          <AiOutlineComment
            onClick={CommentBtn}
            size={40}
            color={isComment === true ? '#6DCEF5' : '#C4C4C4'}
          />
        </div>
        <div className="item-btn">
          <div>
            {user.memberNo === article.memberNo || user.memberNo === 1 ? (
              <Button
                id="delete-btn"
                variant="contained"
                size="large"
                style={{ color: 'white' }}
                onClick={() => {
                  communityAPI
                    .deleteArticle(article.articleNo)
                    .then((request) => console.log(request.data))
                    .catch((err) => console.log(err));
                  window.location.replace('/community');
                }}
              >
                삭제
              </Button>
            ) : null}
          </div>
          <div>
            {user.memberNo === article.memberNo || user.memberNo === 1 ? (
              <Button
                id="update-btn"
                variant="contained"
                size="large"
                style={{ color: 'white' }}
                onClick={(e) => {
                  navigate(`update/${article.articleNo}`, {
                    state: {
                      articleNo: article.articleNo,
                      articleTitle: article.articleTitle,
                      articleContent: article.articleContent,
                    },
                  });
                }}
              >
                수정
              </Button>
            ) : null}
          </div>
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
