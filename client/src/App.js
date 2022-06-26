import React from "react";
import { Route, Switch, Redirect } from "react-router-dom";
import { BrowserRouter } from "react-router-dom";
import ContentPage from "./routes/ContentPage/ContentPage";
// import 'bootstrap/dist/css/bootstrap.min.css';
import LandingPage from './routes/LandingPage/LandingPage';
import Test from './routes/testing/tesing';

function App() {
  return (
    <BrowserRouter>
      <Switch>

        <Route path="/" exact component={LandingPage} />
        <Route path="/testing"  component={Test}/>
        <Route path="/content" component={ContentPage}/>

        <Redirect to="/" />
      
      </Switch>
    </BrowserRouter>
  );
}

export default App;
