package Console;

import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

/**
 * Created by cai on 2017/9/7.
 */

public class Console {
    public String str;
    public TextView newsTitleList;

    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case 1:
                    newsTitleList.setText(str);
                    newsTitleList.invalidate();
                    break;
            }
        }
    };

    public Console(TextView newsTitleList) {
        this.newsTitleList = newsTitleList;
        str = new String("news!");
    }

    public void CallBackStr(String str){
        this.str = str;
    }
}
