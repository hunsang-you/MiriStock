import './css/EquitiesValue.css';
import { useState } from 'react';

const EquitiesValue = () => {
  let [isUpDown, setIsUpDown] = useState(true);
  let [data, setData] = useState([
    { name: '삼성전자', price: '65,304', give: 400, change: '400원 (+1.66%)' },
    { name: '상현전자', price: '65,304', give: 400, change: '400원 (+1.66%)' },
    { name: '도겸전자', price: '65,304', give: 400, change: '400원 (+1.66%)' },
    { name: '헌상전자', price: '65,304', give: 400, change: '400원 (+1.66%)' },
    { name: '재윤전자', price: '65,304', give: 400, change: '400원 (+1.66%)' },
  ]);
  return (
    <div className="equities-container">
      <div className="evaluation">평가금액순</div>
      {data.map((dat, i) => {
        return (
          <div className="stock" key={i}>
            <div className="stock-top">
              <span>{dat.name}</span>
              <span>{dat.price}</span>
            </div>
            <div className="stock-bottom">
              <span>{dat.give}주</span>
              <span
                style={isUpDown ? { color: '#D2143C' } : { color: '#1E90FF' }}
              >
                ▲ {dat.change}
              </span>
            </div>
          </div>
        );
      })}

      <hr id="lines" />
    </div>
  );
};

export default EquitiesValue;
