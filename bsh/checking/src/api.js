import axios from 'axios';

const BASE_URL = 'http://127.0.0.1:8000';

const api = axios.create({
    baseURL : BASE_URL,
    headers : {},
});

export default api;

export const movieAPI = {
    getMovies : () => api.get(`/movies`),
    getSearch : (movie_name) => api.post(`/movies/search/`,{movie_name : movie_name}),
}

// searchMovie({commit}, movie_name){
//     axios({
//       url : `${API_URL}/movies/search/`,
//       method : 'POST',
//       data : {movie_name}
//     })
//     .then((res) => {
//       // console.log(res)
//       commit('SEARCH_MOVIE', res.data)
//     })
//     .catch((err) => console.log(err))
//   },