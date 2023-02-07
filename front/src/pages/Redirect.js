import { useNavigate } from 'react-router-dom';
import { useEffect } from 'react';

function Redirect() {
  const navigate = useNavigate();
  useEffect(() => {
    const getToken = async () => {
      try {
        const url = new URL(document.location).searchParams;
        const accessToken = url.get('accesstoken');
        localStorage.setItem('accessToken', accessToken);
        console.log(accessToken);
        navigate('/', { replace: true }); //닉네임 있으면 홈 없으면 설정창으로 가게 만들기나중에~
      } catch (e) {
        console.log(e);
      }
    };
    getToken();
  }, []);
}

export default Redirect;
