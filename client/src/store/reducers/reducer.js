const initialState = {
    allData: [],
    displayData: [],
    examType: 'All Exams',
    examYear: 'All Years',
    paperType: 'All Papers',
    subject: ""
}

const reducer = (state = initialState, action) => {
    switch(action.type){

        case 'FETCH_ALL_DATA':
            return({
                ...state,
                displayData: action.data
            })

        case 'CHANGE_EXAM_TYPE':
            return({
                ...state,
                examType: action.data
            })

        case 'CHANGE_SESSION':
            return({
                ...state,
                examYear: action.data
            })

        case 'CHANGE_PAPER_TYPE':
            return({
                ...state,
                paperType: action.data
            })

        default: return state

    }
}

export default reducer;
