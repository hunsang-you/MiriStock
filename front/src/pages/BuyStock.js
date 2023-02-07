import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { userStore } from '../store';
import { stockAPI } from '../api/api';

import Keypad from '../components/trade/Keypad';

const BuyStock = () => {
  const { user } = userStore((state) => state);
  let { stockCode } = useParams();
  const today = user.memberassetCurrentTime;
  const navigate = useNavigate();

  const [stockInfo, setStockInfo] = useState();

  // console.log(stockCode);
  useEffect(() => {
    const use = async () => {
      const reqData = await stockAPI.stockDetail(stockCode, today, today);
      setStockInfo({ ...reqData.data });
      console.log(stockInfo);
    };
    use();
  }, []);

  return (
    <div>
      <h1>Trade</h1>
      <Keypad />
    </div>
  );
};

export default BuyStock;
