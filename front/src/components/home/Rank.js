import './css/Rank.css';
import Button from '@mui/material/Button';
import { useState, useEffect } from 'react';
import { rankAPI } from '../../api/api';
import { useNavigate } from 'react-router-dom';
import { userStore } from '../../store';

const Rank = () => {
  const [choose, setChoose] = useState(0);
  const [trades, setTrades] = useState([]);
  const [increases, setIncreases] = useState([]);
  const [decreases, setDecreases] = useState([]);
  const { user } = userStore((state) => state);
  const navigate = useNavigate();

  useEffect(() => {
    const todayTop = async () => {
      await rankAPI
        .todayTop(user.memberassetCurrentTime)
        .then((request) => {
          setTrades(request.data);
        })
        .catch((err) => {
          console.log(err);
        });
    };
    const increase = async () => {
      await rankAPI
        .increase(user.memberassetCurrentTime)
        .then((request) => {
          setIncreases(request.data);
        })
        .catch((err) => {
          console.log(err);
        });
    };
    const decrease = async () => {
      await rankAPI
        .decrease(user.memberassetCurrentTime)
        .then((request) => {
          setDecreases(request.data);
        })
        .catch((err) => {
          console.log(err);
        });
    };
    todayTop();
    increase();
    decrease();

    // rankAPI.todayTop(tempDate).then((request) => {
    //   console.log(request.data);
    // });
  }, [user]);

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
      {choose === 0 ? <Trade trades={trades} navigate={navigate} /> : null}
      {choose === 1 ? (
        <BestIncrease increase={increases} navigate={navigate} />
      ) : null}
      {choose === 2 ? (
        <BestDecrease decrease={decreases} navigate={navigate} />
      ) : null}
    </div>
  );
};

export default Rank;

const Trade = (props) => {
  const navigate = props.navigate;
  const trades = props.trades;
  return (
    <div>
      {trades.map((stock, i) => {
        return (
          <div
            className="rank-choose"
            key={i}
            onClick={() => {
              navigate(`stock/${stock.stockCode}`, {
                state: { stockName: stock.stockName },
              });
            }}
          >
            <span>{stock.stockName}</span>
            <span>{stock.stockDataAmount.toLocaleString()}</span>
          </div>
        );
      })}
    </div>
  );
};

const BestIncrease = (props) => {
  const navigate = props.navigate;
  const increase = props.increase;
  return (
    <>
      {increase.map((stock, i) => {
        return (
          <div
            className="incrdecr-list"
            key={i}
            onClick={() => {
              navigate(`stock/${stock.stockCode}`, {
                state: { stockName: stock.stockName },
              });
            }}
          >
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
  const navigate = props.navigate;
  const decrease = props.decrease;
  return (
    <>
      {decrease.map((stock, i) => {
        return (
          <div
            className="incrdecr-list"
            key={i}
            onClick={() => {
              navigate(`stock/${stock.stockCode}`, {
                state: { stockName: stock.stockName },
              });
            }}
          >
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
