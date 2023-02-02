import { useState } from 'react';
import axios from 'axios';
import { useNavigate, useParams, Route, Routes } from 'react-router-dom';
import { AiOutlineHeart } from 'react-icons/ai';
import { FaRegCommentDots } from 'react-icons/fa';
import Detail from './Detail';
import './css/ArticleItem.css';

const ArticleItem = (item) => {
  const navigate = useNavigate();

  return (
    <div
      className="article-item"
      onClick={() => {
        navigate('detail/');
      }}
    >
      <div className="userid">
        <span> {item.item.userId} </span>
      </div>

      <div className="title">
        <span> {item.item.title} </span>
      </div>
      <div className="icon">
        <div className="like">
          {' '}
          <AiOutlineHeart /> <span> 1 </span>
        </div>
        <div className="comment">
          {' '}
          <FaRegCommentDots /> <span> 2 </span>
        </div>
      </div>
    </div>
  );
};

export default ArticleItem;
