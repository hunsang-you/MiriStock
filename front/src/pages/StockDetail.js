import './css/Charts.css';
import { useState, useEffect } from 'react';
import { stockAPI, api } from '../api/api'; // api 통신

import LineChart from '../components/chart/LineChart';
import Financial from '../components/chart/Financial';
import { Button } from '@mui/material';

const StockDetail = () => {
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

  // 왜 했던건지 기억이 나지않아 주석 =================================
  // const [stockData, setStockData] = useState([]);
  // const [series, setSeries] = useState([
  //   {
  //     name: '오늘의 주식 종가',
  //     data: stockData,
  //   },
  // ]);

  // useEffect(() => {
  //   //   console.log("들어가버려");
  //   setSeries([
  //     {
  //       name: '오늘의 주식 종가',
  //       data: stockData,
  //     },
  //   ]);
  // }, [stockData]);
  // const inserData = () => {
  //   if (stockData.length === 50) {
  //     let copy = [...stockData];
  //     copy.shift();
  //     copy.push(Math.random() * 100);
  //     setStockData(copy);
  //   } else {
  //     setStockData([...stockData, Math.random() * 100]);
  //   }
  // };  // ===========================================================

  return (
    <div className="main-container">
      <h1>Detail</h1>
      <div className="stock-content">
        <div>{stockInfo}</div>
        <div>{stockCode}</div>
        <LineChart
          stockCode={stockCode}
          toDay={toDay}
          setToDay={setToDay}
          dayToTime={dayToTime}
          setStockInfo={setStockInfo}
        />
      </div>

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
