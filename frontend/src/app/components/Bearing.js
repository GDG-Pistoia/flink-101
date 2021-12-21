import React from "react";
import './Bearing.css';

const Bearing = ({space = "50px"}) => {
    return (
        <div className="bearing" style={{height: space}}/>
    )
}

export default Bearing;
