package wang.tengp.common;

import com.mongodb.DBObject;
import com.mongodb.MongoException;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.DocumentCallbackHandler;

/**
 * Created by shumin on 2017/2/14.
 */
public class InfoDocumentCallbackHandler  implements DocumentCallbackHandler{
    @Override
    public void processDocument(DBObject dbObject) throws MongoException, DataAccessException {

        System.out.print("");
    }
}
