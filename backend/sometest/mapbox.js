import React from "react"
import style from "./basic_main.json"

let mapboxgl = { Map: () => {} }

if (typeof window !== "undefined") {
  mapboxgl = require("mapbox-gl")
}

// Context for mapbox Map object https://docs.mapbox.com/mapbox-gl-js/api/#map
const MapContext = React.createContext(undefined)

// Hook for getting mapbox Map object
export const useMap = () => React.useContext(MapContext)

function Mapbox(props) {
  // Ref for root html element
  const mapRoot = React.useRef(undefined)
  // State for mapbox Map object
  const [map, setMap] = React.useState(undefined)

  // Declare a map initialization effect
  React.useEffect(() => {
    const mapObj = new mapboxgl.Map({
      container: mapRoot.current,
      style: style,
      center: [125.744496, 39.010425],
      zoom: 8,
    })

    // We need to wait for 'load' event
    mapObj.on("load", () => {
      setMap(mapObj)
    })
  }, [])

  return (
    <div ref={mapRoot} style={{ height: "400px", margin: "20px 0" }}>
      {map !== undefined && (
        <MapContext.Provider value={map}>{props.children}</MapContext.Provider>
      )}
    </div>
  )
}

export default Mapbox
