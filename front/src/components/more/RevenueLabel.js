const RevenueLabel = ({ viewBox, StockName, StockPrice }) => {
  const { cx, cy } = viewBox;
  return (
    <svg
      className="recharts-text recharts-label"
      textAnchor="middle"
      dominantBaseline="central"
    >
      <text x={cx} y={cy} fill="#D2143C">
        <tspan x={cx} dy="-2.5em" alignmentBaseline="middle" fontSize="20">
          {StockName}
        </tspan>

        <tspan x={cx} dy="1.5em" fontSize="14" fontWeight={600}>
          {StockPrice + '원'}
        </tspan>
      </text>
    </svg>
  );
};

export default RevenueLabel;
