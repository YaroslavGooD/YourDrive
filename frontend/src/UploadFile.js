import React from "react";
import { useInput } from "./hooks";
import Api from "./Api";
import { useToken } from "./Auth";

function UploadFile() {
  const [loading, setLoading] = React.useState(undefined);
  const [error, setError] = React.useState(undefined);
  const fileKey = useInput("sometest/file");
  const fileInputRef = React.useRef(undefined);
  const token = useToken();

  const handleSubmit = async e => {
    setLoading(true);
    setError(undefined);
    e.preventDefault();
    const file = fileInputRef.current.files[0];
    const maybeError = await Api.uploadFile(file, fileKey.value, token);
    setLoading(false);
    if (maybeError) {
      setError(maybeError);
    }
  };

  return (
    <div className="UploadFile Card">
      <h2>Upload file</h2>
      {loading ? "Loading..." : ""}
      {error !== undefined && (
        <div style={{ color: "red" }}>Error: {error}</div>
      )}
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
