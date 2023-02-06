const ShowDate = (props) => {
  // 날짜 변환해주는 함수 (86400000은 1일 -> 초)
  const updateData = (num) => {
    console.log(props.toDay);
    let change = props.dayToTime(props.toDay) - 86400000 * num;
    if (change <= 1514732400000) {
      change = 1514732400000;
    }
    props.setState({
      series: [
        {
          name: '주가', //stockDataClosingPrice
          type: 'line',
          data: props.stockPrice,
        },
        {
          name: '거래량', // stockDataAmount
          type: 'column',
          data: props.dataAmount,
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
          categories: props.totalDate,
          tickAmount: 1, // 표시 틱 간격
          tickPlacement: 'on',
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
    });
  };

  return (
    <div>
      <button id="one_week" onClick={() => updateData(7)}>
        1W
      </button>
      &nbsp;
      <button id="one_month" onClick={() => updateData(31)}>
        1M
      </button>
      &nbsp;
      <button id="six_months" onClick={() => updateData(185)}>
        6M
      </button>
      &nbsp;
      <button id="one_year" onClick={() => updateData(365)}>
        1Y
      </button>
      &nbsp;
      <button id="all" onClick={() => updateData(1850)}>
        ALL
      </button>
    </div>
  );
};

export default ShowDate;
