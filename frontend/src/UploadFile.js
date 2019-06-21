import React from "react";
import { useInput } from "./hooks";
import Api from "./Api";
import { useToken } from "./Auth";

function UploadFile() {
  const fileKey = useInput("sometest/file");
  const fileInputRef = React.useRef(undefined);
  const token = useToken();

  const handleSubmit = e => {
    e.preventDefault();
    const file = fileInputRef.current.files[0];
    Api.uploadFile(file, fileKey.value, token);
  };

  return (
    <div className="UploadFile Card">
      <h2>Upload file</h2>
      <form onSubmit={handleSubmit} encType="multipart/form-data">
        <div style={{ padding: "10px" }}>
          <label>File key</label>
          <input type="text" name="uploadFileKey" required {...fileKey} />
        </div>
        <div style={{ padding: "10px" }}>
          <input type="file" name="uploadFile" required ref={fileInputRef} />
        </div>
        <div style={{ padding: "10px" }}>
          <button type="submit">Upload</button>
        </div>
      </form>
    </div>
  );
}

export default UploadFile;
