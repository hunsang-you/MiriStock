import { Button } from '@mui/material';

const TradeBotton = () => {
  return (
    <div>
      <Button
        style={{ width: '100%' }}
        variant="contained"
        color="primary"
        disableElevation
      >
        <div className="trade-btn">입력 완료</div>
      </Button>
    </div>
  );
};

export default TradeBotton;
