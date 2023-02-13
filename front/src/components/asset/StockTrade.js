import './css/stocktrade.css';
import Button from '@mui/material/Button';
import { useState, useEffect } from 'react';
import { tradeAPI, memberAPI } from '../../api/api';
import { fontSize } from '@mui/system';
import ToggleButton from '@mui/material/ToggleButton';
import ToggleButtonGroup from '@mui/material/ToggleButtonGroup';
import { useNavigate } from 'react-router-dom';

const StockTrade = () => {
  const [choose, setChoose] = useState(0);
  const [myStocks, setMyStocks] = useState([]);
  const [transaction, setTransaction] = useState([]);
  const [expectedTransaction, setExpectedTransaction] = useState([]);
  const navigate = useNavigate();
  useEffect(() => {
    const allTrades = async (type) => {
      await tradeAPI
        .getAllTrades(type)
        .then((request) => {
          setTransaction(request.data);
        })
        .catch((err) => {
          console.log(err);
        });
    };
    const myStocks = async () => {
      await memberAPI
        .stocks()
        .then((request) => {
          setMyStocks(request.data);
        })
        .catch((err) => console.log(err));
    };
    const limitMyStocks = async () => {
      await tradeAPI
        .checkTrades()
        .then((request) => {
          setExpectedTransaction(request.data);
        })
        .catch((err) => console.log(err));
    };
    allTrades();
    myStocks();
    limitMyStocks();
  }, []);
  const dayDay = (date) => {
    date = String(date);
    return date.slice(2, 4) + '.' + date.slice(4, 6) + '.' + date.slice(6, 8);
  };

  return (
    <div className="stock-container">
      <div className="stock-button">
        <Button
          style={{ width: '32%' }}
          color="primary"
          variant={choose === 0 ? 'contained' : 'outlined'}
          disableElevation
          onClick={() => {
            setChoose(0);
          }}
        >
          보유 주식
        </Button>
        <Button
          className="dropdown"
          style={{ width: '32%' }}
          color="primary"
          variant={choose === 1 ? 'contained' : 'outlined'}
          disableElevation
          onClick={() => {
            setChoose(1);
          }}
        >
          거래 내역
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
          거래 예정
        </Button>
      </div>
      {choose === 0 ? (
        <HoldingStock myStocks={myStocks} navigate={navigate} />
      ) : null}
      {choose === 1
        ? transaction.map((stock, i) => {
            return (
              <div key={i}>
                {stock.stockDealType === 'BUY' ? (
                  <TransactionBuy stock={stock} dayDay={dayDay} />
                ) : (
                  <TransactionSell stock={stock} dayDay={dayDay} />
                )}
              </div>
            );
          })
        : null}
      {choose === 2
        ? expectedTransaction.map((stock, i) => {
            return (
              <div key={i}>
                {stock.limitPriceOrderType === 'BUY' ? (
                  <ExpectedTransactionBuy stock={stock} />
                ) : (
                  <ExpectedTransactionSell stock={stock} />
                )}
              </div>
            );
          })
        : null}
      {/* {choose === 0 ? <Trade trade={trade} /> : null}
      {choose === 1 ? <BestIncrease increase={increases} /> : null}
      {choose === 2 ? <BestDecrease decrease={decreases} /> : null} */}
    </div>
  );
};

export default StockTrade;

const HoldingStock = (props) => {
  const stocks = props.myStocks;
  const navigate = props.navigate;
  return (
    <div>
      {stocks.map((stock, i) => {
        return (
          <div
            className="asset-holding-main"
            key={i}
            onClick={() => {
              navigate(`/stock/${stock.stockCode}`, {
                state: { stockName: stock.stockName },
              });
            }}
          >
            <div className="asset-holding-first">
              <span
                style={{
                  fontSize: '18px',
                  overflow: 'hidden',
                  textOverflow: 'ellipsis',
                  whiteSpace: 'nowrap',
                  width: '100px',
                }}
              >
                {stock.stockName}
              </span>
              <span>평균매수가</span>
              <span>현재가</span>
              <span>수익률</span>
            </div>
            <div className="asset-holding-second">
              <span style={{ fontSize: '18px', color: '#AAA7A7' }}>
                {stock.stockCode}
              </span>
              <span>{stock.memberStockAvgPrice.toLocaleString()}원</span>
              <span>{stock.stockDataClosingPrice.toLocaleString()}원</span>
              <span
                style={
                  stock.stockDataClosingPrice * stock.memberStockAmount -
                    stock.memberStockAvgPrice * stock.memberStockAmount >=
                  0
                    ? { color: '#D2143C' }
                    : { color: '#1E90FF' }
                }
              >
                {stock.stockDataClosingPrice * stock.memberStockAmount -
                  stock.memberStockAvgPrice * stock.memberStockAmount >=
                0
                  ? '▲ '
                  : '▼ '}
                {Math.abs(
                  (stock.stockDataClosingPrice / stock.memberStockAvgPrice) *
                    100 -
                    100,
                )
                  .toFixed(2)
                  .toLocaleString()}
                %
              </span>
            </div>
            <div
              className="asset-holding-last"
              style={{
                overflow: 'hidden',
                textOverflow: 'ellipsis',
                whiteSpace: 'nowrap',
                width: '100px',
              }}
            >
              <span style={{ fontSize: '18px' }}>
                {stock.memberStockAmount.toLocaleString()}주
              </span>
              <span>
                {(
                  stock.memberStockAvgPrice * stock.memberStockAmount
                ).toLocaleString()}
                원
              </span>
              <span>
                {(
                  stock.stockDataClosingPrice * stock.memberStockAmount
                ).toLocaleString()}
                원
              </span>
              <span
                style={
                  stock.stockDataClosingPrice * stock.memberStockAmount -
                    stock.memberStockAvgPrice * stock.memberStockAmount >=
                  0
                    ? { color: '#D2143C' }
                    : { color: '#1E90FF' }
                }
              >
                {stock.stockDataClosingPrice * stock.memberStockAmount -
                  stock.memberStockAvgPrice * stock.memberStockAmount >=
                0
                  ? '+'
                  : null}
                {(
                  stock.stockDataClosingPrice * stock.memberStockAmount -
                  stock.memberStockAvgPrice * stock.memberStockAmount
                ).toLocaleString()}
                원
              </span>
            </div>
          </div>
        );
      })}
    </div>
  );
};

