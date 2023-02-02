import { useState } from 'react';
import ArticleItem from './ArticleItem';
import { IoCreateOutline } from 'react-icons/io5';
import { useNavigate } from 'react-router-dom';
import './css/ArticleList.css';

const ArticleList = (props) => {
  const navigate = useNavigate();

  const qna = props.items;

  return (
    <div className="article-list">
      <div className="board">
        QnA 게시판
        <div className="create-item">
          {' '}
          <IoCreateOutline
            onClick={() => {
              navigate('create');
            }}
          />
        </div>
      </div>
      {/* <ArticleList items={items} /> */}
      {qna.map((item, i) => {
        return (
          <div key={i}>
            <ArticleItem item={item} />
          </div>
        );
      })}
    </div>
  );
};

export default ArticleList;
