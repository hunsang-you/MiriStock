import { useState, useEffect } from 'react';
import { newsAPI } from '../../api/api'; // api 통신
import { dateStore } from '../../store';
const News = (props) => {
  const { date } = dateStore((state) => state);
  const [newsList, setNewsList] = useState([]);
  const [newsLink, setNewsLink] = useState([]);
  useEffect(() => {
    const stockName = props.stockInfo;
    const currentDate = props.currentDate;
    const getNewsData = async () => {
      let newsdata = [];
      let newslink = [];
      await newsAPI
        .getNews(stockName, currentDate)
        .then((request) => {
          for (let i = 0; i < 5; i++) {
            newsdata.push(request.data.messages[i].title);
            newslink.push(request.data.messages[i].link);
            setNewsList(newsdata);
            setNewsLink(newslink);
          }
        })
        .catch((err) => {
          console.log(err);
        });
    };
    getNewsData();
  }, []);

  return (
    <div>
      <div>
        {newsList.map(function (title, i) {
          return (
            <div
              key={i}
              style={{
                width: '80vw',
                overflow: 'hidden', // 넘어간부분 숨기기
                textOverflow: 'ellipsis',
                whiteSpace: 'nowrap', // 줄바꿈 방지
                display: 'flex',
              }}
            >
              {i !== newsList.length - 1 ? (
                <div>
                  <span
                    onClick={() => {
                      window.open(newsLink[i]);
                    }}
                  >
                    {title}
                  </span>
                  <hr />
                </div>
              ) : (
                <span
                  onClick={() => {
                    window.open(newsLink[i]);
                  }}
                >
                  {title}
                </span>
              )}
            </div>
          );
        })}
      </div>
    </div>
  );
};

export default News;
