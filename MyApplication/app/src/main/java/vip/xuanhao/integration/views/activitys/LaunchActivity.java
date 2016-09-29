package vip.xuanhao.integration.views.activitys;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;
import com.orhanobut.logger.Logger;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.tbruyelle.rxpermissions.RxPermissions;

import org.jetbrains.annotations.Contract;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import vip.xuanhao.integration.R;

public class LaunchActivity extends AppCompatActivity {


    @BindView(R.id.btn_requestPriss)
    Button btnRequestPriss;
    @BindView(R.id.img_show)
    ImageView imgShow;
    private Button btn_requestpermission;



    String[] strings = {"url1", "url2", "url3"};
    List<String> str = new ArrayList<>(5);
    SparseArrayCompat arrayCompat = new SparseArrayCompat();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_launch);
        ButterKnife.bind(this);
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setTintColor(android.R.color.transparent);

        initView();
        str.add("url1");
        str.add("url2");
        str.add("url3");
        str.add("url5");
        arrayCompat.put(1, "你好");
        arrayCompat.put(2, "TOM");
        arrayCompat.put(3, "jack");
        arrayCompat.put(4, "Bmw");
        arrayCompat.put(5, "Boom");
        arrayCompat.put(6, "what");
        arrayCompat.put(7, "demo");

//        test1();
//        test2();
//        test3();
//        test3f();
//        test5();
//        test6();
//        test7();
//        test8();
//        test9();
//        test10();
//        test11();
//        test12();
//        test13();
//        test13B();
//        test_15();

//        testAnimation();


    }

    private void initView() {
        RxView.clicks(btnRequestPriss)
                .compose(RxPermissions.getInstance(this).ensure(Manifest.permission.CAMERA))
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (aBoolean) {
//                            Toast.makeText(LaunchActivity.this, "已经有权限了", Toast.LENGTH_SHORT).show();
                            test16();
                        } else {
                            Toast.makeText(LaunchActivity.this, "拒绝这个权限将无法进行视频拍摄.你确定要这样做吗?", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }


    AnimatorSet bouncer;

    private void testAnimation() {


        ImageView imageView = (ImageView) findViewById(R.id.img_show);

        ObjectAnimator animatorx = ObjectAnimator.ofFloat(imageView, "scaleX", 1.0f, 1.5f);
        ObjectAnimator animatory = ObjectAnimator.ofFloat(imageView, "scaleY", 1.0f, 1.5f);
        ObjectAnimator animatorx1 = ObjectAnimator.ofFloat(imageView, "scaleX", 1.5f, 1.0f);
        ObjectAnimator animatory1 = ObjectAnimator.ofFloat(imageView, "scaleY", 1.5f, 1.0f);
        bouncer = new AnimatorSet();
        bouncer.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animation.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        bouncer.play(animatorx1).with(animatory1).after(animatorx).after(animatory);
        bouncer.setDuration(3500).start();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        bouncer.end();
        bouncer.removeAllListeners();
        bouncer = null;

    }


    public void test16() {
        Random random = new Random();
        boolean b = random.nextBoolean();
        System.out.println(b);


        Observable<String> fromDB = Observable.just(b)
                .flatMap(new Func1<Boolean, Observable<String>>() {
                    @Override
                    public Observable<String> call(Boolean aBoolean) {
                        if (!aBoolean) {
                            Logger.w("empty");
                            new RuntimeException("出错了");
                            return Observable.empty();
                        }
                        Logger.w("db data");
                        return Observable.just("来自数据库的数据");
                    }
                })
                .doOnCompleted(new Action0() {
                    @Override
                    public void call() {
                        System.out.println("doOnCompleted");
                        Logger.w("doOnCompleted");
                    }
                });


        Observable<String> fromNet = Observable.just("来自网络的数据")
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String s) {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Logger.w(" sleep 后 网络");
                        return Observable.just(s);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());


        Observable.concat(fromDB, fromNet)
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                        Logger.w("onCompleted");
                        System.out.println("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.w("onError");
                        System.out.println("onError");
                    }

                    @Override
                    public void onNext(String s) {
                        Logger.w("onNext");
                        System.out.println("onNext  " + s);
                    }
                });
    }

    private void test_15() {


        final Observable<String> fromDb = Observable.create(
                new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        String s = "";//= queryFormDb();
                        if (!s.equals("")) {
//                            System.out.println("fromDb  onNext");
                            subscriber.onNext(s);
                        }
                        subscriber.onCompleted();
                    }
                })
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
//                        System.out.println("fromDb  map call");
                        Logger.w(s);
                        return s;
                    }
                })
                .subscribeOn(Schedulers.io());

        //                .filter(new Func1<String, Boolean>() {
