package com.android.library.i;

import com.android.library.message.MessageSnapshot;

interface IFileDownloadIPCCallback {
    oneway void callback(in MessageSnapshot snapshot);
}
