import React from "react";
import { useInput, useLocalStorage } from "./hooks";
import Api from "./Api";

// Ghetto enum
const SubmitType = {
  Login: 0,
  Signup: 1
};

function LoginForm({ onSubmit }) {
  const username = useInput("");
  const password = useInput("");

  const submit = type => () => {
    onSubmit({
      type,
      login: username.value,
      password: password.value
    });
  };

  return (
    <div>
      <label>Login</label>
      <input type="text" placeholder="Login" {...username} />

      <label>Password</label>
      <input type="password" placeholder="Password" {...password} />

      <button type="button" onClick={submit(SubmitType.Login)}>
        Login
      </button>
      <button type="button" onClick={submit(SubmitType.Signup)}>
        Signup
      </button>
    </div>
  );
}

const TokenContext = React.createContext(undefined);
export const useToken = () => React.useContext(TokenContext);
export const logout = () => {
  window.localStorage.setItem("token", JSON.stringify(undefined));
  window.location = "/";
};

function Auth(props) {
  const [token, setToken] = useLocalStorage("token", undefined);
  const [errors, setErrors] = React.useState([]);
  const [isLoading, setIsLoading] = React.useState(false);

  const login = async ({ login, password }) => {
    const responseToken = await Api.login(login, password);
    setToken(responseToken);
  };

  const signup = async ({ login, password }) => {
    const response = await Api.signup(login, password);
    if (response !== "OK") {
      setErrors(response);
    }
  };

  const handleSubmit = async ({ type, ...form }) => {
    setErrors([]);
    setIsLoading(true);

    await {
      [SubmitType.Login]: () => login(form),
      [SubmitType.Signup]: () => signup(form)
    }[type]();
    setIsLoading(false);
  };

  if (!token) {
    return (
      <>
        {errors.length > 0 && (
          <div>
            <ul>
              {errors.map(message => (
                <li>{message}</li>
              ))}
            </ul>
          </div>
        )}
        <LoginForm onSubmit={handleSubmit} />
      </>
    );
  }

  return (
    <TokenContext.Provider value={token}>
      {props.children}
    </TokenContext.Provider>
  );
}

export default Auth;
