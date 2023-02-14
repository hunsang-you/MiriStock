import './css/BuySell.css';
import { useState, useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { userStore } from '../store';
import { tradeAPI, stockAPI } from '../api/api';
import { AiOutlineInfoCircle } from 'react-icons/ai';
import Swal from 'sweetalert2';

import {
  UpdateHopeCount,
  UpdateHopePrice,
  Persent,
  UpdatePossible,
  CountPlus,
  Keypad,
  UpdateTradeBotton,
} from '../components/trade';

const UpdateBuy = () => {
  const navigate = useNavigate();

  // 유저 정보
  const { user } = userStore((state) => state);
  // console.log(user);
  const userNo = user.memberNo;
  const today = user.memberassetCurrentTime;
  const userMoney = user.memberassetAvailableAsset;
  const location = useLocation();

  // 기존 구매정보
  const [orderNo, setOrderNo] = useState();

  // 주식 이름 / 주식 코드 / 오늘 가격 / 거래량
  const [stockName, setStockName] = useState();
  const [stockCode, setStockCode] = useState();
  const [stockPrice, setStockPrice] = useState(); // 기존 구매가격 -> 희망가격으로 변동
  const [stockAmount, setStockAmount] = useState(); // 기존 구매개수 -> 희망개수로 변동

  // 구입 희망 가격, 주식수, 수수료계산
  const [hopeInputID, setHopeInputID] = useState(0);
  const [hopePrice, setHopePrice] = useState(0);
  const [hopeCount, setHopeCount] = useState(0);
  const [hopeTax, setHopeTax] = useState(0);

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

  // 보유 주식수
  const [maxCount, setMaxCount] = useState(0);

  const buyInfo = () => {
    Swal.fire({
      icon: 'info',
      html: `<div><b>1주당 구매 희망가</b>는 현재 가의 <br> 상한가(+30%) 또는 하한가(-30%)로<br>구매 가능합니다</div>`,
      showConfirmButton: false,
      showCloseButton: true,
    });
  };

  useEffect(() => {
    let locationstockCode;
    if (location.state) {
      setStockName(location.state.stock.stockName); // 주식 이름
      setStockCode(location.state.stock.stockCode); // 주식 코드
      setOrderNo(location.state.stock.limitPriceOrderNo); // 주식 구매 넘버
      setHopePrice(location.state.stock.limitPriceOrderPrice); // 기존 구매가격
      setHopeCount(location.state.stock.limitPriceOrderAmount); // 기존 구매주수
      locationstockCode = location.state.stock.stockCode; // 기존 구매주수
    } else {
      navigate('/');
    }
    const use = async () => {
      const reqData = await stockAPI.stockDetail(
        locationstockCode,
        today,
        today,
      );
      setStockPrice(reqData.data[0].stockDataClosingPrice); // 오늘날짜 가격
      setStockAmount(reqData.data[0].stockDataAmount); // 오늘 거래량
      setMaxCount(0);
      setHopeTax(0);
    };
    use();
  }, []);

  useEffect(() => {
    if (hopePrice === 0) {
      setMaxCount(0);
    } else {
      let amountMoney = Math.floor(stockAmount / 3);
      let moneyMoney = Math.floor(userMoney / (hopePrice * 1.005));
      let result;
      if (amountMoney >= moneyMoney) {
        result = moneyMoney;
      } else if (amountMoney < moneyMoney) {
        result = amountMoney;
      }
      setMaxCount(result);
    }
  }, [hopePrice]);

  useEffect(() => {
    if (hopeCount === 0) {
      setHopeTax(0);
    } else {
      setHopeTax(Math.floor(hopePrice * 0.0005 * hopeCount));
    }
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
          <UpdateHopePrice
            hopeInputID={hopeInputID}
            setHopeInputID={setHopeInputID}
            inputID={inputID}
            hopePrice={hopePrice}
          />
          <UpdateHopeCount
            hopeInputID={hopeInputID}
            setHopeInputID={setHopeInputID}
            inputID={inputID}
            hopeCount={hopeCount}
          />
        </div>
      </div>
      <div className="trade-keypad">
        <div>
          <UpdatePossible
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
            stockAmount={stockAmount}
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
        <UpdateTradeBotton
          orderNo={orderNo}
          userNo={userNo}
          stockName={stockName}
          stockCode={stockCode}
          stockPrice={stockPrice}
          stockAmount={stockAmount}
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

export default UpdateBuy;
