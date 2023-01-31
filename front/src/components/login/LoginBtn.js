import mirilogo from '../../static/mirilogo.png';
import kakaobtn from '../../static/kakaobtn.png';
import naverbtn from '../../static/naverbtn.png';
import { useNavigate } from 'react-router-dom';

const LoginBtn = () => {
    const navigate = useNavigate();

    return (
        <div className='loginpage'>
            <div className='logo'>  
                <img src={ mirilogo } className='mirilogo' alt='logo' />
            </div>
            
            {/*  소셜 로그인 버튼 */}
            <div className='loginbtn'>
                <div className='kakaologin'>
                    <img src={ kakaobtn } className='btn' alt='kakao'
                        onClick={ () => { navigate('nickname') }}
                    />
                </div>
                
                <div className='naverlogin'>
                    <img src={ naverbtn } className='btn' alt='naver'
                        onClick={ () => { navigate('nickname') }}
                    />
                </div>
            
            </div>
        
        </div>
    );
}
export default LoginBtn;