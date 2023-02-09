import './css/BuySell.css';
import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { userStore } from '../store';
import { stockAPI } from '../api/api';

import {
  HopeCount,
  HopePrice,
  Persent,
  Possible,
  CountPlus,
  Keypad,
  TradeBotton,
} from '../components/trade';

const BuyStock = () => {
  const { user } = userStore((state) => state);
  let { stockCode } = useParams();
  const today = user.memberassetCurrentTime;
  const navigate = useNavigate();

  const [stockName, setStockName] = useState();
  const [stockPrice, setStockPrice] = useState();
  const [stockAmount, setStockAmount] = useState();
  // 구입 희망 가격, 주식수, 수수료계산
  const [hopeInputID, setHopeInputID] = useState(0);
  const [hopePrice, setHopePrice] = useState(0);
  const [hopeCount, setHopeCount] = useState(0);
  const [hopeTax, setHopeTax] = useState();
  // console.log(stockCode);

  const inputID = (id) => {
    if (id === 0) {
      setHopeInputID(0);
    } else if (id === 1) {
      setHopeInputID(1);
    } else if (id === 2) {
      setHopeInputID(2);
    }
  };

  useEffect(() => {
    const use = async () => {
      const reqData = await stockAPI.stockDetail(stockCode, today, today);
      // console.log(reqData.data[0]);
      setStockName(reqData.data[0].stockName);
      setStockPrice(reqData.data[0].stockDataClosingPrice);
      setStockAmount(reqData.data[0].stockDataAmount);
    };
    use();
  }, []);

  useEffect(() => {
    setHopeTax(Math.floor(hopePrice * 0.0005));
  }, [hopePrice]);

  return (
    <div className="trade-body">
      <div>
        <div className="trade-header">
          <strong
            style={{ fontSize: '24px' }}
            onClick={() => {
              navigate(-1);
            }}
          >
            〈
          </strong>
          <div>{stockName}</div>
        </div>
        <div>
          {hopeInputID === 0 ? (
            <HopePrice
              hopeInputID={hopeInputID}
              setHopeInputID={setHopeInputID}
              inputID={inputID}
              hopePrice={hopePrice}
            />
          ) : (
            <div>
              <HopeCount
                hopeInputID={hopeInputID}
                setHopeInputID={setHopeInputID}
                inputID={inputID}
                hopeCount={hopeCount}
              />
              <HopePrice
                hopeInputID={hopeInputID}
                setHopeInputID={setHopeInputID}
                inputID={inputID}
                hopePrice={hopePrice}
              />
            </div>
          )}
        </div>
        <div></div>
      </div>
      <div className="trade-keypad">
        <Possible hopeTax={hopeTax} />
        {hopeInputID !== 1 ? (
          <Persent
            stockPrice={stockPrice}
            hopePrice={hopePrice}
            setHopePrice={setHopePrice}
          />
        ) : (
          <CountPlus />
        )}
        <Keypad
          hopeInputID={hopeInputID}
          setHopeInputID={setHopeInputID}
          hopeCount={hopeCount}
          setHopeCount={setHopeCount}
          hopePrice={hopePrice}
          setHopePrice={setHopePrice}
        />
        <TradeBotton
          hopeInputID={hopeInputID}
          setHopeInputID={setHopeInputID}
          inputID={inputID}
        />
      </div>
    </div>
  );
};

export default BuyStock;
