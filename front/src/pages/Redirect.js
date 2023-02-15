import { useNavigate } from 'react-router-dom';
import { useEffect } from 'react';

function Redirect() {
  const navigate = useNavigate();
  useEffect(() => {
    const getToken = async () => {
      try {
        const url = new URL(document.location).searchParams;
        const accessToken = url.get('accesstoken');
        localStorage.setItem('accessToken', 'Bearer ' + accessToken);
        setTimeout(() => navigate('/', { replace: true }), 300);
      } catch (e) {
        console.log(e);
      }
    };
    getToken();
  }, []);
}

export default Redirect;
