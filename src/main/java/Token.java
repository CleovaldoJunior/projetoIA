import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.*;

import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.*;

public class Token {

    public static List<CoreLabel> token(String s) {
        Properties p = new Properties();
        p.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner");
        p.setProperty("tokenize.options", "splitHyphenated=false,americanize=false");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(p);
        CoreDocument doc = new CoreDocument(s);
        pipeline.annotate(doc);
        return doc.tokens();
    }
}