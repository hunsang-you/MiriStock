import { useEffect } from 'react';
import { api } from '../../api/api'; // api 통신
import Counter from './countanimation';
import CounterPer from './counterperanimation';

import './css/detail.css';

// 디테일 차트를 시작 날짜 부터 현재 날짜까지 불러오는 컴포넌트
const LineChartData = (props) => {
  const truefalse = (num) => {
    if (num >= 0) {
      return true;
    } else {
      return false;
    }
  };

  useEffect(() => {
    const getValueData = async (data1, data2, data3) => {
      let change = props.dayToTime(props.toDay) - 86400000 * 31;
      if (change <= 1514732400000) {
        change = 1514732400000;
      }
      const newPrice = []; // 가격
      const newDataAmount = []; // 거래량
      const newDate = []; // 날짜
      const newPriceIncreasement = [];
      const newDataFlucauationRate = [];
      const newTrueFalse = [];
      const reqData = await api.get(`stockdata/detail`, {
        params: {
          stockCode: data1,
          startDate: data2,
          endDate: data3,
        },
      });
      const todayidx = reqData.data.length - 1;
      props.setIndex(todayidx);
      // console.log(reqData.data);
      props.setStockInfo(reqData.data[0].stockName);
      for (let i = 0; i <= todayidx; i++) {
        newPrice.push(reqData.data[i].stockDataClosingPrice);
        newDataAmount.push(reqData.data[i].stockDataAmount);
        newDate.push(props.dayToTime(reqData.data[i].stockDataDate));
        newPriceIncreasement.push(reqData.data[i].stockDataPriceIncreasement);
        newDataFlucauationRate.push(reqData.data[i].stockDataFlucauationRate);
        newTrueFalse.push(truefalse(reqData.data[i].stockDataFlucauationRate));
      }
      props.setStockPrice(newPrice);
      props.setDataAmount(newDataAmount);
      props.setTotalDate(newDate);
      props.setStockPriceIncreasement(newPriceIncreasement);
      props.setDataFlucauationRate(newDataFlucauationRate);
      props.setTrueFalse(newTrueFalse);
      // 차트 데이터 받아서 입력 ====================================================
      props.setState({
        series: [
          {
            name: '주가', //stockDataClosingPrice
            type: 'line',
            data: newPrice,
          },
          {
            name: '거래량', // stockDataAmount
            type: 'column',
            data: newDataAmount,
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
                  props.setIndex(dataPointIndex);
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
            categories: newDate,
            tickAmount: 1, // 표시 틱 간격
            tickPlacement: 'on',
            // min: new Date('1 Jan 2018').getTime(), // 랜더링시 시작하는 부분 옵션인거같음 이부분을 스위칭에 맞게 조절하는 함수 만들어서 작성 해야함
            min: new Date(change).getTime(), // 랜더링시 시작하는 부분 옵션인거같음 이부분을 스위칭에 맞게 조절하는 함수 만들어서 작성 해야함
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
        selection: props.select,
      }); // ==================================================================> 처음에 차트 데이터 넣기함수
    };
    getValueData(props.stockCode, 20180101, props.toDay); // 페이지가 켜졌을때 차트 데이터 넣기
    // setTimeout(() => {
    //   setToDay(toDay + 1);
    //   console.log(1);
    // }, 1000);    // 날짜 하루씩 추가해보는 함수
  }, [props.toDay]);
  return (
    <div>
      {/* 제발되라
      <h1>
        <Counter from={0} to={props.stockPrice[props.index]} />원
      </h1>
      <h4>
        전일대비 <Counter from={0} to={props.priceIncreasement[props.index]} />
        원
        <CounterPer from={0} to={props.dataFlucauationRate[props.index]} />
      </h4> */}
    </div>
  );
};

export default LineChartData;
