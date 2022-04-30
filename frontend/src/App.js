import React, {Component} from 'react';
import logo from './logo.svg';
import Library from './Components/Library';

class App extends Component {

    state = {};

    /*
                <div className="App">
                <header className="App-header">
                    <img src={logo} className="App-logo" alt="logo"/>
                    <h1 className="App-title">{this.state.message}</h1>
                </header>
                <p className="App-intro">
                    To get started, edit <code>src/App.js</code> and save to reload.
                </p>
            </div>
    */
    render() {
        return (
            <div>
                <h1> Library </h1>
                <Library/>
            </div>
        );
    }
}

export default App;