const TransactionSell = (props) => {
  //판매컴포넌트
  const stock = props.stock;
  const dayDay = props.dayDay;
  return (
    <div className="transaction-sell-container">
      <div className="transaction-sell-items">
        <div style={{ color: '#1E90FF' }}>매도완료</div>
        <div>{dayDay(stock.stockDealDate)}</div>
        <div className="transaction-sell-last">
          {stock.stockDealAmount.toLocaleString()}주
        </div>
      </div>
      <div className="transaction-sell-items">
        <div>{stock.stockName}</div>
        <div>{stock.stockCode}</div>
      </div>
      <div className="transaction-sell-items">
        <div>주당구매가</div>
        <div>{stock.stockDealOrderClosingPrice.toLocaleString()}원</div>
        <div className="transaction-sell-last" style={{ color: '#1E90FF' }}>
          ▼{' '}
          {(
            stock.stockDealOrderClosingPrice * stock.stockDealAmount
          ).toLocaleString()}{' '}
          원
        </div>
      </div>
    </div>
  );
};

const TransactionBuy = (props) => {
  //구매컴포넌트
  const stock = props.stock;
  const dayDay = props.dayDay;
  return (
    <div className="transaction-buy-container">
      <div className="transaction-buy-items">
        <div>{stock.stockDealAmount.toLocaleString()}주</div>
        <div className="transaction-buy-mid">{dayDay(stock.stockDealDate)}</div>
        <div className="transaction-buy-last" style={{ color: '#D2143C' }}>
          매수완료
        </div>
      </div>
      <div className="transaction-buy-items">
        <div>{stock.stockName}</div>
        <div>{stock.stockCode}</div>
      </div>
      <div className="transaction-buy-items">
        <div>주당판매가</div>
        <div>{stock.stockDealOrderClosingPrice.toLocaleString()}원</div>
        <div
          className="transaction-buy-nomid-last"
          style={{ color: '#D2143C' }}
        >
          ▲{' '}
          {(
            stock.stockDealOrderClosingPrice * stock.stockDealAmount
          ).toLocaleString()}{' '}
          원
        </div>
      </div>
    </div>
  );
};

const ExpectedTransactionSell = (props) => {
  //판매예정컴포넌트
  const stock = props.stock;
  console.log(stock.limitPriceOrderAmount);
  return (
    <div className="transaction-sell-container">
      <div className="transaction-sell-items">
        <div>매도예정</div>
        <div></div>
        <div className="transaction-sell-last">
          {stock.limitPriceOrderAmount}주
        </div>
      </div>
      <div className="transaction-sell-items">
        <div>{stock.stockName}</div>
        <div>{stock.stockCode}</div>
      </div>
      <div className="transaction-sell-items">
        <div>주당구매예정가</div>
        <div>{stock.limitPriceOrderPrice.toLocaleString()}원</div>
        <div className="transaction-sell-last">
          {(
            stock.limitPriceOrderPrice * stock.limitPriceOrderAmount
          ).toLocaleString()}{' '}
          원
        </div>
      </div>
    </div>
  );
};

const ExpectedTransactionBuy = (props) => {
  //구매예정컴포넌트
  const stock = props.stock;
  console.log(stock);
  return (
    <div className="transaction-buy-container">
      <div className="transaction-buy-items">
        <div>{stock.limitPriceOrderAmount}주</div>
        <div className="transaction-buy-mid"></div>
        <div className="transaction-buy-last">매수예정</div>
      </div>
      <div className="transaction-buy-items">
        <div>{stock.stockName}</div>
        <div>{stock.stockCode}</div>
      </div>
      <div className="transaction-buy-items">
        <div>주당판매예정가</div>
        <div>{stock.limitPriceOrderPrice.toLocaleString()}원</div>
        <div className="transaction-buy-nomid-last">
          {(
            stock.limitPriceOrderPrice * stock.limitPriceOrderAmount
          ).toLocaleString()}{' '}
          원
        </div>
      </div>
    </div>
  );
};
