import React from 'react';
import './App.css';
import { Navbar } from './layouts/NavbarAndFooter/Navbar';
import { Footer } from './layouts/NavbarAndFooter/Footer';
import { HomePage } from './layouts/HomePage/HomePage';
import { SearchBooksPage } from './layouts/SearchBooksPage/SearchBooksPage';
import { Redirect, Route, Switch, useHistory } from 'react-router-dom';
import { BookCheckoutPage } from './layouts/BookCheckoutPage/BookCheckoutPage';
import { oktaConfig } from './lib/oktaConfig';
import { OktaAuth, toRelativeUrl } from '@okta/okta-auth-js'
import { LoginCallback, SecureRoute, Security } from '@okta/okta-react';
import LoginWidget from './Auth/LoginWidget';
import { ReviewListPage } from './layouts/BookCheckoutPage/ReviewListPage/ReviewListPage';
import { ShelfPage } from './layouts/ShelfPage/ShelfPage';
import { MessagesPage } from './layouts/MessagesPage/MessagesPage';
import { PaymentPage } from './layouts/PaymentPage/PaymentPage';

// Create an OktaAuth instance using the provided Okta configuration.
const oktaAuth = new OktaAuth(oktaConfig);

export const App = () => {

  // Custom authentication handler that redirects users to the login page.
  const customAuthHandler = () => {
    history.push('/login');
  }

  const history = useHistory();

  // Function to restore the original URI after successful authentication.
  const restoreOriginalUri = async (_oktaAuth: any, originalUri: any) => {
    // Replace the current history location with the original URI or the root path.
    history.replace(toRelativeUrl(originalUri || '/', window.location.origin))
  };

  return (
    //add CSS to keep footer on the bottom of page*
    <div className='d-flex flex-column min-vh-100'>
      <Security oktaAuth={oktaAuth} restoreOriginalUri={restoreOriginalUri} onAuthRequired={customAuthHandler}>
        <Navbar />
        {/*The <div> element will take up the remaining vertical space within its parent container */}
        <div className='flex-grow-1'>
          {/* The <Switch> component selects the first <Route> that matches the current URL and renders the corresponding content. It doesn't continue to check other routes once a match is found, ensuring that only one route is rendered.*/}
          <Switch>
            {/*"exact" attribute ensures that a <Route> is only triggered when the URL completely matches the root path.*/}
            <Route path='/' exact><Redirect to='/home' /></Route>
            <Route path='/home'><HomePage /></Route>
            <Route path='/search'><SearchBooksPage /></Route>
            <Route path='/reviewlist/:bookId'><ReviewListPage /></Route>
            {/*:bookId表示一个动态的路由参数*/}
            <Route path='/checkout/:bookId'><BookCheckoutPage /></Route>
            <Route path='/login' render={
              () => <LoginWidget config={oktaConfig} />
            }
            />

            <Route path='/login/callback' component={LoginCallback} />
            <SecureRoute path='/shelf'><ShelfPage /></SecureRoute>
            <SecureRoute path='/messages'><MessagesPage /></SecureRoute>
            <SecureRoute path='/fees'><PaymentPage /></SecureRoute>


          </Switch>
        </div>
        <Footer />
      </Security>
    </div>
  )
}