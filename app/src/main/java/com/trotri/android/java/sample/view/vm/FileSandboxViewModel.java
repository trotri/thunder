package com.trotri.android.java.sample.view.vm;

import com.trotri.android.library.base.BaseViewModel;
import com.trotri.android.library.util.SingletonManager;
import com.trotri.android.thunder.file.Sandbox;

/**
 * FileSandboxViewModel class file
 * thunder.file.Sandbox的ViewModel
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: FileSandboxViewModel.java 1 2017-05-01 10:00:06Z huan.song $
 * @since 1.0
 */
public class FileSandboxViewModel extends BaseViewModel {

    public static final String TAG = "FileSandboxViewModel";

    public static final String FILE_NAME = "sandbox_test01";
    public static final String WRITE_DATA = "abc123";

    public void read(Sandbox.Listener l) {
        SingletonManager.getSandbox().read(FILE_NAME, l);
    }

    public void write(Sandbox.Listener l) {
        SingletonManager.getSandbox().write(FILE_NAME, WRITE_DATA, false, l);
    }

    public void appendWrite(Sandbox.Listener l) {
        SingletonManager.getSandbox().write(FILE_NAME, WRITE_DATA, true, l);
    }

}
