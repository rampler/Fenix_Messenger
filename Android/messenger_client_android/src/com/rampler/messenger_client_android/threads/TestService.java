package com.rampler.messenger_client_android.threads;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

/**
 * Created with IntelliJ IDEA.
 * User: Mateusz
 * Date: 04.03.13
 * Time: 22:27
 * To change this template use File | Settings | File Templates.
 */
public class TestService extends Service {

    @Override
    public IBinder onBind(Intent intent){ return null; }

    @Override
    public void onCreate() {
        Toast.makeText(this, "My Service Created", Toast.LENGTH_LONG).show();
        try{Thread.sleep(20000);}catch (Exception e){ Toast.makeText(this, "Bug", Toast.LENGTH_LONG).show();}
        Toast.makeText(this,"2",Toast.LENGTH_SHORT).show();
    }

}
