import './css/Simulation.css';
import Button from '@mui/material/Button';
import { dateStore } from '../../store';
import { simulAPI } from '../../api/api';
import { useEffect, useState } from 'react';
import Loading from '../Loading';
const Simulation = () => {
  const { date, setDate } = dateStore((state) => state);
  const [isLoading, setIsLoading] = useState(false);
  let today = date.memberassetCurrentTime;
  today = String(today);
  let userDate =
    today.slice(0, 4) + '.' + today.slice(4, 6) + '.' + today.slice(6, 8);
  return (
    <div>
      {isLoading === true ? <Loading /> : null}
      <div className="simulation">
        <div className="date">{userDate}</div>
        <div className="simul-btn">
          <Button
            className="si-btn"
            color="primary"
            variant="contained"
            disableElevation
            size="small"
            style={{ color: 'white', width: '5%' }}
            onClick={() => {
              const changeDate = async () => {
                await simulAPI
                  .changeDate(1)
                  .then((request) => {
                    console.log(request.data);
                  })
                  .catch((err) => console.log(err));
                await simulAPI
                  .currentDate()
                  .then((request) => {
                    setDate(request.data);
                  })
                  .catch((err) => console.log(err));
              };
              changeDate();
            }}
          >
            1일
          </Button>
          <Button
            className="si-btn"
            size="small"
            color="primary"
            variant="contained"
            disableElevation
            style={{ color: 'white', width: '5%' }}
            onClick={() => {
              const changeDate = async () => {
                await simulAPI
                  .changeDate(7)
                  .then((request) => {
                    console.log(request.data);
                  })
                  .catch((err) => console.log(err));
                await simulAPI
                  .currentDate()
                  .then((request) => {
                    setDate(request.data);
                  })
                  .catch((err) => console.log(err));
              };
              changeDate();
            }}
          >
            7일
          </Button>
          <Button
            className="si-btn"
            size="small"
            color="primary"
            variant="contained"
            disableElevation
            style={{ color: 'white', width: '5%' }}
            onClick={() => {
              const changeDate = async () => {
                setIsLoading(true);
                await simulAPI
                  .changeDate(30)
                  .then((request) => {
                    console.log(request.data);
                  })
                  .catch((err) => console.log(err));
                await simulAPI
                  .currentDate()
                  .then((request) => {
                    setDate(request.data);
                    setIsLoading(false);
                  })
                  .catch((err) => console.log(err));
              };
              changeDate();
            }}
          >
            30일
          </Button>
        </div>
      </div>
      <hr id="lines" />
    </div>
  );
};

export default Simulation;
