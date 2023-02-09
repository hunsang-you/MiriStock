import { Button } from '@mui/material';

const TradeBotton = (props) => {
  const Swal = require('sweetalert2');

  const errerPrice = () => {
    Swal.fire({
      position: 'center',
      icon: 'warning',
      title: '구매 희망가를 확인해 주세요',
      showConfirmButton: false,
      timer: 1000,
    });
  };
  const errerCount = () => {
    Swal.fire({
      position: 'center',
      icon: 'warning',
      title: '구매 주식수를 확인해 주세요',
      showConfirmButton: false,
      timer: 1000,
    });
  };

  return (
    <div>
      {(props.hopeInputID === 0 || props.hopeInputID === 2) &&
      (props.hopePrice <= props.stockPrice * 0.7 ||
        props.hopePrice >= props.stockPrice * 1.3) ? (
        <Button
          style={{ width: '100%' }}
          variant="contained"
          color="primary"
          disableElevation
          onClick={() => {
            errerPrice();
          }}
        >
          <div className="trade-btn">입력 완료</div>
        </Button>
      ) : props.hopeInputID === 0 ? (
        <Button
          style={{ width: '100%' }}
          variant="contained"
          color="primary"
          disableElevation
          onClick={() => {
            props.inputID(1);
          }}
        >
          <div className="trade-btn">입력 완료</div>
        </Button>
      ) : props.hopeCount === 0 ? (
        <Button
          style={{ width: '100%' }}
          variant="contained"
          color="primary"
          disableElevation
          onClick={() => {
            errerCount();
          }}
        >
          <div className="trade-btn">최종</div>
        </Button>
      ) : props.hopeCount > Math.floor(props.stockAmount / 3) ? (
        <Button
          style={{ width: '100%' }}
          variant="contained"
          color="primary"
          disableElevation
          onClick={() => {}}
        >
          <div className="trade-btn">최종</div>
        </Button>
      ) : (
        <Button
          style={{ width: '100%' }}
          variant="contained"
          color="primary"
          disableElevation
          onClick={() => {
            props.inputID(1);
            console.log('거래완뇨');
          }}
        >
          <div className="trade-btn">최종</div>
        </Button>
      )}
    </div>
  );
};

export default TradeBotton;
