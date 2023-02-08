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
  Keypad,
  TradeBotton,
} from '../components/trade';

const BuyStock = () => {
  const { user } = userStore((state) => state);
  let { stockCode } = useParams();
  const today = user.memberassetCurrentTime;
  const navigate = useNavigate();

  const [stockInfo, setStockInfo] = useState();

  const [hopePrice, setHopePrice] = useState(0);
  // console.log(stockCode);
  useEffect(() => {
    const use = async () => {
      const reqData = await stockAPI.stockDetail(stockCode, today, today);
      console.log(reqData.data[0]);
      setStockInfo(reqData.data[0]);
    };
    use();
    console.log(stockInfo);
  }, []);
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
          <div></div>
        </div>
        <div>
          <HopeCount />
        </div>
        <div>
          <HopePrice hopePrice={hopePrice} />
        </div>
      </div>
      <div className="trade-keypad">
        <Possible />
        <Persent />
        <Keypad hopePrice={hopePrice} setHopePrice={setHopePrice} />
        <TradeBotton />
      </div>
    </div>
  );
};

export default BuyStock;
