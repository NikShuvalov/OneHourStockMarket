package shuvalov.nikita.onehourchallenge;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    int mCash, mStockPrice, mStocks;
    TextView mStockTicker, mPortfolio, mTimeleftContainer;
    EditText mInput;
    Button buyButt, sellButt, restartButt;
    Boolean gameOver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStockTicker = (TextView)findViewById(R.id.stock_ticker);
        mPortfolio =(TextView)findViewById(R.id.portfolio_view);
        mTimeleftContainer = (TextView)findViewById(R.id.time_left_container);
        mInput = (EditText)findViewById(R.id.user_amount_input);
        buyButt = (Button)findViewById(R.id.buy_button);
        sellButt = (Button)findViewById(R.id.sell_button);
        restartButt = (Button)findViewById(R.id.restart_button);


        sellButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mInput.getText().toString().equals("")) {
                    sellStocks();
                }else{
                    Toast.makeText(MainActivity.this, "Enter Amount", Toast.LENGTH_SHORT).show();
                }
            }
        });
        buyButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mInput.getText().toString().equals("")){
                    buyStocks();
                }else{
                    Toast.makeText(MainActivity.this, "Enter Amount", Toast.LENGTH_SHORT).show();
                }
            }
        });
        simulateStocks();

    }

    public void simulateStocks(){
        restartButt.setVisibility(View.INVISIBLE);
        gameOver=false;
        mCash = 10000;
        mStockPrice = 50;
        mStocks=0;
        CountDownTimer countDownTimer = new CountDownTimer(456250,1250) {
            @Override
            public void onTick(long l) {
                Random rng = new Random();
                int percentChange = 5+rng.nextInt(10);
                boolean goUp = rng.nextBoolean();
                if (goUp){
                    mStockPrice+=(percentChange*mStockPrice)/100;
                }else{
                    mStockPrice-= (percentChange*mStockPrice)/100;
                }
                mStockTicker.setText(String.valueOf("Current stock Value: $"+mStockPrice));
                mPortfolio.setText(String.format("Current Cash: $%s\n" +
                        "Number of Stocks: %s\n" +
                        "Total Value: $%s", mCash,mStocks,String.valueOf(mCash+mStockPrice*mStocks)));
                mTimeleftContainer.setText(String.format("%s days remaining\nuntil year end",l/1250));

            }

            @Override
            public void onFinish() {
                mTimeleftContainer.setText(String.format("%s weeks remaining\nuntil year end",0));
                finishGame();
            }

        };
        countDownTimer.start();
    }

    public void finishGame(){
        Toast.makeText(this, "Game is over", Toast.LENGTH_SHORT).show();
        gameOver=true;
        restartButt.setVisibility(View.VISIBLE);
        restartButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                simulateStocks();
            }
        });
    }

    public void buyStocks(){
        if(!gameOver) {
            int numberOfStocks = Integer.valueOf(mInput.getText().toString());
            int purchaseCost = numberOfStocks * mStockPrice;
            if (purchaseCost > mCash) {
                Toast.makeText(this, "Insufficient capital", Toast.LENGTH_SHORT).show();
            } else {
                mCash -= purchaseCost;
                mStocks += numberOfStocks;
            }
        }
    }

    public void sellStocks(){
        if(!gameOver) {
            int numberOfStocks = Integer.valueOf(mInput.getText().toString());
            int sellAmount = numberOfStocks * mStockPrice;
            if (mStocks < numberOfStocks) {
                Toast.makeText(this, "Insufficient stocks", Toast.LENGTH_SHORT).show();
            } else {
                mStocks -= numberOfStocks;
                mCash += sellAmount;
            }
        }
    }
}
