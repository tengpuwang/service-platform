package wang.tengp.spider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import wang.tengp.model.City;
import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import com.google.common.collect.Lists;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * 抓取美团数据填充城市信息
 * Created by shumin on 16-9-9.
 */
public class MeiTuan {

    private static final Logger logger = LoggerFactory.getLogger(MeiTuan.class);

    private static final String url = "http://i.meituan.com/";

    public static City fill(City city) {

        try {
            String pinyin = PinyinHelper.convertToPinyinString(city.getName(), "", PinyinFormat.WITHOUT_TONE);
            city.setPinyin(pinyin);
            Document doc = Jsoup.connect(url + pinyin).get();
            Element element = doc.getElementById("filterData");
            String data = element.data();
            JSONObject obj = JSON.parseObject(data);
            JSONArray areaList = obj.getJSONArray("BizAreaList");       //  区域列表
            JSONArray subwayList = obj.getJSONArray("SubwayList");      //  地铁

            // 解析区域
            List<City.Area> areas = Lists.newArrayList();
            areaList.parallelStream().forEach(item -> {
                City.Area area = new City.Area();
                area.name = (String) ((JSONObject) item).get("name");
                JSONArray subAreas = ((JSONObject) item).getJSONArray("subareas");
                List<City.ShopArea> shopAreas = Lists.newArrayList();
                subAreas.stream().forEach(subArea -> {
                    City.ShopArea shopArea = new City.ShopArea();
                    shopArea.name = (String) ((JSONObject) subArea).get("name");
                    shopAreas.add(shopArea);
                });
                area.shopAreas = shopAreas;
                areas.add(area);
            });
            city.setAreas(areas);

            // 解析地铁
            List<City.Subway> subways = Lists.newArrayList();
            subwayList.stream().forEach(item -> {
                City.Subway subway = new City.Subway();
                subway.name = (String) ((JSONObject) item).get("name");
                JSONArray stations = ((JSONObject) item).getJSONArray("stations");
                stations.stream().forEach(station -> {
                    subway.stations.add((String) ((JSONObject) station).get("name"));
                });
                subways.add(subway);
            });
            city.setSubways(subways);
            String str = JSON.toJSONString(city);
            System.out.println(str);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (PinyinException e) {
            e.printStackTrace();
        }

        return city;

    }
}
