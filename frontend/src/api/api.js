import 'babel-polyfill'

class ApiError extends Error {
    constructor(message, code) {
        super();
        this.message = message;
        this.code = code;
    }
}

const API_PATH = 'http://localhost:9024/'

const get = async (url) => new Promise((resolve, reject) => {
    fetch(url, { method: 'GET', credentials: 'include', mode: 'cors' }).then((result) => {
        if (result.status === 200) {
            return resolve(result.json());
        }
        return reject(
            new ApiError(`Error while fetching: ${url}`, result.status)
        );
    });
});


export const searchStillinger = (from, to, employer) => get(`${API_PATH}search/${from}/${to}/${employer}`);
export const getStillingByBatchId = (id) => get(`${API_PATH}get/${id}`);

