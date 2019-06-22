import React from "react";
import "./App.css";
import Auth from "./Auth";
import MyFiles from "./MyFiles";
import NavBar from "./NavBar";
import UploadFile from "./UploadFile";

function App() {
  return (
    <div className="App">
      <Auth>
        <NavBar />
        <div className="Content">
          <UploadFile />
          <MyFiles />
        </div>
      </Auth>
    </div>
  );
}

export default App;
