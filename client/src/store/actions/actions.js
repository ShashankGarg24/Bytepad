import ServerService from '../../API/ServerService';

export const fetchData = (data) => {
  return{
    type: 'FETCH_ALL_DATA',
    data: data
  }
}
export const fetchDataAsync = (data) => {
  return (dispatch) => {
    console.log('Sending Request!');
    ServerService.fetchAllData()
      .then((res)=>{
        console.log(res);
        dispatch(fetchData(res))
      })
      .catch((err)=>{
        console.log(err);
      })
  }
}

export const changeExamType = (data) => {
  return{
    type: 'CHANGE_EXAM_TYPE',
    data: data
  }
}

export const changeSession = (data) => {
  return{
    type: 'CHANGE_SESSION',
    data: data
  }
}

export const changePaperType = (data) => {
  return{
    type: 'CHANGE_PAPER_TYPE',
    data: data
  }
}

