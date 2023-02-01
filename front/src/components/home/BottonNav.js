import './css/BottonNav.css';
// import { useState } from 'react';
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
import { motion } from 'framer-motion';

const BottomNav = (props) => {
  const navigate = useNavigate();
  let pathName = props.location.pathname;
  const boxVariants = {
    click: {
      opacity: 0.4,
      scale: 1.2,
      backgroundColor: '#6DCEF5',
    },
  };
  return (
    <nav className="wrapper">
      <motion.div
        onClick={() => {
          navigate('/');
        }}
        whileTap="click"
        variants={boxVariants}
      >
        {pathName.length === 1 || pathName.indexOf('home') !== -1 ? (
          <RiHome5Fill size={40} color="#6DCEF5" />
        ) : (
          <RiHome5Line size={40} />
        )}
      </motion.div>
      <motion.div
        onClick={() => {
          navigate('/search');
        }}
        whileTap="click"
        variants={boxVariants}
      >
        {pathName.indexOf('search') === -1 ? (
          <AiOutlineSearch size={40} />
        ) : (
          <AiOutlineSearch size={40} color="#6DCEF5" />
        )}
      </motion.div>
      <motion.div
        onClick={() => {
          navigate('/asset');
        }}
        whileTap="click"
        variants={boxVariants}
      >
        {pathName.indexOf('asset') === -1 ? (
          <IoWalletOutline size={40} />
        ) : (
          <IoWalletSharp size={40} color="#6DCEF5" />
        )}
      </motion.div>
      <motion.div
        onClick={() => {
          navigate('/question');
        }}
        whileTap="click"
        variants={boxVariants}
      >
        {pathName.indexOf('question') === -1 ? (
          <RiQuestionAnswerLine size={40} />
        ) : (
          <RiQuestionAnswerFill size={40} color="#6DCEF5" />
        )}
      </motion.div>
      <motion.div
        onClick={() => {
          navigate('/more');
        }}
        whileTap="click"
        variants={boxVariants}
      >
        {pathName.indexOf('more') === -1 ? (
          <FiMoreHorizontal size={40} />
        ) : (
          <FiMoreHorizontal size={40} color="#6DCEF5" />
        )}
      </motion.div>
    </nav>
  );
};

export default BottomNav;

// const BottomNav = (props) => {
//   const navigate = useNavigate();
//   let pathName = props.location.pathname;
//   const [value, setValue] = useState(0);

//   return (
//     <Box sx={{ position: 'fixed', bottom: 0, left: 0, right: 0, width: 1 }}>
//       <BottomNavigation
//         showLabels
//         value={value}
//         onChange={(event, newValue) => {
//           setValue(newValue);
//         }}
//       >
//         <BottomNavigationAction label="홈" icon={<RiHome5Line />} />
//         <BottomNavigationAction label="검색" icon={<AiOutlineSearch />} />
//         <BottomNavigationAction label="자산현황" icon={<IoWalletOutline />} />
//         <BottomNavigationAction label="질문" icon={<RiQuestionAnswerLine />} />
//         <BottomNavigationAction label="더보기" icon={<FiMoreHorizontal />} />
//       </BottomNavigation>
//     </Box>
//   );
// };

// export default BottomNav;
