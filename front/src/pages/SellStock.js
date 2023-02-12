import './css/BuySell.css';
import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { userStore } from '../store';
import { stockAPI, tradeAPI } from '../api/api';
import { AiOutlineInfoCircle } from 'react-icons/ai';
import Swal from 'sweetalert2';

import {
  SellHopePrice,
  SellHopeCount,
  Persent,
  SellPossible,
  CountPlus,
  Keypad,
  SellTradeBotton,
} from '../components/trade';

const SellStock = () => {
  const navigate = useNavigate();

  // 유저 정보
  const { user } = userStore((state) => state);
  const userNo = user.memberNo;
  // console.log(user);
  let { stockCode } = useParams();
  const today = user.memberassetCurrentTime;
  const userMoney = user.memberassetAvailableAsset;

  // 주식 이름 / 오늘 가격
  const [stockName, setStockName] = useState();
  const [stockPrice, setStockPrice] = useState();
  // const [stockAmount, setStockAmount] = useState();
  // 구입 희망 가격, 주식수, 수수료계산
  const [hopeInputID, setHopeInputID] = useState(0);
  const [hopePrice, setHopePrice] = useState(0);
  const [hopeCount, setHopeCount] = useState(0);
  const [hopeTax, setHopeTax] = useState(0);
  // console.log(stockCode);
  // id = 0 => 구매 희망가 만 입력 (클릭이벤트x) / id = 1
  const inputID = (id) => {
    if (id === 0) {
      setHopeInputID(0);
    } else if (id === 1) {
      setHopeInputID(1);
    } else if (id === 2) {
      setHopeInputID(2);
    } else {
      return 0;
    }
  };

  // 보유 주식수 (판매니까)
  const [maxCount, setMaxCount] = useState(0);

  const buyInfo = () => {
    Swal.fire({
      icon: 'info',
      html: `<div><b>1주당 판매 희망가</b>는 현재 가의 <br> 상한가(+30%) 또는 하한가(-30%)로<br>판매 가능합니다</div>`,
      showConfirmButton: false,
      showCloseButton: true,
    });
  };

  useEffect(() => {
    const use = async () => {
      const reqData = await stockAPI.stockDetail(stockCode, today, today);
      // console.log(reqData.data[0]);
      setStockName(reqData.data[0].stockName);
      setStockPrice(reqData.data[0].stockDataClosingPrice);
    };
    const stockDate = async (stockCode) => {
      const reqData = await tradeAPI.stockData(stockCode);
      // console.log(reqData.data.memberStockAmount);
      setMaxCount(reqData.data.memberStockAmount);
    };
    use();
    stockDate(stockCode);
  }, []);

  useEffect(() => {
    setHopeTax(Math.floor(hopePrice * 0.005 * hopeCount));
  }, [hopePrice, hopeCount]);

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
          <AiOutlineInfoCircle
            onClick={() => {
              buyInfo();
            }}
          />
        </div>
        <div>
          {hopeInputID === 0 ? (
            <SellHopePrice
              hopeInputID={hopeInputID}
              setHopeInputID={setHopeInputID}
              inputID={inputID}
              hopePrice={hopePrice}
            />
          ) : (
            <div>
              <SellHopePrice
                hopeInputID={hopeInputID}
                setHopeInputID={setHopeInputID}
                inputID={inputID}
                hopePrice={hopePrice}
              />
              <SellHopeCount
                hopeInputID={hopeInputID}
                setHopeInputID={setHopeInputID}
                inputID={inputID}
                hopeCount={hopeCount}
              />
            </div>
          )}
        </div>
      </div>
      <div className="trade-keypad">
        <div>
          <SellPossible
            maxCount={maxCount}
            hopeTax={hopeTax.toLocaleString()}
          />
        </div>
        {hopeInputID !== 1 ? (
          <Persent
            stockPrice={stockPrice}
            hopePrice={hopePrice}
            setHopePrice={setHopePrice}
          />
        ) : (
          <CountPlus
            userMoney={userMoney}
            hopePrice={hopePrice}
            hopeCount={hopeCount}
            setHopeCount={setHopeCount}
            hopeTax={hopeTax}
            maxCount={maxCount}
          />
        )}
        <Keypad
          hopeInputID={hopeInputID}
          setHopeInputID={setHopeInputID}
          hopeCount={hopeCount}
          setHopeCount={setHopeCount}
          hopePrice={hopePrice}
          setHopePrice={setHopePrice}
        />
        <SellTradeBotton
          stockName={stockName}
          stockCode={stockCode}
          stockPrice={stockPrice}
          hopeInputID={hopeInputID}
          inputID={inputID}
          setHopeInputID={setHopeInputID}
          hopePrice={hopePrice}
          hopeCount={hopeCount}
          hopeTax={hopeTax}
          maxCount={maxCount}
        />
      </div>
    </div>
  );
};

export default SellStock;
