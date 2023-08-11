import React from 'react';
import './App.css';
import { Navbar } from './layouts/NavbarAndFooter/Navbar';
import { Footer } from './layouts/NavbarAndFooter/Footer';
import { HomePage } from './layouts/HomePage/HomePage';
import { SearchBooksPage } from './layouts/SearchBooksPage/SearchBooksPage';
import { Redirect, Route, Switch } from 'react-router-dom';


export const App = () => {
  return (
    //add CSS to keep footer on the bottom of page*
    <div className='d-flex flex-column min-vh-100'>
      <Navbar />
      {/*The <div> element will take up the remaining vertical space within its parent container */}
      <div className='flex-grow-1'>
        {/* The <Switch> component selects the first <Route> that matches the current URL and renders the corresponding content. It doesn't continue to check other routes once a match is found, ensuring that only one route is rendered.*/}
        <Switch>
          {/*"exact" attribute ensures that a <Route> is only triggered when the URL completely matches the root path.*/}
          <Route path='/' exact>
            <Redirect to='/home' />
          </Route>

          <Route path='/home'>
            <HomePage />
          </Route>

          <Route path='/search'>
            <SearchBooksPage />
          </Route>
        </Switch>
      </div>
      <Footer />
    </div>
  )
}