/*
 * Copyright (c) 2015 LingoChamp Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.library;

import com.android.library.event.DownloadServiceConnectChangedEvent;
import com.android.library.event.IDownloadEvent;
import com.android.library.event.IDownloadListener;

/**
 * The listener for listening whether the service establishes connection or disconnected.
 *
 * @see com.android.library.services.BaseFileServiceUIGuard#releaseConnect(boolean)
 */
public abstract class FileDownloadConnectListener extends IDownloadListener {

    private DownloadServiceConnectChangedEvent.ConnectStatus mConnectStatus;

    public FileDownloadConnectListener() {
    }

    @Override
    public boolean callback(IDownloadEvent event) {
        if (event instanceof DownloadServiceConnectChangedEvent) {
            final DownloadServiceConnectChangedEvent connectChangedEvent
                    = (DownloadServiceConnectChangedEvent) event;
            mConnectStatus = connectChangedEvent.getStatus();

            if (mConnectStatus == DownloadServiceConnectChangedEvent.ConnectStatus.connected) {
                connected();
            } else {
                disconnected();
            }
        }
        return false;
    }

    /**
     * connected file download service
     */
    public abstract void connected();

    /**
     * disconnected file download service
     */
    public abstract void disconnected();

    public DownloadServiceConnectChangedEvent.ConnectStatus getConnectStatus() {
        return mConnectStatus;
    }
}
