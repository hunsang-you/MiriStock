import { Button } from '@mui/material';

const TradeBotton = (props) => {
  return (
    <div>
      {1 === 1 ? (
        <Button
          style={{ width: '100%' }}
          variant="contained"
          color="primary"
          disableElevation
          onTouchStart={() => {
            props.inputID(1);
          }}
        >
          <div className="trade-btn">입력 완료</div>
        </Button>
      ) : (
        <Button
          style={{ width: '100%' }}
          variant="contained"
          color="primary"
          disableElevation
        >
          <div className="trade-btn">입력 완료</div>
        </Button>
      )}
    </div>
  );
};

export default TradeBotton;

// hopeInputID={hopeInputID}
// setHopeInputID={setHopeInputID}
