package test.bocai.com;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class MainActivity extends AppCompatActivity {
    //微信初始化
    public static final String APP_ID="wx0a97ea92aeb12cee";
    private IWXAPI api;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        api = WXAPIFactory.createWXAPI(this,APP_ID);
        //注册到微信
        api.registerApp(APP_ID);
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
    //生成微信唯一标识
    private String buildTransaction(final String type){
        return (type == null)?String.valueOf(System.currentTimeMillis()):type + System.currentTimeMillis();
    }
    //打开微信
    public void onClickopenwx(View view)
    {
        Toast.makeText(this,String.valueOf(api.openWXApp()),Toast.LENGTH_LONG).show();
    }
    //分享给朋友
    public void onClickpy(View view)
    {
        //创建edittext控件
        final EditText editor = new EditText(this);
        editor.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        editor.setText("分享到微信好友的文本");
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.ic_launcher_foreground);
        builder.setTitle("微信共享");
        builder.setView(editor);
        builder.setMessage("请输入要分享的文本");
        builder.setPositiveButton("分享", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String text = editor.getText().toString();
                System.out.println(text);
                if(text == null || text.length() == 0)
                {
                    return;
                }
                //Toast.makeText(getApplicationContext(), "按钮组值发生改变", Toast.LENGTH_LONG).show();
                //初始化对象
                WXTextObject textobj = new WXTextObject();
                textobj.text = text;
                //用于传输的对象
                WXMediaMessage msg = new WXMediaMessage();
                msg.mediaObject = textobj;
                msg.description = text;
                //创建用于请求的对象
                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.message = msg;
                //唯一标识
                req.transaction = buildTransaction("text");

                req.scene = SendMessageToWX.Req.WXSceneSession;
                api.sendReq(req);
                Toast.makeText(MainActivity.this,String.valueOf(api.sendReq(req)),Toast.LENGTH_LONG).show();
            }
        });
        //取消按钮
        builder.setNegativeButton("取消",null);
        final AlertDialog alert = builder.create();
        alert.show();
    }
    //分享到朋友圈
    public void onClickpyq(View view) {
        //创建edittext控件
        final EditText editor = new EditText(this);
        editor.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        editor.setText("分享到微信朋友圈的文本");
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.ic_launcher_foreground);
        builder.setTitle("微信共享");
        builder.setView(editor);
        builder.setMessage("请输入要分享的文本");
        builder.setPositiveButton("分享", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String text = editor.getText().toString();
                if (text == null || text.length() == 0) {
                    return;
                }
                //初始化对象
                WXTextObject textobj = new WXTextObject();
                textobj.text = text;
                //用于传输的对象
                WXMediaMessage msg = new WXMediaMessage();
                msg.mediaObject = textobj;
                msg.description = text;
                //创建用于请求的对象
                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.message = msg;
                //唯一标识
                req.transaction = buildTransaction("text");

                req.scene = SendMessageToWX.Req.WXSceneTimeline;
                api.sendReq(req);
                Toast.makeText(MainActivity.this,String.valueOf(api.sendReq(req)),Toast.LENGTH_LONG).show();
            }
        });
        //取消按钮
        builder.setNegativeButton("取消", null);
        final AlertDialog alert = builder.create();
        alert.show();
    }
}
