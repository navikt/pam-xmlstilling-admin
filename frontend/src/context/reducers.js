const initialState = {
    searchResult: {},
    from: '2018-01-23',
    to: '2018-02-12',
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
            throw new Error('Unexpected action');
    }
};
export { initialState, types, reducer };
