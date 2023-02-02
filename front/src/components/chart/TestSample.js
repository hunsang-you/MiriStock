import { useState, useEffect } from 'react';
import ApexCharts from 'apexcharts';
import ReactApexChart from 'react-apexcharts';

const TestChart = () => {
  const [testDay, setTestDay] = useState([2017, 0, 0]);
  const [testData, setTestData] = useState([Math.floor(Math.random() * 100)]);
  const [lastData, setLastData] = useState([]);
  const [select, setSelect] = useState('one_year');
  const [state, setState] = useState({
    series: [
      {
        data: lastData,
      },
    ],
    options: {
      chart: {
        id: 'area-datetime',
        type: 'area',
        height: 350,
        zoom: {
          autoScaleYaxis: true,
        },
      },
      annotations: {
        yaxis: [],
        xaxis: [],
      },
      dataLabels: {
        enabled: false,
      },
      markers: {
        size: 0,
        style: 'hollow',
      },
      xaxis: {
        type: 'datetime',
        min: new Date('01 Feb 2017').getTime(), // 랜더링시 시작하는 부분 옵션인거같음 이부분을 스위칭에 맞게 조절하는 함수 만들어서 작성 해야함
        tickAmount: 6,
      },
      tooltip: {
        x: {
          format: 'dd MMM yyyy',
        },
      },
      fill: {
        type: 'gradient',
        gradient: {
          shadeIntensity: 1,
          opacityFrom: 0.7,
          opacityTo: 0.9,
          stops: [0, 100],
        },
      },
    },
    selection: select,
  });
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
  useEffect(() => {
    // console.log('tlqkf');
    setState({
      series: [
        {
          data: lastData,
        },
      ],
      options: {
        chart: {
          id: 'area-datetime',
          type: 'area',
          height: 500,
          zoom: {
            autoScaleYaxis: true,
          },
        },
        annotations: {
          yaxis: [],
          xaxis: [],
        },
        dataLabels: {
          enabled: false,
        },
        markers: {
          size: 0,
          style: 'hollow',
        },
        xaxis: {
          type: 'datetime',
          min: new Date('01 Feb 2017').getTime(),
          tickAmount: 6,
        },
        tooltip: {
          x: {
            format: 'dd MMM yyyy',
          },
        },
        fill: {
          type: 'gradient',
          gradient: {
            shadeIntensity: 1,
            opacityFrom: 0.7,
            opacityTo: 0.9,
            stops: [0, 100],
          },
        },
      },
      selection: select,
    });
  }, [lastData]); // 이또한 제거될 함수

  const dayDay = () => {
    let copy = [...testDay];
    let testyear = copy[0],
      testmonth = copy[1],
      testday = copy[2] + 1;
    if (testday === 29) {
      testday = 0;
      testmonth = testmonth + 1;
    }
    if (testmonth === 12) {
      testmonth = 0;
      testyear = testyear + 1;
    }
    setTestDay([testyear, testmonth, testday]);
  }; // 시뮬레이션 시간 설정 함수 (axios 연결시 제거)

  const pushData = () => {
    setTestData([Math.floor(Math.random() * 100)]);
  }; // 임의의 값 추가하는 함수 (axios 연결시 제거)

  const pushPush = () => {
    dayDay();
    pushData();
    let copyday = [...testDay];
    let dayTime = new Date(copyday[0], copyday[1], copyday[2]);
    let copy = [...lastData, [dayTime.getTime(), testData]];
    setLastData(copy);
  }; // useState에 값을 넣는 함수 (axios 연결시 제거)

  const generateDayWiseTimeSeries = (baseval, count, yrange) => {
    let i = 0;
    let series = [];
    while (i < count) {
      let x = baseval;
      let y =
        Math.floor(Math.random() * (yrange.max - yrange.min + 1)) + yrange.min;

      series.push([x, y]);
      baseval += 86400000;
      i++;
    }
    return series;
  };

  return (
    <div>
      <div>
        <div>
          <ReactApexChart
            options={state.options}
            series={state.series}
            type="area"
            height={350}
          />
        </div>
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

      <button
        onClick={() => {
          pushPush();
        }}
      >
        제발
      </button>
    </div>
  );
};

export default TestChart;
