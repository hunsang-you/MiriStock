import ReactApexChart from 'react-apexcharts';

const PortChart = () => {
  // 수익률이 가장 높은 top5
  const up_stock1 = 50;
  const up_stock2 = 40;
  const up_stock3 = 30;
  const up_stock4 = 20;
  const up_stock5 = 10;
  const up_stockname = [
    '삼성전자',
    '삼성카드',
    'LG에너지솔루션',
    '룰루',
    '랄라',
  ];

  // 손실률이 가장 낮은 top5
  const down_stock1 = 30;
  const down_stock2 = 20;
  const down_stock3 = 15;
  const down_stock4 = 10;
  const down_stock5 = 5;
  const down_stockname = ['카카오', '2등~', '3등~', '4등~', '5등~'];

  const donutData1 = {
    series: [up_stock1, up_stock2, up_stock3, up_stock4, up_stock5],
    options: {
      chart: {
        type: 'donut',
      },
      legend: {
        position: 'top',
        show: false,
      },
      responsive: [
        {
          breakpoint: 480,
          options: {},
        },
      ],
      grid: {
        padding: {
          bottom: -120,
        },
      },
      colors: ['#D2143C', '#ef4444', '#f87171', '#fca5a5', '#fecaca'],
      labels: ['삼성전자', '삼성카드', 'LG에너지솔루션', '룰루', '랄라'],
      plotOptions: {
        pie: {
          startAngle: -90,
          endAngle: 90,
          offsetY: 10,
          donut: {
            labels: {
              show: true,
              name: {
                show: true,
                fontSize: '22px',
                fontWeight: 600,
                offsetY: -40,
              },
              total: {
                showAlways: true,
                show: true,
                label: '최고 수익률',
                fontSize: '16px',
              },
              value: {
                offsetY: -30,
                fontSize: '16px',
                show: true,
              },
            },
          },
        },
      },
    },
  };
  const donutData2 = {
    series: [down_stock1, down_stock2, down_stock3, down_stock4, down_stock5],
    options: {
      chart: {
        type: 'donut',
      },
      legend: {
        position: 'top',
        show: false,
      },
      responsive: [
        {
          breakpoint: 480,
          options: {},
        },
      ],
      grid: {
        padding: {
          bottom: -120,
        },
      },
      colors: ['#1e40af', '#3b82f6', '#0ea5e9', '#38bdf8', '#bae6fd'],
      labels: ['카카오', '2등~', '3등~', '4등~', '5등~'],
      plotOptions: {
        pie: {
          startAngle: -90,
          endAngle: 90,
          offsetY: 10,
          donut: {
            labels: {
              show: true,
              name: {
                show: true,
                fontSize: '22px',
                fontWeight: 600,
                offsetY: -40,
              },
              total: {
                showAlways: true,
                show: true,
                label: '최저 수익률',
                fontSize: '16px',
              },
              value: {
                offsetY: -30,
                fontSize: '16px',
                show: true,
              },
            },
          },
        },
      },
    },
  };

  return (
    <div className="port-chart">
      <div className="portfolio-nickname">주식고수김싸피// 님의 결과</div>
      <div className="portfolio-chart1">
        <ReactApexChart
          type="donut"
          series={donutData1.series}
          options={donutData1.options}
        />
      </div>
      <div className="portfolio-chart2">
        <ReactApexChart
          type="donut"
          series={donutData2.series}
          options={donutData2.options}
        />
      </div>
    </div>
  );
};

export default PortChart;
