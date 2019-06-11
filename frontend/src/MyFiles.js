import React from "react";
import Api from "./Api";
import download from "downloadjs";
import { useToken } from "./Auth";

function MyFiles() {
  const files = Api.useMyFiles();
  const token = useToken();

  if (files === "loading") {
    return "loading";
  }

  return (
    <div className="MyFiles">
      <h2>My Files</h2>
      <ul>
        {files.map(file => (
          <li>
            #{file.id} — {file.pathKey}
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
            <button onClick={() => Api.deleteFile(file.id, token)}>
              Delete
            </button>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default MyFiles;
