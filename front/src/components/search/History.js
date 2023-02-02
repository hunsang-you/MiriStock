import './css/SearchList.css';
import { useState } from 'react';

const History = () => {
  const [check, Setcheck] = useState([]);

  const datas = [
    { name: '삼성전자', code: '000001' },
    { name: '삼성화재', code: '000002' },
    { name: '삼성카드', code: '000003' },
    { name: '카카오', code: '000004' },
    { name: 'LG전자', code: '000005' },
    { name: 'LG에너지솔루션', code: '000006' },
  ];

  return (
    <div>
      <h3> 최근 조회한 종목</h3>
    </div>
  );
};

export default History;
