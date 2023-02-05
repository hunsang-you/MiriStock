import { AiFillHeart, AiOutlineHeart } from 'react-icons/ai';
import './css/HeartBtn.css';

const HeartBtn = (props) => {
  const isLike = props.like;

  const CheckBtn = () => {
    if (isLike === true) {
      return (
        <div className="like-on">
          <AiFillHeart />
        </div>
      );
    } else {
      return (
        <div className="like-off">
          <AiOutlineHeart />
        </div>
      );
    }
  };

  return (
    <div>
      <CheckBtn />
    </div>
  );
};

export default HeartBtn;
