// const Test = () => {
//   return (
//     <div>
//       조건A ? ("실행문1") : 조건B ? ("실행문2") : 조건C ? ("실행문3") :
//       ("실행문4");
//       {props.hopeInputID === 0 &&
//       (props.hopePrice <= props.stockPrice * 0.7 ||
//         props.hopePrice >= props.stockPrice * 1.3) ? (
//         <Button
//           style={{ width: '100%' }}
//           variant="contained"
//           color="primary"
//           disableElevation
//         >
//           <div className="trade-btn">입력 완료</div>
//         </Button>
//       ) : props.hopeInputID === 0 ? (
//         <Button
//           style={{ width: '100%' }}
//           variant="contained"
//           color="primary"
//           disableElevation
//           onClick={() => {
//             props.inputID(1);
//           }}
//         >
//           <div className="trade-btn">입력 완료</div>
//         </Button>
//       ) : props.hopeCount === 0 ? (
//         <Button
//           style={{ width: '100%' }}
//           variant="contained"
//           color="primary"
//           disableElevation
//           onClick={() => {
//             errorqwe();
//           }}
//         >
//           <div className="trade-btn">최종</div>
//         </Button>
//       ) : props.hopeCount > Math.floor(props.stockAmount / 3) ? (
//         <Button
//           style={{ width: '100%' }}
//           variant="contained"
//           color="primary"
//           disableElevation
//           onClick={() => {
//             errorqwe();
//           }}
//         >
//           <div className="trade-btn">최종</div>
//         </Button>
//       ) : (
//         <Button
//           style={{ width: '100%' }}
//           variant="contained"
//           color="primary"
//           disableElevation
//           onClick={() => {
//             errorqwe();
//             props.inputID(1);
//             console.log('거래완뇨');
//           }}
//         >
//           <div className="trade-btn">최종</div>
//         </Button>
//       )}
//     </div>
//   );
// };

// export default Test;
