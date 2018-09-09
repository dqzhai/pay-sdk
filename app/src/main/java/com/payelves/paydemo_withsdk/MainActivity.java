package com.payelves.paydemo_withsdk;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.lid.lib.LabelButtonView;
import com.payelves.sdk.EPay;
import com.payelves.sdk.enums.EPayResult;
import com.payelves.sdk.listener.ConfigResultListener;
import com.payelves.sdk.listener.PayResultListener;


public class MainActivity extends AppCompatActivity {

    ru.github.igla.ferriswheel.FerrisWheelView ferrisWheelView;

    FloatingActionButton fabButton;

    LabelButtonView priceButton;

    LabelButtonView payButton;

    static volatile boolean isRunning = false;


    int payPrice = 10;


    final Handler runningHandler = new Handler() {
    };

    class UpdateThread extends Thread {

        int speed = -1;
        Context context;

        private UpdateThread(int speed, Context context) {
            this.speed = speed;
            this.context = context;
        }

        @Override
        public void run() {
            super.run();
            try {
                int sleep1 = 4200;
                while (true) {
                    long sleep = sleep1;
                    int speed = ferrisWheelView.getRotateDegreeSpeedInSec();
                    if (speed > 20) {
                        speed = speed - 50;
                    }
                    Thread.sleep(sleep);
                    if (speed <= 0) {
                        speed = 0;
                    }
                    int price = (int) (Math.random() * 100);
                    runningHandler.post(new DrawThread(speed, price));
                    if (speed == 0) {
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class DrawThread extends Thread {
        int speed = -1;
        int price = 0;

        DrawThread(int speed, int price) {
            this.speed = speed;
            this.price = price;
        }

        @Override
        public void run() {
            updateView(speed);
        }

        private void updateView(int speed) {
            if (speed == 0) {
                ferrisWheelView.stopAnimation();
                isRunning = false;
            } else {
                ferrisWheelView.setRotateDegreeSpeedInSec(speed);
                //ferrisWheelView.pauseAnimation();
                ferrisWheelView.resumeAnimation();
            }
            if (price < 50) {
                priceButton.setText("金额:¥0.01");
                payPrice = 1;
            } else if (price < 90) {
                priceButton.setText("金额:¥0.02");
                payPrice = 2;
            } else {
                priceButton.setText("金额:¥0.05");
                payPrice = 5;
            }
        }

    }


    private void initView() {
        ferrisWheelView = findViewById(R.id.ferrisWheelView);
        fabButton = findViewById(R.id.fab);
        payButton = findViewById(R.id.payButton);
        priceButton = findViewById(R.id.priceButton);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        //init Epay
        EPay.getInstance(MainActivity.this).init("tZmNIobZL", "77cd7a5ef528400aac865e2a001a6432", "6623341290717185",
                "xiaomi");

        //Config
        EPay.getInstance(getApplicationContext()).loadConfig("key1", new ConfigResultListener() {
            @Override
            public void onSuccess(String value) {
                Log.e("e", value);
            }
        });


        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRunning) {
                    return;
                }
                isRunning = true;
                try {
                    ferrisWheelView.setRotateDegreeSpeedInSec(40);
                    ferrisWheelView.startAnimation();
                    new UpdateThread(100, MainActivity.this).start();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

        );

        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EPay.getInstance(MainActivity.this).pay("美立生活", "支付测试", payPrice, "123", "123", null, new PayResultListener() {
                    @Override
                    public void onFinish(Context context, String orderId, String payUserId, EPayResult payResult, int payType, Integer amount) {
                        EPay.getInstance(context).closePayView();//关闭快捷支付页面
                        if(payResult.getCode() == EPayResult.SUCCESS_CODE.getCode()){
                            //支付成功逻辑处理
                            Toast.makeText(MainActivity.this, payResult.getMsg(), Toast.LENGTH_LONG).show();
                        }else if(payResult.getCode() == EPayResult.FAIL_CODE.getCode()){
                            //支付失败逻辑处理
                            Toast.makeText(MainActivity.this, payResult.getMsg(), Toast.LENGTH_LONG).show();
                        }
                    }

                });

            }
        });
    }
}

