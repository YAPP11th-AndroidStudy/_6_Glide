package ridickle7.co.kr.glideexample;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

public class MainActivity extends AppCompatActivity {
    ImageView iv1, iv2;
    Button bu1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bu1 = (Button) findViewById(R.id.defaultURLCallButton);
        iv1 = (ImageView) findViewById(R.id.defaultURLCallIv);
        iv2 = (ImageView) findViewById(R.id.GIFIv);

        // 1. Drawable 데이터를 얻어 처리하는 방법
        bu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 1. 로드된 이미지를 받을 Target 생성 (인수에 width, height 설정 가능)
                SimpleTarget target = new SimpleTarget<Drawable>(250, 250) {
                    @Override
                    public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                        //TODO:: 리소스 로드가 끝난 후 수행할 작업
                        iv1.setBackground(resource);
                    }
                };

                // 2. Glide를 활용한다.
                GlideApp.with(getApplicationContext())
                        .load("https://cdn.rideapart.com/wp-content/uploads/2016/04/HD6.jpg")
                        .placeholder(R.mipmap.ic_launcher)
                        .error(R.mipmap.ic_launcher_round)
                        .into(target);
            }
        });

        //2. 이미지뷰에 직접 URL 이미지를 넣는 방법
        Glide.with(this).load("https://github.com/bumptech/glide/raw/master/static/glide_logo.png").into(iv2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
