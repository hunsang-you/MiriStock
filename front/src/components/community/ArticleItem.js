import HeartBtn from './HeartBtn';
import { FaRegCommentDots } from 'react-icons/fa';
import Comment from './Comment';
import { Button } from '@mui/material';
import { useState } from 'react';
import './css/ArticleItem.css';

const ArticleItem = (props) => {
  // const navigate = useNavigate();
  const item = props.item;
  const comment = props.comment;

  const [like, setLike] = useState(false);

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
      <div className="title">
        <span> {item.title} </span>
      </div>
      {/* 내용 */}
      <div className="content">
        <span> {item.content}</span>
      </div>

      {/* 버튼 */}
      <div className="icon">
        <div
          className="like"
          onClick={() => {
            setLike(!like);
          }}
        >
          <HeartBtn like={like} item={item} />
        </div>
        <div className="comment">
          <FaRegCommentDots />
          <span> 2 </span>
        </div>
        <div className="item-btn">
          <Button id="delete-btn" variant="outlined" size="large">
            삭제
          </Button>
          {/* <Button id="update-btn" variant="outlined" size="large">
            수정
          </Button> */}
        </div>
      </div>
    </div>
  );
};

export default ArticleItem;
