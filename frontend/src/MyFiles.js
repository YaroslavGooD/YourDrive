import React from "react";
import Api from "./Api";
import download from "downloadjs";
import { useToken } from "./Auth";

function MyFiles() {
  const files = Api.useMyFiles();
  const token = useToken();
  const size = Api.useFilesSize();

  if (files === "loading" || size === undefined) {
    return "loading";
  }

  return (
    <div className="MyFiles Card">
      <h2>My Files</h2>
      <p>Files size: {size} bytes</p>
      <ul className="FilesList">
        {files.map(file => (
          <li className="FilesList__file">
            <div className="File__id">#{file.id}</div>
            <div className="File__key">{file.pathKey}</div>
            <div className="File__actions">
              <button
                type="button"
                onClick={async () => {
                  const response = await fetch(
                    "http://localhost:8080/api/file/file?id=" + file.id,
                    {
                      method: "GET",
                      headers: {
                        Authorization: "Bearer " + token
                      }
                    }
                  );
                  const doc = await response.blob();
                  const parts = file.pathKey.split("/");
                  const name = parts[parts.length - 1];
                  download(doc, name, response.headers.get("Content-Type"));
                }}
              >
                Download
              </button>
              <button
                onClick={() =>
                  Api.share(file.id, token).then(url =>
                    alert("File available at: " + url)
                  )
                }
              >
                Share
              </button>
              <button onClick={() => Api.deleteFile(file.id, token)}>
                Delete
              </button>
            </div>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default MyFiles;
