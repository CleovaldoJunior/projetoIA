import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class Database {
    public static Connection conn;

    public static void connect() throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/projetoIA";
        conn = DriverManager.getConnection(url, "postgres","lacunar1");
        conn.setAutoCommit(false);
    }
    public static void disconnect()throws SQLException{
        conn.close();
    }
    public static void cria_tabela(String nome_tabela) throws SQLException {
        try (Statement st = conn.createStatement()){
            st.execute("CREATE TABLE IF NOT EXISTS "+nome_tabela+"()");
            conn.commit();
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) throws SQLException {
        connect();
        String frase = "Tobby is a great actor and he likes to eat oranges while he's watching tigers";
        List<CoreLabel> tokens = entidades(frase);
        for(CoreLabel entidade: tokens){
            System.out.println(entidade.word());
            switch (entidade.tag()) {
                case "NNP":
                    cria_tabela("PERSON");
                    break;
                case "NN":
                    cria_tabela(entidade.word());
                    break;
                case "NNS":
                    cria_tabela(entidade.word());
                    break;
            }
        }
        disconnect();
    }

    public static List<CoreLabel> entidades(String text){
        Properties propriedades = new Properties();
        propriedades.setProperty("annotators", "tokenize,ssplit,pos");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(propriedades);
        CoreDocument doc = pipeline.processToCoreDocument(text);
        return doc.tokens();
    }

}
