import './css/trade.css';
import { useState } from 'react';

const Keypad = (props) => {
  const [classOnOff, setClassOnOff] = useState([
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
  ]);

  const riceCalculation = (i) => {
    props.setHopePrice(props.hopePrice * 10 + i);
  };

  const deleteCalculation = (i) => {
    if (i === 9) {
      props.setHopePrice(0);
    } else if (i === 11) {
      const num = Math.floor(props.hopePrice / 10);
      props.setHopePrice(num);
    }
  };

  const clickOn = (i) => {
    const copy = [...classOnOff];
    copy[i] = !copy[i];
    if (i === 9) {
      deleteCalculation(9);
    } else if (i === 11) {
      deleteCalculation(11);
    } else if (i === 10) {
      riceCalculation(0);
    } else {
      riceCalculation(i + 1);
    }
    setClassOnOff(copy);
  };
  const clickOut = (i) => {
    const copy = [...classOnOff];
    copy[i] = !copy[i];
    setClassOnOff(copy);
  };
  return (
    <div className="keypad-layout">
      <div>
        <div
          className={classOnOff[0] ? 'click-num' : null}
          onTouchStart={() => {
            clickOn(0);
          }}
          onTouchEnd={() => {
            clickOut(0);
          }}
        >
          1
        </div>
      </div>
      <div>
        <div
          className={classOnOff[1] ? 'click-num' : null}
          onTouchStart={() => {
            clickOn(1);
          }}
          onTouchEnd={() => {
            clickOut(1);
          }}
        >
          2
        </div>
      </div>
      <div>
        <div
          className={classOnOff[2] ? 'click-num' : null}
          onTouchStart={() => {
            clickOn(2);
          }}
          onTouchEnd={() => {
            clickOut(2);
          }}
        >
          3
        </div>
      </div>
      <div>
        <div
          className={classOnOff[3] ? 'click-num' : null}
          onTouchStart={() => {
            clickOn(3);
          }}
          onTouchEnd={() => {
            clickOut(3);
          }}
        >
          4
        </div>
      </div>
      <div>
        <div
          className={classOnOff[4] ? 'click-num' : null}
          onTouchStart={() => {
            clickOn(4);
          }}
          onTouchEnd={() => {
            clickOut(4);
          }}
        >
          5
        </div>
      </div>
      <div>
        <div
          className={classOnOff[5] ? 'click-num' : null}
          onTouchStart={() => {
            clickOn(5);
          }}
          onTouchEnd={() => {
            clickOut(5);
          }}
        >
          6
        </div>
      </div>
      <div>
        <div
          className={classOnOff[6] ? 'click-num' : null}
          onTouchStart={() => {
            clickOn(6);
          }}
          onTouchEnd={() => {
            clickOut(6);
          }}
        >
          7
        </div>
      </div>
      <div>
        <div
          className={classOnOff[7] ? 'click-num' : null}
          onTouchStart={() => {
            clickOn(7);
          }}
          onTouchEnd={() => {
            clickOut(7);
          }}
        >
          8
        </div>
      </div>
      <div>
        <div
          className={classOnOff[8] ? 'click-num' : null}
          onTouchStart={() => {
            clickOn(8);
          }}
          onTouchEnd={() => {
            clickOut(8);
          }}
        >
          9
        </div>
      </div>
      <div>
        <div
          style={{ fontSize: '16px', verticalAlign: 'bottom' }}
          className={classOnOff[9] ? 'click-num' : null}
          onTouchStart={() => {
            clickOn(9);
          }}
          onTouchEnd={() => {
            clickOut(9);
          }}
        >
          전체삭제
        </div>
      </div>
      <div>
        <div
          className={classOnOff[10] ? 'click-num' : null}
          onTouchStart={() => {
            clickOn(10);
          }}
          onTouchEnd={() => {
            clickOut(10);
          }}
        >
          0
        </div>
      </div>
      <div>
        <div
          style={{ fontSize: '24px' }}
          className={classOnOff[11] ? 'click-num' : null}
          onTouchStart={() => {
            clickOn(11);
          }}
          onTouchEnd={() => {
            clickOut(11);
          }}
        >
          del
        </div>
      </div>
    </div>
  );
};

export default Keypad;
