import ArticleItem from './ArticleItem';
import { IoCreateOutline } from 'react-icons/io5';
import { useNavigate } from 'react-router-dom';

const ArticleList = (props) => {
  const navigate = useNavigate();

  const qna = props.items;
  const comment = props.comment;

  return (
    <div className="article-list">
      <div className="board">
        <h2>질문방</h2>
        <div className="create-item">
          <IoCreateOutline
            onClick={() => {
              navigate('create');
            }}
          />
        </div>
      </div>

      {qna.map((item, i) => {
        return (
          <div key={i}>
            <ArticleItem item={item} comment={comment} />
          </div>
        );
      })}
    </div>
  );
};

export default ArticleList;
