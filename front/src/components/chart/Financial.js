import { useState, useEffect } from 'react';
import ApexCharts from 'apexcharts';
import ReactApexChart from 'react-apexcharts';
import FinancialData from './FinancialData';

const Financial = (props) => {
  // 현재 날짜 = props.toDay
  // 날짜에서 연도만 추출
  const dayToYear = (date) => {
    let year;
    year = parseInt(date / 10000);
    return year;
  };
  // 현재 날짜 기준 연도 정해주는 인덱스
  const [idx, setIdx] = useState(3);
  // 해당년도
  const [financialYear, setFinancialYear] = useState([]);
  // 매출액
  const [salesRevenue, setSalesRevenue] = useState([]);
  // 영업이익
  const [operatingProfit, setOperatingProfit] = useState([]);
  // 순이익
  const [newIncome, setNewIncome] = useState([]);
  // 재무제표 차트 설정
  const [financialState, setFinancialState] = useState({
    series: [
      {
        name: '매출액',
        data: [],
      },
      {
        name: '영업이익',
        data: [],
      },
      {
        name: '순이익',
        data: [],
      },
    ],
    options: {
      chart: {
        type: 'bar',
        // width: '100%',
      },
      colors: ['#F2A3B3', '#94CAEF', '#9FD9D9'],
      plotOptions: {
        bar: {
          horizontal: false,
          columnWidth: '55%',
          endingShape: 'rounded',
        },
      },
      dataLabels: {
        enabled: false,
      },
      stroke: {
        show: true,
        width: 0,
        colors: ['transparent'],
      },
      xaxis: {
        categories: [],
        labels: {
          formatter: function (val) {
            return val;
          },
        },
      },
      yaxis: {
        show: false, // y 축 보여줄지 안보여줄지
        title: {
          text: '$ (thousands)',
        },
      },
      fill: {
        opacity: 1,
      },
      tooltip: {
        y: {
          formatter: function (val) {
            return '$ ' + val + ' thousands';
          },
        },
      },
    },
  });

  useEffect(() => {
    let year = dayToYear(props.toDay);
    if (year === 2018) {
      setIdx(3);
    } else if (year === 2019) {
      setIdx(4);
    } else if (year === 2020) {
      setIdx(5);
    } else if (year >= 2021) {
      setIdx(6);
    }
  }, [props.toDay]);
  return (
    <div id="chart">
      <ReactApexChart
        options={financialState.options}
        series={financialState.series}
        type="bar"
        height={250}
        width={'100%'}
      />
      <FinancialData
        idx={idx}
        setFinancialState={setFinancialState}
        financialYear={financialYear}
        setFinancialYear={setFinancialYear}
        salesRevenue={salesRevenue}
        setSalesRevenue={setSalesRevenue}
        operatingProfit={operatingProfit}
        setOperatingProfit={setOperatingProfit}
        newIncome={newIncome}
        setNewIncome={setNewIncome}
      />
    </div>
  );
};

export default Financial;
