import { useState, useEffect } from 'react';
import ApexCharts from 'apexcharts';
import ReactApexChart from 'react-apexcharts';
import { stockAPI } from '../../api/api'; // api 통신

const RealChart = () => {
  const [toDay, setToDay] = useState([20221213]);
  const [startDay, setStartDay] = useState([toDay]);
  const [stockName, setStockName] = useState([]);
  const [stockDataAmount, setStockDataAmount] = useState([]);
  const [stockDataFlucauationRate, setStockDataFlucauationRate] = useState([]);
  const [stockDataPriceIncreasement, setStockDataPriceIncreasement] = useState(
    [],
  );
  const [select, setSelect] = useState('one_year');
  const state = {
    series: [
      {
        name: 'Revenue',
        type: 'line',
        data: [20, 29, 37, 36, 44, 45, 50, 58],
      },
      {
        name: 'Income',
        type: 'column',
        data: [1.4, 2, 2.5, 1.5, 2.5, 2.8, 3.8, 4],
      },
    ],
    options: {
      chart: {
        height: 500,
        type: 'line',
        stacked: false,
        zoom: {
          enabled: false,
          autoScaleYaxis: true,
        },
      },
      colors: ['#6DCEF5', '#FF0000'],
      dataLabels: {
        enabled: false,
      },
      stroke: {
        curve: 'smooth',
        width: [5, 1],
      },
      // title: {
      //   text: 'XYZ - Stock Analysis (2009 - 2016)',
      //   align: 'left',
      //   offsetX: 10,
      // }, // 표 상단에 이름 없어도 될듯
      tooltip: {
        // x: {
        //   format: 'dd MMM yyyy',
        // }, x 축 표시 형식
        enabled: true,
      },
      xaxis: {
        categories: [2009, 2010, 2011, 2012, 2013, 2014, 2015, 2016],
        //   type: 'datetime',
        //   min: new Date('01 Feb 2017').getTime(), // 랜더링시 시작하는 부분 옵션인거같음 이부분을 스위칭에 맞게 조절하는 함수 만들어서 작성 해야함
        //   tickAmount: 6, // 표시 틱 간격
      }, // x 축
      yaxis: [
        {
          show: false, // y 축 보여줄지 안보여줄지
          seriesName: 'Revenue',
          axisTicks: {
            show: true,
          },
          axisBorder: {
            show: true,
            color: '#FEB019',
          },
          labels: {
            style: {
              colors: '#FEB019',
            },
          },
          title: {
            text: 'Revenue (thousand crores)',
            style: {
              color: '#FEB019',
            },
          },
        },
        {
          show: false, // y 축 보여줄지 안보여줄지
          opposite: true, // 오른쪽으로 보내는 옵션
          axisTicks: {
            show: true,
          },
          max: 30,
          axisBorder: {
            show: true,
            color: '#008FFB',
          },
          labels: {
            style: {
              colors: '#008FFB',
            },
          },
          title: {
            text: 'Income (thousand crores)',
            style: {
              color: '#008FFB',
            },
          },
        },
      ],
      tooltip: {
        fixed: {
          enabled: true,
          position: 'topLeft', // topRight, topLeft, bottomRight, bottomLeft
          offsetY: 30,
          offsetX: 60,
        },
      },
      legend: {
        horizontalAlign: 'left',
        offsetX: 40,
      },
    },
  };

  const updateData = (timeline) => {
    setSelect({
      selection: timeline,
    });

    switch (timeline) {
      case 'one_week':
        ApexCharts.exec(
          'area-datetime',
          'zoomX',
          new Date(2022, 9, 4).getTime(),
          new Date(2022, 9, 11).getTime(),
        );
        break;
      case 'one_month':
        ApexCharts.exec(
          'area-datetime',
          'zoomX',
          new Date(2022, 8, 11).getTime(),
          new Date(2022, 9, 11).getTime(),
        );
        break;
      case 'six_months':
        ApexCharts.exec(
          'area-datetime',
          'zoomX',
          new Date(2022, 3, 11).getTime(),
          new Date(2022, 9, 11).getTime(),
        );
        break;
      case 'one_year':
        ApexCharts.exec(
          'area-datetime',
          'zoomX',
          new Date(2021, 9, 11).getTime(),
          new Date(2022, 9, 11).getTime(),
        );
        break;
      case 'all':
        ApexCharts.exec(
          'area-datetime',
          'zoomX',
          new Date(2017, 1, 1).getTime(),
          new Date(2022, 12, 30).getTime(),
        );
        break;
      default:
    }
  }; // 1w, 1m, 6m, 1y, all 토글 스위치 함수

  const ymdReturn = (stockcode, before, now) => {
    stockAPI
      .stockDetail(stockcode, before, now)
      .then((request) => {
        console.log(request.data);
        return request.data;
      })
      .catch((err) => {
        console.log(err);
      });
  };

  // useEffect(() => {
  //   let todayday = ymdReturn('005930', toDay, toDay);
  //   setStockName(todayday.stockName);
  //   setStockDataAmount(todayday.stockDataAmount);
  //   setStockDataFlucauationRate(todayday.stockDataFlucauationRate);
  //   setStockDataPriceIncreasement(todayday.stockDataPriceIncreasement);
  // }, []);

  return (
    <div id="chart">
      <ReactApexChart
        options={state.options}
        series={state.series}
        type="line"
        height={350}
      />
      <div>
        <button
          id="one_week"
          onClick={() => updateData('one_week')}
          className={state.selection === 'one_week' ? 'active' : ''}
        >
          1W
        </button>
        &nbsp;
        <button
          id="one_month"
          onClick={() => updateData('one_month')}
          className={state.selection === 'one_month' ? 'active' : ''}
        >
          1M
        </button>
        &nbsp;
        <button
          id="six_months"
          onClick={() => updateData('six_months')}
          className={state.selection === 'six_months' ? 'active' : ''}
        >
          6M
        </button>
        &nbsp;
        <button
          id="one_year"
          onClick={() => updateData('one_year')}
          className={state.selection === 'one_year' ? 'active' : ''}
        >
          1Y
        </button>
        &nbsp;
        <button
          id="all"
          onClick={() => updateData('all')}
          className={state.selection === 'all' ? 'active' : ''}
        >
          ALL
        </button>
      </div>
      <div>
        <button
          onClick={() => {
            stockAPI
              .stockDetail('005930', 20220330, 20220502)
              .then((request) => {
                console.log(request.data);
              })
              .catch((err) => {
                console.log(err);
              });
          }}
        >
          stockdetail
        </button>
        <button
          onClick={() => {
            console.log(ymdReturn('005930', 20220330, 20220502));
          }}
        >
          asdasd
        </button>
      </div>
    </div>
  );
};

export default RealChart;

// {
//   stockCode: '005930';   // 종목 코드
//   stockDataAmount: 12670187;  // 거래량
//   stockDataClosingPrice: 69900;    // 종가
//   stockDataDate: 20220330;   // 날짜
//   stockDataFlucauationRate: -0.42735;    // 전일 등락률
//   stockDataPriceIncreasement: -300;      // 전일 떨어진 금액
//   stockName: '삼성전자';     // 종목 명
// }
