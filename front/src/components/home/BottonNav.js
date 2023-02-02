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
import { navStore } from '../../store.js';

const BottomNav = () => {
  const navigate = useNavigate();
  const { page } = navStore((state) => state);
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
        {page.length === 1 || page.indexOf('home') !== -1 ? (
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
        {page.indexOf('search') === -1 ? (
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
        {page.indexOf('asset') === -1 ? (
          <IoWalletOutline size={40} />
        ) : (
          <IoWalletSharp size={40} color="#6DCEF5" />
        )}
      </motion.div>
      <motion.div
        onClick={() => {
          navigate('/community');
        }}
        whileTap="click"
        variants={boxVariants}
      >
        {page.indexOf('community') === -1 ? (
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
        {page.indexOf('more') === -1 ? (
          <FiMoreHorizontal size={40} />
        ) : (
          <FiMoreHorizontal size={40} color="#6DCEF5" />
        )}
      </motion.div>
    </nav>
  );
};

export default BottomNav;
