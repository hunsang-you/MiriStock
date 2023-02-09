import Button from '@mui/material/Button';

const CountPlus = () => {
  return (
    <div className="persent-btn">
      <Button
        style={{ width: '24%' }}
        color="red"
        variant="outlined"
        disableElevation
      >
        +5주
      </Button>
      <Button
        style={{ width: '24%' }}
        color="red"
        disableElevation
        variant="outlined"
      >
        +10주
      </Button>
      <Button
        style={{ width: '24%' }}
        color="red"
        disableElevation
        variant="outlined"
      >
        +50주
      </Button>
      <Button
        style={{ width: '24%' }}
        color="red"
        disableElevation
        variant="outlined"
      >
        최대
      </Button>
    </div>
  );
};

export default CountPlus;
