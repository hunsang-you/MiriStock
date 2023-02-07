import './css/Charts.css';
import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
// import { stockAPI, api } from '../api/api'; // api 통신
import mirilogo2 from '../static/mirilogo2.png';

import LineChart from '../components/chart/LineChart';
import Financial from '../components/chart/Financial';
import News from '../components/detail/News';
import { Button } from '@mui/material';
import { AiFillStar } from 'react-icons/ai';

const StockDetail = () => {
  const navigate = useNavigate();
  // 오늘 날짜
  const [toDay, setToDay] = useState(20181003);
  const stockCode = '005930';
  // 날짜데이터를 시간으로 변환하는 함수
  const dayToTime = (date) => {
    let year, month, day, time;
    year = parseInt(date / 10000);
    // console.log(year);
    month = parseInt((date - year * 10000) / 100) - 1;
    // console.log(month);
    day = date - year * 10000 - (month + 1) * 100;
    // console.log(day);
    time = new Date(year, month, day);
    // console.log(time.getTime());
    // console.log(year, month, day);
    return time.getTime();
  };
  // 오늘날짜 -> 종목명
  const [stockInfo, setStockInfo] = useState('');
  const [isFavorite, setIsFavorite] = useState(true);

  return (
    <div className="main-container">
      <div className="detail-header">
        <strong
          style={{ fontSize: '24px' }}
          onClick={() => {
            navigate(-1);
          }}
        >
          〈
        </strong>
        <div>
          <img src={mirilogo2} className="mirilogo2" />
        </div>
      </div>
      <div className="detail-title">
        <b>{stockInfo}</b>
        <span>{stockCode}</span>
        <div className="favorite-icon">
          <AiFillStar
            // 클릭시 관심주식 on / off
            id="favorite-icon"
            size={40}
            style={isFavorite ? { color: '#FFCC00' } : { color: '#AAA7A7' }}
            onClick={() => {
              setIsFavorite(!isFavorite);
            }}
          />
        </div>
        {/* <div>★☆</div> */}
      </div>
      <LineChart
        stockCode={stockCode}
        toDay={toDay}
        setToDay={setToDay}
        dayToTime={dayToTime}
        setStockInfo={setStockInfo}
      />

      <div className="space-between space-margin">
        <Button
          style={{ width: '48%' }}
          variant="contained"
          color="primary"
          disableElevation
        >
          매수
        </Button>
        <Button
          style={{ width: '48%' }}
          variant="contained"
          color="primary"
          disableElevation
        >
          매도
        </Button>
      </div>
      <div className="space-margin divbox">
        <div className="charts-title">주요 뉴스</div>
        <div className="charts-content">
          <News />
          <div>낄낄 뉴스내용</div>
          <div>낄낄 뉴스내용</div>
          <div>낄낄 뉴스내용</div>
          <div>낄낄 뉴스내용</div>
        </div>
      </div>
      <div className="space-margin divbox">
        <div className="div-title">재무 제표</div>
        <div>
          <Financial toDay={toDay} />
        </div>
      </div>
    </div>
  );
};

export default StockDetail;
