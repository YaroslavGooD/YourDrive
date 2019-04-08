export const ACCESS_TOKEN = 'accessToken';


const request = (options) => {
    const headers = new Headers({
        'Content-Type': 'application/json',
    });

    if(localStorage.getItem(ACCESS_TOKEN)) {
        headers.append('Authorization', 'Bearer ' + localStorage.getItem(ACCESS_TOKEN))
    }

    const defaults = {headers: headers};
    options = Object.assign({}, defaults, options);

    return fetch(options.url, options)
        .then(response =>
            response.json().then(json => {
                if(!response.ok || !response.status === 201) {
                    return Promise.reject(json);
                }
                return json;
            })
        );
};

export function login(loginRequest) {
    return request({
        url: process.env.REACT_APP_BASE_URL + "api/auth/login",
        method: 'POST',
        body: JSON.stringify(loginRequest)
    });
}

export function signUp(SignUpRequest) {
    return request({
        url: process.env.REACT_APP_BASE_URL + "api/auth/signup",
        method: 'POST',
        body: JSON.stringify(SignUpRequest)
    });
}