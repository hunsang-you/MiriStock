import Spinner from '../static/Spinner-1s-217px.gif';

export const NewsLoading = () => {
  return (
    <div>
      <div
        style={{
          position: 'absolute',
          display: 'flex',
          justifyContent: 'center',
          alignContent: 'center',
        }}
      >
        <img src={Spinner} alt="로딩중" />
      </div>
    </div>
  );
};
