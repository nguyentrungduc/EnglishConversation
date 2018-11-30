package com.ntduc.audioselector.data.source;

import com.ntduc.audioselector.data.model.Audio;
import com.ntduc.audioselector.data.source.callback.OnGetDataCallback;

import java.util.List;

/**
 * Created by fs-sournary.
 * Date on 1/16/18.
 * Description:
 */

public interface AudioDataSource {

    interface LocalDataSource {
        void getAudios(OnGetDataCallback<List<Audio>> callback);
    }
}
