import { useParams } from 'react-router-dom';
import './css/Detail.css';

const Detail = (props) => {
  const { id } = useParams();
  const item = props.items;
  return (
    <div className="detail-page">
      <div className="detail-userId">
        <p id="article-userId">{item[id].userId}</p>
      </div>

      <div className="detail-title">
        <p id="article-title">{item[id].title}</p>
      </div>

      <div className="detail-content">
        <p id="article-content"> {item[id].content} </p>
      </div>
    </div>
  );
};

export default Detail;
