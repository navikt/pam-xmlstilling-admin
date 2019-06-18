import { types } from './reducers';

export const useActions = (state, dispatch) => {
    function setSearchResult(result) {
        dispatch({ type: types.SET_SEARCH_RESULT, result });
    }

    function setToDate(value) {
        dispatch({ type: types.SET_TO_DATE, value });
    }

    function setFromDate(value) {
        dispatch({ type: types.SET_FROM_DATE, value });
    }

    function setEmployer(value) {
        dispatch({ type: types.SET_EMPLOYER, value });
    }

    return {
        setSearchResult,
        setToDate,
        setFromDate,
        setEmployer
    };
};
