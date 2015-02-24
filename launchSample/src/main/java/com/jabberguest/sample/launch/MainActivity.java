/*
 * Copyright (c) 2013 Cisco Systems, Inc. All Rights Reserved.
 */

package com.jabberguest.sample.launch;

import com.cisco.jabber.guest.JabberGuestCall;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

    // !!! Specify your own Server and Address !!!     
    public static final String DEFAULT_SERVER = "ksucjg.kk.dk:9443";
    public static final String DEFAULT_ADDRESS = "hjemmeplejen";
    public static final String DEFAULT_ADDRESS1 = "morgenmode";
    public static final String DEFAULT_ADDRESS2 = "telemesteren";

    // Package ID of the Jabber Guest client application
    private static final String JABBER_GUEST_PACKAGE_ID = "com.cisco.jabber.guest";

    /**
     * Checks the package manager to see if JabberGuest application is installed.
     * 
     * @return Returns true if JabberGuest is installed
     */
    private boolean checkForJabberGuestOnDevice() {

        PackageManager pm = this.getPackageManager();

        try {
            pm.getPackageInfo(JABBER_GUEST_PACKAGE_ID, PackageManager.GET_META_DATA);
        } catch (NameNotFoundException e) {
            return false;
        }

        return true;
    }

    /**
     * Launches an activity to resolve the Jabber Guest client on the market.
     * This method will first try the content schema "market" exposed by the Google Play Store application and will fall back to a web view if that application is not available.
     * 
     */
    private void launchJabberGuestOnMarket() {

        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + JABBER_GUEST_PACKAGE_ID)));
        } catch (android.content.ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + JABBER_GUEST_PACKAGE_ID)));
        }
    }

    /*
     * (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create the text to display above the Call Now button
        StringBuilder sb = new StringBuilder();
        sb.append(getResources().getString(R.string.call_now_description));
        sb.append(System.getProperty("line.separator"));
        sb.append(DEFAULT_SERVER.concat(" @ ").concat(DEFAULT_ADDRESS));


        boolean jabberGuestIsInstalled = checkForJabberGuestOnDevice();
        if (jabberGuestIsInstalled) {

            // Proceed as normal and wire the UI elements

            // Handle the user clicking the Call Now button
            findViewById(R.id.callNowButton).setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    Uri callUri = JabberGuestCall.createUri(DEFAULT_SERVER, DEFAULT_ADDRESS, null);
                    previewCall(callUri);
                }

            });

            // Handle the user clicking the Call Now button
            findViewById(R.id.callNowButton1).setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    Uri callUri = JabberGuestCall.createUri(DEFAULT_SERVER, DEFAULT_ADDRESS1, null);
                    previewCall(callUri);
                }

            });


            // Handle the user clicking the Call Now button
            findViewById(R.id.callNowButton2).setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    Uri callUri = JabberGuestCall.createUri(DEFAULT_SERVER, DEFAULT_ADDRESS2, null);
                    previewCall(callUri);
                }

            });


        } else {

            // Show the warning
            findViewById(R.id.jabberc_warning).setVisibility(View.VISIBLE);

            // Enable the ability to get Jabber Guest
            findViewById(R.id.getJabberGuestButton).setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    launchJabberGuestOnMarket();
                }

            });

            // Disable all the UI elements
            findViewById(R.id.callNowButton).setEnabled(false);
        }
    }


    /**
     * Start the JabberC Preview.
     * @param callUri
     */
    private void previewCall(Uri callUri) {

        Intent previewIntent = new Intent();
        previewIntent.setAction(Intent.ACTION_VIEW);
        previewIntent.setData(callUri);

        startActivity(previewIntent);
    }

}
