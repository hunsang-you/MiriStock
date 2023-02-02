import { useParams } from 'react-router-dom';
import './css/Detail.css';

const Detail = (items) => {
  const { id } = useParams();

  return (
    <div className="detail-page">
      <div className="detail-userId">
        <p id="article-userId">{items.items[id].userId}</p>
      </div>

      <div className="detail-title">
        <p id="article-title">{items.items[id].title}</p>
      </div>

      <div className="detail-content">
        <p id="article-content"> {items.items[id].content} </p>
      </div>
    </div>
  );
};

export default Detail;
