import { useState, useEffect } from 'react';
import ApexCharts from 'apexcharts';
import ReactApexChart from 'react-apexcharts';
import { stockAPI, api } from '../../api/api'; // api 통신

const RealChart = () => {
  // 오늘날짜
  const [toDay, setToDay] = useState([20221213]);
  // 시작날짜
  const [startDay, setStartDay] = useState([toDay]);
  // 오늘날짜 -> [종목명, 종목코드, 등락금액, 등락률]
  const [stockInfo, setStockInfo] = useState([]);
  //
  const [todayPrice, setTodayPrice] = useState([]);
  // 거래량
  const [stockDataAmount, setStockDataAmount] = useState(['']);
  // 보여주는 일자 선택 (디폴트 -> 일주일)
  const [select, setSelect] = useState('one_week');
  // 차트 설정 (수정해야함)
  const [state, setState] = useState({
    series: [
      {
        name: 'stockDataClosingPrice',
        type: 'line',
        data: [],
      },
      {
        name: 'stockDataAmount',
        type: 'column',
        data: [],
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
        categories: [],
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
  });
  // 1w, 1m, 6m, 1y, all 토글 스위치 함수
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
  };

  // axios 통신 (종목코드, 시작 날짜, 끝나는 날짜)
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
  // 날짜데이터를 시간으로 변환하는 함수
  const dayToTime = (date) => {
    let year, month, day, time;
    year = parseInt(date / 10000);
    month = parseInt((date - year * 10000) / 100);
    day = date - year * 10000 - month * 100;
    time = new Date(year, month, day);
    console.log(time.getTime());
    console.log(year, month, day);
    return time.getTime();
  };

  useEffect(() => {
    const getValueData = async (data1, data2, data3) => {
      const newPrice = [];
      const newDataAmount = [];
      const newDate = [];
      const reqData = await api.get(`stockdata/detail`, {
        params: {
          stockCode: data1,
          startDate: data2,
          endDate: data3,
        },
      });
      console.log(reqData.data);
      const today = reqData.data.length - 1;
      setStockInfo([
        reqData.data[today].stockName,
        reqData.data[today].stockCode,
        reqData.data[today].stockDataPriceIncreasement,
        reqData.data[today].stockDataFlucauationRate,
      ]);
      setTodayPrice(reqData.data[today].stockDataClosingPrice);
      for (let i = 0; i <= today; i++) {
        newPrice.push(reqData.data[i].stockDataClosingPrice);
        newDataAmount.push(reqData.data[i].stockDataAmount);
        newDate.push(dayToTime(reqData.data[i].stockDataDate));
      }
    };
    getValueData('005930', 20180101, 20221202);
    dayToTime(20221111);
  }, []);

  return (
    <div id="chart">
      <h2>{stockInfo[0]}</h2>
      <h4>{stockInfo[1]}</h4>
      <h1>{todayPrice}원</h1>
      <h4>
        전일대비 {stockInfo[2]}원{stockInfo[3]}
      </h4>
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
            ymdReturn('005930', 20220330, 20220502);
          }}
        >
          차트데이터추가
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
