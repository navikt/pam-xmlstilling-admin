import moment from 'moment';

const initialState = {
    searchResult: [],
    from: moment().subtract(7, 'd').format('YYYY-MM-DD'),
    to: moment().format('YYYY-MM-DD'),
    employer: ''
};

const types = {
    SET_SEARCH_RESULT: 'SET_SEARCH_RESULT',
    SET_FROM_DATE: 'SET_FROM_DATE',
    SET_TO_DATE: 'SET_TO_DATE',
    SET_EMPLOYER: 'SET_EMPLOYER'
};

const reducer = (state = initialState, action) => {
    switch (action.type) {
        case types.SET_SEARCH_RESULT:
            return {
                ...state,
                searchResult: action.result
            };
        case types.SET_FROM_DATE:
            return {
                ...state,
                from: action.value
            };
        case types.SET_TO_DATE:
            return {
                ...state,
                to: action.value
            };
        case types.SET_EMPLOYER:
            return {
                ...state,
                employer: action.value
            };
        default:
            return state;
    }
};
export { initialState, types, reducer };
