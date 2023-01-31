import { useState, useEffect } from 'react';
import ApexChart from "react-apexcharts";

const LineChart = (props) => {
  
  const options = {
    colors: ['#6DCEF5'],
    chart : { //차트의 툴바와 배경색을 투명으로 바꿈
        height: 100,
        width: 500,
        background: "transparent", //투명
        toolbar:{show:false},
    }, 
    stroke: { //선의 커브를 부드럽게 하고, 두께를 3으로 지정
        curve: "smooth",
        width: 3,
    },
    fill: {
      type: 'gradient',
      gradient: {
        opacityFrom: 0.9,
        opacityTo: 0.1,
      }
    },
    grid: { //격자 없앰
        show:false,
    },
    markers : {
      strokeWidth : 1,
    },
    xaxis: { //x축의 라벨과 선들을 없앰
        axisBorder:{show:false},
        axisTicks:{show:false},
        labels:{show:false}   
    },
    yaxis: { //y축의 내용 없앰
      show:false,
    }
  }

  const series = props.series
  
  // const [stockData, setStockData] = useState([]);
  // const [series, setSeries] = useState(
  //   [{
  //     name: " 오늘의 주식 종가",
  //     data: stockData,
  //   },]
  // )

  // useEffect(() => {
  //   console.log(22);
  //   setSeries([{
  //     name: " 오늘의 주식 종가",
  //     data: stockData,
  //   },]);
  // }, [stockData]);


  // const inserData = () => {
  //   // console.log(stockData)
  //   // console.log(stockData.length)
  //   if (stockData.length === 10) {
  //     let copy = [...stockData]
  //     copy.shift();
  //     copy.push(Math.random() * 100);
  //     setStockData(copy);
  //   } else {
  //     setStockData([...stockData, Math.random() * 100]);
  //   }
  // };

  // setInterval(inserData, 5000)

  return (
    <div>
      {/* <button onClick={ () => {inserData()}}>추가</button> */}
      <ApexChart 
        type="area"
        series={ series } 
        options={ options }>
      </ApexChart>
    </div>
  )
}

export default LineChart;