import { useParams } from 'react-router-dom';

const SellStock = () => {
  let { stockCode } = useParams();
  console.log(stockCode);
  return (
    <div>
      <h1>Trade</h1>
    </div>
  );
};

export default SellStock;
