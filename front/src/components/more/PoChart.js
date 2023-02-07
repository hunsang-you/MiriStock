import { PieChart, Pie, Cell, Label, Tooltip } from 'recharts';
import RevenueLabel from './RevenueLabel';
import LossLabel from './LossLabel';
import './css/PoChart.css';

const PoChart = () => {
  const data01 = [
    {
      StockName: '삼성전자',
      StockPrice: 55555,
    },
    {
      StockName: '삼성카드',
      StockPrice: 44444,
    },
    {
      StockName: 'SK하이닉스',
      StockPrice: 33333,
    },
    {
      StockName: 'LG전자',
      StockPrice: 22222,
    },
    {
      StockName: '현대차',
      StockPrice: 11111,
    },
  ];
  const data02 = [
    {
      StockName: '카카오뱅크',
      StockPrice: 44444,
    },
    {
      StockName: '룰루',
      StockPrice: 33333,
    },
    {
      StockName: '랄라',
      StockPrice: 22222,
    },
    {
      StockName: '이제그만',
      StockPrice: 18283,
    },
    {
      StockName: '그만',
      StockPrice: 11111,
    },
  ];
  const colors1 = ['#D2143C', '#ef4444', '#f87171', '#fca5a5', '#fecaca'];
  const colors2 = ['#1e40af', '#3b82f6', '#0ea5e9', '#38bdf8', '#bae6fd'];

  return (
    <div className="Chart-page">
      <div className="Chart-Circle">
        <PieChart width={400} height={400}>
          {/* 최고 수익금 */}
          <Pie
            data={data01}
            dataKey="StockPrice"
            nameKey="StockName"
            cx={180}
            cy={180}
            innerRadius={90}
            outerRadius={160}
            fill="#8884d8"
            startAngle={180}
            endAngle={0}
            labelLine={true}
          >
            <Label
              width={30}
              position="centerTop"
              content={
                <RevenueLabel
                  StockName={data01[0].StockName}
                  StockPrice={data01[0].StockPrice}
                />
              }
            ></Label>
            {data01.map((entry, index) => (
              <Cell key={`cell-${index}`} fill={colors1[index]} />
            ))}
          </Pie>

          {/* 최고 손실금 */}
          <Pie
            data={data02}
            dataKey="StockPrice"
            nameKey="StockName"
            cx={180}
            cy={180}
            innerRadius={90}
            outerRadius={160}
            fill="#82ca9d"
            startAngle={0}
            endAngle={-180}
          >
            <Label
              width={30}
              position="center"
              content={
                <LossLabel
                  StockName={data02[0].StockName}
                  StockPrice={data02[0].StockPrice}
                />
              }
            ></Label>
            {data02.map((entry, index) => (
              <Cell key={`cell-${index}`} fill={colors2[index]} />
            ))}
          </Pie>
          <Tooltip />
        </PieChart>
      </div>
      <div className="port-income">
        <div className="high-revenue">
          <span> 최고 수익</span>
        </div>
        <div className="high-loss">
          <span> 최고 손실</span>
        </div>
      </div>
    </div>
  );
};

export default PoChart;
