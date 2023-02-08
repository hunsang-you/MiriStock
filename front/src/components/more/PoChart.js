import RevenueLabel from './RevenueLabel';
import LossLabel from './LossLabel';
import { PieChart, Pie, Cell, Label } from 'recharts';
import { useState } from 'react';
import './css/PoChart.css';

const PoChart = () => {
  const colors1 = ['#D2143C', '#ef4444', '#f87171', '#fca5a5', '#fecaca'];
  const colors2 = ['#1e40af', '#3b82f6', '#0ea5e9', '#38bdf8', '#bae6fd'];

  const [state, setState] = useState({
    RevenueData: [
      {
        StockName: '삼성전자',
        StockPrice: 555555,
        CellNo: 0,
      },
      {
        StockName: '삼성카드',
        StockPrice: 44444,
        CellNo: 1,
      },
      {
        StockName: 'SK하이닉스',
        StockPrice: 33333,
        CellNo: 2,
      },
      {
        StockName: 'LG전자',
        StockPrice: 22222,
        CellNo: 3,
      },
      {
        StockName: '현대차',
        StockPrice: 11111,
        CellNo: 4,
      },
    ],
    LossData: [
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
    ],
    dataKey: 'StockPrice',
    nameKey: 'StockName',
    cx: '50%',
    cy: '50%',
    innerRadius: 120,
    outerRadius: 160,
    fill: '#8884d8',
    startAngle: [180, 0],
    endAngle: [0, -180],
    paddingAngle: 1,
  });

  const [labelState, setLabelState] = useState([0, 0]);

  return (
    <div className="Chart-page">
      <div className="Chart-Circle">
        <div className="port-income">
          <div className="high-revenue">
            <span> 수익 Top 5</span>
          </div>
        </div>
        <PieChart width={320} height={360}>
          {/* 최고 수익금 */}
          <Pie
            data={state.RevenueData}
            dataKey={state.dataKey}
            nameKey={state.nameKey}
            cx={state.cx}
            cy={state.cy}
            innerRadius={state.innerRadius}
            outerRadius={state.outerRadius}
            fill={state.fill}
            startAngle={state.startAngle[0]}
            endAngle={state.endAngle[0]}
            paddingAngle={state.paddingAngle}
          >
            {/* Cell 클릭시 종목-수익 출력 */}
            <Label
              content={
                <RevenueLabel
                  StockName={state.RevenueData[labelState[0]].StockName}
                  StockPrice={state.RevenueData[labelState[0]].StockPrice}
                />
              }
            />

            {/* Cell 색깔 변화 */}
            {state.RevenueData.map((entry, index) => (
              <Cell
                key={`cell-${index}`}
                fill={colors1[index]}
                onClick={(e) => {
                  setLabelState([index, labelState[1]]);
                }}
              />
            ))}
          </Pie>

          {/* 최고 손실금 */}
          <Pie
            data={state.LossData}
            dataKey={state.dataKey}
            nameKey={state.nameKey}
            cx={state.cx}
            cy={state.cy}
            innerRadius={state.innerRadius}
            outerRadius={state.outerRadius}
            fill={state.fill}
            startAngle={state.startAngle[1]}
            endAngle={state.endAngle[1]}
            paddingAngle={state.paddingAngle}
          >
            {/* Cell 클릭시 종목-손해 출력 */}
            <Label
              content={
                <LossLabel
                  StockName={state.LossData[labelState[1]].StockName}
                  StockPrice={state.LossData[labelState[1]].StockPrice}
                />
              }
            />
            {/* Cell 색깔 변화 */}
            {state.LossData.map((entry, index) => (
              <Cell
                key={`cell-${index}`}
                fill={colors2[index]}
                onClick={(e) => {
                  setLabelState([labelState[0], index]);
                }}
              />
            ))}
          </Pie>
        </PieChart>
        <div className="port-income">
          <div className="high-loss">
            <span> 손실 Top 5</span>
          </div>
        </div>
      </div>
    </div>
  );
};

export default PoChart;
