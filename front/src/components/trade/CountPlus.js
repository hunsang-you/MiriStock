import Button from '@mui/material/Button';

const CountPlus = (props) => {
  return (
    <div className="persent-btn">
      <Button
        style={{ width: '24%' }}
        color="red"
        variant="outlined"
        disableElevation
        onClick={() => {
          props.setHopeCount(props.hopeCount + 5);
        }}
      >
        +5주
      </Button>
      <Button
        style={{ width: '24%' }}
        color="red"
        disableElevation
        variant="outlined"
        onClick={() => {
          props.setHopeCount(props.hopeCount + 10);
        }}
      >
        +10주
      </Button>
      <Button
        style={{ width: '24%' }}
        color="red"
        disableElevation
        variant="outlined"
        onClick={() => {
          props.setHopeCount(props.hopeCount + 50);
        }}
      >
        +50주
      </Button>
      <Button
        style={{ width: '24%' }}
        color="red"
        disableElevation
        variant="outlined"
        onClick={() => {
          props.setHopeCount(props.maxCount);
        }}
      >
        최대
      </Button>
    </div>
  );
};

export default CountPlus;
