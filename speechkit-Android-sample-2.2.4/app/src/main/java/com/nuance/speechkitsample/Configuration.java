package com.nuance.speechkitsample;

import android.net.Uri;

import com.nuance.speechkit.PcmFormat;

/**
 * All Nuance Developers configuration parameters can be set here.
 *
 * Copyright (c) 2015 Nuance Communications. All rights reserved.
 */
public class Configuration {

    //All fields are required.
    //Your credentials can be found in your Nuance Developers portal, under "Manage My Apps".
    public static final String APP_KEY = "2053066309d89177afd605fd347107a60548d35f1107a926ed8bd04697ba22b70d281a1600ac8daf8292acb603b45d09fc30680e371134249c046a33be8ceacb";
    public static final String APP_ID = "NMDPTRIAL_andy_pham2956_gmail_com20180127123533";
    public static final String SERVER_HOST = "sslsandbox-nmdp.nuancemobility.net";
    public static final String SERVER_PORT = "443";

    public static final String LANGUAGE = "!LANGUAGE!";

    public static final Uri SERVER_URI = Uri.parse("nmsps://" + APP_ID + "@" + SERVER_HOST + ":" + SERVER_PORT);

    //Only needed if using NLU
    public static final String CONTEXT_TAG = "!NLU_CONTEXT_TAG!";

    public static final PcmFormat PCM_FORMAT = new PcmFormat(PcmFormat.SampleFormat.SignedLinear16, 16000, 1);
    public static final String LANGUAGE_CODE = (Configuration.LANGUAGE.contains("!") ? "eng-USA" : Configuration.LANGUAGE);

}



