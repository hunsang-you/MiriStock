# 11팀

---

23.01.16

현재 리액트 상태관리 라이브러리는 참 많이 있습니다. 대표적인 **Redux와 MobX, Recoil, Jotai, ...**

Redux가 상태관리 라이브러리의 시초격(Flux 패턴)이라 할 수 있는데요. 그렇기 때문에 많은 개발자들로부터 현재 사용되고 있고 여전히 인기가 많습니다. 하지만 오늘날 여전히 Redux를 기피하는 이유도 있습니다.

그것은 바로 **보일러플레이트 코드(Boilerplate code)** 때문이었죠. Redx Toolkit이 이러한 점 등을 극복하고 있고, 계속해서 업데이트하고는 있지만 여전히 보일러플레이트가 존재합니다.

**최소한의 코드로 상태를 관리하는 방법은 없을까요?** 제가 이번에 회사에서 새로운 프로젝트를 진행하게 되었는데요. 그때 상태관리의 필요성을 느꼈고 그리하여 **최소한의 코드로 상태를 관리할 수 있는 Zustand를 도입하게 되었습니다.**

## **Zustand**

**Zustand는 독일어로 상태**라는 뜻을 가졌습니다. 상태관리 라이브러리 중에 Jotai라고 아시나요. Jotai를 만든 개발진들이 Zustand도 만든 것으로 알려져 있습니다.

Zustand가 가진 아주 강력한 **장점은 굉장히 쉽다**는 것입니다. **보일러플레이트가 거의 없다 싶을 정도**로 간단한 코드만 필요로 하고요. 저 또한 도입을 하려고 했을 때 아주 쉽게 연결할 수 있었습니다. 또한 **Redux Devtools를 사용할 수 있어서 Debugging을 하는데도 아주 용이**합니다.

**사용법을 간단하게 살펴보도록 하죠. 추가로 Devtools 연결도 소개하겠습니다.**

## **Zustand 사용법**

### 1. Zustand 설치

```
npm i zustand # or yarn add zustand
```

### 2. store 생성

```
// store.js

import create from 'zustand' // create로 zustand를 불러옵니다.

const useStore = create(set => ({
  bears: 0,
  increasePopulation: () => set(state => ({ bears: state.bears + 1 })),
  removeAllBears: () => set({ bears: 0 })
}))

export default useStore
```

`bears` 라는 초기값을 선언하고요. 그 값을 조작하는 `increasePopulation(bears를 1씩 증가)`과 `removeAllBears(bears를 0으로 리셋)`를 선언합니다. 이때는 `set`을 활용합니다.

### 3. store에 생성한 useStore를 불러와서 사용하기

```
// App.js

import useStore from '../store.js'

const App = () => {
  const { bears, increasePopulation, removeAllBears } = useStore(state => state)

  return (
    <>
      <h1>{bears} around here ...</h1>
      <button onClick={increasePopulation}>one up</button>
      <button onClick={removeAllBears}>remove all</button>
    </>
  )}
```

store에 선언한 값과 메서드를 useStore를 통해 불러와서 아주 간단하게 사용할 수 있습니다. 코드도 굉장히 간결하죠?

이게 끝입니다!

## **Devtools를 통해 상태 Debugging하기**

**Zustand는 Middleware로 Devtools를 지원하고 있는데요.** 이것도 굉장히 쉽게 연결할 수 있습니다. 먼저 [Redux DevTools](https://chrome.google.com/webstore/detail/redux-devtools/lmhkpmbekcpmknklioeibfkpmmfibljd?hl=ko&refresh=1)를 Chrome 웹 스토어에서 설치해줍니다.

```
// store.js

import create from 'zustand'
import { devtools } from 'zustand/middleware'

const store = (set) => ({
  bears: 0,
  increasePopulation: () => set(state => ({ bears: state.bears + 1 })),
  removeAllBears: () => set({ bears: 0 })
})

const useStore = create(devtools(store))

export default useStore
```

이후에 위에 2에서 생성한 store에서 devtools를 불러온 후에 연결을 해줍니다. 그리고 현재 내가 작업하고 있는 애플리케이션을 브라우저로 띄운 다음 개발자 도구 창에서 Redux DevTools를 확인해보세요. store의 상태를 확인하실 수 있을 것입니다.

## **production일 때와 development일 때 분기처리**

```
// store.js

import create from 'zustand'
import { devtools } from 'zustand/middleware'

const store = (set) => ({
  bears: 0,
  increasePopulation: () => set(state => ({ bears: state.bears + 1 })),
  removeAllBears: () => set({ bears: 0 })
})

const useStore = create(
  process.env.NODE_ENV !== 'production' ? devtools(store) : store
)

export default useStore
```

현재 개발환경에서는 상태를 Debugging하고 실제 배포할 때는 보여지지 않게 하기 위해서는 위처럼 간단하게 분기처리를 통해 설정할 수 있습니다.

---

Zustand Github에서 더 많은 API를 확인할 수 있습니다.

- [Zustand Github Page
