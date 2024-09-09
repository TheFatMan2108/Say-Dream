package com.thuydev.saydream.Activity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.thuydev.saydream.Interface.ICallBackAction;
import com.thuydev.saydream.Interface.ICallbackPram;
import com.thuydev.saydream.databinding.ActivityTestBinding;

public class TestSend extends AppCompatActivity {
ActivityTestBinding view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        view = ActivityTestBinding.inflate(getLayoutInflater());
        setContentView(view.getRoot());
        view.btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreatedLink();
            }
        });
    }

    private void CreatedLink() {
        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("https://saydream.page.link/product/?email=okem&pass=okanh"))
                .setDomainUriPrefix("https://saydream.page.link")
                .setAndroidParameters(
                        new DynamicLink.AndroidParameters.Builder("com.example.android")
                                .setMinimumVersion(125)
                                .build())
                .setIosParameters(
                        new DynamicLink.IosParameters.Builder("com.example.ios")
                                .setAppStoreId("123456789")
                                .setMinimumVersion("1.0.1")
                                .build())
                .setGoogleAnalyticsParameters(
                        new DynamicLink.GoogleAnalyticsParameters.Builder()
                                .setSource("orkut")
                                .setMedium("social")
                                .setCampaign("example-promo")
                                .build())
                .setItunesConnectAnalyticsParameters(
                        new DynamicLink.ItunesConnectAnalyticsParameters.Builder()
                                .setProviderToken("123456")
                                .setCampaignToken("example-promo")
                                .build())
                .setSocialMetaTagParameters(
                        new DynamicLink.SocialMetaTagParameters.Builder()
                                .setTitle("Example of a Dynamic Link")
                                .setDescription("This link works whether the app is installed or not!")
                                .build())
                .buildDynamicLink();  // Or buildShortDynamicLink()
        Uri dynamicLinkUri = dynamicLink.getUri();
        MakeShortLink(dynamicLinkUri.toString(), new ICallbackPram() {
            @Override
            public void CallBack(Object... obj) {
                if(obj==null||obj.length<0)return;

                Log.e("TAG", "CallBack: "+obj[0].toString() );
                Log.e("TAG", "CallBack: "+obj[1].toString() );
            }
        });
    }
private void MakeShortLink(String link, ICallbackPram action){
    Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
            .setLongLink(Uri.parse(link))
            .buildShortDynamicLink()
            .addOnCompleteListener(this, new OnCompleteListener<ShortDynamicLink>() {
                @Override
                public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                    if (task.isSuccessful()) {
                        // Short link created
                        Uri shortLink = task.getResult().getShortLink();
                        Uri flowchartLink = task.getResult().getPreviewLink();
                        action.CallBack(shortLink,flowchartLink);
                    } else {
                        // Error
                        // ...
                    }
                }
            });
}
}