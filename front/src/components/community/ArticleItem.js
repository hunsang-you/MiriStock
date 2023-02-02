import { useState } from 'react';
import { useNavigate, useParams, Route, Routes } from 'react-router-dom';
import { AiOutlineHeart } from 'react-icons/ai';
import { FaRegCommentDots } from 'react-icons/fa';
import './css/ArticleItem.css';

const ArticleItem = (props) => {
  const navigate = useNavigate();
  const item = props.item;
  return (
    <div
      className="article-item"
      onClick={() => {
        navigate('detail/' + item.id);
      }}
    >
      <div className="userid">
        <span> {item.userId} </span>
      </div>

      <div className="title">
        <span> {item.title} </span>
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
