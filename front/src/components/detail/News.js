import { useState, useEffect } from 'react';
import { newsAPI } from '../../api/api'; // api 통신

const News = () => {
  const [newsList, setNewsList] = useState([]);

  const getNewsData = () => {
    newsAPI
      .getNews('삼성전자', 20200123)
      .then((request) => {
        console.log(request.data.messages);
        setNewsList(request.data.messages);
      })
      .catch((err) => {
        console.log(err);
      });
  };

  return (
    <div>
      <button
        onClick={() => {
          getNewsData();
        }}
      >
        뉴스데이터
      </button>
    </div>
  );
};

export default News;
