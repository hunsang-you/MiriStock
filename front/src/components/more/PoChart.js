import RevenueLabel from './RevenueLabel';
import LossLabel from './LossLabel';
import { PieChart, Pie, Cell, Label } from 'recharts';
import { useState } from 'react';
import './css/PoChart.css';

const PoChart = (props) => {
  const colors1 = ['#D2143C', '#ef4444', '#f87171', '#fca5a5', '#fecaca'];
  const colors2 = ['#1e40af', '#3b82f6', '#0ea5e9', '#38bdf8', '#bae6fd'];

  const portfol = props.portfol;
  // 수익금, 손실금이 큰 순서대로
  const [state, setState] = useState({
    dataKey: 'memberStockEarnPrice',
    nameKey: 'stockName',
    cx: '50%',
    cy: '50%',
    innerRadius: 120,
    outerRadius: 160,
    fill: '#8884d8',
    startAngle: [180, 0],
    endAngle: [0, -180],
    paddingAngle: 1,
  });

  // 차트 라벨 클릭시 변화
  const [labelState, setLabelState] = useState([0, 0]);

  if (!portfol) {
    return <div> 데이터를 불러오지 못했습니다</div>;
  }

  return (
    <div className="Chart-page">
      <div className="Chart-Circle">
        <div className="high-revenue">
          <span> 수익 Top 3</span>
        </div>
        <PieChart width={320} height={360}>
          {/* 최고 수익금 */}
          <Pie
            data={portfol.highMemberStock}
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
                  StockName={portfol.highMemberStock[labelState[0]].stockName}
                  Rate={
                    portfol.highMemberStock[labelState[0]].memberStockEarnRate
                  }
                />
              }
            />

            {/* Cell 색깔 변화 + 클릭시 종목,수익률 변화*/}
            {portfol.highMemberStock.map((entry, index) => (
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
            data={portfol.lowMemberStock}
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
            {/* Cell 클릭시 종목-손실률 출력 */}
            <Label
              content={
                <LossLabel
                  StockName={portfol.lowMemberStock[labelState[1]].stockName}
                  Rate={
                    portfol.lowMemberStock[labelState[1]].memberStockEarnRate
                  }
                />
              }
            />
            {/* Cell 색깔 변화 + 클릭시 종목,수익 변화*/}
            {portfol.lowMemberStock.map((entry, index) => (
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

        <div className="high-loss">
          <span> 손실 Top 3</span>
        </div>
      </div>
    </div>
  );
};

export default PoChart;
