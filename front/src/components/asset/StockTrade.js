import './css/stocktrade.css';
import Button from '@mui/material/Button';
import { useState, useEffect } from 'react';
import { tradeAPI, memberAPI } from '../../api/api';
import { fontSize } from '@mui/system';
import ToggleButton from '@mui/material/ToggleButton';
import ToggleButtonGroup from '@mui/material/ToggleButtonGroup';
import { useNavigate } from 'react-router-dom';
import Swal from 'sweetalert2';
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
  const expectedModal = () => {
    Swal.fire({
      html:
        `<div style="display:flex; flex-direction: column; justify-content: center; width: 100%; margin:auto;">
          <div style="display: flex; justify-content: center; align-items: center;">
            <div><div style="width: 80px; height: 80px; border-radius: 100%; border: 1px solid #1E90FF;  font-weight:bold; display: table-cell; vertical-align: middle; text-align:center;">매도 중</div></div>
            <div>
              <div style="width: 30vw; border-bottom-color: #1E90FF; border-bottom-width: 1px; border-bottom-style: dashed;"></div>
              <div></div>
            </div>
            <div><div style="width: 80px; height: 80px; border-radius: 100%; background: #1E90FF; color: #FFFFFF; font-weight:bold; display: table-cell; vertical-align: middle; text-align:center;">매도 완료</div></div>
          </div>
          <div style="font-weight:bold; font-size:32px; margin-bottom: 20px;">
            ` +
        stock.stockName +
        `
          </div>
          <div style="padding:16px; style=display:flex; flex-direction: column; background: #F4F4F4; border-radius: 10px;">
            <div style="display:flex; justify-content: center; justify-content: space-between; margin:10px 0px;">
              <div>1주당 체결가</div>
              <div>` +
        stock.stockDealOrderClosingPrice.toLocaleString() +
        `원</div>
            </div>
            <div style="display:flex; justify-content: center; justify-content: space-between; margin:10px 0px;">
              <div>주식수</div>
              <div>` +
        stock.stockDealAmount.toLocaleString() +
        `주</div>
            </div>
            <div style="display:flex; justify-content: center; justify-content: space-between; margin:10px 0px;">
              <div>수수료</div>
              <div>` +
        Math.floor(stock.stockDealOrderClosingPrice * 0.0005).toLocaleString() +
        `원
              </div>
            </div>
          </div>
          <div style="padding:16px; display:flex; justify-content: center; justify-content: space-between; margin:10px 0px;">
            <div>체결금액</div>
            <div>` +
        Math.floor(
          stock.stockDealOrderClosingPrice * stock.stockDealAmount * 1.0005,
        ).toLocaleString() +
        `원
            </div>
          </div>
        </div>`,
      showConfirmButton: false,
    });
  };

  return (
    <div
      className="transaction-sell-container"
      onClick={() => {
        expectedModal();
      }}
    >
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
          {Math.floor(
            stock.stockDealOrderClosingPrice * stock.stockDealAmount * 1.0005,
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
  const expectedModal = () => {
    Swal.fire({
      html:
        `<div style="display:flex; flex-direction: column; justify-content: center; width: 100%; margin:auto;">
          <div style="display: flex; justify-content: center; align-items: center;">
            <div><div style="width: 80px; height: 80px; border-radius: 100%; border: 1px solid #D2143C; font-weight:bold; display: table-cell; vertical-align: middle; text-align:center;">매수 중</div></div>
            <div>
              <div style="width: 30vw; border-bottom-color: #D2143C; border-bottom-width: 1px; border-bottom-style: dashed;"></div>
              <div></div>
            </div>
            <div><div style="width: 80px; height: 80px; border-radius: 100%; background: #D2143C; color: #FFFFFF; font-weight:bold; display: table-cell; vertical-align: middle; text-align:center;">매수 완료</div></div>
          </div>
          <div style="font-weight:bold; font-size:32px; margin-bottom: 20px;">
            ` +
        stock.stockName +
        `
          </div>
          <div style="padding:16px; style=display:flex; flex-direction: column; background: #F4F4F4; border-radius: 10px;">
            <div style="display:flex; justify-content: center; justify-content: space-between; margin:10px 0px;">
              <div>1주당 체결가</div>
              <div>` +
        stock.stockDealOrderClosingPrice.toLocaleString() +
        `원</div>
            </div>
            <div style="display:flex; justify-content: center; justify-content: space-between; margin:10px 0px;">
              <div>주식수</div>
              <div>` +
        stock.stockDealAmount.toLocaleString() +
        `주</div>
            </div>
            <div style="display:flex; justify-content: center; justify-content: space-between; margin:10px 0px;">
              <div>수수료</div>
              <div>` +
        Math.floor(stock.stockDealOrderClosingPrice * 0.0005).toLocaleString() +
        `원
              </div>
            </div>
          </div>
          <div style="padding:16px; display:flex; justify-content: center; justify-content: space-between; margin:10px 0px;">
            <div>체결금액</div>
            <div>` +
        Math.floor(
          stock.stockDealOrderClosingPrice * stock.stockDealAmount * 1.0005,
        ).toLocaleString() +
        `원
            </div>
          </div>
        </div>`,
      showConfirmButton: false,
    });
  };

  return (
    <div
      className="transaction-buy-container"
      onClick={() => {
        expectedModal();
      }}
    >
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
          {Math.floor(
            stock.stockDealOrderClosingPrice * stock.stockDealAmount * 1.0005,
          ).toLocaleString()}{' '}
          원
        </div>
      </div>
    </div>
  );
};

const ExpectedTransactionSell = (props) => {
  // 판매예정컴포넌트
  const stock = props.stock;
  const navigate = useNavigate();

  const expectedModal = () => {
    Swal.fire({
      html:
        `<div style="display:flex; flex-direction: column; justify-content: center; width: 100%; margin:auto;">
          <div style="display: flex; justify-content: center; align-items: center;">
            <div><div style="width: 80px; height: 80px; border-radius: 100%; background: #1E90FF; color: #FFFFFF; font-weight:bold; display: table-cell; vertical-align: middle; text-align:center;">매도 중</div></div>
            <div>
              <div style="width: 30vw; border-bottom-color: #1E90FF; border-bottom-width: 1px; border-bottom-style: dashed;"></div>
              <div></div>
            </div>
            <div><div style="width: 80px; height: 80px; border-radius: 100%; border: 1px solid #1E90FF; font-weight:bold; display: table-cell; vertical-align: middle; text-align:center;">매도 완료</div></div>
          </div>
          <div style="font-weight:bold; font-size:32px; margin-bottom: 20px;">
            ` +
        stock.stockName +
        `
          </div>
          <div style="padding:16px; style=display:flex; flex-direction: column; background: #F4F4F4; border-radius: 10px;">
            <div style="display:flex; justify-content: center; justify-content: space-between; margin:10px 0px;">
              <div>1주당 체결가</div>
              <div>` +
        stock.limitPriceOrderPrice.toLocaleString() +
        `원</div>
            </div>
            <div style="display:flex; justify-content: center; justify-content: space-between; margin:10px 0px;">
              <div>주식수</div>
              <div>` +
        stock.limitPriceOrderAmount.toLocaleString() +
        `주</div>
            </div>
            <div style="display:flex; justify-content: center; justify-content: space-between; margin:10px 0px;">
              <div>예상 수수료</div>
              <div>
                ` +
        Math.floor(
          stock.limitPriceOrderPrice * stock.limitPriceOrderAmount * 0.0005,
        ).toLocaleString() +
        `원
              </div>
            </div>
          </div>
          <div style="padding:16px; display:flex; justify-content: center; justify-content: space-between; margin:10px 0px;">
            <div>체결예정금액</div>
            <div>
              ` +
        Math.floor(
          stock.limitPriceOrderPrice * stock.limitPriceOrderAmount * 1.0005,
        ).toLocaleString() +
        `원
            </div>
          </div>
        </div>`,
      reverseButtons: true,
      showDenyButton: true,
      showCancelButton: true,
      confirmButtonText: '수정',
      denyButtonText: `삭제`,
      cancelButtonText: '확인',
      confirmButtonColor: '#6DCEF5',
      denyButtonColor: '#d33',
      cancelButtonColor: '8e8e8e',
    }).then((result) => {
      /* Read more about isConfirmed, isDenied below */
      if (result.isConfirmed) {
        console.log('수정');
        navigate(`/stock/` + stock.stockCode + `/updateSell`, {
          state: {
            stock: stock,
          },
        });
      } else if (result.isDenied) {
        console.log('삭제');
      }
    });
  };

  return (
    <div
      className="transaction-sell-container"
      onClick={() => {
        expectedModal();
      }}
    >
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
  const navigate = useNavigate();

  const expectedModal = () => {
    Swal.fire({
      html:
        `<div style="display:flex; flex-direction: column; justify-content: center; width: 100%; margin:auto;">
          <div style="display: flex; justify-content: center; align-items: center;">
            <div><div style="width: 80px; height: 80px; border-radius: 100%; background: #D2143C; color: #FFFFFF; font-weight:bold; display: table-cell; vertical-align: middle; text-align:center;">매수 중</div></div>
            <div>
              <div style="width: 30vw; border-bottom-color: #D2143C; border-bottom-width: 1px; border-bottom-style: dashed;"></div>
              <div></div>
            </div>
            <div><div style="width: 80px; height: 80px; border-radius: 100%; border: 1px solid #D2143C; font-weight:bold; display: table-cell; vertical-align: middle; text-align:center;">매수 완료</div></div>
          </div>
          <div style="font-weight:bold; font-size:32px; margin-bottom: 20px;">
            ` +
        stock.stockName +
        `
          </div>
          <div style="padding:16px; style=display:flex; flex-direction: column; background: #F4F4F4; border-radius: 10px;">
            <div style="display:flex; justify-content: center; justify-content: space-between; margin:10px 0px;">
              <div>1주당 체결가</div>
              <div>` +
        stock.limitPriceOrderPrice.toLocaleString() +
        `원</div>
            </div>
            <div style="display:flex; justify-content: center; justify-content: space-between; margin:10px 0px;">
              <div>주식수</div>
              <div>` +
        stock.limitPriceOrderAmount.toLocaleString() +
        `주</div>
            </div>
            <div style="display:flex; justify-content: center; justify-content: space-between; margin:10px 0px;">
              <div>예상 수수료</div>
              <div>
                ` +
        (
          stock.limitPriceOrderPrice *
          stock.limitPriceOrderAmount *
          0.0005
        ).toLocaleString() +
        `원
              </div>
            </div>
          </div>
          <div style="padding:16px; display:flex; justify-content: center; justify-content: space-between; margin:10px 0px;">
            <div>체결예정금액</div>
            <div>
              ` +
        (
          stock.limitPriceOrderPrice *
          stock.limitPriceOrderAmount *
          1.0005
        ).toLocaleString() +
        `원
            </div>
          </div>
        </div>`,
      reverseButtons: true,
      showDenyButton: true,
      showCancelButton: true,
      confirmButtonText: '수정',
      denyButtonText: `삭제`,
      cancelButtonText: '확인',
      confirmButtonColor: '#6DCEF5',
      denyButtonColor: '#d33',
      cancelButtonColor: '8e8e8e',
    }).then((result) => {
      /* Read more about isConfirmed, isDenied below */
      if (result.isConfirmed) {
        console.log('수정');
        navigate(`/stock/` + stock.stockCode + `/updateBuy`, {
          state: {
            stock: stock,
          },
        });
      } else if (result.isDenied) {
        console.log('삭제');
      }
    });
  };

  return (
    <div
      className="transaction-buy-container"
      onClick={() => {
        expectedModal();
      }}
    >
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
            stock.limitPriceOrderPrice *
            stock.limitPriceOrderAmount *
            1.0005
          ).toLocaleString()}{' '}
          원
        </div>
      </div>
    </div>
  );
};
