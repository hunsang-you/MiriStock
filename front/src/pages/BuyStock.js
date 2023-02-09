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

  const [hopeInputID, setHopeInputID] = useState(0);
  const [hopeCount, setHopeCount] = useState(0);
  const [hopePrice, setHopePrice] = useState(0);
  // console.log(stockCode);

  const inputID = (id) => {
    if (id === 0) {
      setHopeInputID(0);
    } else if (id === 1) {
      setHopeInputID(1);
    } else if (id === 2) {
      setHopeInputID(2);
    }
    console.log(hopeInputID);
  };

  useEffect(() => {
    const use = async () => {
      const reqData = await stockAPI.stockDetail(stockCode, today, today);
      console.log(reqData.data[0]);
      setStockInfo(reqData.data[0]);
    };
    use();
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
          {/* <div>{stockInfo.stockName}</div> */}
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
        <Possible />
        <Persent />
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
