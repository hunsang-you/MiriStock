import './css/Charts.css';
import { useState, useEffect } from 'react';
import { stockAPI, api } from '../api/api'; // api 통신

import LineChart from '../components/chart/LineChart';
import Financial from '../components/chart/Financial';
import { Button } from '@mui/material';

const StockDetail = () => {
  // 오늘 날짜
  const [toDay, setToDay] = useState(20210401);
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
  // 오늘날짜 -> 종목명, 종목코드
  const [stockInfo, setStockInfo] = useState({ stockName: '', stockCode: '' });

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
  useEffect(() => {
    const getValueData = async (data1, data2, data3) => {
      const reqData = await api.get(`stockdata/detail`, {
        params: {
          stockCode: data1,
          startDate: data2,
          endDate: data3,
        },
      });
      setStockInfo([reqData.data[0].stockName, reqData.data[0].stockCode]);
    };
    getValueData('005930', toDay, toDay);
  }, []);

  return (
    <div className="main-container">
      <h1>Detail</h1>
      <div>
        <h2>{stockInfo[0]}</h2>
        <h4>{stockInfo[1]}</h4>
        <LineChart toDay={toDay} setToDay={setToDay} dayToTime={dayToTime} />
      </div>

      <div className="space-between space-margin">
        <Button
          style={{ width: '49%' }}
          variant="contained"
          color="primary"
          disableElevation
        >
          매수
        </Button>
        <Button
          style={{ width: '49%' }}
          variant="contained"
          color="primary"
          disableElevation
        >
          매도
        </Button>
      </div>
      <div className="space-margin divbox">
        <h3 className="divleft">주요 뉴스</h3>
        <div>낄낄 뉴스내용</div>
        <div>낄낄 뉴스내용</div>
        <div>낄낄 뉴스내용</div>
        <div>낄낄 뉴스내용</div>
      </div>
      <div className="space-margin divbox">
        <h3>재무 제표</h3>
        <div>
          <Financial toDay={toDay} />
        </div>
        {/* <div style={{ height: 400 }}></div> */}
      </div>
    </div>
  );
};

export default StockDetail;
