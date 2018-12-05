package com.zpp.demo.view.other;

import android.os.Bundle;
import android.widget.ListView;

import com.zpp.demo.R;
import com.zpp.demo.adapter.VolleyImagerLoaderAdapter;
import com.zpp.demo.base.BaseActivity;
import com.zpp.demo.bean.Person;

import java.util.ArrayList;
import java.util.List;

public class ImageLoaderActivity extends BaseActivity {
    private ListView listView;
    private List<Person> list = new ArrayList<>();
    private List<String> listUrl = new ArrayList<>();
    private List<String> listName = new ArrayList<>();
    private VolleyImagerLoaderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listView = (ListView) findViewById(R.id.listView);

        loadPicture();
    }
    @Override
    protected int setLayoutView() {
        return R.layout.activity_image_loader;
    }
    private void loadPicture() {
        //添加测试头像地址
        listUrl.add("http://down.tutu001.com/d/file/20101129/2f5ca0f1c9b6d02ea87df74fcc_560.jpg");
        listUrl.add("http://pic24.nipic.com/20121022/9252150_193011306000_2.jpg");
        listUrl.add("http://pic1.nipic.com/2008-08-12/200881211331729_2.jpg");
        listUrl.add("http://pic28.nipic.com/20130402/9252150_190139450381_2.jpg");
        listUrl.add("http://pic9.nipic.com/20100812/3289547_144304019987_2.jpg");
        listUrl.add("http://imgsrc.baidu.com/forum/pic/item/3ac79f3df8dcd1004e9102b8728b4710b9122f1e.jpg");
        listUrl.add("http://www.th7.cn/Article/UploadFiles/200801/2008012120273536.jpg");
        listUrl.add("http://img.sucai.redocn.com/attachments/images/201012/20101213/20101211_0e830c2124ac3d92718fXrUdsYf49nDl.jpg");
        listUrl.add("http://a2.att.hudong.com/38/59/300001054794129041591416974.jpg");
        listUrl.add("http://pic13.nipic.com/20110415/1347158_132411659346_2.jpg");
        listUrl.add("http://down.tutu001.com/d/file/20101129/2f5ca0f1c9b6d02ea87df74fcc_560.jpg");
        listUrl.add("http://pic24.nipic.com/20121022/9252150_193011306000_2.jpg");
        listUrl.add("http://pic1.nipic.com/2008-08-12/200881211331729_2.jpg");
        listUrl.add("http://pic28.nipic.com/20130402/9252150_190139450381_2.jpg");
        listUrl.add("http://pic9.nipic.com/20100812/3289547_144304019987_2.jpg");
        listUrl.add("http://imgsrc.baidu.com/forum/pic/item/3ac79f3df8dcd1004e9102b8728b4710b9122f1e.jpg");
        listUrl.add("http://www.th7.cn/Article/UploadFiles/200801/2008012120273536.jpg");
        listUrl.add("http://img.sucai.redocn.com/attachments/images/201012/20101213/20101211_0e830c2124ac3d92718fXrUdsYf49nDl.jpg");
        listUrl.add("http://a2.att.hudong.com/38/59/300001054794129041591416974.jpg");
        listUrl.add("http://pic13.nipic.com/20110415/1347158_132411659346_2.jpg");
        //添加测试名字
        listName.add("武松");
        listName.add("吴用");
        listName.add("林冲");
        listName.add("李逵");
        listName.add("华荣");
        listName.add("宋江");
        listName.add("卢俊义");
        listName.add("鲁智深");
        listName.add("杨志");
        listName.add("柴进");
        listName.add("武松");
        listName.add("吴用");
        listName.add("林冲");
        listName.add("李逵");
        listName.add("华荣");
        listName.add("宋江");
        listName.add("卢俊义");
        listName.add("鲁智深");
        listName.add("杨志");
        listName.add("柴进");
        for (int i = 0; i < 19; i++) {
            Person p = new Person();
            p.setImgUrl(listUrl.get(i));
            p.setName(listName.get(i));
            list.add(p);
        }
        adapter = new VolleyImagerLoaderAdapter(this, list);
        listView.setAdapter(adapter);
    }

}
