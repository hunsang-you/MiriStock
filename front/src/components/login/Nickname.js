import { React, useState } from 'react';
import { TextField, Button } from '@mui/material';
import { useLocation, useNavigate } from 'react-router-dom';
import { memberStore } from '../../store';
import { useEffect } from 'react';
import '../login/nickname.css';
import axios from 'axios';
import Swal from 'sweetalert2';
const BASE_URL = 'https://miristockserverurl/api';
const api2 = axios.create({
  baseURL: BASE_URL,
  withCredentials: true,
  headers: {
    'Content-Type': 'application/json',
  },
});
const profileAPI = {
  nicknameCheck: (memberEmail) =>
    api2.post(`/member/nicknamecheck`, {
      memberEmail: memberEmail,
    }),
  nicknameChange: (email, nickname) =>
    api2.put(`/member/nickname`, {
      memberEmail: email,
      memberNickname: nickname,
    }),
  deleteMember: (memberEmail) =>
    api2.delete(`/member`, { params: { memberEmail: memberEmail } }),
};

const KAKAO_URL = `${BASE_URL}/oauth2/authorization/kakao`;
const NAVER_URL = `${BASE_URL}/oauth2/authorization/naver`;
const Nickname = () => {
  const errAlert = () => {
    Swal.fire({
      text: '닉네임을 확인해주세요',
    });
  };
  const [text, setText] = useState('');
  const { info, setInfo } = memberStore((state) => state);
  const navigate = useNavigate();
  const location = useLocation();
  let check_spe =
    /[\{\}\[\]\/?.,;:|\)*~`!^\-_+<>@\#$ΑΒΓΔΕΖΗΘΙΚΛΜΝΞΟΠΡΣΤΥΦΧΨΩαβγδεζηθικλμνξοπρστυφχψω½⅓⅔¼¾⅛⅜⅝⅞¹²³⁴ⁿ₁₂₃₄ⅰⅱⅲⅳⅴⅵⅶⅷⅸⅹⅠⅡⅢⅣⅤⅥⅦⅧⅨⅩ─│┌┐┘└├┬┤┴┼━┃┏┓┛┗┣┳┫┻╋┠┯┨┷┿┝┰┥┸╂┒┑┚┙┖┕┎┍┞┟┡┢┦┧┩┪┭┮┱┲┵┶┹┺┽┾╀╁╃╄╅╆╇╈╉╊＄％￦Ｆ′″℃Å￠￡￥¤℉‰?㎕㎖㎗ℓ㎘㏄㎣㎤㎥㎦㎙㎚㎛㎜㎝㎞㎟㎠㎡㎙㏊㎍㎎㎏㏏㎈㎉㏈㎧㎨㎰㎱㎲㎳㎴㎵㎶㎷㎸㎹㎀㎁㎂㎃㎄㎺㎻㎼㎽㎾㎿㎐㎑㎒㎓㎔Ω㏀㏁㎊㎋㎌㏖㏅㎭㎮㎯㏛㎩㎪㎫㎬㏝㏐㏓㏃㏉㏜㏆＋－＜＝＞±×÷≠≤≥∞∴♂♀∠⊥⌒∂∇≡≒≪≫√∽∝∵∫∬∈∋⊆⊇⊂⊃∪∩∧∨￢⇒⇔∀∃∮∑∏＂（）［］｛｝‘’“”〔〕〈〉《》「」『』【】！＇，．￣：；‥…¨〃­―∥＼∼´～ˇ˘˝˚˙¸˛¡¿ː＃＆＊＠§※☆★○●◎◇◆□■△▲▽▼→←↑↓↔〓◁◀▷▶♤♠♡♥♧♣⊙◈▣◐◑▒▤▥▨▧▦▩♨☏☎☜☞¶†‡↕↗↙↖↘♭♩♪♬㉿㈜№㏇™㏂㏘℡?ªⓐⓑⓒⓓⓔⓕⓖⓗⓘⓙⓚⓛⓜⓝⓞⓖⓠⓡⓢⓣⓤⓥⓦⓧⓨⓩ①②③④⑤⑥⑦⑧⑨⑩⑪⑫⑬⑭⑮⒜⒝⒞⒟⒠⒡⒢⒣⒤⒥⒦⒧⒨⒩⒪⒫⒬⒭⒮⒯㉠㉡㉢㉣㉭㉥㉦㉧㉨㉩㉪㉫㉬㉭㉮㉯㉰㉱㉲㉳㉴㉵㉶㉷㉸㉹㉺㉻㈀㈁㈂㈃㈄㈅㈆㈇㈈㈉㈊㈋㈌㈍㈎㈏㈐㈑㈒㈓㈔㈕㈖㈗㈘㈙㈚㈛⒜⒝⒞⒟⒠⒡⒢⒣⒤⒥⒦⒧⒨⒩⒪⒫⒬⒭⒮⒯⒰⒱⒲⒳⒴⒵⑴⑵⑶⑷⑸⑹⑺⑻⑼⑽⑾⑿⒀⒁⒂%&\\\=\(\'\"]/gi; // 특수문자 제거
  let check_str = /[ㄱ-ㅎㅏ-ㅣ]/gi; // 자음, 모음 제거
  let space = /\s/g; // 공백 제거
  const ChangeText = (e) => {
    setText(e.target.value);
  };
  useEffect(() => {
    setInfo(location.search.slice(7));
  }, []);

  useEffect(() => {
    const checkNickname = async () => {
      await profileAPI
        .nicknameCheck(info)
        .then((request) => {
          if (request.data.memberNickname !== null) {
            navigate('/');
          } // 어싱크어웨잇써보기
        })
        .catch((err) => console.log(err));
    };
    checkNickname();
  }, [info]);

  // 닉네임 가능여부 확인
  const Validation = () => {
    let check_spe =
      /[\{\}\[\]\/?.,;:|\)*~`!^\-_+<>@\#$ΑΒΓΔΕΖΗΘΙΚΛΜΝΞΟΠΡΣΤΥΦΧΨΩαβγδεζηθικλμνξοπρστυφχψω½⅓⅔¼¾⅛⅜⅝⅞¹²³⁴ⁿ₁₂₃₄ⅰⅱⅲⅳⅴⅵⅶⅷⅸⅹⅠⅡⅢⅣⅤⅥⅦⅧⅨⅩ─│┌┐┘└├┬┤┴┼━┃┏┓┛┗┣┳┫┻╋┠┯┨┷┿┝┰┥┸╂┒┑┚┙┖┕┎┍┞┟┡┢┦┧┩┪┭┮┱┲┵┶┹┺┽┾╀╁╃╄╅╆╇╈╉╊＄％￦Ｆ′″℃Å￠￡￥¤℉‰?㎕㎖㎗ℓ㎘㏄㎣㎤㎥㎦㎙㎚㎛㎜㎝㎞㎟㎠㎡㎙㏊㎍㎎㎏㏏㎈㎉㏈㎧㎨㎰㎱㎲㎳㎴㎵㎶㎷㎸㎹㎀㎁㎂㎃㎄㎺㎻㎼㎽㎾㎿㎐㎑㎒㎓㎔Ω㏀㏁㎊㎋㎌㏖㏅㎭㎮㎯㏛㎩㎪㎫㎬㏝㏐㏓㏃㏉㏜㏆＋－＜＝＞±×÷≠≤≥∞∴♂♀∠⊥⌒∂∇≡≒≪≫√∽∝∵∫∬∈∋⊆⊇⊂⊃∪∩∧∨￢⇒⇔∀∃∮∑∏＂（）［］｛｝‘’“”〔〕〈〉《》「」『』【】！＇，．￣：；‥…¨〃­―∥＼∼´～ˇ˘˝˚˙¸˛¡¿ː＃＆＊＠§※☆★○●◎◇◆□■△▲▽▼→←↑↓↔〓◁◀▷▶♤♠♡♥♧♣⊙◈▣◐◑▒▤▥▨▧▦▩♨☏☎☜☞¶†‡↕↗↙↖↘♭♩♪♬㉿㈜№㏇™㏂㏘℡?ªⓐⓑⓒⓓⓔⓕⓖⓗⓘⓙⓚⓛⓜⓝⓞⓖⓠⓡⓢⓣⓤⓥⓦⓧⓨⓩ①②③④⑤⑥⑦⑧⑨⑩⑪⑫⑬⑭⑮⒜⒝⒞⒟⒠⒡⒢⒣⒤⒥⒦⒧⒨⒩⒪⒫⒬⒭⒮⒯㉠㉡㉢㉣㉭㉥㉦㉧㉨㉩㉪㉫㉬㉭㉮㉯㉰㉱㉲㉳㉴㉵㉶㉷㉸㉹㉺㉻㈀㈁㈂㈃㈄㈅㈆㈇㈈㈉㈊㈋㈌㈍㈎㈏㈐㈑㈒㈓㈔㈕㈖㈗㈘㈙㈚㈛⒜⒝⒞⒟⒠⒡⒢⒣⒤⒥⒦⒧⒨⒩⒪⒫⒬⒭⒮⒯⒰⒱⒲⒳⒴⒵⑴⑵⑶⑷⑸⑹⑺⑻⑼⑽⑾⑿⒀⒁⒂%&\\\=\(\'\"]/gi; // 특수문자 제거
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
          <div className="text-1">닉네임 설정</div>
          <div className="nameInput">
            <TextField
              sx={{ width: { xs: 288, sm: 400, md: 500, lg: 700, xl: 1300 } }}
              id="nickname"
              placeholder="닉네임을 입력해주세요."
              variant="standard"
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

      <div className="createBtn">
        <Button
          id="createBtn"
          variant="outlined"
          size="large"
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
                  window.location.href = KAKAO_URL;
                })
                .catch((err) => console.log(err));
            } else {
              errAlert();
            }
          }}
        >
          미리하기
        </Button>
      </div>
    </div>
  );
};

export default Nickname;
