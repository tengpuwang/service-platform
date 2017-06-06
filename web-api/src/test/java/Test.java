import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Iterator;
import java.util.Set;

/**
 * Created by shumin on 16-6-29.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/applicationContext.xml")
public class Test {

    protected MongoTemplate mongoTemplate;

    /**
     * 注入mongodbTemplate
     *
     * @param mongoTemplate
     */

    @Autowired
    @Qualifier("mongoTemplate")
    protected void setMongoTemplate(MongoTemplate mongoTemplate) {
        if (this.mongoTemplate == null) {
            this.mongoTemplate = mongoTemplate;
        }
    }

    @org.junit.Test
    public void run(){


//        DBCollection dbCollection = mongoTemplate.getCollection("user");
//        DBCursor dbCursor = dbCollection.find();
//        System.out.print(dbCursor.explain().toString());
        DBCollection testcreat = mongoTemplate.getCollection("testcreat");
//        testcreat.save(dbCursor.explain());

        Set<String> names =  mongoTemplate.getCollectionNames();
        Iterator<String> s = names.iterator();
        while (s.hasNext()){
            System.out.println(s.next());
        }
    }
}
