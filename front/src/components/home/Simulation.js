import './css/Simulation.css';
import Button from '@mui/material/Button';

const Simulation = () => {
  return (
    <div>
      <div className="simulation">
        <div className="date">2018.01.02</div>
        <Button color="primary" variant="contained" style={{ color: 'white' }}>
          시뮬레이션버튼공간
        </Button>
      </div>
      <hr id="lines" />
    </div>
  );
};

export default Simulation;
