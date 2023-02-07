import './css/Simulation.css';
import Button from '@mui/material/Button';
import { userStore } from '../../store';

const Simulation = () => {
  const { user } = userStore((state) => state);
  let today = user.memberassetCurrentTime;
  today = String(today);
  let userDate =
    today.slice(0, 4) + '.' + today.slice(4, 6) + '.' + today.slice(6, 8);
  return (
    <div>
      <div className="simulation">
        <div className="date">{userDate}</div>
        <Button color="primary" variant="contained" style={{ color: 'white' }}>
          시뮬레이션버튼공간
        </Button>
      </div>
      <hr id="lines" />
    </div>
  );
};

export default Simulation;
