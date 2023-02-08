import Button from '@mui/material/Button';

const Persent = () => {
  return (
    <div className="persent-btn">
      <Button
        style={{ width: '32%' }}
        color="primary"
        variant="outlined"
        disableElevation
      >
        -1.0%
      </Button>
      <Button
        style={{ width: '32%' }}
        color="primary"
        disableElevation
        variant="outlined"
      >
        현재가
      </Button>
      <Button
        style={{ width: '32%' }}
        color="primary"
        disableElevation
        variant="outlined"
      >
        +1.0%
      </Button>
    </div>
  );
};

export default Persent;
