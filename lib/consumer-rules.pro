-keep class * extends androidx.lifecycle.ViewModel{
    public <init>(android.app.Application);
    public <init>(androidx.lifecycle.ViewModelCreator);
}



#---------------------------------------RxJava      start---------------------------------------
-dontwarn java.util.concurrent.Flow*
-keep class * extends org.reactivestreams.Subscriber {*;}
-keep class * extends io.reactivex.rxjava3.core.Observer {*;}
-keep class * extends io.reactivex.rxjava3.core.MaybeObserver {*;}
-keep class * extends io.reactivex.rxjava3.core.SingleObserver {*;}
-keep class * extends io.reactivex.rxjava3.core.CompletableObserver {*;}

#---------------------------------------RxJava        end---------------------------------------
