import { React, useState } from 'react';
import { TextField, Button } from '@mui/material';
import '../login/nickname.css';

function Nickname () {
    const [text, setText] = useState(''); 
  
    const ChangeText = (e) => {
        setText(e.target.value);
    };

  // 닉네임 가능여부 확인
    const Validation = () => {
        let check_spe = /[~!@\#$%^&*\()\-=+_'\;<>\/.\`:\"\\,\[\]?|{}]/gi;      // 특수문자 제거
        let check_str = /[ㄱ-ㅎㅏ-ㅣ]/gi;                                         // 자음, 모음 제거
        let space = /\s/g;                                                      // 공백 제거

    if (check_spe.test(text) === true) {
      return '특수문자는 사용할 수 없습니다.';
    }
      else if (check_str.test(text) === true) {
      return '자음 혹은 모음만 사용할 수 없습니다';
    } else if (space.test(text) === true) {
      return '공백은 사용할 수 없습니다.';
    } else if (text.length > 10) {
      return '글자수가 초과되었습니다.';
    } 
    // else if (check_name.includes(text) === true) {
    //   return '중복된 닉네임이 존재합니다.'
    // }
  }
  
  // 닉네임 오류시 색 변경
    const ChangeRed = () => {
        if (text.length > 10) {
        return true;
        } else {
        return false;
        }
    };

    return (
        <div className='namepage'>
            <div className='view'>

              <div className='text-1'>
                  닉네임 설정
              </div>

              <div className='nameInput'>
                  <TextField
                  sx = {{ width : { xs:272, sm:400, md:600 }}}
                  id="nickname"
                  placeholder="닉네임을 입력해주세요."
                  variant="standard"
                  onChange={ ChangeText }
                  value = { text }
                  error = { Validation() }
                  helperText = { Validation() ? Validation() : "" }
                  inputProps = {{ maxLength: 15 }}
                  />
                  
                  <div className={ ChangeRed() ? 'lenError' : 'noError'} >
                    {text.length}/10
                  </div>
              </div>
                  
            </div >

            <div className='createBtn'>
              <Button id='createBtn' variant='outlined' size='large' >
                  미리하기
              </Button>
            </div>

        </div>

  );
}

export default Nickname;