import './Charts.css';
import { useState, useEffect } from 'react';
import LineChart from './LineChart'
import BarChart from './BarChart'
import Button from '@mui/material/Button';

const StockDetail = () => {

    const [stockData, setStockData] = useState([]);
    const [series, setSeries] = useState(
        [{
        name: " 오늘의 주식 종가",
        data: stockData,
        },]
    )

    useEffect(() => {
        console.log(22);
        setSeries([{
        name: " 오늘의 주식 종가",
        data: stockData,
        },]);
    }, [stockData]);


    const inserData = () => {
        // console.log(stockData)
        // console.log(stockData.length)
        if (stockData.length === 50) {
        let copy = [...stockData]
        copy.shift();
        copy.push(Math.random() * 100);
        setStockData(copy);
        } else {
        setStockData([...stockData, Math.random() * 100]);
        }
    };

    return (
        <div>
            <h1>Detail</h1>
            
            <Button onClick={ () => {inserData()} } variant="contained" color="primary">제발~~</Button>
            <LineChart series={series} />
            <BarChart series={series} />
            <div className='space-between'>
                <Button variant="contained" color="primary">매수</Button>
                <Button variant="contained" color="primary">매도</Button>
            </div>
            <div>
                ddd
            </div>
        </div>
    )
}

export default StockDetail;