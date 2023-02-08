accessClient.interceptors.response.use(
  (response) => {
    return response;
  },
  (err) => {
    return new Promise((resolve, reject) => {
      const originalReq = err.config;
      if (
        err.response.status === 401 &&
        err.config &&
        !err.config.__isRetryRequest
      ) {
        originalReq._retry = true;

        let res = fetch(refresh_url, {
          method: 'POST',
          mode: 'cors',
          cache: 'no-cache',
          credentials: 'same-origin',
          headers: {
            'Content-Type': 'application/json',
            Authorization: 'Bearer ' + localStorage.getItem(ACCESS_TOKEN),
          },
          redirect: 'follow',
          referrer: 'no-referrer',
          body: JSON.stringify({
            refresh: localStorage.getItem(REFRESH_TOKEN),
          }),
        })
          .then((res) => res.json())
          .then((res) => {
            localStorage.setItem(ACCESS_TOKEN, res.access);
            originalReq.headers['Authorization'] = 'Bearer ' + res.access;

            return axios(originalReq);
          });

        resolve(res);
      }

      return reject(err);
    });
  },
);
