package com.ai.moonvsky.hanzhuanpin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.dictionary.py.Pinyin;

import org.w3c.dom.Text;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.editText);
        textView=findViewById(R.id.textView);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_DONE:
                        updateText("正在处理……");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String ori_str=editText.getEditableText().toString();
                                if(TextUtils.isEmpty(ori_str)){
                                    updateText("请输入汉字");
                                }else{
                                    List<Pinyin> pinyinList = HanLP.convertToPinyinList(ori_str);
                                    StringBuffer stringBuffer=new StringBuffer();
                                    stringBuffer.append("原文是：");
                                    for (char c : ori_str.toCharArray()) {
                                        stringBuffer.append(c+",");
                                    }
                                    stringBuffer.append("\n");
                                    stringBuffer.append("拼音（符号音调）：");
                                    for (Pinyin pinyin : pinyinList) {
                                        stringBuffer.append(pinyin+",");
                                    }
                                    stringBuffer.append("\n");
                                    stringBuffer.append("拼音（无音调）：");
                                    for (Pinyin pinyin : pinyinList) {
                                        stringBuffer.append(pinyin.getPinyinWithoutTone()+",");
                                    }
                                    stringBuffer.append("\n");
                                    stringBuffer.append("声调：");
                                    for (Pinyin pinyin : pinyinList) {
                                        stringBuffer.append( pinyin.getTone()+",");
                                    }
                                    stringBuffer.append("\n");

                                    stringBuffer.append("声母：");
                                    for (Pinyin pinyin : pinyinList) {
                                        stringBuffer.append( pinyin.getShengmu()+",");
                                    }
                                    stringBuffer.append("\n");

                                    stringBuffer.append("韵母：");
                                    for (Pinyin pinyin : pinyinList) {
                                        stringBuffer.append( pinyin.getYunmu()+",");
                                    }
                                    stringBuffer.append("\n");

                                    stringBuffer.append("输入法头：");
                                    for (Pinyin pinyin : pinyinList) {
                                        stringBuffer.append( pinyin.getHead()+",");
                                    }
                                    updateText(stringBuffer.toString());
                                }
                            }
                        }).start();

                        break;
                }
                return false;
            }
        });
    }
    private void updateText(final String content){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText(content);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
