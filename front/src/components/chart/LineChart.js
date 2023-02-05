import { useState, useEffect } from 'react';
import ApexCharts from 'apexcharts';
import ReactApexChart from 'react-apexcharts';
import { stockAPI, api } from '../../api/api'; // api 통신
import LineChartData from './LineChartData';
import ShowDate from './ShowDate';

// {
//   stockCode: '005930';   // 종목 코드
//   stockDataAmount: 12670187;  // 거래량
//   stockDataClosingPrice: 69900;    // 종가
//   stockDataDate: 20220330;   // 날짜
//   stockDataFlucauationRate: -0.42735;    // 전일 등락률
//   stockDataPriceIncreasement: -300;      // 전일 떨어진 금액
//   stockName: '삼성전자';     // 종목 명
// }

const LineChart = () => {
  // 오늘날짜
  const totoday = 20180401;
  const [toDay, setToDay] = useState(20200401);
  // 날짜데이터를 시간으로 변환하는 함수
  const dayToTime = (date) => {
    let year, month, day, time;
    year = parseInt(date / 10000);
    // console.log(year);
    month = parseInt((date - year * 10000) / 100) - 1;
    // console.log(month);
    day = date - year * 10000 - (month + 1) * 100;
    // console.log(day);
    time = new Date(year, month, day);
    // console.log(time.getTime());
    // console.log(year, month, day);
    return time.getTime();
  };
  // 배열 인덱스
  const [index, setIndex] = useState();
  // 오늘날짜 -> 종목명, 종목코드
  const [stockInfo, setStockInfo] = useState({ stockName: '', stockCode: '' });
  // 18.01 ~ 오늘 까지 금액
  const [stockPrice, setStockPrice] = useState([]);
  // 18.01 ~ 거래량
  const [dataAmount, setDataAmount] = useState([]);
  // 18.01 ~ 날짜
  const [totalDate, setTotalDate] = useState([]);
  // 18.01 ~ 등락금액
  const [priceIncreasement, setStockPriceIncreasement] = useState([]);
  // 18.01 ~ 등락률
  const [dataFlucauationRate, setDataFlucauationRate] = useState([]);
  // 차트 설정
  const [state, setState] = useState({
    series: [
      {
        name: '주가', //stockDataClosingPrice
        type: 'line',
        data: [],
      },
      {
        name: '거래량', // stockDataAmount
        type: 'column',
        data: [],
      },
    ],
    options: {
      chart: {
        height: 500,
        width: '100%',
        // parentHeightOffset: 100, // 차트 아래 마진값
        stacked: false,
        toolbar: {
          show: false,
        }, // 햄버거 메뉴 지우는거 사진저장옵션인데 필요x
        type: ['line', 'bar'],
        zoom: {
          enabled: false,
          autoScaleYaxis: true,
        }, // y 축이 두개 이상일때는 작동 안하지만 혹시 몰라 적용
      },
      colors: ['#6DCEF5', '#FF0000'],
      dataLabels: {
        enabled: false,
      },
      legend: {
        show: false,
      },
      stroke: {
        show: true,
        curve: 'smooth',
        width: [2, 1.2],
      }, // 선
      // title: {}, // 표 상단에 이름 없어도 될듯
      tooltip: {
        enabled: true,
        followCursor: true,
        x: {
          show: false,
          format: 'yyyy년 M월 dd일',
        }, // x 축 표시 형식
        y: [
          {
            formatter: function (value, { dataPointIndex }) {
              // console.log(dataPointIndex);   // 인덱스 값 출력 확인 (value 를 안넣으면 dataPointIndex 가 value 가 됨)
              setIndex(dataPointIndex);
              return null;
            }, // dataPointIndex = 인덱스값
            title: { formatter: () => null },
          },
          {
            formatter: function (value) {
              return null;
            },
            title: { formatter: () => null },
          },
        ],
        marker: {
          show: false,
        },
      },
      xaxis: {
        type: 'datetime',
        categories: [],
        tickAmount: 1, // 표시 틱 간격
        tickPlacement: 'on',
        min: new Date('01 Jan 2018').getTime(), // 랜더링시 시작하는 부분 옵션인거같음 이부분을 스위칭에 맞게 조절하는 함수 만들어서 작성 해야함
        tooltip: {
          enabled: false,
        },
      }, // x 축
      yaxis: [
        {
          show: false, // y 축 보여줄지 안보여줄지
          labels: {
            style: {
              // colors: '#FEB019',
            },
          },
          axisBorder: {
            show: false,
            // color: '#FEB019',
          },
          axisTicks: {
            show: true,
          },
        },
        {
          show: false, // y 축 보여줄지 안보여줄지
          opposite: true, // 오른쪽으로 보내는 옵션
          max: 200000000,
          labels: {
            style: {
              // colors: '#008FFB',
            },
          },
          axisBorder: {
            show: true,
            // color: '#008FFB',
          },
          axisTicks: {
            show: true,
          },
        },
      ],
    },
  }); // ==================================================> setState 종료
  // 1w, 1m, 6m, 1y, all 토글 스위치 함수
  // const updateData = (timeline) => {
  //   setSelect({
  //     selection: timeline,
  //   });

  //   switch (timeline) {
  //     case 'one_week':
  //       ApexCharts.exec(
  //         'area-datetime',
  //         'zoomX',
  //         new Date(2022, 9, 4).getTime(),
  //         new Date(dayToTime(toDay)).getTime(),
  //       );
  //       break;
  //     case 'one_month':
  //       ApexCharts.exec(
  //         'area-datetime',
  //         'zoomX',
  //         new Date(2022, 8, 11).getTime(),
  //         new Date(dayToTime(toDay)).getTime(),
  //       );
  //       break;
  //     case 'six_months':
  //       ApexCharts.exec(
  //         'area-datetime',
  //         'zoomX',
  //         new Date(2022, 3, 11).getTime(),
  //         new Date(dayToTime(toDay)).getTime(),
  //       );
  //       break;
  //     case 'one_year':
  //       ApexCharts.exec(
  //         'area-datetime',
  //         'zoomX',
  //         new Date(2021, 9, 11).getTime(),
  //         new Date(dayToTime(toDay)).getTime(),
  //       );
  //       break;
  //     case 'all':
  //       ApexCharts.exec(
  //         'area-datetime',
  //         'zoomX',
  //         new Date(2017, 1, 1).getTime(),
  //         new Date(dayToTime(toDay)).getTime(),
  //       );
  //       break;
  //     default:
  //   }
  // };

  useEffect(() => {
    const getValueData = async (data1, data2, data3) => {
      const reqData = await api.get(`stockdata/detail`, {
        params: {
          stockCode: data1,
          startDate: data2,
          endDate: data3,
        },
      });
      setStockInfo([reqData.data.stockName, reqData.data.stockCode]);
    };
    getValueData('005930', toDay, toDay);
  }, []);

  return (
    <div id="chart">
      <LineChartData
        totoday={totoday}
        toDay={toDay}
        dayToTime={dayToTime}
        setIndex={setIndex}
        stockPrice={stockPrice}
        setStockPrice={setStockPrice}
        dataAmount={dataAmount}
        setDataAmount={setDataAmount}
        totalDate={totalDate}
        setTotalDate={setTotalDate}
        priceIncreasement={priceIncreasement}
        setStockPriceIncreasement={setStockPriceIncreasement}
        dataFlucauationRate={dataFlucauationRate}
        setDataFlucauationRate={setDataFlucauationRate}
        setState={setState}
      />
      <h2>{stockInfo[0]}</h2>
      <h4>{stockInfo[1]}</h4>
      <h1>{stockPrice[index]}원</h1>
      <h4>
        전일대비 {priceIncreasement[index]}원{dataFlucauationRate[index]}
      </h4>
      <ReactApexChart
        options={state.options}
        series={state.series}
        type="line"
        height={350}
      />
      <ShowDate
        toDay={toDay}
        dayToTime={dayToTime}
        stockPrice={stockPrice}
        dataAmount={dataAmount}
        totalDate={totalDate}
        setIndex={setIndex}
        state={state}
        setState={setState}
      />
      {/* <div>
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
      </div> */}
      <div>
        <button
          onClick={() => {
            setToDay(toDay + 1);
          }}
        >
          하루+
        </button>
      </div>
      {/* 차트 데이터 확인해볼 때 쓴 버튼 */}
      {/* <div>
        <button
          onClick={() => {
            stockAPI
              .stockDetail('005930', 20200123, 20200126)
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
      </div> */}
    </div>
  );
};

export default LineChart;
