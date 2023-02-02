import { useState, useEffect } from 'react';
import ApexChart from "react-apexcharts";

const BarChart = (props) => {
  
  const options = {
    fill : {
      colors : '#FF0000',
    },
    theme:{
        mode:"dark", //다크테마
    },   
    chart : { //차트의 툴바와 배경색을 투명으로 바꿈
        height: 50,
        width: 500,
        background: "transparent", //투명
        toolbar:{show:false},
    }, 
    grid: { //격자 없앰
        show:false,
    },
    xaxis: { //x축의 라벨과 선들을 없앰
        axisBorder:{show:false},
        axisTicks:{show:false},
        labels:{show:false}   
    },
    yaxis: { //y축의 내용 없앰
      show: false,
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

  // // setInterval(inserData, 5000)

  return (
    <div>
      <ApexChart 
        type="bar"
        height= "120"
        series={ series } 
        options={ options }>
      </ApexChart>
      {/* <button onClick={ () => {inserData()}}>추가</button> */}
    </div>
  )
}

export default BarChart;