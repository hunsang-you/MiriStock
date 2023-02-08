import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { userStore } from '../store';
import { stockAPI } from '../api/api';

import { Persent, Keypad, TradeBotton } from '../components/trade';

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
      console.log(reqData.data);
      setStockInfo({ ...reqData.data });
      console.log(stockInfo);
    };
    use();
  }, []);
  return (
    <div>
      <div className="detail-header">
        <strong
          style={{ fontSize: '24px' }}
          onClick={() => {
            navigate(-1);
          }}
        >
          〈
        </strong>
        <div>123</div>
      </div>
      <h1>{hopePrice}</h1>
      <Persent />
      <Keypad hopePrice={hopePrice} setHopePrice={setHopePrice} />
      <TradeBotton />
    </div>
  );
};

export default BuyStock;
