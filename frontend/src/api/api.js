class ApiError extends Error {
    constructor(message, code) {
        super();
        this.message = message;
        this.code = code;
    }
}

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


export const searchStillinger = (from, to, employer) => get(`${__API__}search/${from}/${to}/${employer}`);
export const getStillingByBatchId = (id) => get(`${__API__}get/${id}`);