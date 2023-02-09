import { Hidden } from '@mui/material';

const HopePrice = (props) => {
  return (
    <div>
      {props.hopeInputID === 0 ? (
        <div
          className={props.hopeInputID !== 1 ? 'hope-body' : 'disabled-body'}
        >
          <div
            className={
              props.hopeInputID !== 1 ? 'hope-title' : 'disabled-title'
            }
          >
            1주당 구매 희망가
          </div>
          <div>
            <div className="hope-input">
              <div className="hope-money">{props.hopePrice}</div>
              <div>원</div>
            </div>
          </div>
        </div>
      ) : (
        <div
          className={props.hopeInputID !== 1 ? 'hope-body' : 'disabled-body'}
        >
          <div
            className={
              props.hopeInputID !== 1 ? 'hope-title' : 'disabled-title'
            }
          >
            1주당 구매 희망가
          </div>
          <div>
            <div
              className="hope-input"
              onClick={() => {
                props.inputID(2);
              }}
            >
              <div className="hope-money">{props.hopePrice}</div>
              <div>원</div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default HopePrice;
