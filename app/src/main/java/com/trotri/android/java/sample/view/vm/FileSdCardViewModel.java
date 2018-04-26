package com.trotri.android.java.sample.view.vm;

import com.trotri.android.library.base.BaseViewModel;
import com.trotri.android.library.util.SingletonManager;
import com.trotri.android.thunder.file.SdCard;

/**
 * FileSdCardViewModel class file
 * thunder.file.SdCard的ViewModel
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: FileSdCardViewModel.java 1 2017-05-01 10:00:06Z huan.song $
 * @since 1.0
 */
public class FileSdCardViewModel extends BaseViewModel {

    public static final String TAG = "FileSdCardViewModel";

    public static final String FILE_NAME = "sdcard_test01";
    public static final String WRITE_DATA = "def456";

    public void read(SdCard.Listener l) {
        SingletonManager.getSdCard().read(FILE_NAME, l);
    }

    public void write(SdCard.Listener l) {
        SingletonManager.getSdCard().write(FILE_NAME, WRITE_DATA, false, l);
    }

    public void appendWrite(SdCard.Listener l) {
        SingletonManager.getSdCard().write(FILE_NAME, WRITE_DATA, true, l);
    }

}
