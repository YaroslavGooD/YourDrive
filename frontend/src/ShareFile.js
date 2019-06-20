import React from "react";
import { useToken } from "./Auth";

function ShareFile(fileId) {
  const token = useToken();
  var guid = null;

  if(guid === null){
    return (
        <button onClick={guid = this.getGuid}>Generate Link</button>
    );
  }

  // const copyToClipboard = e => {
  //   getLink.select();
  //   document.execCommand('copy');
  //   console.log("Copy link success!")
  // };

  const getGuid = async () => {
    const response = await fetch(
      "http://localhost:8080/api/file/share?id=" + fileId,
      {
        method: "GET",
        headers: {
          Authorization: "Bearer " + token
        }
      }
    );
    console.log(response);
    return response;
  }

  const getLink = () => {
    return "http://localhost:8080/api/file/shared?token="+guid;
  }

  return (
    <div>
      <a href={getLink}>{getGuid}</a>
      {/* <button onClick={copyToClipboard}>Copy</button> */}
    </div>
      
  );

}

export default ShareFile;
