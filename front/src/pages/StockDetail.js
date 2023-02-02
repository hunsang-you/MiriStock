import './css/Charts.css';
import { useState, useEffect } from 'react';

import Tlqkf from '../components/chart/Tlqkf';
import TlqkfBar from '../components/chart/TlqkfBar';
import { Button } from '@mui/material';

const StockDetail = () => {
  const [stockData, setStockData] = useState([]);
  const [series, setSeries] = useState([
    {
      name: '오늘의 주식 종가',
      data: stockData,
    },
  ]);

  useEffect(() => {
    //   console.log("들어가버려");
    setSeries([
      {
        name: '오늘의 주식 종가',
        data: stockData,
      },
    ]);
  }, [stockData]);

  const inserData = () => {
    if (stockData.length === 50) {
      let copy = [...stockData];
      copy.shift();
      copy.push(Math.random() * 100);
      setStockData(copy);
    } else {
      setStockData([...stockData, Math.random() * 100]);
    }
  };

  return (
    <div className="main-container">
      <h1>Detail</h1>
      <div>
        <Tlqkf />
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
          <TlqkfBar />
        </div>
        {/* <div style={{ height: 400 }}></div> */}
      </div>
    </div>
  );
};

export default StockDetail;
