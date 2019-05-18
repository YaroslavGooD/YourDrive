import React from "react";
import { useToken } from "./Auth";

const BASE_URL = "http://localhost:8080";

const request = (url, options, token) => {
  return fetch(BASE_URL + url, {
    ...options,
    headers: {
      ...options.headers,
      ...(token ? { Authorization: "Bearer " + token } : {})
    }
  });
};

const Api = {
  login: async (email, password) => {
    const response = await request("/api/auth/login", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({
        email,
        password
      })
    });

    const jwt = await response.json();

    return jwt.accessToken;
  },
  signup: async (email, password) => {
    const response = await request("/api/auth/signup", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({
        email,
        password
      })
    });

    const parsed = await response.json();

    if (parsed.errors) {
      return parsed.errors.map(x => x.defaultMessage);
    }
    return "OK";
  },
  useMyFiles: () => {
    const [files, setFiles] = React.useState([]);
    const [loading, isLoading] = React.useState(false);

    const token = useToken();

    React.useEffect(() => {
      const fetching = async () => {
        isLoading(true);
        const response = await request("/api/file/files", {}, token);
        const files = await response.json();
        setFiles(files);
        isLoading(false);
      };
      fetching();
    }, []);

    return loading ? "loading" : files;
  },
  uploadFile: async (file, key, token) => {
    const formData = new FormData();
    formData.append("file", file);

    request(
      "/api/file/file?key=" + key,
      { method: "POST", body: formData },
      token
    );
  }
};

export default Api;
