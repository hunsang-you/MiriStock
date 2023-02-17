import { React, useState, useEffect } from 'react';
import { TextField, Button } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import { profileAPI, memberAPI } from '../../api/api';
import { memberStore } from '../../store';
import './css/ChangeName.css';
import '../login/nickname.css';
import Swal from 'sweetalert2';

const ChangeName = () => {
  const navigate = useNavigate();
  const [text, setText] = useState('');
  const { info, setInfo } = memberStore((state) => state);
  const errAlert = () => {
    Swal.fire({
      text: '닉네임을 확인해주세요',
    });
  };
  useEffect(() => {
    memberAPI.asset().then((request) => {
      setInfo(request.data.memberEmail);
    });
  }, []);
  const ChangeText = (e) => {
    setText(e.target.value);
  };
  let check_spe = /[~!@\#$%^&*\()\-=+_'\;<>\/.\`:\"\\,\[\]?|{}]/gi; // 특수문자 제거
  let check_str = /[ㄱ-ㅎㅏ-ㅣ]/gi; // 자음, 모음 제거
  let space = /\s/g; // 공백 제거
  // 닉네임 가능여부 확인
  const Validation = () => {
    let check_spe = /[~!@\#$%^&*\()\-=+_'\;<>\/.\`:\"\\,\[\]?|{}]/gi; // 특수문자 제거
    let check_str = /[ㄱ-ㅎㅏ-ㅣ]/gi; // 자음, 모음 제거
    let space = /\s/g; // 공백 제거
    if (check_spe.test(text) === true) {
      return '특수문자는 사용할 수 없습니다.';
    } else if (check_str.test(text) === true) {
      return '자음 혹은 모음만 사용할 수 없습니다';
    } else if (space.test(text) === true) {
      return '공백은 사용할 수 없습니다.';
    } else if (text.length > 10) {
      return '글자수가 초과되었습니다.';
    }
    // else if (check_name.includes(text) === true) {
    //   return '중복된 닉네임이 존재합니다.'
    // }
  };
  // 닉네임 오류시 색 변경
  const isChangeRed = () => {
    if (text.length > 10) {
      return true;
    } else {
      return false;
    }
  };
  return (
    <div className="namepage">
      <div className="view">
        <div className="setname">
          <div className="text-1">닉네임 변경</div>
          <div className="nameInput">
            <TextField
              sx={{ width: { xs: 288, sm: 400, md: 500, lg: 700, xl: 1300 } }}
              id="nickname"
              placeholder="변경할 닉네임을 입력하세요."
              variant="standard"
              required
              onChange={ChangeText}
              value={text}
              error={Validation() ? true : false}
              helperText={Validation() ? Validation() : ''}
              inputProps={{ maxLength: 15 }}
            />

            <div className={isChangeRed() ? 'lenError' : 'noError'}>
              {text.length}/10
            </div>
          </div>
        </div>
      </div>

      <div className="Btn">
        <div className="more-back">
          <Button
            id="createBtn"
            variant="outlined"
            onClick={() => {
              navigate(-1);
            }}
            disableElevation
          >
            뒤로가기
          </Button>
        </div>
        <div className="more-ok">
          <Button
            id="createBtn"
            variant="contained"
            style={{ color: 'white' }}
            disableElevation
            onClick={() => {
              if (
                check_spe.test(text) === false &&
                check_str.test(text) === false &&
                space.test(text) === false &&
                text.length < 11 &&
                text.length !== 0
              ) {
                profileAPI
                  .nicknameChange(info, text)
                  .then((request) => {
                    navigate('/');
                  })
                  .catch((err) => console.log(err));
              } else {
                errAlert();
              }
            }}
          >
            수정하기
          </Button>
        </div>
      </div>
    </div>
  );
};

export default ChangeName;
