import './css/Rank.css';
import Button from '@mui/material/Button';
import { useState } from 'react';

const Rank = () => {
  const [choose, setChoose] = useState(0);
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

  const increase = [
    {
      stockCode: '003850',
      stockName: '보령',
      stockDataDate: 20210111,
      stockDataClosingPrice: 21201,
      stockDataAmount: 15380632,
      stockDataPriceIncreasement: 4889,
      stockDataFlucauationRate: 29,
    },
    {
      stockCode: '011090',
      stockName: '에넥스',
      stockDataDate: 20210111,
      stockDataClosingPrice: 1735,
      stockDataAmount: 99691584,
      stockDataPriceIncreasement: 400,
      stockDataFlucauationRate: 29,
    },
    {
      stockCode: '044060',
      stockName: '조광ILI',
      stockDataDate: 20210111,
      stockDataClosingPrice: 2322,
      stockDataAmount: 25561592,
      stockDataPriceIncreasement: 535,
      stockDataFlucauationRate: 29,
    },
    {
      stockCode: '004140',
      stockName: '동방',
      stockDataDate: 20210111,
      stockDataClosingPrice: 2908,
      stockDataAmount: 64074876,
      stockDataPriceIncreasement: 670,
      stockDataFlucauationRate: 29,
    },
    {
      stockCode: '265740',
      stockName: '엔에프씨',
      stockDataDate: 20210111,
      stockDataClosingPrice: 18250,
      stockDataAmount: 3560074,
      stockDataPriceIncreasement: 4200,
      stockDataFlucauationRate: 29,
    },
  ];

  const decrease = [
    {
      stockCode: '353810',
      stockName: '이지바이오',
      stockDataDate: 20210111,
      stockDataClosingPrice: 8170,
      stockDataAmount: 12261084,
      stockDataPriceIncreasement: -2180,
      stockDataFlucauationRate: -21,
    },
    {
      stockCode: '035810',
      stockName: '이지홀딩스',
      stockDataDate: 20210111,
      stockDataClosingPrice: 4695,
      stockDataAmount: 14475992,
      stockDataPriceIncreasement: -1115,
      stockDataFlucauationRate: -19,
    },
    {
      stockCode: '001360',
      stockName: '삼성제약',
      stockDataDate: 20210111,
      stockDataClosingPrice: 9800,
      stockDataAmount: 17780774,
      stockDataPriceIncreasement: -1850,
      stockDataFlucauationRate: -15,
    },
    {
      stockCode: '036090',
      stockName: '위지트',
      stockDataDate: 20210111,
      stockDataClosingPrice: 1655,
      stockDataAmount: 25770296,
      stockDataPriceIncreasement: -290,
      stockDataFlucauationRate: -14,
    },
    {
      stockCode: '092590',
      stockName: '럭스피아',
      stockDataDate: 20210111,
      stockDataClosingPrice: 1410,
      stockDataAmount: 300,
      stockDataPriceIncreasement: -245,
      stockDataFlucauationRate: -14,
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
      {choose === 1 ? <BestIncrease increase={increase} /> : null}
      {choose === 2 ? <BestDecrease decrease={decrease} /> : null}
    </div>
  );
};

export default Rank;

const Trade = (props) => {
  const trade = props.trade;
  return (
    <div>
      {trade.map((stock, i) => {
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
