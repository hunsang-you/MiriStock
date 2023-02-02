import './css/Community.css';
import { Routes, Route } from 'react-router-dom';
import ArticleList from '../components/community/ArticleList';
import Create from '../components/community/Create';
// import Update from '../components/community/Update';

import { useState } from 'react';

const Community = () => {
  const [items, setItems] = useState([
    {
      id: 0,
      title: '등락률이 뭔가요',
      userId: '주식고수한재윤',
      content:
        '등락률이 뭔가요등락률이 뭔가요등락률이 뭔가요등락률이 뭔가요등락률이 뭔가요등락률이 뭔가요등락률이 뭔가요등락률이 뭔가요등락률이 뭔가요',
      createAt: '2022-02-02',
    },
    {
      id: 1,
      title: '삼성전자 호재인가요',
      userId: '주식고수안효관',
      content: '삼성전자 호재인가요삼성전자 호재인가요삼성전자 호재인가요',
      createAt: '2022-02-02',
    },
    {
      id: 2,
      title: '매수타이밍이 맞나요',
      userId: '주식고수배상현',
      content: '매수타이밍이 맞나요매수타이밍이 맞나요매수타이밍이 맞나요',
      createAt: '2022-02-02',
    },
    {
      id: 3,
      title: '호가가 뭔가요',
      userId: '주식고수이도겸',
      content: '호가가 뭔가요호가가 뭔가요호가가 뭔가요호가가 뭔가요',
      createAt: '2022-02-01',
    },
    {
      id: 4,
      title: '유가증권이 뭔가요',
      userId: '주식고수유헌상',
      content: '유가증권이 뭔가요유가증권이 뭔가요유가증권이 뭔가요',
      createAt: '2022-02-01',
    },
    {
      id: 5,
      title: '무형자산이 뭔가요',
      userId: '홍길동',
      content:
        '무형자산이 뭔가요무형자산이 뭔가요무형자산이 뭔가요무형자산이 뭔가요',
      createAt: '2022-02-01',
    },
    {
      id: 6,
      title: '채권이 뭔가요',
      userId: '김철수',
      content: '채권이 뭔가요채권이 뭔가요채권이 뭔가요채권이 뭔가요',
      createAt: '2022-01-31',
    },
    {
      id: 7,
      title:
        '리액트가 뭔가요 리액트가 뭔가요 리액트가 뭔가요 리액트가 뭔가요 리액트가 뭔가요 ',
      userId: '박박박',
      createAt: '2022-01-31',
    },
  ]);

  const [comment, setComment] = useState([
    { commentId: 0, userId: 1, content: '모름', articleNo: 0 },
    { commentId: 1, userId: 1, content: '모름', articleNo: 0 },
    { commentId: 2, userId: 1, content: '모름', articleNo: 1 },
    { commentId: 3, userId: 1, content: '모름', articleNo: 2 },
    { commentId: 0, userId: 1, content: '모름', articleNo: 2 },
  ]);

  return (
    <div className="main-container">
      <Routes>
        <Route
          path=""
          element={<ArticleList items={items} comment={comment} />}
        />
        ;
        <Route path="create" element={<Create />} />;
        {/* <Route path="update" element={<Update />} />; */}
      </Routes>
    </div>
  );
};

export default Community;
