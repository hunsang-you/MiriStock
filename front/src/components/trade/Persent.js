import Button from '@mui/material/Button';

const Persent = (props) => {
  return (
    <div className="persent-btn">
      <Button
        style={{ width: '32%' }}
        color="blue"
        variant="outlined"
        disableElevation
        onClick={() => {
          props.setHopePrice(Math.floor(props.hopePrice * 0.99));
        }}
      >
        -1.0%
      </Button>
      <Button
        style={{ width: '32%' }}
        color="primary"
        disableElevation
        variant="outlined"
        onClick={() => {
          props.setHopePrice(props.stockPrice);
        }}
      >
        현재가
      </Button>
      <Button
        style={{ width: '32%' }}
        color="red"
        disableElevation
        variant="outlined"
        onClick={() => {
          props.setHopePrice(Math.floor(props.hopePrice * 1.01));
        }}
      >
        +1.0%
      </Button>
    </div>
  );
};

export default Persent;
