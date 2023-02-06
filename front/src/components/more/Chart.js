import ReactApexChart from 'react-apexcharts';
import './css/Chart.css';

const PortChart = () => {
  // 수익률이 가장 높은 top5
  const up_stock1 = 9999;
  const up_stock2 = 6666;
  const up_stock3 = 3000;
  const up_stock4 = 2000;
  const up_stock5 = 1000;
  // const up_stockname = [
  //   '삼성전자',
  //   '삼성카드',
  //   'LG에너지솔루션',
  //   '룰루',
  //   '랄라',
  // ];

  // 손실률이 가장 낮은 top5
  const down_stock1 = 66666666;
  const down_stock2 = 55555555;
  const down_stock3 = 33333333;
  const down_stock4 = 22222222;
  const down_stock5 = 11111111;
  // const down_stockname = ['카카오', '2등~', '3등~', '4등~', '5등~'];

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
          bottom: -160,
        },
      },
      colors: ['#D2143C', '#ef4444', '#f87171', '#fca5a5', '#fecaca'],
      labels: ['삼성전자', '삼성카드', 'LG에너지솔루션', '룰루', '랄라'],
      plotOptions: {
        pie: {
          startAngle: -90,
          endAngle: 90,
          offsetY: 10,
          // customScale: 0.6,
          donut: {
            size: '60%',
            labels: {
              show: true,
              name: {
                show: true,
                fontSize: '22px',
                fontWeight: 600,
                offsetY: -50,
              },
              total: {
                showAlways: true,
                show: true,
                label: '최고 수익금',
                fontSize: '16px',
                fontWeight: 600,
                color: '#D2143C ',
                formatter: function (val) {
                  let best = '삼성전자 ';
                  let worth = `${up_stock1}`;
                  return best + worth + ' 원';
                },
              },
              value: {
                offsetY: -40,
                fontSize: '16px',
                show: true,
                fontWeight: 600,
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
          // customScale: 0.6,
          donut: {
            labels: {
              show: true,
              name: {
                show: true,
                fontSize: '22px',
                fontWeight: 600,
                offsetY: -50,
              },
              total: {
                showAlways: true,
                show: true,
                label: '최고 손실금',
                fontSize: '16px',
                fontWeight: 600,
                color: '#1E90FF',
                formatter: function (val) {
                  let worst = '카카오 ';
                  let worth = `${down_stock1}`;
                  return worst + worth + '원';
                },
              },
              value: {
                offsetY: -40,
                fontSize: '16px',
                show: true,
                fontWeight: 600,
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