//                    @Override
//                    public Boolean call(String s) {
//                        return s.equals("");
//                    }
//                });
        final Observable<String> fromNet = Observable.create(
                new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        String s = "";//requestFromNet();
                        if (!s.equals("")) {
//                            System.out.println("fromNet  onNext");
                            subscriber.onNext(s);
                        }
                        subscriber.onCompleted();
                    }
                })
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
//                        System.out.println("fromNet  map call");
                        return s;
                    }
                })
                .doOnNext(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        if (!s.equals("")) {
//                            System.out.println("fromNet doOnNext call");
//                            System.out.println(s + "存入数据库");
                            Logger.w(s + "存入数据库");
                        }
                    }
                })
                .subscribeOn(Schedulers.io());
        Observable.concat(fromDb, fromNet)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                               @Override
                               public void call(String s) {
                                   System.out.println(s);
                                   Logger.w(s);
                               }
                           },
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                System.out.println(throwable.toString());
                                System.out.println(throwable.toString());
                            }
                        });
    }


    public List<String> queryFormDb() {

        List<String> dataSourceFromDb = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            dataSourceFromDb.add("" + i);
        }
        return dataSourceFromDb;
    }

    public List<String> requestFromNet() {
        List<String> dataSourceFromDb = null;
        try {
            Thread.sleep(2000);
            dataSourceFromDb = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                dataSourceFromDb.add("" + i);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return dataSourceFromDb;
    }

  /*  private void test13B() {
// Create a RealmConfiguration that saves the Realm file in the app's "files" directory.
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(realmConfig);


        // Get a Realm instance for this thread
        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();
        //... add or update objects here ...
        Student student = realm.createObject(Student.class);
        student.setAge("10");
        student.setName("Dival");
        student.setId("11111");


        RealmList<Classes> classes = new RealmList<>();
        for (int i = 0; i < 5; i++) {
            Classes classes1 = new Classes();
            classes1.setName("" + i);
            classes.add(classes1);
        }
        student.setClasses(classes);
        realm.commitTransaction();
        RealmQuery<Student> query = realm.where(Student.class);
        Student std = query.findFirst();
        Classes classes1 = std.getClasses().get(1);
        Logger.w(std.getName());
        Logger.w(classes1.getName());

    }

    private void test13() {
        Observable.just("hello")
                .map(new Func1<String, Integer>() {
                    @Override
                    public Integer call(String s) {
                        return s.length();
                    }
                })
                .observeOn(Schedulers.io())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Logger.w(integer.toString());
                    }
                });
    }*/

    private void test12() {
        /**
         * 观察者
         */
        Observer observer = new Observer() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Object o) {

            }
        };

        /**a
         * 订阅者
         */
        Subscriber subscriber = new Subscriber() {

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Object o) {

            }
        };


        /**
         * 被观察者
         */
        Observable observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("hi");
                subscriber.onNext("hello");
                subscriber.onNext("Hyuanx");
                subscriber.onCompleted();
            }
        });

//        observable.subscribe(observer);
        // 或者：
        observable.subscribe(subscriber);
    }

    @NonNull
    @Contract(pure = true)
    private String queryDb() {
        return "this method is query Database";
    }

    @NonNull
    @Contract(pure = true)
    private String queryNet() {

        String str = "";
        try {
            Thread.sleep(3000);
            str = "this method is query net";
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return str;
    }

    private void test11() {

        Observable.just(queryDb(), queryNet())

                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        Logger.d(s + System.currentTimeMillis());
                        return s + System.currentTimeMillis();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        new RuntimeException(throwable);
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Logger.w(s);
                    }
                });
    }

    private void test10() {
        Observable.just(arrayCompat)
                .flatMap(new Func1<SparseArrayCompat, Observable<SparseArrayCompat>>() {
                    @Override
                    public Observable<SparseArrayCompat> call(SparseArrayCompat sparseArrayCompat) {
                        sparseArrayCompat.put(1, "变更后1");
                        return Observable.just(sparseArrayCompat);
                    }
                }).subscribe(new Action1<SparseArrayCompat>() {
            @Override
            public void call(SparseArrayCompat sparseArrayCompat) {
                Logger.w(sparseArrayCompat.toString());
            }
        });

    }

    private void test9() {
        File root = Environment.getExternalStorageDirectory();
        File[] files = root.listFiles();

        Observable.from(files)
                .filter(new Func1<File, Boolean>() {
                    @Override
                    public Boolean call(File file) {
                        return file.getName().equals("chexun");
                    }
                })
                .flatMap(new Func1<File, Observable<File>>() {
                    @Override
                    public Observable<File> call(File file) {
                        return Observable.from(file.listFiles());
                    }
                })
                .subscribe(new Action1<File>() {
                    @Override
                    public void call(File file) {
                        Logger.w(file.getName());
                    }
                });
    }

    private void test8() {
        Observable.from(str).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Logger.w(s);
            }
        });
    }

    private void test7() {

        Observable.from(strings)
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String s) {
                        return Observable.just(s);
                    }
                }).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Logger.w(s);
            }
        });
    }

    private void test6() {
        Observable.from(strings).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Logger.w(s);
            }
        });
    }

    private void test5() {
        Observable.just("hello ,world")
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        return s + "你好";
                    }
                }).map(new Func1<String, Integer>() {
            @Override
            public Integer call(String s) {
                Logger.w(s);
                return s.length();
            }
        }).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Logger.w(integer.toString());
            }
        });
    }

    private void test3f() {
        Observable.just("hello , world")
                .map(new Func1<String, Integer>() {
                    @Override
                    public Integer call(String s) {
                        return s.hashCode();
                    }
                }).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer s) {
                Logger.w(s.toString());
            }
        });

    }

    private void test3() {
        Observable.just("hello , world")
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        return s + " xuanhao";
                    }
                }).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Logger.w(s);
            }
        });
    }

    private void test2() {
        Observable.just("hello world")
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Logger.w(s);
                    }
                });

    }

    private void test1() {
        Observable.create(
                new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> sub) {
                        sub.onNext("Hello, world!");
                        sub.onNext("Hello, world!");
                        sub.onNext("Hello, world!");
                        sub.onNext("Hello, world!");
                        sub.onNext("Hello, world!");
                        sub.onCompleted();
                    }
                }
        ).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                Logger.w(s);
            }
        });
    }


}
