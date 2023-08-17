import { useEffect, useRef } from "react";
import OktaSignIn from "@okta/okta-signin-widget";
import '@okta/okta-signin-widget/dist/css/okta-sign-in.min.css';
import { oktaConfig } from './../lib/oktaConfig';

// OktaSignInWidget component definition
const OktaSignInWidget = ({ onSuccess, onError }) => {
    const widgetRef = useRef(); // Create a reference for the OktaSignIn widget

    useEffect(() => {
        // Check if the widgetRef has been initialized
        if (!widgetRef.current) {
            return false; // If not, return
        }

        // Create a new instance of OktaSignIn with the provided configuration
        const widget = new OktaSignIn(oktaConfig);

        // Show the OktaSignIn widget in the specified container (widgetRef)
        // When the user successfully logs in, call the onSuccess callback function
        // If there's an error, catch it and call the onError callback function
        widget.showSignInToGetTokens({
            el: widgetRef.current,
        }).then(onSuccess).catch(onError);

        // Cleanup: remove the widget when the component unmounts
        return () => widget.remove();
    }, [onSuccess, onError]);

    // Render the OktaSignInWidget component
    return (
        <div className="container mt-5 mb-5">
            <div ref={widgetRef}></div> {/* Attach the widget to this DOM element */}
        </div>
    );
};

export default OktaSignInWidget