import { tradeAPI } from '../../../api/api';
import { useNavigate } from 'react-router-dom';
import { Button } from '@mui/material';

const SellTradeBotton = (props) => {
  const navigate = useNavigate();
  const Swal = require('sweetalert2');

  const hopeTotal = props.hopePrice * props.hopeCount + props.hopeTax;

  const finalCheck = () => {
    Swal.fire({
      title: props.stockName,
      html:
        `<div style='display:flex; flex-direction: column; flex-wrap: wrap; width: 90%; margin: auto;'>
        <div style="border-top: 1px solid #6DCEF5; border-bottom: 1px solid #6DCEF5; padding:20px 0px; margin:20px 0px; ">
          <div style='display:flex; justify-content: center; justify-content: space-between; margin:10px 0px;'><div>1주당 희망가</div><div>` +
        props.hopePrice.toLocaleString() +
        ` 원</div></div>
          <div style='display:flex; justify-content: center; justify-content: space-between; margin:10px 0px;'><div>판매 주식수</div><div>` +
        props.hopeCount.toLocaleString() +
        ` 주</div></div>
          <div style='display:flex; justify-content: center; justify-content: space-between; margin:10px 0px;'><div>예상 수수료</div><div>` +
        props.hopeTax.toLocaleString() +
        ` 원</div></div>
        </div>
          <div style='display:flex; justify-content: center; justify-content: space-between;'><div>총금액</div><div>` +
        hopeTotal.toLocaleString() +
        ` 원</div></div>
        </div>`,
      showCancelButton: true,
      confirmButtonText: '판매하기',
      cancelButtonText: '취소',
      reverseButtons: true,
      confirmButtonColor: '#6DCEF5',
      cancelButtonColor: '#d33',
    }).then((result) => {
      if (result.isConfirmed) {
        Swal.fire({
          icon: 'success',
          title: '주문하였습니다',
          confirmButtonText: '확인',
        }).then(() => {
          tradeAPI
            .updateOrder(
              props.orderNo,
              props.stockCode,
              props.stockName,
              props.userNo,
              props.hopePrice,
              props.hopeCount,
              'SELL',
            )
            .then((request) => {
              navigate('/asset');
            });
        });
      }
    });
  };

  return (
    <div>
      {props.hopePrice === 0 ? (
        <Button
          style={{ width: '100%' }}
          variant="contained"
          color="grey"
          disableElevation
        >
          <div className="trade-nonbtn">판매 가격을 입력해주세요</div>
        </Button>
      ) : props.hopePrice < props.stockPrice * 0.7 ? (
        <Button
          style={{ width: '100%' }}
          variant="contained"
          color="grey"
          disableElevation
        >
          <div className="trade-nonbtn">판매 가격이 낮습니다</div>
        </Button>
      ) : props.hopePrice > props.stockPrice * 1.3 ? (
        <Button
          style={{ width: '100%' }}
          variant="contained"
          color="grey"
          disableElevation
        >
          <div className="trade-nonbtn">판매 가격이 높습니다</div>
        </Button>
      ) : props.hopeInputID === 0 &&
        props.stockPrice * 0.7 <= props.hopePrice <= props.stockPrice * 1.3 ? (
        <Button
          style={{ width: '100%' }}
          variant="contained"
          color="primary"
          disableElevation
          onClick={() => {
            props.inputID(1);
          }}
        >
          <div className="trade-nonbtn">입력 완료</div>
        </Button>
      ) : props.hopeCount === 0 ? (
        <Button
          style={{ width: '100%' }}
          variant="contained"
          color="grey"
          disableElevation
        >
          <div className="trade-nonbtn">판매 수량을 입력해주세요</div>
        </Button>
      ) : props.hopeCount > Math.floor(props.maxCount) ? (
        <Button
          style={{ width: '100%' }}
          variant="contained"
          color="grey"
          disableElevation
        >
          <div className="trade-nonbtn">판매 수량을 확인하세요</div>
        </Button>
      ) : (
        <Button
          style={{ width: '100%' }}
          variant="contained"
          color="primary"
          disableElevation
          onClick={() => {
            finalCheck();
          }}
        >
          <div className="trade-btn">
            <div>{hopeTotal.toLocaleString()}원</div>
          </div>
        </Button>
      )}
    </div>
  );
};

export default SellTradeBotton;
