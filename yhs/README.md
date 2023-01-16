# 11팀 유헌상

---

# 23.01.16

## Redux Toolkit

### state가 array, object인 경우 변경방법

- array / object의 경우 직접 수정해도 state는 변경된다.
  
  - state가 array / object 면 return 없이 직접 수정해도 된다.

- state 변경함수에 파라미터 작성 -> 파라미터를 이용하여 비슷한 함수를 여러개 작성하지 않아도 된다.
  
  - action => state 변경함수
  
  ```
  
  ```
  
  

- state 수정
  
  - 수정함수를 만들고
  
  - export하고
  
  - import 해서 사용

- state._findIndex_(()=>{ })
  
  - array에서 원하는 요소가 몇번째에 있나 찾아주는 함수

- state._push_()
  
  - array 뒤에 자료 추가해주는 함수

## Figma

### Auto Layout

- 내부 컨텐츠에 따라 자동으로 변형되는 프레임

- 버튼이나 카드컴포넌트들의 내부 내용이 수정되어도 프레임의 사이즈를 자동으로 수정

- shift + A
1. 오토레이아웃 구성요소
   
   1. 컨테이너
   
   2. 아이템

2. 한 프레임에 하나의 방향 -> 한 컨테이너에 한가지 방향밖에 안되므로 줄바꿈이 되지 않는다

3. Resizing
   
   - 요소의 크기가 변경되었을 때, 이와 관련된 요소들의 크기도 같이 변경
   
   - Hug Contents : 아이템 변화 -> 컨테이너도 변화 (컨테이너에 설정)
   
   - Fill Container : 컨테이너 변화 -> 아이템도 변화 (아이템에 설정)
   
   - Fixed Size : 변화 없음
