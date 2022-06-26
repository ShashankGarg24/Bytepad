import axios from './baseURL/baseURL';

class ServerService{

  fetchAllData(){
    return axios.get(`/api/blah`);
  }

}

export default new ServerService();