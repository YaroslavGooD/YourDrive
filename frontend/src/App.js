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
        App
        <MyFiles />
        <UploadFile />
      </Auth>
    </div>
  );
}

export default App;
