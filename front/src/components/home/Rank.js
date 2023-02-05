import './css/Rank.css';
import Button from '@mui/material/Button';
import { useState, useEffect } from 'react';
import { rankAPI } from '../../api/api';

const Rank = () => {
  const [choose, setChoose] = useState(0);
  const [trades, setTrades] = useState([]);
  const [increases, setIncreases] = useState([]);
  const [decreases, setDecreases] = useState([]);
  const tempDate = 20200525;
  useEffect(() => {
    // const getTrades = async (date) => {
    //   await rankAPI
    //     .increase(date)
    //     .then((request) => {
    //       setTrades(request.data);
    //     })
    //     .catch((err) => {
    //       console.log(err);
    //     });
    // };
    // getTrades(tempDate);
    rankAPI
      .increase(tempDate)
      .then((request) => {
        setIncreases(request.data);
      })
      .catch((err) => {
        console.log(err);
      });
    rankAPI
      .decrease(tempDate)
      .then((request) => {
        setDecreases(request.data);
      })
      .catch((err) => {
        console.log(err);
      });
    // rankAPI.todayTop(tempDate).then((request) => {
    //   console.log(request.data);
    // });
  }, []);
  const trade = [
    {
      stockName: '삼성전자',
      stockDateAmount: 123456834,
    },
    {
      stockName: '똥카오',
      stockDateAmount: 5348941418,
    },
    {
      stockName: '상현전자',
      stockDateAmount: 658294834,
    },
    {
      stockName: '애플',
      stockDateAmount: 89684868,
    },
    {
      stockName: '동양데이타시스템',
      stockDateAmount: 125858283884,
    },
  ];

  return (
    <div className="rank-container">
      <div className="rank">순위별 확인</div>
      <div className="rank-button">
        <Button
          style={{ width: '32%' }}
          color="primary"
          variant={choose === 0 ? 'contained' : 'outlined'}
          disableElevation
          onClick={() => {
            setChoose(0);
          }}
        >
          거래량
        </Button>
        <Button
          style={{ width: '32%' }}
          color="primary"
          variant={choose === 1 ? 'contained' : 'outlined'}
          disableElevation
          onClick={() => {
            setChoose(1);
          }}
        >
          상승
        </Button>
        <Button
          style={{ width: '32%' }}
          color="primary"
          variant={choose === 2 ? 'contained' : 'outlined'}
          disableElevation
          onClick={() => {
            setChoose(2);
          }}
        >
          하락
        </Button>
      </div>
      {choose === 0 ? <Trade trade={trade} /> : null}
      {choose === 1 ? <BestIncrease increase={increases} /> : null}
      {choose === 2 ? <BestDecrease decrease={decreases} /> : null}
    </div>
  );
};

export default Rank;

const Trade = (props) => {
  const trades = props.trade;
  return (
    <div>
      {trades.map((stock, i) => {
        return (
          <div className="rank-choose" key={i}>
            <span>{stock.stockName}</span>
            <span>{stock.stockDateAmount.toLocaleString()}</span>
          </div>
        );
      })}
    </div>
  );
};

const BestIncrease = (props) => {
  const increase = props.increase;
  return (
    <>
      {increase.map((stock, i) => {
        return (
          <div className="incrdecr-list" key={i}>
            <div className="incrdecr-top">
              <span>{stock.stockName}</span>
              <span>{stock.stockDataClosingPrice.toLocaleString()}원</span>
            </div>
            <div className="incrdecr-bottom">
              <span>{stock.stockCode}</span>
              <span
                style={
                  stock.stockDataPriceIncreasement > 0
                    ? { color: '#D2143C' }
                    : { color: '#1E90FF' }
                }
              >
                ▲ {stock.stockDataPriceIncreasement.toLocaleString()}원 (
                {stock.stockDataFlucauationRate}%)
              </span>
            </div>
          </div>
        );
      })}
    </>
  );
};

const BestDecrease = (props) => {
  const decrease = props.decrease;
  return (
    <>
      {decrease.map((stock, i) => {
        return (
          <div className="incrdecr-list" key={i}>
            <div className="incrdecr-top">
              <span>{stock.stockName}</span>
              <span>{stock.stockDataClosingPrice.toLocaleString()}원</span>
            </div>
            <div className="incrdecr-bottom">
              <span>{stock.stockCode}</span>
              <span
                style={
                  stock.stockDataPriceIncreasement > 0
                    ? { color: '#D2143C' }
                    : { color: '#1E90FF' }
                }
              >
                ▼ {stock.stockDataPriceIncreasement.toLocaleString()}원 (
                {stock.stockDataFlucauationRate}%)
              </span>
            </div>
          </div>
        );
      })}
    </>
  );
};
