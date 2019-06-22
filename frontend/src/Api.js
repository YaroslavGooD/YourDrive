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

const FileChangeObserver = {
  fns: [],
  sub: fn => {
    FileChangeObserver.fns.push(fn);
  },
  unsub: fn => {
    FileChangeObserver.fns = FileChangeObserver.fns.filter(f => f !== fn);
  },
  trigger: () => {
    FileChangeObserver.fns.forEach(f => f());
  }
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
      FileChangeObserver.sub(fetching);
      fetching();

      return () => FileChangeObserver.unsub(fetching);
    }, []);

    return loading ? "loading" : files;
  },
  uploadFile: async (file, key, token) => {
    const formData = new FormData();
    formData.append("file", file);

    const response = await request(
      "/api/file/file?key=" + key,
      { method: "POST", body: formData },
      token
    );
    FileChangeObserver.trigger();

    const text = await response.text();

    try {
      JSON.parse(text);
    } catch (_) {
      return text;
    }
  },
  deleteFile: async (id, token) => {
    request("/api/file/delete?id=" + id, { method: "POST" }, token).then(() => {
      FileChangeObserver.trigger();
    });
  },
  getFilesSize: async token => {
    return request("/api/file/files/size", {}, token);
  },
  useFilesSize: () => {
    const token = useToken();
    const [filesSize, setFilesSize] = React.useState(undefined);
    React.useEffect(() => {
      const request = async () => {
        const response = await Api.getFilesSize(token);
        const size = await response.json();
        setFilesSize(size);
      };
      FileChangeObserver.sub(request);
      request();

      return () => FileChangeObserver.unsub(request);
    }, []);

    return filesSize;
  },
  share: async (id, token) => {
    const response = await request("/api/file/share?id=" + id, {}, token);
    const fileToken = await response.text();
    const url = BASE_URL + "/api/file/shared?token=" + fileToken;

    return url;
  }
};

export default Api;
