import React, { Fragment } from 'react';

export default function Book(props){
    return(
        <Fragment>
            <h4>{props.author}</h4>
            <h4>{props.name}</h4>
            <button onClick = {props.removeHandler} >Remove</button>
        </Fragment>
    )
}