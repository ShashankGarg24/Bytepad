import axios from 'axios';

const instance = axios.create(
    {
      // baseURL: "http://7d2d9d813c30.ngrok.io"
    }
);

export default instance;