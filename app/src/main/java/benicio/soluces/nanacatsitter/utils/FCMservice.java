package benicio.soluces.nanacatsitter.utils;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FCMservice extends FirebaseMessagingService {

    //firebase deploy --only functions
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        Log.d("gustavoBenicio", "onMessageReceived: " + message.getMessageId());
    }
}
