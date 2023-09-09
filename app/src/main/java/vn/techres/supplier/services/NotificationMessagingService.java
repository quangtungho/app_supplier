package vn.techres.supplier.services;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import vn.techres.supplier.helper.PreferenceHelper;
import vn.techres.supplier.helper.TechresEnum;
import vn.techres.supplier.helper.WriteLog;

public class NotificationMessagingService extends FirebaseMessagingService {
    public static final String DEFAULT_NOTIFICATION = "DEFAULT NOTIFICATION";

    @Override
    public void onNewToken(@NotNull String token) {
        WriteLog.INSTANCE.d("Push token: ",token);
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
        PreferenceHelper sharedPreference = new PreferenceHelper(getApplicationContext());
        sharedPreference.save(TechresEnum.PUSH_TOKEN.toString(), token);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onMessageReceived(@NotNull RemoteMessage remoteMessage) {
        Gson gson = new Gson();
        Log.d("Tungcute", gson.toJson(remoteMessage.getData()));
    }


}
