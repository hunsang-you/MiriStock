import './css/BottonNav.css';
import { useNavigate } from 'react-router-dom';
import {
  RiHome5Line,
  RiHome5Fill,
  RiQuestionAnswerLine,
  RiQuestionAnswerFill,
} from 'react-icons/ri'; // 집색깔 안채워진거 , 채워진거, 질문 안채워진거, 채워진거
import { AiOutlineSearch } from 'react-icons/ai'; // 돋보기
import { IoWalletOutline, IoWalletSharp } from 'react-icons/io5'; // 지갑 안채워진거 , 채워진거
import { FiMoreHorizontal } from 'react-icons/fi';

const BottomNav = (props) => {
  const navigate = useNavigate();
  let pathName = props.location.pathname;
  return (
    <nav className="wrapper">
      <div
        onClick={() => {
          navigate('/');
        }}
      >
        {pathName.length === 1 || pathName.indexOf('home') !== -1 ? (
          <RiHome5Fill size={40} color="#6DCEF5" />
        ) : (
          <RiHome5Line size={40} />
        )}
      </div>
      <div
        onClick={() => {
          navigate('/search');
        }}
      >
        {pathName.indexOf('search') === -1 ? (
          <AiOutlineSearch size={40} />
        ) : (
          <AiOutlineSearch size={40} color="#6DCEF5" />
        )}
      </div>
      <div
        onClick={() => {
          navigate('/asset');
        }}
      >
        {pathName.indexOf('asset') === -1 ? (
          <IoWalletOutline size={40} />
        ) : (
          <IoWalletSharp size={40} color="#6DCEF5" />
        )}
      </div>
      <div
        onClick={() => {
          navigate('/community');
        }}
      >
        {pathName.indexOf('community') === -1 ? (
          <RiQuestionAnswerLine size={40} />
        ) : (
          <RiQuestionAnswerFill size={40} color="#6DCEF5" />
        )}
      </div>
      <div
        onClick={() => {
          navigate('/more');
        }}
      >
        {pathName.indexOf('more') === -1 ? (
          <FiMoreHorizontal size={40} />
        ) : (
          <FiMoreHorizontal size={40} color="#6DCEF5" />
        )}
      </div>
    </nav>
  );
};

export default BottomNav;
